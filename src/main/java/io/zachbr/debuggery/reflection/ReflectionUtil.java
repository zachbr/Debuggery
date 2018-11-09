/*
 * This file is part of Debuggery.
 *
 * Debuggery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Debuggery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Debuggery.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.zachbr.debuggery.reflection;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

public class ReflectionUtil {
    private static final Map<Class, Map<String, Method>> globalMethodMap = new HashMap<>();

    /**
     * Tests if a given string starts with a vowel
     */
    private static final Predicate<String> startsWithVowel = s -> {
        s = s.toLowerCase();
        return s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u");
    };

    /**
     * Gets a method map for the specified class
     * <p>
     * Will check the local cache before generating a new one.
     *
     * @param classIn class to get a method map for
     * @return method map for class
     */
    public static @NotNull Map<String, Method> getMethodMapFor(@NotNull Class classIn) {
        Validate.notNull(classIn);
        if (globalMethodMap.containsKey(classIn)) {
            return globalMethodMap.get(classIn);
        } else {
            Map<String, Method> methodMap = createMethodMapFor(classIn);
            globalMethodMap.put(classIn, methodMap);
            return methodMap;
        }
    }

    /**
     * Clears the global method map
     */
    public static void clearMethodMapCache() {
        globalMethodMap.clear();
    }

    /**
     * Attempts to parse a string for method parameters
     * <p>
     * Doesn't do any actual type detection or object instantiation, that's
     * not what this is for.
     *
     * @param args   A list of all arguments to search
     * @param method method to check against
     * @return a best-effort list of method params from the given list
     */
    public static @NotNull List<String> getArgsForMethod(@NotNull List<String> args, @NotNull Method method) {
        if (args.size() == 0 && method.getParameterCount() == 0) {
            return Collections.emptyList();
        }

        List<String> argsOut = new ArrayList<>();

        if (args.size() < method.getParameterCount()) {
            argsOut.addAll(args);
        } else {
            for (int i = 0; i < method.getParameterCount(); i++) {
                argsOut.add(args.get(i));
            }
        }

        return argsOut;
    }

    /**
     * Gets all public methods associated with a class
     *
     * @param clazz class to get associated public methods
     * @return array of public methods
     */
    private static @NotNull Method[] getAllPublicMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                methods.add(method);
            }
        }

        return methods.toArray(new Method[0]);
    }

    /**
     * Generates a map containing method names and the methods themselves
     * <p>
     * Used for reflection based command handling
     *
     * @param clazz which class to get methods for
     * @return a new Map
     */
    private static @NotNull Map<String, Method> createMethodMapFor(Class clazz) {
        Map<String, Method> map = new HashMap<>();

        for (Method method : getAllPublicMethods(clazz)) {
            String identifier = getFormattedMethodSignature(method).replaceAll(" ", "");
            map.put(identifier, method);
        }

        return map;
    }

    /**
     * Gets a complete and formatted method signature
     * <p>
     * The signature is exact and should tell user's what to expect
     * It will look something like this "method(ParamType, ParamType)"
     * <p>
     * Each param will result in an entry between the parentheses,
     * a method with no params will result in nothing between the parentheses
     *
     * @param method which method to get a formatted name for
     * @return a formatted name
     */
    public static @NotNull String getFormattedMethodSignature(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName());

        builder.append("(");
        boolean first = true;
        for (Class type : method.getParameterTypes()) {
            if (first) {
                builder.append(type.getSimpleName());
                first = false;
            } else {
                builder.append(", ").append(type.getSimpleName());
            }
        }
        builder.append(")");

        return builder.toString();
    }

    /**
     * Gets the error message we should send when the input string is missing arguments
     *
     * @param method method with missing arguments
     * @return error message
     */
    public static @NotNull String getArgMismatchString(Method method) {
        final String methodName = method.getName();
        final Class returnType = method.getReturnType();
        final String returnTypeName = returnType.getSimpleName();
        String returnInfo;

        if (returnType.equals(Void.TYPE)) {
            returnInfo = "returns void.";
        } else if (startsWithVowel.test(returnTypeName)) {
            returnInfo = "returns an " + returnTypeName;
        } else {
            returnInfo = "returns a " + returnTypeName;
        }

        return "Method " + methodName + " requires " + method.getParameterCount() + " args and " + returnInfo + "\n"
                + ReflectionUtil.getFormattedMethodSignature(method);
    }
}

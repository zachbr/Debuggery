/*
 * This file is part of Debuggery.
 *
 * Debuggery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * distributed with this repository.
 *
 * Debuggery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Debuggery. If not, see <http://www.gnu.org/licenses/>.
 */

package com.destroystokyo.debuggery.reflection;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectionUtil {
    private static Map<Class, Map<String, Method>> globalMethodMap = new HashMap<>();

    /**
     * Gets a method map for the specified class
     * Will check the local cache before generating a new one.
     *
     * @param classIn class to get a method map for
     * @return method map for class
     */
    @Nonnull
    public static Map<String, Method> getMethodMapFor(@Nonnull Class classIn) {
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
     * Doesn't do any actual type detection or object instantiation, that's
     * not what this is for.
     *
     * @param args   A list of all arguments to search
     * @param method method to check against
     * @return a best-effort list of method params from the given list
     */
    @Nonnull
    public static List<String> getArgsForMethod(@Nonnull List<String> args, @Nonnull Method method) {
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
    @Nonnull
    private static Method[] getAllPublicMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                methods.add(method);
            }
        }

        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Generates a map containing method names and the methods themselves
     * Used for reflection based command handling
     *
     * @param clazz which class to get methods for
     * @return a new Map
     */
    @Nonnull
    private static Map<String, Method> createMethodMapFor(Class clazz) {
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
    @Nonnull
    public static String getFormattedMethodSignature(Method method) {
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
}

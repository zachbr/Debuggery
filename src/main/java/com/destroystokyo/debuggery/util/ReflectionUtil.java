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

package com.destroystokyo.debuggery.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

public class ReflectionUtil {
    public static final Predicate<Method> IS_GETTER = m -> m.getName().startsWith("get") || m.getName().startsWith("is") || m.getName().startsWith("has");
    public static final Predicate<Method> HAS_NO_PARAMS = m -> m.getParameterCount() == 0;

    public static List<Method> getAllPublicMethods(Class clazz) {
        return getAllPublicMethodsMatching(clazz, method -> true);
    }

    public static List<Method> getAllPublicMethodsMatching(Class clazz, Predicate<Method> predicate) {
        List<Method> publicMethods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            if (predicate.test(method)) {
                publicMethods.add(method);
            }
        }

        return publicMethods;
    }

    /**
     * Entry point for calling methods
     *
     * @param method method to invoke
     * @param instance instance to invoke with
     * @param input arguments to pass along at invocation
     * @return any output as a result of invocation
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static String doReflection(Method method, Object instance, String[] input) throws InvocationTargetException, IllegalAccessException {
        if (HAS_NO_PARAMS.test(method)) {
            return OutputFormatter.getOutput(method.invoke(instance));
        }

        if (method.getParameterCount() != input.length) {

            return "Method " + method.getName() + " requires " + method.getParameterCount() + " args.\n"
                    + getFormattedMethodSignature(method);
        }

        if (method.getParameterCount() == input.length) {
            return reflect(method, instance, input);
        }

        throw new AssertionError("Unhandled case in reflection logic!");
    }

    /**
     * Internal function to organize and cleanup the structure.
     *
     * This function calls another function that deduces input types via method parameters,
     * it invokes the method, and then it calls another function that takes the output and makes it human readable.
     *
     * Finally, it returns a string.
     *
     * @param method method to invoke
     * @param instance instance to invoke with
     * @param input arguments to pass along at invocation
     * @return any output as a result of invocation
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    private static String reflect(Method method, Object instance, String[] input) throws InvocationTargetException, IllegalAccessException {
        Object[] methodArgs = InputFormatter.getTypesFromInput(method.getParameterTypes(), input);
        Object returnVal = method.invoke(instance, methodArgs);
        String output = OutputFormatter.getOutput(returnVal);

        return output;
    }

    /**
     * Generates a map containing method names and the methods themselves
     * Used for reflection based command handling
     *
     * @param clazz which class to get methods for
     * @return a new Map
     */
    public static Map<String, Method> createMethodMapFor(Class clazz) {
        Map<String, Method> map = new HashMap<>();
        Map<String, Integer> methodCollisionMap = new HashMap<>();
        char[] acceptableParamVals = { '.', '*', ',', ';', '\'' };

        for (Method method : getAllPublicMethods(clazz)) {
            String identifier;

            if (method.getParameterCount() == 0) {
                identifier = getSimpleMethodSignature(method, acceptableParamVals[0]);
            } else {
                int methodCollisionCount = 0;

                String methodID = method.getName() + method.getParameterCount();
                if (methodCollisionMap.containsKey(methodID)) {
                    methodCollisionCount = methodCollisionMap.get(methodID);
                }

                methodCollisionMap.put(methodID, methodCollisionCount + 1);
                identifier = getSimpleMethodSignature(method, acceptableParamVals[methodCollisionCount]);
            }

            map.put(identifier, method);
        }

        return map;
    }

    /**
     * Gets a simplified method signature
     *
     * The signature is simplified to aid in command processing and UX
     * It will look something like this "method(..)"
     *
     * Each param will result in an additional character between the parentheses,
     * a method with no params will result in nothing between the parentheses
     *
     * @param method which method to get a simplified name for
     * @param paramVal the character to use between the parentheses
     * @return a simplified name
     */
    private static String getSimpleMethodSignature(Method method, char paramVal) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName());

        builder.append("(");
        for (int i = 0; i < method.getParameterCount(); i++) {
            builder.append(paramVal);
        }
        builder.append(")");

        return builder.toString();
    }


    /**
     * Gets a complete and formatted method signature
     *
     * The signature is exact and should tell user's what to expect
     * It will look something like this "method(ParamType, ParamType)"
     *
     * Each param will result in an entry between the parentheses,
     * a method with no params will result in nothing between the parentheses
     *
     * @param method which method to get a formatted name for
     * @return a formatted name
     */
    private static String getFormattedMethodSignature(Method method) {
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

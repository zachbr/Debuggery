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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {

    /**
     * Gets all public methods associated with a class
     *
     * @param clazz class to get associated public methods
     * @return array of public methods
     */
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
    public static Map<String, Method> createMethodMapFor(Class clazz) {
        Map<String, Method> map = new HashMap<>();
        Map<String, Integer> methodCollisionMap = new HashMap<>();
        char[] acceptableParamVals = {'.', '*', ',', ';', '\''};

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
     * <p>
     * The signature is simplified to aid in command processing and UX
     * It will look something like this "method(..)"
     * <p>
     * Each param will result in an additional character between the parentheses,
     * a method with no params will result in nothing between the parentheses
     *
     * @param method   which method to get a simplified name for
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

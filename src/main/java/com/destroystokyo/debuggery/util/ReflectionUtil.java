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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ReflectionUtil {
    public static final Predicate<Method> IS_GETTER = m -> m.getName().startsWith("get") || m.getName().startsWith("is") || m.getName().startsWith("has");
    public static final Predicate<Method> HAS_NO_PARAMS = m -> m.getParameterCount() == 0;

    public static List<Method> getAllPublicMethods(Class clazz) {
        return getAllPublicMethodsMatching(clazz, method -> true);
    }

    public static List<Method> getAllPublicMethodsMatching(Class clazz, Predicate<Method> predicate) {
        List<Method> publicMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            if (predicate.test(method)) {
                publicMethods.add(method);
            }
        }

        return publicMethods;
    }
}

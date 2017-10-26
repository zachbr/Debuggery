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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles formatting the arguments we send to method invocation
 */
public class InputFormatter {

    @Nonnull
    public static Object[] getTypesFromInput(Class[] classes, String[] input) {
        List<Object> out = new ArrayList<>();

        for (int i = 0; i < classes.length; i++) {
            out.add(getTypeForClass(classes[i], input[i]));
        }

        return out.toArray();
    }

    @Nullable
    private static Object getTypeForClass(Class clazz, String input) {
        if (clazz == null) {
            throw new IllegalArgumentException("Cannot determine input type for null class");
        }

        if (input.toLowerCase().equals("null")) {
            return null;
        }

        if (clazz.equals(String.class)) {
            return input;
        } else if (clazz.isPrimitive()) {
            return handlePrimitive(clazz, input);
        }

        return null; // TODO
    }

    private static Object handlePrimitive(Class clazz, String input) {
        if (clazz.equals(byte.class)) {
            return Byte.valueOf(input);
        } else if (clazz.equals(short.class)) {
            return Short.valueOf(input);
        } else if (clazz.equals(int.class)) {
            return Integer.valueOf(input);
        } else if (clazz.equals(long.class)) {
            return Long.valueOf(input);
        } else if (clazz.equals(float.class)) {
            return Float.valueOf(input);
        } else if (clazz.equals(double.class)) {
            return Double.valueOf(input);
        } else if (clazz.equals(boolean.class)) {
            return Boolean.valueOf(input);
        } else if (clazz.equals(char.class)) {
            return input.charAt(0);
        }

        throw new AssertionError("Java added another primitive type!");
    }
}

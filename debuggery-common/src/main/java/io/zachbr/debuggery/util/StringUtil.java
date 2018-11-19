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

package io.zachbr.debuggery.util;

import org.jetbrains.annotations.NotNull;

public class StringUtil {
    /**
     * Parses an array of doubles from a given comma separated string
     *
     * @param input        string to parse
     * @param expectedSize count expected
     * @return new array of doubles
     */
    public static @NotNull double[] parseDoublesFromString(String input, int expectedSize) {
        String[] split = input.split(",", expectedSize);
        if (split.length != expectedSize) {
            throw new IllegalArgumentException("Could not find exactly " + expectedSize + " comma separated values to parse!");
        }

        double[] parsed = new double[expectedSize];
        for (int i = 0; i < split.length; i++) {
            parsed[i] = Double.parseDouble(split[i]);
        }

        return parsed;
    }
}

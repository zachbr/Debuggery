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

class ReflTestClass {
    private int some;
    private int random;
    private int numbers;

    ReflTestClass(int num, int num1, int num2) {
        some = num;
        random = num1;
        numbers = num2;
    }

    @SuppressWarnings("WeakerAccess")
    public int[] getNumbersPlusParam(int param) {
        return new int[]{some, random, numbers, param};
    }
}

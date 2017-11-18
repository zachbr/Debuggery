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

@SuppressWarnings("WeakerAccess")
class ReflTestClass {
    private int some;
    private int random;
    private int numbers;
    private ReflSubClass subClass;

    ReflTestClass(int num, int num1, int num2) {
        some = num;
        random = num1;
        numbers = num2;
        subClass = new ReflSubClass();
    }

    public int[] getSomeNumbers() {
        return new int[]{some, random, numbers};
    }

    public int[] getNumbersPlusParam(int param) {
        return new int[]{some, random, numbers, param};
    }

    public ReflSubClass getSubClass() {
        return subClass;
    }

    static class ReflSubClass {

        public int[] get1234(int num) {
            return new int[]{1, 2, 3, 4, num};
        }
    }
}

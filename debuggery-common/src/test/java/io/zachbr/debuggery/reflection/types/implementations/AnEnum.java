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

package io.zachbr.debuggery.reflection.types.implementations;

public enum AnEnum {
    VAL_1("value 1"),
    VAL_2("value 2"),
    VAL_3("value 3"),
    VAL_4("value 4"),
    VAL_5("value 5");

    private final String text;

    AnEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

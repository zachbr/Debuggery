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

import com.destroystokyo.debuggery.reflection.formatters.InputException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ReflectCallTest {

    @Test
    public void reflect() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        Method method = ReflTestClass.class.getMethod("getNumbersPlusParam", int.class);
        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{"4"};

        String result = new ReflectCall<>(instance, method, input, null).reflect();

        Predicate<String> passes = s -> s.contains("1") && s.contains("2") && s.contains("3") && s.contains("4");
        if (!passes.test(result)) {
            System.out.println("Expected result to include 1, 2, 3, and 4. Actual result below");
            System.out.println(result);
        }

        // Verify the output contains the expected data we put in, ignoring random formatting details
        assertTrue(passes.test(result));
    }
}

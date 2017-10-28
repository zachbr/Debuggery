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

import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilTest {

    @Test
    public void getAllPublicMethods() {
        final Class TESTER = Block.class;

        List<Method> reflUtil = ReflectionUtil.getAllPublicMethods(TESTER);
        List<Method> reflection = Arrays.stream(TESTER.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .collect(Collectors.toList());

        // Verify our results match Java's
        assertEquals(reflUtil, reflection);
    }

    @Test
    public void getAllPublicMethodsMatching() {
        final Predicate<Method> HAS_ONE_PARAM = m -> m.getParameterCount() == 1;
        final Class TESTER = World.class;

        List<Method> reflUtil = ReflectionUtil.getAllPublicMethodsMatching(TESTER, HAS_ONE_PARAM);
        List<Method> reflection = Arrays.stream(TESTER.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(HAS_ONE_PARAM)
                .collect(Collectors.toList());

        // Verify our results match Java's
        assertEquals(reflUtil, reflection);
    }

    @Test
    public void doReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InputException {
        // TODO: this could use more cases
        Method method = ReflTestClass.class.getMethod("getNumbersPlusParam", int.class);
        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{"4"};

        String result = ReflectionUtil.doReflection(method, instance, input);

        Predicate<String> passes = s -> s.contains("1") && s.contains("2") && s.contains("3") && s.contains("4");
        if (!passes.test(result)) {
            System.out.println("Expected result to include 1, 2, 3, and 4. Actual result below");
            System.out.println(result);
        }
        // Verify the output contains the expected data we put in, ignoring random formatting details
        assertTrue(passes.test(result));
    }

    @Test
    public void createMethodMapFor() {
        final Class TESTER = World.class;

        Set<String> reflUtil = ReflectionUtil.createMethodMapFor(TESTER).keySet();
        HashMap<Method, Boolean> reflection = new HashMap<>();

        Arrays.stream(TESTER.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .forEach(m -> reflection.put(m, false));

        for (String formattedName : reflUtil) {
            for (Method method : reflection.keySet()) {
                if (formattedName.contains(method.getName())) {
                    reflection.put(method, true);
                }
            }
        }

        boolean pass = true;

        for (Map.Entry<Method, Boolean> entry : reflection.entrySet()) {
            if (!entry.getValue()) {
                System.out.println(entry.getKey().getName() + " MISSING!");
                pass = false;
            }
        }

        // Verify our method map contains all methods for a class
        assertTrue(pass);
    }
}

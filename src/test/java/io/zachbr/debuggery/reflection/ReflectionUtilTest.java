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

package io.zachbr.debuggery.reflection;

import com.google.common.collect.Lists;
import org.bukkit.World;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilTest {

    @Test
    public void createMethodMapFor() {
        final Class TESTER = World.class;

        Set<String> reflUtil = ReflectionUtil.getMethodMapFor(TESTER).keySet();
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

    @Test
    public void ensureArgsForMethodAccurate() throws NoSuchMethodException {
        // Given a method with only one param, pick out that param without the others
        List<String> inputStr = Lists.newArrayList("1", "nextMethod", "param", "param2");
        Method testClass1234 = ReflTestClass.ReflSubClass.class.getMethod("get1234", int.class);
        List<String> out = ReflectionUtil.getArgsForMethod(inputStr, testClass1234);

        assertEquals(1, out.size());
        assertEquals("1", out.get(0));

        // Given a method that requires multiple params, but not provided enough, ensure all are passed through
        inputStr = Lists.newArrayList("1", "2", "3");
        Method lotsOfParams = ReflTestClass.class.getMethod("methodWithLotsOfParams", int.class, int.class, int.class, int.class, int.class, int.class, int.class);
        out = ReflectionUtil.getArgsForMethod(inputStr, lotsOfParams);

        assertEquals(inputStr.size(), out.size());
        for (int i = 0; i < inputStr.size(); i++) {
            assertEquals(inputStr.get(i), out.get(i));
        }

        // if no params are required, and no input is given, ensure we always get an empty list
        inputStr = Lists.newArrayList();
        Method noParams = ReflTestClass.class.getMethod("getSomeNumbers");
        out = ReflectionUtil.getArgsForMethod(inputStr, noParams);

        assertEquals(0, out.size());
    }
}

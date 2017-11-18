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

import org.bukkit.World;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
}

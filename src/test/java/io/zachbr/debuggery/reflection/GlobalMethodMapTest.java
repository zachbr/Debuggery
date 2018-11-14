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

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalMethodMapTest {

    @Test
    public void ensureSingletonNeverNull() throws NoSuchFieldException, IllegalAccessException {
        assertNotNull(GlobalMethodMap.getInstance());

        GlobalMethodMap.getInstance().clearCache();
        assertNotNull(GlobalMethodMap.getInstance());

        Field singleton = GlobalMethodMap.class.getDeclaredField("singletonInstance");
        singleton.setAccessible(true);
        singleton.set(null, null);

        assertNotNull(GlobalMethodMap.getInstance());
    }

    @Test
    public void ensureContainsAllClasses() {
        GlobalMethodMap globalMap = GlobalMethodMap.getInstance();

        Class[] expected = {Assertions.class, ReflTestClass.class, this.getClass()};
        for (Class clazz : expected) {
            globalMap.getMethodMapFor(clazz);
        }

        Set<Class<?>> mappedClasses = globalMap.getAllMappedClasses();
        long missing = Arrays.stream(expected)
                .filter(c -> !mappedClasses.contains(c))
                .peek(c -> System.out.println("Expected class missing from mapped classes: " + c.getName()))
                .count();

        assertEquals(0, missing);
    }

    @Test
    public void ensureContainsExpectedMethodMaps() {
        Class[] testers = {this.getClass(), ReflTestClass.class, Assertions.class};

        MethodMap[] expected = new MethodMap[testers.length];
        for (int i = 0; i < testers.length; i++) {
            expected[i] = new MethodMap(testers[i]);
        }

        for (Class tester : testers) {
            GlobalMethodMap.getInstance().getMethodMapFor(tester);
        }

        Set<MethodMap> resulting = GlobalMethodMap.getInstance().getAllMethodMaps();
        long missing = Arrays.stream(expected)
                .filter(e -> !resulting.contains(e))
                .peek(e -> System.out.println("Expected method map missing from global results: " + e))
                .count();

        assertEquals(0, missing);
    }

    @Test
    public void ensureCacheClear() {
        GlobalMethodMap.getInstance().clearCache();

        Class[] testers = {this.getClass(), ReflTestClass.class, Assertions.class};
        for (Class clazz : testers) {
            GlobalMethodMap.getInstance().getMethodMapFor(clazz);
        }

        int prior = GlobalMethodMap.getInstance().getAllMethodMaps().size();
        assertTrue(prior != 0);

        GlobalMethodMap.getInstance().clearCache();
        int post = GlobalMethodMap.getInstance().getAllMappedClasses().size();

        assertTrue(post != prior);
        assertEquals(0, post);
    }
}

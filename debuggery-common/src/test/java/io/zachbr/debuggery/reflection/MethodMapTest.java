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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MethodMapTest {
    private static final Class<?> TESTER = Assertions.class;

    @Test
    public void testContainsMethods() {
        MethodMap methodmap = new MethodMap(TESTER);
        Set<Method> mappedMethods = methodmap.getAllMethods();

        long missing = Arrays.stream(TESTER.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> !mappedMethods.contains(m))
                .peek(m -> System.out.println(m.getName() + " MISSING!"))
                .count();

        assertEquals(0, missing);
    }

    @Test
    public void testContainsIds() {
        MethodMap methodMap = new MethodMap(TESTER);
        Set<String> identifiers = methodMap.getAllIds();

        long missing = Arrays.stream(TESTER.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(ReflectionUtil::getMethodId)
                .filter(s -> !identifiers.contains(s))
                .peek(s -> System.out.println("Method map does not contain expected: " + s))
                .count();

        assertEquals(0, missing);
    }

    @Test
    public void testContainsSpecificId() throws NoSuchMethodException {
        Method toLookFor = ReflTestClass.class.getMethod("getSomeNumbers");
        String id = ReflectionUtil.getMethodId(toLookFor);

        MethodMap methodMap = new MethodMap(ReflTestClass.class);
        assertTrue(methodMap.containsId(id));
        assertNotNull(methodMap.getById(id));
    }

    @Test
    public void ensureHashcodes() {
        MethodMap map1 = new MethodMap(TESTER);
        MethodMap map2 = new MethodMap(TESTER);

        assertEquals(map1.hashCode(), map2.hashCode());
    }

    @Test
    public void ensureEquality() {
        MethodMap map1 = new MethodMap(TESTER);
        MethodMap map2 = new MethodMap(TESTER);

        assertEquals(map1, map2);
    }

    @Test
    public void ensureHashcodeAndEqualityResultSame() {
        MethodMap map1 = new MethodMap(TESTER);
        MethodMap map2 = new MethodMap(TESTER);

        boolean hashEquality = map1.hashCode() == map2.hashCode();
        boolean objectEquality = map1.equals(map2);

        assertEquals(hashEquality, objectEquality);
    }

    @Test
    public void ensureToStringIsntUseless() {
        MethodMap map = new MethodMap(TESTER);
        String toString = map.toString();

        assertTrue(toString.contains(TESTER.getName()));
        assertTrue(toString.contains(String.valueOf(map.getAllMethods().size())));
    }

    @Test
    public void ensureMappedClassMatchesActual() {
        Class[] testers = {TESTER, Object.class};
        MethodMap[] maps = new MethodMap[testers.length];

        for (int i = 0; i < testers.length; i++) {
            maps[i] = new MethodMap(testers[i]);
        }

        boolean allMatch = true;
        for (int i = 0; i < testers.length; i++) {
            MethodMap tested = maps[i];
            if (tested.getMappedClass() != testers[i]) {
                System.out.println(tested.toString() + " method map mapped class does not match expected: " + testers[i]);
                allMatch = false;
            }
        }

        assertTrue(allMatch);
    }
}

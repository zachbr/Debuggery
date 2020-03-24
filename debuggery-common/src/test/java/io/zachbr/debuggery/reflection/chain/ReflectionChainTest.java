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

package io.zachbr.debuggery.reflection.chain;

import io.zachbr.debuggery.TestLoggerImpl;
import io.zachbr.debuggery.reflection.*;
import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionChainTest {
    private static final Predicate<String> CONTAINS_1234 = s -> s.contains("1") && s.contains("2") && s.contains("3") && s.contains("4");
    private final TypeHandler typeHandler = new TypeHandler(new TestLoggerImpl());
    private final MethodMapProvider mapProvider = new MethodMapProvider();
    private final ReflectionChainFactory chainFactory = new ReflectionChainFactory(typeHandler, mapProvider, new TestLoggerImpl());

    @Test
    public void simpleReflectTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        // First something simple
        Method method = ReflTestClass.class.getMethod("getNumbersPlusParam", int.class);
        String methodName = ReflectionUtil.getMethodId(method);

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{methodName, "4"};

        ReflectionResult chainResult = chainFactory.runChain(input, instance, null);

        assertNotNull(chainResult.endingInstance());
        String result = typeHandler.getOutputFor(chainResult.endingInstance());

        if (!CONTAINS_1234.test(result)) {
            System.out.println("Expected result to include 1, 2, 3, and 4. Actual result below");
            System.out.println(result);
        }

        assertNotNull(chainResult);
        assertNotNull(result);

        // Verify the output contains the expected data we put in, ignoring random formatting details
        assertTrue(CONTAINS_1234.test(result));
    }

    @Test
    public void chainReflectTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        // Now test calling a method on an returned instance
        Method subClassGet = ReflTestClass.class.getMethod("getSubClass");
        String subClassGetterName = ReflectionUtil.getMethodId(subClassGet);

        Method subClassGetNum = ReflTestClass.ReflSubClass.class.getMethod("get1234", int.class);
        String subClassGetNumName = ReflectionUtil.getMethodId(subClassGetNum);

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{subClassGetterName, subClassGetNumName, "5"};

        ReflectionResult chainResult = chainFactory.runChain(input, instance, null);

        assertNotNull(chainResult.endingInstance());
        String result = typeHandler.getOutputFor(chainResult.endingInstance());

        assertNotNull(result);

        if (!CONTAINS_1234.test(result)) {
            System.out.println("Expected result to include 1, 2, 3, 4 Actual result below");
            System.out.println(result);
        }

        if (!result.contains("5")) {
            System.out.println("Expected result to contain 5 Actual result below");
            System.out.println(result);
        }

        // Verify the output contains the expected data we put in, ignoring random formatting details
        assertTrue(CONTAINS_1234.test(result));
        assertTrue(result.contains("5"));
    }

    @Test
    public void noSuchMethodTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        Method subClassGet = ReflTestClass.class.getMethod("getSubClass");
        String subClassGetterName = ReflectionUtil.getMethodId(subClassGet);

        String methodThatDoesNotExist = "getPotatoes()";

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{subClassGetterName, methodThatDoesNotExist, "5"};

        ReflectionResult chainResult = chainFactory.runChain(input, instance, null);

        assertNotNull(chainResult);
        String reason = chainResult.reason();

        assertNotNull(reason);
        assertSame(ReflectionResult.Type.UNKNOWN_REFERENCE, chainResult.type());
        assertTrue(reason.toLowerCase().contains("unknown"));
    }

    @Test
    public void chainOnNullTest() throws NoSuchMethodException {
        Method alwaysReturnsNull = ReflTestClass.class.getMethod("alwaysReturnsNull");
        String nullMethodName = ReflectionUtil.getMethodId(alwaysReturnsNull);

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{nullMethodName, "get(int)", "5"};

        ReflectionResult chainResult = chainFactory.runChain(input, instance, null);

        assertNotNull(chainResult);
        String reason = chainResult.reason();

        assertNotNull(reason);
        assertSame(ReflectionResult.Type.NULL_REFERENCE, chainResult.type());

        String out = (String) reason;
        // make sure it always includes something about null and the responsible method
        assertTrue(out.toLowerCase().contains("null"));
        assertTrue(out.toLowerCase().contains(nullMethodName.toLowerCase()));
    }
}

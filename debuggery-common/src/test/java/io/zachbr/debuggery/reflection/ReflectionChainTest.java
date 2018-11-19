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

import io.zachbr.debuggery.TestLoggerImpl;
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

    @Test
    public void simpleReflectTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        // First something simple
        Method method = ReflTestClass.class.getMethod("getNumbersPlusParam", int.class);
        String methodName = ReflectionUtil.getMethodId(method);

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{methodName, "4"};

        ReflectionChain chain = new ReflectionChain(mapProvider, typeHandler, input, instance);
        ReflectionChain.Result chainResult = chain.chain();

        assertNotNull(chainResult.getEndingInstance());
        String result = typeHandler.getOutputFor(chainResult.getEndingInstance());

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

        ReflectionChain chain = new ReflectionChain(mapProvider, typeHandler, input, instance);
        ReflectionChain.Result chainResult = chain.chain();

        assertNotNull(chainResult.getEndingInstance());
        String result = typeHandler.getOutputFor(chainResult.getEndingInstance());

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

        // Verify we can get the ending instance and that it matches our earlier result
        assertSame(chainResult, chain.getResult());
    }

    @Test
    public void noSuchMethodTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        Method subClassGet = ReflTestClass.class.getMethod("getSubClass");
        String subClassGetterName = ReflectionUtil.getMethodId(subClassGet);

        String methodThatDoesNotExist = "getPotatoes()";

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{subClassGetterName, methodThatDoesNotExist, "5"};

        ReflectionChain chain = new ReflectionChain(mapProvider, typeHandler, input, instance);
        ReflectionChain.Result chainResult = chain.chain();

        assertNotNull(chainResult);
        String reason = chainResult.getReason();

        assertNotNull(reason);
        assertSame(ReflectionChain.Result.Type.UNKNOWN_REFERENCE, chainResult.getType());
        assertTrue(reason.toLowerCase().contains("unknown"));
    }

    @Test
    public void chainOnNullTest() throws NoSuchMethodException, IllegalAccessException, InputException, InvocationTargetException {
        Method alwaysReturnsNull = ReflTestClass.class.getMethod("alwaysReturnsNull");
        String nullMethodName = ReflectionUtil.getMethodId(alwaysReturnsNull);

        ReflTestClass instance = new ReflTestClass(1, 2, 3);
        String[] input = new String[]{nullMethodName, "get(int)", "5"};

        ReflectionChain chain = new ReflectionChain(mapProvider, typeHandler, input, instance);
        ReflectionChain.Result chainResult = chain.chain();

        assertNotNull(chainResult);
        String reason = chainResult.getReason();

        assertNotNull(reason);
        assertSame(ReflectionChain.Result.Type.NULL_REFERENCE, chainResult.getType());

        String out = (String) reason;
        // make sure it always includes something about null and the responsible method
        assertTrue(out.toLowerCase().contains("null"));
        assertTrue(out.toLowerCase().contains(nullMethodName.toLowerCase()));
    }
}

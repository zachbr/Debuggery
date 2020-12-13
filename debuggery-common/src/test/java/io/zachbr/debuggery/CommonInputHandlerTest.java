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

package io.zachbr.debuggery;

import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonInputHandlerTest {
    private final Logger logger = new TestLoggerImpl();
    private final TypeHandler typeHandler = new TypeHandler(logger);

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void testPrimitiveArrays() throws InputException {
        Class[] inputTypes = {byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class, boolean[].class, char[].class};
        String[] input = {"1,2,3", "127,356,400", "1000,4142,122142", "2312,1231414,2412421", "2.33,1.22,1.42", "3.14,2.22,7.533", "true,false", "c,$,%"};

        Object[] output = typeHandler.instantiateTypes(inputTypes, Arrays.asList(input));

        assertEquals(inputTypes.length, output.length);
        assertTrue(output[0] instanceof byte[]);
        assertTrue(output[1] instanceof short[]);
        assertTrue(output[2] instanceof int[]);
        assertTrue(output[3] instanceof long[]);
        assertTrue(output[4] instanceof float[]);
        assertTrue(output[5] instanceof double[]);
        assertTrue(output[6] instanceof boolean[]);
        assertTrue(output[7] instanceof char[]);

        byte[] byteArray = (byte[]) output[0];
        short[] shortArray = (short[]) output[1];
        int[] intArray = (int[]) output[2];
        long[] longArray = (long[]) output[3];
        float[] floatArray = (float[]) output[4];
        double[] doubleArray = (double[]) output[5];
        boolean[] booleanArray = (boolean[]) output[6];
        char[] charArray = (char[]) output[7];

        assertEquals(1, byteArray[0]);
        assertEquals(2, byteArray[1]);
        assertEquals(3, byteArray[2]);

        assertEquals(127, shortArray[0]);
        assertEquals(356, shortArray[1]);
        assertEquals(400, shortArray[2]);

        assertEquals(1000, intArray[0]);
        assertEquals(4142, intArray[1]);
        assertEquals(122142, intArray[2]);

        assertEquals(2312, longArray[0]);
        assertEquals(1231414, longArray[1]);
        assertEquals(2412421, longArray[2]);

        assertEquals(2.33, floatArray[0], 0.01D);
        assertEquals(1.22, floatArray[1], 0.01D);
        assertEquals(1.42, floatArray[2], 0.01D);

        assertEquals(3.14, doubleArray[0]);
        assertEquals(2.22, doubleArray[1]);
        assertEquals(7.533, doubleArray[2]);

        assertEquals(true, booleanArray[0]);
        assertEquals(false, booleanArray[1]);

        assertEquals('c', charArray[0]);
        assertEquals('$', charArray[1]);
        assertEquals('%', charArray[2]);
    }
}

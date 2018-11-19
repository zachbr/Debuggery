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

package io.zachbr.debuggery.reflection.types;

import io.zachbr.debuggery.Logger;
import io.zachbr.debuggery.TestLoggerImpl;
import io.zachbr.debuggery.reflection.types.implementations.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonOutputHandlerTest {
    private final Logger logger = new TestLoggerImpl();
    private final TypeHandler typeHandler = new TypeHandler(logger);

    @Test
    public void testNullInput() {
        assertNull(typeHandler.getOutputFor(null));
    }

    @Test
    public void testArray() {
        // Test Objects first
        Pair[] objectArray = {
                new Pair<>("element", true),
                new Pair<>("value", false),
                new Pair<>("taco", true)
        };

        String out = typeHandler.getOutputFor(objectArray);

        assertNotNull(out);
        for (Pair pair : objectArray) {
            assertTrue(out.contains(String.valueOf(pair.getLeft())));
            assertTrue(out.contains(String.valueOf(pair.getRight())));
        }

        // Now test primitives, since we do some special handling there
        int[] intArray = {1, 2, 3, 4, 5};

        out = typeHandler.getOutputFor(intArray);
        assertNotNull(out);

        for (int val : intArray) {
            assertTrue(out.contains(String.valueOf(val)));
        }
    }

    @Test
    public void testMap() {
        String tacoKey = "taco";
        String tacoVal = "tacos are great";
        String potatoKey = "potato";
        String potatoVal = "potatoes are bad";

        Map<String, String> map = new HashMap<>();
        map.put(tacoKey, tacoVal);
        map.put(potatoKey, potatoVal);

        String out = typeHandler.getOutputFor(map);

        assertNotNull(out);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertTrue(out.contains(entry.getKey()));
            assertTrue(out.contains(entry.getValue()));
        }
    }

    @Test
    public void testCollection() {
        // Bet you don't guess what song just came on
        String[] values = {
                "Buy it",
                "use it",
                "break it",
                "fix it",
                "Trash it",
                "change it",
                "mail - upgrade it",
                "Charge it",
                "point it",
                "zoom it,",
                "press it",
                "Snap it",
                "work it",
                "quick - erase it",
                "Write it",
                "cut it",
                "paste it",
                "save it",
                "Load it",
                "check it",
                "quick - rewrite it",
                "Plug it",
                "play it",
                "burn it",
                "rip it",
                "Drag and drop it",
                "zip - unzip it",
                "Lock it",
                "fill it",
                "call it",
                "find it",
                "View it",
                "code it",
                "jam - unlock it",
                "Surf it",
                "scroll it",
                "pause it",
                "click it",
                "Cross it",
                "crack it",
                "switch - update it",
                "Name it",
                "read it",
                "tune it",
                "print it",
                "Scan it",
                "send it",
                "fax - rename it",
                "Touch it",
                "bring it",
                "pay it",
                "watch it",
                "Turn it",
                "leave it",
                "start - format it"
        };
        List<String> collection = Arrays.asList(values);

        String out = typeHandler.getOutputFor(collection);

        assertNotNull(out);

        for (String val : values) {
            assertTrue(out.contains(val));
        }
    }
}

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

package io.zachbr.debuggery.reflection.bukkit;

import io.zachbr.debuggery.Logger;
import io.zachbr.debuggery.TestLoggerImpl;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import io.zachbr.debuggery.reflection.types.handlers.base.Handler;
import io.zachbr.debuggery.reflection.types.handlers.bukkit.BukkitBootstrap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BukkitTypeHandlerTest {
    private final Logger logger = new TestLoggerImpl();
    private final TypeHandler typeHandler = new TypeHandler(logger);

    public BukkitTypeHandlerTest() {
        new BukkitBootstrap(typeHandler, logger);
    }

    @Test
    public void validateAllHandlers() {
        List<Handler> allHandlers = new ArrayList<>();
        allHandlers.addAll(typeHandler.getAllInputHandlers());
        allHandlers.addAll(typeHandler.getAllOutputHandlers());

        // test to make sure all handlers have a class type associated with them
        // this is supposed to always be true but if you're just implementing classes really fast
        // your IDE or tool or whatever will tend to generate implementations with a null return and
        // you can miss it and this is cheap to test at compile time so what the hell why not

        @SuppressWarnings("ConstantConditions")
        Predicate<Handler> hasNoClassType = handler -> handler.getRelevantClass() == null;

        List<Handler> offending = allHandlers.stream()
                .filter(hasNoClassType)
                .peek(h -> System.out.println(h.getClass().getCanonicalName() + " has null associated class!"))
                .collect(Collectors.toList());

        assertEquals(0, offending.size());
    }
}

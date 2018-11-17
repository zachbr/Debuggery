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

import io.zachbr.debuggery.reflection.ReflTestClass;
import io.zachbr.debuggery.reflection.types.handlers.base.*;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Difficulty;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TypeHandlerTest {
    private static final Class CASE_NEVER_WILL_BE_REGISTERED = Assertions.class;

    @Test
    public void validateAllHandlers() {
        List<Handler> allHandlers = new ArrayList<>();
        allHandlers.addAll(TypeHandler.getInstance().getAllInputHandlers());
        allHandlers.addAll(TypeHandler.getInstance().getAllOutputHandlers());

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

    @Test
    public void ensurePolymorphicInput() throws InputException {
        // ensures that polymorphic input handlers are functioning correctly
        // todo - in future, test additional polymorphic type handlers
        Class[] requestedTypes = {Difficulty.class, WeatherType.class}; // Test various enums to be handled by IEnumHandler
        String[] inputArgs = {"EASY", "CLEAR"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(requestedTypes, Arrays.asList(inputArgs), null);

        assertTrue(output[0] instanceof Difficulty);
        assertTrue(output[1] instanceof WeatherType);
        assertEquals(Difficulty.EASY, output[0]);
        assertEquals(WeatherType.CLEAR, output[1]);
    }

    @Test
    public void ensureAddAndRemoveHandlers() {
        //
        // Handler additions
        //

        // attempt to add a string handler
        // this should clash and fail, returning false
        IHandler iStringHandler = new IHandler() {
            @NotNull
            @Override
            public Object instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return String.class;
            }
        };

        // same but with an output handler
        OHandler oCollectionHandler = new OHandler() {
            @NotNull
            @Override
            public String getFormattedOutput(Object object) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return Collection.class;
            }
        };

        final boolean stringInputAddSuccess = TypeHandler.getInstance().registerHandler(iStringHandler);
        final boolean collectionOutputAddSuccess = TypeHandler.getInstance().registerHandler(oCollectionHandler);

        // now test that adding a random new class will work
        Class testClassToRegister = ReflTestClass.class;

        IHandler iLocalClassHandler = new IHandler() {
            @NotNull
            @Override
            public Object instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return testClassToRegister;
            }
        };

        OHandler oLocalClassHandler = new OHandler() {
            @NotNull
            @Override
            public String getFormattedOutput(Object object) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return testClassToRegister;
            }
        };

        final boolean localClassInputAddSuccess = TypeHandler.getInstance().registerHandler(iLocalClassHandler);
        final boolean localClassOutputAddSuccess = TypeHandler.getInstance().registerHandler(oLocalClassHandler);

        //
        // Handler removals
        //

        // remove earlier additions
        final boolean iLocalClassRemoveByInstanceSuccess = TypeHandler.getInstance().removeHandler(iLocalClassHandler);
        final boolean oLocalClassRemoveByInstanceSucesss = TypeHandler.getInstance().removeHandler(oLocalClassHandler);

        // attempt removing default bootstrapped class handlers by class type
        final boolean iStringRemoveByClassSuccess = TypeHandler.getInstance().removeInputHandlerFor(String.class);
        final boolean oCollectionRemoveByClassSuccess = TypeHandler.getInstance().removeOutputHandlerFor(Collection.class);

        // attempt removing handlers for a class that doesn't exist
        IHandler iNotRegistered = new IHandler() {
            @NotNull
            @Override
            public Object instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) throws Exception {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return CASE_NEVER_WILL_BE_REGISTERED;
            }
        };

        OHandler oNotRegistered = new OHandler() {
            @NotNull
            @Override
            public String getFormattedOutput(Object object) {
                throw new NotImplementedException();
            }

            @NotNull
            @Override
            public Class<?> getRelevantClass() {
                return CASE_NEVER_WILL_BE_REGISTERED;
            }
        };

        final boolean removedUnregisteredIHandler = TypeHandler.getInstance().removeHandler(iNotRegistered);
        final boolean removedUnregisteredOHandler = TypeHandler.getInstance().removeHandler(oNotRegistered);

        //
        // Validation
        //

        // addition
        assertFalse(stringInputAddSuccess, "String Handler addition did not clash!");
        assertFalse(collectionOutputAddSuccess, "Collection handler addition did not clash!");
        assertTrue(localClassInputAddSuccess, "Unable to register new input local class!");
        assertTrue(localClassOutputAddSuccess, "Unable to register new output local class!");

        // removal
        assertTrue(iLocalClassRemoveByInstanceSuccess, "Unable to remove input local class by instance!");
        assertTrue(oLocalClassRemoveByInstanceSucesss, "Unable to remove output local class by instance!");
        assertTrue(iStringRemoveByClassSuccess, "Unable to remove input string handler by class type!");
        assertTrue(oCollectionRemoveByClassSuccess, "Unable to remove output collection handler by class type!");
        assertFalse(removedUnregisteredIHandler, "Attempting to remove an unregistered IHandler didnt return false!");
        assertFalse(removedUnregisteredOHandler, "Attempting to remvoe an unregistered OHandler didnt return false!");

        //
        // Cleanup
        //

        // null out our singleton reference in TypeHandler to prevent this test from conflicting with any other tests
        try {
            Field singleton = TypeHandler.class.getDeclaredField("singletonInstance");
            singleton.setAccessible(true);
            singleton.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            System.out.println("Unable to cleanup after TypeHandler add remove test: " + ex);
            ex.printStackTrace();
        }
    }

    @Test
    public void ensureThrowsOnNoSuchIHandler() {
        // verify that if we have an unknown type requested, it always throws
        Assertions.assertThrows(InputException.class, () -> {
            Class[] requestedType = {CASE_NEVER_WILL_BE_REGISTERED};
            TypeHandler.getInstance().instantiateTypes(requestedType, Collections.singletonList("blah"), null);
        });
    }

    @Test
    public void ensureNullAlwaysAvailable() throws InputException {
        Object[] array = TypeHandler.getInstance().instantiateTypes(new Class[]{Object.class}, Collections.singletonList(TypeHandler.NULL_INSTANCE_KEYWORD), null);
        Object instance = array[0];

        // verify we always have access to null

        assertNull(instance);
    }

    @Test
    public void ensureNullInputGivesNullOutput() {
        String output = TypeHandler.getInstance().getOutputFor(null);

        // verifies that the output handler never gives us anything for a null input object
        // this matters because it's how we know not to send any message at all, rather than an empty string or empty
        // brackets or something

        assertNull(output);
    }

    @Test
    public void ensureInputExceptionNeverWrapsItself() {
        // Make sure that InputExceptions never end up wrapping themselves
        // We only ever want to wrap the base cause

        Exception trueException = new NotImplementedException("hiya!");
        InputException baseException = new InputException(trueException);
        InputException level2 = new InputException(baseException);

        assertSame(trueException, level2.getCause());

        InputException level3 = new InputException(level2);
        InputException level4 = new InputException(level3);

        assertSame(trueException, level4.getCause());
    }
}

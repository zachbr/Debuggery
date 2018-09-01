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

package com.destroystokyo.debuggery.reflection.types;

import com.destroystokyo.debuggery.reflection.types.handlers.base.*;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.destroystokyo.debuggery.Debuggery.debugLn;

/**
 * Manages all class handling
 * <p>
 * All conversions from string -> object and vice-versa are implemented as individual
 * {@link IHandler} instances and accessed through this managing class
 */
public class TypeHandler {
    private static TypeHandler singletonInstance;

    // input handlers
    // keep these two in sync on additions and removals
    private final Map<Class, IHandler> inputHandlers = new HashMap<>();
    private final Set<IPolymorphicHandler> polymorphicHandlers = new HashSet<>();

    // output handlers
    private final Set<OHandler> outputHandlers = new HashSet<>();

    private TypeHandler() {
        Bootstrap.init(this);
    }

    /**
     * Gets an instance of the {@link TypeHandler} for use
     *
     * @return instance of {@link TypeHandler}
     */
    @Nonnull
    public static TypeHandler getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new TypeHandler();
        }

        return singletonInstance;
    }

    /**
     * Gets the formatted "friendly" {@link String} output for the given instance of
     * an object.
     *
     * @param object instance to get output for
     * @return String output or null
     */
    @Nullable
    public String getOutputFor(@Nullable Object object) {
        // if the object is null, just return null
        // indicating that we shouldn't send any input at all
        if (object == null) {
            return null;
        }

        // loop through all values looking for any assignable classes that
        // we can use the formatters on
        for (OHandler handler : outputHandlers) {
            if (handler.getRelevantClass().isAssignableFrom(object.getClass())) {
                debugLn("Found output handler " + handler + " for " + object.getClass().getCanonicalName());
                return handler.getFormattedOutput(object);
            }
        }

        // failing that, just give the generic toString
        debugLn("Unable to find compatible handler for " + object.getClass().getCanonicalName());
        return String.valueOf(object);
    }

    @Nonnull
    public Object[] instantiateTypes(Class[] classes, List<String> input, @Nullable CommandSender sender) throws InputException {
        Validate.notNull(classes);
        Validate.notNull(input);

        List<Object> outputObjects = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            outputObjects.add(instantiateObjectFor(classes[i], input.get(i), sender));
        }

        return outputObjects.toArray();
    }

    @Nullable
    private Object instantiateObjectFor(Class clazz, String input, @Nullable CommandSender sender) throws InputException {
        Validate.notNull(clazz);
        Validate.notNull(input);

        Object out = null;

        // if the user has explicitly requested null (via '\null\') then just give them null
        if (input.equals("\\null\\")) {
            return null;
        }

        // otherwise, lookup a handler and use it to instantiate an object
        IHandler handler = getIHandlerForClass(clazz);
        if (handler != null) {
            try {
                out = handler.instantiateInstance(input, clazz, sender);
            } catch (Exception ex) {
                // re-wrap exception and toss up the stack
                throw new InputException(ex);
            }
        } else {
            throw new InputException(new NotImplementedException("Input handling for class type " + clazz.getSimpleName() + " not implemented yet"));
        }

        return out;
    }

    /**
     * Registers a new handler to the system
     *
     * @param handler type handler
     * @return true if successfully registered
     */
    public boolean registerHandler(Handler handler) {
        Validate.notNull(handler);

        debugLn("Attempting to register handler: " + handler + " for class: " + handler.getRelevantClass().getCanonicalName());

        if (handler instanceof IHandler) {
            return registerInputHandler((IHandler) handler);
        } else {
            return registerOuputHandler((OHandler) handler);
        }
    }

    /**
     * Registers an input handler to the system
     *
     * @param handler type handler to register
     * @return true if sucessfully registered
     */
    private boolean registerInputHandler(IHandler handler) {
        final Class handlerRelevantClass = handler.getRelevantClass();

        // first, make sure there isn't an existing handler already registered to this type
        final IHandler existingHandler = inputHandlers.get(handlerRelevantClass);
        if (existingHandler != null) {
            debugLn("Handler clashing with " + existingHandler);
            return false;
        } else {
            inputHandlers.put(handlerRelevantClass, handler);
            debugLn("Added handler " + handler + " to Input Handlers");

            // if this handler is polymorphic, add it to that collection as well
            // we MUST keep these in sync with one another
            if (handler instanceof IPolymorphicHandler) {
                polymorphicHandlers.add((IPolymorphicHandler) handler);
                debugLn("Handler " + handler + " registered as polymorphic");
            }

            return true;
        }
    }

    /**
     * Registers an output handler to the system
     *
     * @param handler type handler to register
     * @return true if successfully added
     */
    private boolean registerOuputHandler(OHandler handler) {
        // first, make sure this handler isn't already registered
        if (outputHandlers.contains(handler)) {
            debugLn("Handler already registered!");
            return false;
        } else {
            outputHandlers.add(handler);
            debugLn("Added handler " + handler + " to Output Handlers");

            return true;
        }
    }

    /**
     * Removes a handler from the system
     *
     * @param handler type handler
     * @return true if successfully removed
     */
    public boolean removeHandler(Handler handler) {
        Validate.notNull(handler);

        debugLn("Attempting to remove handler: " + handler);

        if (handler instanceof IHandler) {
            return removeInputHandler((IHandler) handler);
        } else {
            return removeOutputHandler((OHandler) handler);
        }
    }

    /**
     * Removes an Input Handler from the input handling system
     *
     * @param handler type handler to remove
     * @return true if successfully removed
     */
    private boolean removeInputHandler(IHandler handler) {
        final Class clazz = handler.getRelevantClass();

        // make sure the given handler is even registered in the first place
        if (!inputHandlers.containsKey(clazz)) {
            debugLn("Input Handler doesn't appear to be registered, can't remove");
            return false;
        } else {
            inputHandlers.remove(clazz);
            debugLn("Removed handler " + handler + " from Input Handlers");

            // if we removed earlier and this is polymorphic, remove it from that collection
            // we MUST keep these in sync with one anotehr
            if (handler instanceof IPolymorphicHandler) {
                polymorphicHandlers.remove(handler);
                debugLn("Removed handler " + handler + " from polymorphic map");
            }

            return true;
        }
    }

    /**
     * Removes an Output Handler from the output handling system
     *
     * @param handler type handler to remove
     * @return true if successfully removed
     */
    private boolean removeOutputHandler(OHandler handler) {
        // make sure the given handler is even registered in the first place
        if (!outputHandlers.contains(handler)) {
            debugLn("Handler doesn't appear to be registered, can't remove");
            return false;
        } else {
            outputHandlers.remove(handler);
            debugLn("Removed handler " + handler + " from Output Handlers");

            return true;
        }
    }

    /**
     * Searches all handlers looking for the relevant handler for the given {@link Class}.
     * <p>
     * If it cannot find an explicit handler, it will fall back to looking through the
     * polymorphic handlers
     *
     * @param clazz {@link Class} type to look for a handler for
     * @return Relevant handler or null if none could be found
     */
    @Nullable
    private IHandler getIHandlerForClass(Class clazz) {
        Validate.notNull(clazz);

        // first check for an explicit input handler to use for this type
        IHandler handler = inputHandlers.get(clazz);
        if (!(handler == null)) {
            debugLn("Found input handler " + handler + " for " + clazz);
        } else {
            // otherwise fall back to a polymorphic handler lookup
            debugLn("Could not find any specific input handler for " + clazz + " , using polymorphic lookup...");
            handler = getIPolymorphicHandler(clazz);
        }

        return handler;
    }

    /**
     * Searches through the polymorphic handlers looking for one capable of handling
     * the specific {@link Class}
     *
     * @param clazz {@link Class} type to look for a handler for
     * @return Relevant handler or null if none could be found
     */
    @Nullable
    private IPolymorphicHandler getIPolymorphicHandler(Class clazz) {
        Validate.notNull(clazz);

        IPolymorphicHandler handler = null;
        for (IPolymorphicHandler potential : polymorphicHandlers) {
            // check if the polymorphic handler is capable of handling the given class
            if (potential.getRelevantClass().isAssignableFrom(clazz)) {
                debugLn("Found polymorphic input handler " + potential + " for " + clazz);

                handler = potential;
                break; // do not attempt to keep looking, we don't want overwriting
            }
        }

        if (handler == null) {
            debugLn("Unable to find any polymorphic input handler for " + clazz);
        }

        return handler;
    }

    /**
     * Gets an unmodifiable collection of all registered Input Handlers
     *
     * @return unmodifiable collection
     */
    Collection<IHandler> getAllInputHandlers() {
        return Collections.unmodifiableCollection(inputHandlers.values());
    }

    /**
     * Gets an unmodifiable collection all registered Output Handlers
     *
     * @return unmodifiable collection
     */
    Collection<OHandler> getAllOutputHandlers() {
        return Collections.unmodifiableCollection(outputHandlers);
    }
}

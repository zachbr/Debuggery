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

import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Represents a chain of reflection operations
 */
public class ReflectionChain {
    private final List<String> input;
    private final Object initialInstance;
    @Nullable
    private final CommandSender owner;
    private Object endingInstance;

    public ReflectionChain(@NotNull String[] args, @NotNull Object initialInstance, @Nullable CommandSender owner) {
        this.input = Arrays.asList(args);
        this.initialInstance = initialInstance;
        this.owner = owner;
    }

    /**
     * Performs the reflection operation
     *
     * @return final object instance at the end of the chain
     * @throws InputException            see {@link TypeHandler#instantiateTypes(Class[], List, CommandSender)}
     * @throws InvocationTargetException see {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException    see {@link Method#invoke(Object, Object...)}
     */
    public Object chain() throws InputException, InvocationTargetException, IllegalAccessException {
        Map<String, Method> reflectionMap;
        Object result = initialInstance;

        Method currentMethod;
        Object[] methodParameters;
        int argsToSkip = 0;

        for (int i = 0; i < input.size(); i++) {
            String currentArg = input.get(i);
            if (argsToSkip > 0) {
                argsToSkip--;
                continue;
            }

            Validate.notNull(result);
            reflectionMap = ReflectionUtil.getMethodMapFor(result.getClass());

            currentMethod = reflectionMap.get(currentArg);
            if (currentMethod == null) {
                result = ChatColor.RED + "Unknown or unavailable method";
                break;
            }

            List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(this.input.subList(i + 1, input.size()), currentMethod);
            argsToSkip = stringMethodArgs.size();

            methodParameters = TypeHandler.getInstance().instantiateTypes(currentMethod.getParameterTypes(), stringMethodArgs, this.owner);
            result = reflect(result, currentMethod, methodParameters);

            if (currentMethod.getReturnType() != Void.TYPE && result == null) {
                result = ChatColor.RED + ReflectionUtil.getFormattedMethodSignature(currentMethod) + " returned null!";
                break;
            }

        }

        this.endingInstance = result;
        return result;
    }

    /**
     * Gets the object instance that this reflection chain ended at, or null
     * if the chain was never started.
     *
     * @return ending object instance or null
     */
    @Nullable
    public Object getEndingInstance() {
        return this.endingInstance;
    }

    /**
     * Performs an individual reflective operation
     *
     * @param instance what to operate on
     * @param method   what to call
     * @param args     parameters to pass to operation
     * @return resulting object instance or null if that's the correct result
     * @throws InvocationTargetException see {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException    see {@link Method#invoke(Object, Object...)}
     */
    @Nullable
    private Object reflect(@NotNull Object instance, @NotNull Method method, @NotNull Object[] args) throws InvocationTargetException, IllegalAccessException {
        final int paramCount = method.getParameterCount();

        if (args.length != paramCount) {
            return ReflectionUtil.getArgMismatchString(method);
        }

        if (!method.canAccess(instance)) {
            method.setAccessible(true);
        }

        return method.invoke(instance, args);
    }
}

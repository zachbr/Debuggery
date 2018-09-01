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

package com.destroystokyo.debuggery.reflection;

import com.destroystokyo.debuggery.reflection.types.InputException;
import com.destroystokyo.debuggery.reflection.types.TypeHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    public ReflectionChain(@Nonnull String[] args, @Nonnull Object initialInstance, @Nullable CommandSender owner) {
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
        Object[] methodParameters;
        Object result = initialInstance;

        int argsToSkip = 0;

        for (int i = 0; i < input.size(); i++) {
            String currentArg = input.get(i);
            if (argsToSkip > 0) {
                argsToSkip--;
                continue;
            }

            reflectionMap = ReflectionUtil.getMethodMapFor(result.getClass());
            if (reflectionMap.get(currentArg) == null) {
                result = ChatColor.RED + "Unknown or unavailable method";
                break;
            }


            Method method = reflectionMap.get(currentArg);
            List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(this.input.subList(i + 1, input.size()), method);
            argsToSkip = stringMethodArgs.size();

            methodParameters = TypeHandler.getInstance().instantiateTypes(method.getParameterTypes(), stringMethodArgs, this.owner);
            result = reflect(result, method, methodParameters);

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

    @Nullable
    private Object reflect(@Nonnull Object instance, @Nonnull Method method, @Nonnull Object[] args) throws InvocationTargetException, IllegalAccessException {
        final int paramCount = method.getParameterCount();

        if (args.length != paramCount) {
            return ReflectionUtil.getArgMismatchString(method);
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        return method.invoke(instance, args);
    }
}

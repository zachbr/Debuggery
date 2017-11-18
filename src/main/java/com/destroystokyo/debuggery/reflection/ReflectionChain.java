/*
 * This file is part of Debuggery.
 *
 * Debuggery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * distributed with this repository.
 *
 * Debuggery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Debuggery. If not, see <http://www.gnu.org/licenses/>.
 */

package com.destroystokyo.debuggery.reflection;

import com.destroystokyo.debuggery.reflection.formatters.InputException;
import com.destroystokyo.debuggery.reflection.formatters.InputFormatter;
import com.destroystokyo.debuggery.reflection.formatters.OutputFormatter;
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
    private final List<String> input = new ArrayList<>();
    private final Object initialInstance;
    @Nullable
    private final CommandSender owner;

    public ReflectionChain(@Nonnull String[] args, @Nonnull Object initialInstance, @Nullable CommandSender owner) {
        input.addAll(Arrays.asList(args));
        this.initialInstance = initialInstance;
        this.owner = owner;
    }

    /**
     * Performs the reflection operation
     *
     * @return Formatted output as a result of the operation
     * @throws InputException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String startChain() throws InputException, InvocationTargetException, IllegalAccessException {
        Map<String, Method> reflectionMap;
        Object[] methodArgs;
        Object result = initialInstance;

        int argsToSkip = 0;
        Iterator<String> iterator = input.iterator();

        while (iterator.hasNext()) {
            String currentArg = iterator.next();
            if (argsToSkip > 0) {
                iterator.remove();
                argsToSkip--;
                continue;
            }

            reflectionMap = ReflectionUtil.getMethodMapFor(result.getClass());

            if (reflectionMap.get(currentArg) != null) {
                Method method = reflectionMap.get(currentArg);
                List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(this.input.subList(1, input.size()), method);
                argsToSkip = stringMethodArgs.size();

                methodArgs = InputFormatter.getTypesFromInput(method.getParameterTypes(), stringMethodArgs, this.owner);
                result = reflect(result, method, methodArgs);
                iterator.remove();
            } else {
                result = ChatColor.RED + "Unknown or unavailable method";
                break;
            }
        }

        return OutputFormatter.getOutput(result);
    }

    private Object reflect(Object instance, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (args.length != method.getParameterCount()) {
            return "Method " + method.getName() + " requires " + method.getParameterCount() + " args.\n"
                    + ReflectionUtil.getFormattedMethodSignature(method);
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        Object result;
        if (method.getParameterCount() == 0) {
            result = method.invoke(instance);
        } else {
            result = method.invoke(instance, args);
        }

        return result;
    }
}

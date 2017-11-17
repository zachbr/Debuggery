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
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a reflection invocation operation
 *
 * @param <T> Instance type
 */
public class ReflectCall<T> {
    private final T instance;
    private final Method method;
    private final String[] inputArgs;
    @Nullable
    private final CommandSender sender;

    public ReflectCall(T instance, Method method, String[] inputArgs, @Nullable CommandSender sender) {
        this.instance = instance;
        this.method = method;
        this.inputArgs = inputArgs;
        this.sender = sender;
    }

    /**
     * Performs the reflection operation
     *
     * @return Formatted output as a result of the operation
     * @throws InputException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String reflect() throws InputException, InvocationTargetException, IllegalAccessException {
        final int paramCount = method.getParameterCount();

        if (paramCount != inputArgs.length) {
            return "Method " + method.getName() + " requires " + method.getParameterCount() + " args.\n"
                    + ReflectionUtil.getFormattedMethodSignature(method);
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        Object result;
        if (paramCount == 0) {
            result = this.method.invoke(instance);
        } else {
            final Object[] methodArgs = InputFormatter.getTypesFromInput(method.getParameterTypes(), inputArgs, sender);
            result = this.method.invoke(instance, methodArgs);
        }

        return OutputFormatter.getOutput(result);
    }
}

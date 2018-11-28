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

package io.zachbr.debuggery.reflection.chain;

import io.zachbr.debuggery.reflection.*;
import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import org.jetbrains.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Represents a chain of reflection operations
 */
class ReflectionChain {
    private final MethodMapProvider methodMapProvider;
    private final TypeHandler typeHandler;
    private final List<String> input;
    private final Object initialInstance;
    private ReflectionResult result;

    ReflectionChain(MethodMapProvider mapProvider, TypeHandler handler, @NotNull String[] args, @NotNull Object initialInstance) {
        this.methodMapProvider = mapProvider;
        this.typeHandler = handler;
        this.input = Arrays.asList(args);
        this.initialInstance = initialInstance;
    }

    /**
     * Performs the reflection operation
     *
     * @return result, containing final instance and any additional data
     */
    @NotNull ReflectionResult runChain() {
        MethodMap reflectionMap;
        Object currentInstance = initialInstance;
        ReflectionResult result = null;

        Method currentMethod;
        Object[] methodParameters;
        int argsToSkip = 0;

        for (int i = 0; i < input.size(); i++) {
            String currentArg = input.get(i);
            if (argsToSkip > 0) {
                argsToSkip--;
                continue;
            }

            Objects.requireNonNull(currentInstance);
            reflectionMap = methodMapProvider.getMethodMapFor(currentInstance.getClass());

            currentMethod = reflectionMap.getById(currentArg);
            if (currentMethod == null) {
                result = new ReflectionResult(ReflectionResult.Type.UNKNOWN_REFERENCE, null,
                        "Unknown or unavailable method");
                break;
            }

            List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(this.input.subList(i + 1, input.size()), currentMethod);
            argsToSkip = stringMethodArgs.size();

            try {
                methodParameters = typeHandler.instantiateTypes(currentMethod.getParameterTypes(), stringMethodArgs);
                currentInstance = reflect(currentInstance, currentMethod, methodParameters);
            } catch (Throwable ex) {
                ReflectionResult.Type type = ex instanceof InputException ? ReflectionResult.Type.INPUT_ERROR : ReflectionResult.Type.UNHANDLED_EXCEPTION;
                Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                String reason = cause.getLocalizedMessage() != null ? cause.getLocalizedMessage() : cause.toString();

                result = new ReflectionResult(type, null, reason, cause);
                break;
            }

            if (currentMethod.getReturnType() != Void.TYPE && currentInstance == null) {
                result = new ReflectionResult(ReflectionResult.Type.NULL_REFERENCE, null,
                        ReflectionUtil.getFormattedMethodSignature(currentMethod) + " returned null!");
                break;
            }
        }

        if (result == null) {
            // if we've made it this far without any result, we should assume that the operation was a
            // success and create a new successful result object with the currentInstance as the result
            result = new ReflectionResult(ReflectionResult.Type.SUCCESS, currentInstance);
        }

        Objects.requireNonNull(result);
        return this.result = result;
    }

    /**
     * Gets the results of the reflective operation
     *
     * @return results
     * @throws IllegalStateException if called before the chain is run
     */
    @NotNull ReflectionResult getResult() {
        if (this.result == null) {
            throw new IllegalStateException("Cannot return a result before the chain is run!");
        }

        return this.result;
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
    private @Nullable Object reflect(@NotNull Object instance, @NotNull Method method, @NotNull Object[] args) throws InvocationTargetException, IllegalAccessException {
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

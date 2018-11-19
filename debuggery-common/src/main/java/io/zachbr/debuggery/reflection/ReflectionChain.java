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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Represents a chain of reflection operations
 */
public class ReflectionChain {
    private final MethodMapProvider methodMapProvider;
    private final TypeHandler typeHandler;
    private final List<String> input;
    private final Object initialInstance;
    private Result result;

    public ReflectionChain(MethodMapProvider mapProvider, TypeHandler handler, @NotNull String[] args, @NotNull Object initialInstance) {
        this.methodMapProvider = mapProvider;
        this.typeHandler = handler;
        this.input = Arrays.asList(args);
        this.initialInstance = initialInstance;
    }

    /**
     * Performs the reflection operation
     *
     * @return final object instance at the end of the chain
     * @throws InputException            see {@link TypeHandler#instantiateTypes(Class[], List)}
     * @throws InvocationTargetException see {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException    see {@link Method#invoke(Object, Object...)}
     */
    public @NotNull Result chain() throws InputException, InvocationTargetException, IllegalAccessException {
        MethodMap reflectionMap;
        Object currentInstance = initialInstance;
        Result result = null;

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
                result = new Result(Result.Type.UNKNOWN_REFERENCE, null,
                        "Unknown or unavailable method");
                break;
            }

            List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(this.input.subList(i + 1, input.size()), currentMethod);
            argsToSkip = stringMethodArgs.size();

            methodParameters = typeHandler.instantiateTypes(currentMethod.getParameterTypes(), stringMethodArgs);
            currentInstance = reflect(currentInstance, currentMethod, methodParameters);

            if (currentMethod.getReturnType() != Void.TYPE && currentInstance == null) {
                result = new Result(Result.Type.NULL_REFERENCE, null,
                        ReflectionUtil.getFormattedMethodSignature(currentMethod) + " returned null!");
                break;
            }
        }

        if (result == null) {
            // if we've made it this far without any result, we should assume that the operation was a
            // success and create a new successful result object with the currentInstance as the result
            result = new Result(Result.Type.SUCCESS, currentInstance, null);
        }

        Objects.requireNonNull(result);
        return this.result = result;
    }

    /**
     * Gets the results of the reflective operation or null if this was called before
     * the reflective operation has completed after running.
     *
     * @return results or null
     */
    public @Nullable Result getResult() {
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

    public static class Result {
        /**
         * Refers to the general type of outcome of this Result
         */
        public enum Type {
            /**
             * The reflective operation was successful
             */
            SUCCESS,
            /**
             * The reflective operation encountered a null reference and was unsuccessful
             */
            NULL_REFERENCE,
            /**
             * The reflective operation encountered an unknown or unavailable method call and was unsuccessful
             */
            UNKNOWN_REFERENCE
        }

        private final Type type;
        private final String reason;
        private final Object endingInstance;

        Result(@NotNull Type type, @Nullable Object endingInstance, @Nullable String reason) {
            Objects.requireNonNull(type);

            this.type = type;
            this.endingInstance = endingInstance;
            this.reason = reason;
        }

        /**
         * Gets the general outcome type of this Result
         *
         * @return general outcome
         */
        public @NotNull Type getType() {
            return this.type;
        }

        /**
         * Gets a String detailing what may have caused the outcome
         * <p>
         * This will typically be null if the outcome completed without issues
         *
         * @return rationale or null
         */
        public @Nullable String getReason() {
            return this.reason;
        }

        /**
         * Gets the final object that was produced as a result of the operation. Null is
         * a perfectly acceptable return type even if the outcome was a success
         *
         * @return resulting object, including null
         */
        public @Nullable Object getEndingInstance() {
            return this.endingInstance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return type == result.type &&
                    Objects.equals(reason, result.reason) &&
                    Objects.equals(endingInstance, result.endingInstance);
        }

        @Override
        public int hashCode() {
            return 47 * type.hashCode() * Objects.hashCode(reason) * Objects.hashCode(endingInstance);
        }
    }
}

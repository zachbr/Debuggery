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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents the results of a reflective operation
 */
public class ReflectionResult {
    private final Type type;
    private final String reason;
    private final Object endingInstance;
    private final Throwable exception;

    ReflectionResult(@NotNull Type type, @Nullable Object endingInstance) {
        this(type, endingInstance, null);
    }

    ReflectionResult(@NotNull Type type, @Nullable Object endingInstance, @Nullable String reason) {
        this(type, endingInstance, reason, null);
    }

    ReflectionResult(@NotNull Type type, @Nullable Object endingInstance, @Nullable String reason, @Nullable Throwable exception) {
        Objects.requireNonNull(type);

        if (type != Type.SUCCESS && reason == null) {
            throw new IllegalArgumentException("Should not be an unsuccessful result without a reason!");
        }

        if (type == Type.UNHANDLED_EXCEPTION && exception == null || type == Type.INPUT_ERROR && exception == null) {
            throw new IllegalArgumentException("Cannot report a result of an exception type without the exception!");
        }

        this.type = type;
        this.endingInstance = endingInstance;
        this.reason = reason;
        this.exception = exception;
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

    /**
     * Gets the exception (throwable) that was encountered during execution. This is
     * expected to be null if no exception was thrown
     *
     * @return exception or null
     */
    public @Nullable Throwable getException() {
        return this.exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReflectionResult result = (ReflectionResult) o;
        return type == result.type &&
                Objects.equals(reason, result.reason) &&
                Objects.equals(endingInstance, result.endingInstance) &&
                Objects.equals(exception, result.exception);
    }

    @Override
    public int hashCode() {
        int hash = 43;
        hash = 79 * hash + type.hashCode();
        hash = 79 * hash + Objects.hashCode(reason);
        hash = 79 * hash + Objects.hashCode(endingInstance);
        hash = 79 * hash + Objects.hashCode(exception);
        return hash;
    }

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
        UNKNOWN_REFERENCE,
        /**
         * The reflective operation encountered an unhandled exception
         */
        UNHANDLED_EXCEPTION,
        /**
         * The reflective operation encountered an issue parsing a user's input
         */
        INPUT_ERROR
    }
}

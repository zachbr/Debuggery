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
public record ReflectionResult(
        @NotNull Type type,
        @Nullable Object endingInstance,
        @Nullable String reason,
        @Nullable Throwable exception
) {
    ReflectionResult(@NotNull Type type, @Nullable Object endingInstance) {
        this(type, endingInstance, null);
    }

    ReflectionResult(@NotNull Type type, @Nullable Object endingInstance, @Nullable String reason) {
        this(type, endingInstance, reason, null);
    }

    public ReflectionResult(@NotNull Type type, @Nullable Object endingInstance, @Nullable String reason, @Nullable Throwable exception) {
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
        INPUT_ERROR,
        /**
         * The number of arguments given do not match the requested method requirements
         */
        ARG_MISMATCH
    }
}

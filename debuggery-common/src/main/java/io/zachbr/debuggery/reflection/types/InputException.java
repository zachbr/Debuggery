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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Wrapper so that we can properly inform the user when it's their input
 * that's causing the problem, rather than some API/Implementation contract.
 */
public class InputException extends Exception {

    /**
     * See {@link #of(Throwable)}
     *
     * @param throwable what to wrap
     */
    private InputException(@NotNull Throwable throwable) {
        super(throwable);
        if (throwable instanceof InputException) {
            throw new IllegalArgumentException("An input exception cannot re-wrap itself. Stop doing bad things!");
        }
    }

    /**
     * Creates a new InputException wrapping the given {@link Throwable}
     *
     * @param throwable instance to wrap
     * @return new InputException wrapping the given throwable
     */
    static InputException of(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable);

        Throwable toWrap = throwable;
        if (throwable instanceof InputException) {
            toWrap = throwable.getCause();
        }

        return new InputException(toWrap);
    }

    @Override
    public @NotNull String getMessage() {
        return getCause().getMessage();
    }

    @Override
    public @NotNull String getLocalizedMessage() {
        return getCause().getLocalizedMessage();
    }
}

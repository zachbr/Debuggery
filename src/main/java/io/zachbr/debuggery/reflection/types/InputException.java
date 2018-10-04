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

import javax.annotation.Nonnull;

/**
 * Wrapper so that we can properly inform the user when it's their input
 * that's causing the problem, rather than some API/Implementation contract.
 */
public class InputException extends Exception {
    @Nonnull
    private final Throwable wrappedException;

    InputException(@Nonnull Throwable throwable) {
        if (throwable instanceof InputException) {
            // Ensure we do not re-wrap ourselves here
            throwable = ((InputException) throwable).getCause();
        }

        this.wrappedException = throwable;
    }

    @Override
    @Nonnull
    public Throwable getCause() {
        return wrappedException;
    }
}

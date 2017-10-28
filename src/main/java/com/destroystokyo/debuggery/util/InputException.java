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

package com.destroystokyo.debuggery.util;

import javax.annotation.Nonnull;

/**
 * Wrapper so that we can properly inform the user when it's their input
 * that's causing the problem, rather than some API/Implementation contract.
 */
public class InputException extends Exception {
    @Nonnull
    private final Throwable wrappedException;

    InputException(@Nonnull Throwable throwable) {
        this.wrappedException = throwable;
    }

    @Nonnull
    public Throwable getCause() {
        return wrappedException;
    }
}

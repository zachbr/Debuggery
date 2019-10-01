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
 * Thrown when the handler does not have an implementation for the requested class type
 */
public class HandlerNotImplementedException extends RuntimeException {
    private final @NotNull Class<?> requested;

    public HandlerNotImplementedException(@NotNull Class<?> attempted) {
        super("Handler for type: " + Objects.requireNonNull(attempted).getCanonicalName() + " has not been implemented yet!");
        this.requested = attempted;
    }

    public @NotNull Class<?> getRequestedClass() {
        return this.requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerNotImplementedException that = (HandlerNotImplementedException) o;
        return requested.equals(that.requested);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requested);
    }
}

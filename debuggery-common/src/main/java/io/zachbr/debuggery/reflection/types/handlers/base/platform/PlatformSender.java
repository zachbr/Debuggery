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

package io.zachbr.debuggery.reflection.types.handlers.base.platform;

import io.zachbr.debuggery.util.PlatformType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Wrapper class to provide some basic information about platform-specific
 * command senders being sent through the type handling system.
 * <p>
 * These platform-specific senders can be used by {@link PlatformExtension}s to
 * provide platform-specific functionality even on classes implemented in other modules.
 *
 * @param <T> sender type
 */
public class PlatformSender<T> {
    private final T rawSender;
    private final PlatformType type;

    public PlatformSender(@NotNull PlatformType type, @NotNull T rawSender) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(rawSender);

        this.type = type;
        this.rawSender = rawSender;
    }

    /**
     * Gets the raw command sender object instance passed through the type
     * handler system at runtime.
     *
     * @return sender instance
     */
    public @NotNull T getRawSender() {
        return rawSender;
    }

    /**
     * Gets the {@link PlatformType} the sender came from
     *
     * @return platform
     */
    public @NotNull PlatformType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformSender that = (PlatformSender) o;
        return rawSender.equals(that.rawSender) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawSender, type);
    }

    @Override
    public String toString() {
        return "{" + type.toString() + ", " + rawSender.toString() +"}";
    }
}

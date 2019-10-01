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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A platform extension is a platform-specific {@link io.zachbr.debuggery.reflection.types.handlers.base.IHandler}
 * extension that allows specific platforms to override and provide alternative implementations for instantiating
 * objects that are otherwise defined in the common modules.
 *
 * @param <T> class type
 */
@FunctionalInterface
public interface PlatformExtension<T> {
    /**
     * Instantiates an instance of T using the given params
     *
     * @param args command input
     * @param clazz requested class type
     * @param sender platform command sender instance
     * @return instantiated object or null if it could not be instantiated
     */
    @Nullable T getInstance(String args, Class<?> clazz, @NotNull PlatformSender<?> sender);
}

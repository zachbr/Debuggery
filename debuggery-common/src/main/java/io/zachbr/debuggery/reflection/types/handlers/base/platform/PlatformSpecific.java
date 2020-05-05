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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Used to mark that a handler supports platform-specific extensions
 */
public abstract class PlatformSpecific<T> {
    private final List<PlatformExtension<T>> extensions = new ArrayList<>();

    /**
     * Adds a platform specific extension
     *
     * @param extension to add
     */
    public final void add(PlatformExtension<T> extension) {
        this.extensions.add(extension);
    }

    /**
     * Gets a collection of all currently registered platform extensions
     *
     * @return collection of platform extensions
     */
    public final Collection<PlatformExtension<T>> query() {
        return new ArrayList<>(this.extensions);
    }

    /**
     * Gets an instance of T from the currently registered extensions
     * @param input string args
     * @param clazz requested class
     * @param sender requester
     * @return instance of T or null if one cannot be instantiated from the given params
     */
    @Contract("_, _, null -> null")
    protected final @Nullable T fromExtensions(String input, Class<?> clazz, PlatformSender<?> sender) {
        if (sender == null || this.extensions.isEmpty()) {
            return null;
        }

        T platformSpecific = null;
        for (PlatformExtension<T> extension : extensions) {
            platformSpecific = extension.getInstance(input, clazz, sender);
            if (platformSpecific != null) {
                break;
            }
        }

        return platformSpecific;
    }
}

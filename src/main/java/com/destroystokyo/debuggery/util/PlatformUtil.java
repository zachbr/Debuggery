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

/**
 * Contains platform specific utilities that I'd rather not
 * have stuffed in the main class.
 */
public class PlatformUtil {
    private static Boolean runningSpigotOrDeriv = null;

    /**
     * Gets whether or not this server is running Spigot or a derivative
     *
     * @return true if the server is running Spigot or a derivative
     */
    public static boolean isServerRunningSpigotOrDeriv() {
        if (runningSpigotOrDeriv == null) {
            try {
                runningSpigotOrDeriv = Class.forName("org.spigotmc.SpigotConfig") != null;
            } catch (ClassNotFoundException ignored) {
                runningSpigotOrDeriv = false;
            }
        }

        return runningSpigotOrDeriv;
    }
}

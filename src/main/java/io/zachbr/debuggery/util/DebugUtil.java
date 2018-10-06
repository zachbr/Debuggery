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

package io.zachbr.debuggery.util;

import io.zachbr.debuggery.Debuggery;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DebugUtil {
    private static final boolean DEBUG_MODE = Boolean.getBoolean("debuggery.debug");
    private static final boolean DUMMY_SERVER = Bukkit.getServer() == null;
    private static Debuggery plugin;

    /**
     * Gets whether the plugin is running in debug mode
     *
     * @return True if debug mode enabled
     */
    public static boolean isDebugMode() {
        return DebugUtil.DEBUG_MODE;
    }

    public static void printDebugInfo() {
        if (!isDebugMode()) {
            return;
        }

        DebugUtil.debugLn("========================");
        DebugUtil.debugLn("Debuggery Ver: " + getPlugin().getDescription().getVersion());
        DebugUtil.debugLn("Server Impl: " + Bukkit.getServer().getName());
        DebugUtil.debugLn("Server Ver: " + Bukkit.getServer().getVersion());
        DebugUtil.debugLn("Impl API Ver: " + Bukkit.getServer().getBukkitVersion());
        DebugUtil.debugLn("Java Runtime: " + System.getProperty("java.runtime.version"));
        DebugUtil.debugLn("Operating System: " + System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + " "
                + System.getProperty("os.version"));
        DebugUtil.debugLn("========================");
    }

    /**
     * Writes a debug log message if the plugin's debug mode has been enabled
     *
     * @param arg message to log
     */
    public static void debugLn(String arg) {
        if (!isDebugMode()) {
            return;
        }

        final String out = "DEBUG: " + arg;

        if (DUMMY_SERVER) {
            System.out.println(out);
        } else {
            getPlugin().getLogger().warning(out);
        }
    }

    private static Debuggery getPlugin() {
        if (plugin == null) {
            Plugin byName = Bukkit.getServer().getPluginManager().getPlugin("Debuggery");
            if (byName == null) {
                // account for stupidity
                throw new AssertionError("Congrats, you broke it.");
            }

            plugin = (Debuggery) byName;
        }

        return plugin;
    }
}

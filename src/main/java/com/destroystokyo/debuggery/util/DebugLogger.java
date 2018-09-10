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

package com.destroystokyo.debuggery.util;

import com.destroystokyo.debuggery.Debuggery;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DebugLogger {
    private static final boolean DUMMY_SERVER = Bukkit.getServer() == null;
    private static Debuggery instance;

    public static void debugLn(String arg) {
        if (!Debuggery.DEBUG_MODE) {
            return;
        }

        final String out = "DEBUG: " + arg;

        if (DUMMY_SERVER) {
            System.out.println(out);
        } else {
            if (instance == null) {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Debuggery");
                if (plugin == null) {
                    // account for stupidity
                    throw new AssertionError("Congrats, you broke it.");
                }

                instance = (Debuggery) plugin;
            }

            instance.getLogger().warning(out);
        }
    }
}

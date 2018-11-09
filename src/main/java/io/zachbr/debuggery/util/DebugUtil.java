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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DebugUtil {
    private static final boolean DEBUG_MODE = Boolean.getBoolean("debuggery.debug");
    private static final boolean DUMMY_SERVER = Bukkit.getServer() == null;
    private static DebugUtil instance;

    private final Debuggery plugin;
    private final Set<CommandSender> debugListeners = new HashSet<>();

    public DebugUtil(Debuggery instance) {
        this.plugin = instance;
        DebugUtil.instance = this;
    }

    public static boolean isDebugMode() {
        return DebugUtil.DEBUG_MODE;
    }

    /**
     * Logs a message to the debugging log stream
     * <p>
     * Which is currently just the normal one with a prefix
     *
     * @param arg what to log
     */
    public static void debugLn(@NotNull String arg) {
        if (!isDebugMode()) {
            return;
        }

        final String out = "DEBUG: " + arg;

        if (DUMMY_SERVER) {
            System.out.println(out);
        } else {
            if (instance == null) {
                throw new NullPointerException("Null instance at runtime!");
            }

            String colorOut = ChatColor.YELLOW + out;

            instance.plugin.getLogger().info(colorOut);
            for (CommandSender sender : instance.debugListeners) {
                sender.sendMessage(colorOut);
            }
        }
    }

    /**
     * Prints system information
     */
    public void printSystemInfo() {
        if (!isDebugMode()) {
            return;
        }

        debugLn("========================");
        for (String line : getSystemInfo()) {
            debugLn(line);
        }
        debugLn("========================");
    }

    /**
     * Gets system information as an array of lines
     *
     * @return system information
     */
    public @NotNull String[] getSystemInfo() {
        List<String> out = new ArrayList<>();

        out.add("Debuggery Ver: " + plugin.getDescription().getVersion());
        out.add("Server Impl: " + Bukkit.getServer().getName());
        out.add("Server Ver: " + Bukkit.getServer().getVersion());
        out.add("Impl API Ver: " + Bukkit.getServer().getBukkitVersion());
        out.add("Java Runtime: " + System.getProperty("java.runtime.version"));
        out.add("Operating System: " + System.getProperty("os.name") + " "
                + System.getProperty("os.version") + " "
                + "(" + System.getProperty("os.arch") + ")");

        return out.toArray(new String[0]);
    }

    public @NotNull Set<CommandSender> getListeners() {
        return debugListeners;
    }
}

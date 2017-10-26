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

package com.destroystokyo.debuggery.commands.base;

import com.destroystokyo.debuggery.util.OutputFormatter;
import com.destroystokyo.debuggery.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandReflection extends CommandBase {
    private final Map<String, Method> availableMethods = new HashMap<>();
    private final Class classType;

    public CommandReflection(String name, String permission, boolean requiresPlayer, Class clazz) {
        super(name, permission, requiresPlayer);
        this.classType = clazz;
        ReflectionUtil.getAllPublicMethods(clazz).stream()
                .filter(ReflectionUtil.IS_GETTER)
                .filter(ReflectionUtil.HAS_NO_PARAMS)
                .forEach(m -> availableMethods.put(m.getName(), m));
    }

    @Override
    protected boolean helpLogic(CommandSender sender, String[] args) {
        sender.sendMessage("Uses reflection to call API methods built into Bukkit.");
        sender.sendMessage("Try using the tab completion to see all available subcommands.");
        return true;
    }

    /**
     * Handles all the reflection based command logic
     *
     * @param sender sender to send information to
     * @param args   command arguments
     * @param object instance of the class type
     * @return true if handled successfully
     */
    protected boolean doReflectionLookups(CommandSender sender, String[] args, Object object) {
        Validate.isInstanceOf(classType, object);
        if (args.length == 1) {
            final String arg = args[0];

            if (availableMethods.containsKey(arg)) {
                Method method = availableMethods.get(arg);
                try {
                    sender.sendMessage(OutputFormatter.getOutput(method.invoke(object)));
                    return true;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown or unavailable method");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "Unknown function");
        return true;
    }

    @Override
    protected Collection<String> getTabCompletions() {
        return availableMethods.keySet();
    }
}

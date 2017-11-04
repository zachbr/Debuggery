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

import com.destroystokyo.debuggery.util.PlatformUtil;
import com.destroystokyo.debuggery.util.ReflectCall;
import com.destroystokyo.debuggery.util.ReflectionUtil;
import com.destroystokyo.debuggery.util.formatters.FancyExceptionFormatter;
import com.destroystokyo.debuggery.util.formatters.InputException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public abstract class CommandReflection extends CommandBase {
    private Map<String, Method> availableMethods;
    private Class classType;

    protected CommandReflection(String name, String permission, boolean requiresPlayer, Class clazz) {
        super(name, permission, requiresPlayer);
        updateReflectionClass(clazz);
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
        final String inputMethod = args[0];

        if (!availableMethods.containsKey(inputMethod)) {
            sender.sendMessage(ChatColor.RED + "Unknown or unavailable method");
            return true;
        }

        Method method = availableMethods.get(inputMethod);
        String[] methodArgs = ArrayUtils.removeElement(args, args[0]);
        String output;

        try {
            output = new ReflectCall<>(object, method, methodArgs, sender).reflect();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InputException ex) {
            final String errorMessage = ex instanceof InputException ? "Exception deducing proper types from your input!" : "Exception invoking method - See console for more details!";
            final Throwable cause = ex.getCause() == null ? ex : ex.getCause();

            if (PlatformUtil.isServerRunningSpigotOrDeriv()) {
                FancyExceptionFormatter.sendFancyChatException(sender, errorMessage, cause);
            } else {
                sender.sendMessage(ChatColor.RED + errorMessage);
            }

            ex.printStackTrace();
            return true;
        }

        if (output != null) {
            sender.sendMessage(output);
        }

        return true;
    }

    protected void updateReflectionClass(Class typeIn) {
        if (this.classType != typeIn) {
            availableMethods = ReflectionUtil.createMethodMapFor(typeIn);
            this.classType = typeIn;
        }
    }

    @Override
    protected Collection<String> getTabCompletions() {
        return availableMethods.keySet();
    }
}

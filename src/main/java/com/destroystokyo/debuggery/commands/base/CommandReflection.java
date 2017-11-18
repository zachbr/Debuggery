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

import com.destroystokyo.debuggery.reflection.ReflectionChain;
import com.destroystokyo.debuggery.reflection.ReflectionUtil;
import com.destroystokyo.debuggery.reflection.formatters.InputException;
import com.destroystokyo.debuggery.util.FancyChatException;
import com.destroystokyo.debuggery.util.PlatformUtil;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
     * @param sender   sender to send information to
     * @param args     command arguments
     * @param instance instance of the class type
     * @return true if handled successfully
     */
    protected boolean doReflectionLookups(CommandSender sender, String[] args, Object instance) {
        Validate.isInstanceOf(classType, instance);
        final String inputMethod = args[0];

        if (!availableMethods.containsKey(inputMethod)) {
            sender.sendMessage(ChatColor.RED + "Unknown or unavailable method");
            return true;
        }

        String output;

        try {
            output = new ReflectionChain(args, instance, sender).startChain();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InputException ex) {
            final String errorMessage = ex instanceof InputException ? "Exception deducing proper types from your input!" : "Exception invoking method - See console for more details!";
            final Throwable cause = ex.getCause() == null ? ex : ex.getCause();

            if (PlatformUtil.isServerRunningSpigotOrDeriv()) {
                FancyChatException.sendFancyChatException(sender, errorMessage, cause);
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
            availableMethods = ReflectionUtil.getMethodMapFor(typeIn);
            this.classType = typeIn;
        }
    }

    @Override
    public List<String> tabCompleteLogic(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        Map<String, Method> reflectionMap = this.availableMethods;
        Class returnType = this.classType;

        int argsToSkip = 0;
        Iterator<String> iterator = arguments.iterator();

        while (iterator.hasNext()) {
            String currentArg = iterator.next();
            if (argsToSkip > 0) {
                iterator.remove();
                argsToSkip--;
                reflectionMap = null;
                continue;
            }

            reflectionMap = ReflectionUtil.getMethodMapFor(returnType);

            if (reflectionMap.get(currentArg) != null) {
                Method method = reflectionMap.get(currentArg);
                List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(arguments.subList(1, arguments.size()), method);
                argsToSkip = stringMethodArgs.size();

                returnType = method.getReturnType();
                iterator.remove();
            }
        }

        return reflectionMap == null ? Collections.emptyList() : getCompletionsMatching(args, reflectionMap.keySet());
    }
}

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

package com.destroystokyo.debuggery.commands.base;

import com.destroystokyo.debuggery.reflection.ReflectionChain;
import com.destroystokyo.debuggery.reflection.ReflectionUtil;
import com.destroystokyo.debuggery.reflection.formatters.InputException;
import com.destroystokyo.debuggery.reflection.formatters.OutputFormatter;
import com.destroystokyo.debuggery.util.FancyChatException;
import com.destroystokyo.debuggery.util.PlatformUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
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
        // 0 args just return info on object itself

        if (args.length == 0) {
            sender.sendMessage(getOutputStringFor(instance));
            return true;
        }

        // more than 0 args, start chains

        Validate.isTrue(classType.isInstance(instance), "Instance is of type: " + classType.getSimpleName() + "but was expecting: " + classType.getSimpleName());
        final String inputMethod = args[0];

        if (!availableMethods.containsKey(inputMethod)) {
            sender.sendMessage(ChatColor.RED + "Unknown or unavailable method");
            return true;
        }

        Object lastLink;

        try {
            lastLink = new ReflectionChain(args, instance, sender).chain();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InputException ex) {
            final String errorMessage = ex instanceof InputException ? "Exception deducing proper types from your input!" : "Exception invoking method - See console for more details!";
            final Throwable cause = ex.getCause() == null ? ex : ex.getCause();

            if (PlatformUtil.canUseFancyChatExceptions()) {
                FancyChatException.sendFancyChatException(sender, errorMessage, cause);
            } else {
                sender.sendMessage(ChatColor.RED + errorMessage);
            }

            cause.printStackTrace();
            return true;
        }

        String output = getOutputStringFor(lastLink);
        if (output != null) {
            sender.sendMessage(output);
        }

        return true;
    }

    /**
     * Updates the locally cached reflection class
     *
     * @param typeIn class type to cache a reflection map for
     */
    protected void updateReflectionClass(Class typeIn) {
        if (this.classType != typeIn) {
            availableMethods = ReflectionUtil.getMethodMapFor(typeIn);
            this.classType = typeIn;
        }
    }

    /**
     * Convenience method to run objects past the OutputFormatter
     *
     * @param object Object to get String output for
     * @return textual description of Object
     */
    @Nullable
    protected String getOutputStringFor(@Nullable Object object) {
        return OutputFormatter.getOutput(object);
    }

    @Override
    public List<String> tabCompleteLogic(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = Arrays.asList(args);
        Map<String, Method> reflectionMap = this.availableMethods;
        Method lastMethod = null;
        Class returnType = this.classType;

        int argsToSkip = 0;

        for (int i = 0; i < arguments.size(); i++) {
            String currentArg = arguments.get(i);
            if (argsToSkip > 0) {
                argsToSkip--;
                reflectionMap = null;

                continue;
            }

            reflectionMap = ReflectionUtil.getMethodMapFor(returnType);

            if (reflectionMap.get(currentArg) != null) {
                lastMethod = reflectionMap.get(currentArg);
                List<String> stringMethodArgs = ReflectionUtil.getArgsForMethod(arguments.subList(i + 1, arguments.size()), lastMethod);
                argsToSkip = stringMethodArgs.size();

                returnType = lastMethod.getReturnType();
            }
        }

        return reflectionMap == null ? Collections.emptyList() : getCompletionsMatching(args, reflectionMap.keySet());
    }
}

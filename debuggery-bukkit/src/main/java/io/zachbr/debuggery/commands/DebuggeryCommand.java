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

package io.zachbr.debuggery.commands;

import io.zachbr.debuggery.DebuggeryBukkit;
import io.zachbr.debuggery.commands.base.CommandBase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class DebuggeryCommand extends CommandBase {
    private final DebuggeryBukkit debuggery;

    public DebuggeryCommand(DebuggeryBukkit debuggery) {
        super("debuggery", "debuggery.debuggery", false, false);
        this.debuggery = debuggery;
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "=== Debuggery v" + debuggery.getJavaPlugin().getDescription().getVersion() + " ===");
            sender.sendMessage("Debuggery is designed to expose API values at runtime.");
            sender.sendMessage("To see what commands are available and any help associated with them, use tab completion on this command.");
            sender.sendMessage("Source code can be found here: " + ChatColor.BLUE + debuggery.getJavaPlugin().getDescription().getWebsite());
            return true;
        }

        if (args.length == 1) {
            String arg = args[0].toLowerCase(Locale.ENGLISH);
            if (!debuggery.getAllCommands().containsKey(arg)) {
                sender.sendMessage(ChatColor.RED + "Debuggery command not found");
                return true;
            }

            CommandBase target = debuggery.getAllCommands().get(arg);
            return target.showHelpText(sender, args);
        }

        sender.sendMessage(ChatColor.RED + "Too many arguments");
        return true;
    }

    @Override
    protected boolean helpLogic(CommandSender sender, String[] args) {
        sender.sendMessage("Displays general information about the plugin.");
        sender.sendMessage("Also shows more specific help for each command when entered");
        sender.sendMessage("Try using tab completion to see all available subtopics.");
        return true;
    }

    @Override
    protected List<String> tabCompleteLogic(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) {
            return Collections.emptyList();
        }

        List<String> commands = debuggery.getAllCommands().values().stream()
                .filter(CommandBase::shouldShowInHelp)
                .map(CommandBase::getName)
                .collect(Collectors.toList());

        return getCompletionsMatching(args, commands);
    }
}

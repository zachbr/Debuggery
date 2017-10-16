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

package com.destroystokyo.debuggery.commands;

import com.destroystokyo.debuggery.commands.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class FlyCommand extends CommandBase {

    public FlyCommand() {
        super("dfly", "debuggery.fly", false);
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        Player targetPlayer = null;

        if (sender instanceof Player) {
            targetPlayer = (Player) sender;
        }

        if (args.length == 1) {
            targetPlayer = Bukkit.getPlayer(args[0]);
        }

        if (targetPlayer == null) {
            sender.sendMessage("Please specify a valid player!");
            return true;
        }

        if (targetPlayer.getAllowFlight()) {
            targetPlayer.setAllowFlight(false);
            sender.sendMessage(targetPlayer.getName() + " can no longer fly");
        } else {
            targetPlayer.setAllowFlight(true);
            sender.sendMessage(targetPlayer.getName() + " can now fly");
        }

        return true;
    }

    @Override
    protected boolean helpLogic(CommandSender sender, String[] args) {
        sender.sendMessage("Allows you to toggle your ability to fly.");
        sender.sendMessage("You can also enter another player's name to toggle their ability to fly.");
        return true;
    }

    @Override
    protected Collection<String> getTabCompletions() {
        return Bukkit.getServer().getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .collect(Collectors.toList());
    }
}

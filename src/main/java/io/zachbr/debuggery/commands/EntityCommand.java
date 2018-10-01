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

import io.zachbr.debuggery.commands.base.CommandReflection;
import io.zachbr.debuggery.util.PlatformUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityCommand extends CommandReflection {
    public EntityCommand() {
        super("dentity", "debuggery.entity", true, Entity.class);
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Entity entity = PlatformUtil.getEntityPlayerLookingAt(player, 25, 1.5D);

        if (entity == null) {
            sender.sendMessage(ChatColor.RED + "Couldn't detect the entity you were looking at!");
            return true;
        }

        updateReflectionClass(entity.getClass());
        return doReflectionLookups(sender, args, entity);
    }
}

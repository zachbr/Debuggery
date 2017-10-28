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

import com.destroystokyo.debuggery.commands.base.CommandReflection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCommand extends CommandReflection {
    public EntityCommand() {
        super("dentity", "debuggery.entity", true, Entity.class);
    }

    /**
     * Does a raytrace to check which entity the player is probably looking at
     *
     * @param player    Player to check
     * @param range     Max distance to search against
     * @param tolerance How close we want the search to be
     * @return entity player is looking at, or null if we couldn't find one
     */
    @Nullable
    private static Entity getEntityPlayerLookingAt(Player player, int range, double tolerance) {
        List<Entity> entities = player.getNearbyEntities(range, range, range);

        for (Block block : player.getLineOfSight(null, range)) {
            final Location location = block.getLocation();
            for (Entity entity : entities) {
                // X
                if (!(Math.abs(entity.getLocation().getX() - location.getX()) < tolerance)) {
                    continue;
                }

                // Y
                if (!(Math.abs(entity.getLocation().getY() - location.getY()) < tolerance)) {
                    continue;
                }

                // Z
                if (!(Math.abs(entity.getLocation().getX() - location.getX()) < tolerance)) {
                    continue;
                }

                return entity;
            }
        }

        return null;
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Entity entity = getEntityPlayerLookingAt(player, 25, 1.5D);

        if (entity == null) {
            sender.sendMessage(ChatColor.RED + "Couldn't detect the entity you were looking at!");
            return true;
        }

        updateReflectionClass(entity.getClass());

        if (args.length == 0) {
            sender.sendMessage(entity.toString());
            return true;
        }

        return doReflectionLookups(sender, args, entity);
    }
}

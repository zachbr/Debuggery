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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Contains platform specific utilities that I'd rather not
 * have stuffed in the main class.
 */
public class PlatformUtil {
    private static Boolean baseComponentApiPresent = null;

    /**
     * Checks if we are able to use our fancy chat exceptions
     * Really we're checking for Spigot-API's BaseComponent API
     *
     * @return true if we can use our fancy chat exceptions
     */
    public static boolean canUseFancyChatExceptions() {
        if (baseComponentApiPresent == null) {
            try {
                baseComponentApiPresent = Class.forName("net.md_5.bungee.api.chat.BaseComponent") != null;
            } catch (ClassNotFoundException ignored) {
                baseComponentApiPresent = false;
            }
        }

        return baseComponentApiPresent;
    }

    /**
     * Performs a raytrace to check which entity the player is probably looking at
     *
     * @param player    Player to check
     * @param range     Max distance to search against
     * @param tolerance How close we want the search to be
     * @return entity player is looking at, or null if we couldn't find one
     */
    @Nullable
    public static Entity getEntityPlayerLookingAt(Player player, int range, double tolerance) {
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

    /**
     * Gets the first entity at the location that's within the specified tolerance
     *
     * @param location  location to search at
     * @param range     Max distance to search against
     * @param tolerance How close we want the search to be
     * @return entity closest to the location, or null if we couldn't find one
     */
    @Nullable
    public static Entity getEntityNearestTo(Location location, int range, double tolerance) {
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, range, range, range);

        return entities.stream()
                .filter(e -> Math.abs(e.getLocation().getX() - location.getX()) < tolerance)
                .filter(e -> Math.abs(e.getLocation().getY() - location.getY()) < tolerance)
                .filter(entity -> Math.abs(entity.getLocation().getX() - location.getX()) < tolerance)
                .findFirst().orElse(null);

    }
}

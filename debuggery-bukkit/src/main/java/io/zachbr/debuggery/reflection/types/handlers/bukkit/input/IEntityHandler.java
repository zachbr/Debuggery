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

package io.zachbr.debuggery.reflection.types.handlers.bukkit.input;

import io.zachbr.debuggery.reflection.types.handlers.base.IHandler;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class IEntityHandler implements IHandler {

    // TODO - Needs platform extensions feature that I haven't designed yet
    static @NotNull Entity getEntity(String input) {
//        Entity target;
//
//        // player specific commands to make things easier for them
//        if (sender instanceof Player) {
//            if (input.equalsIgnoreCase("that")) {
//                target = PlatformUtil.getEntityPlayerLookingAt((Player) sender, 25, 1.5D);
//
//                if (target != null) {
//                    return target;
//                }
//            } else if (input.equalsIgnoreCase("me")) {
//                return ((Player) sender);
//            }
//        }
//
//        // otherwise fall back to just getting the closest entity to the given location
//        Location loc = ILocationHandler.getLocation(input, sender);
//        Entity nearest = PlatformUtil.getEntityNearestTo(loc, 25, 1.5D);
//
//        if (nearest != null) {
//            return nearest;
//        } else {
            throw new NullPointerException("Cannot find any entities near you!");
//        }
    }

    @Override
    public @NotNull Entity instantiateInstance(String input, Class<?> clazz) {
        return getEntity(input); // separate method so that other entity related commands can get to it
    }

    @Override
    public @NotNull Class<?> getRelevantClass() {
        return Entity.class;
    }
}

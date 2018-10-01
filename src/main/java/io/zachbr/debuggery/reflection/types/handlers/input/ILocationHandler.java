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

package io.zachbr.debuggery.reflection.types.handlers.input;

import io.zachbr.debuggery.reflection.types.handlers.base.IHandler;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ILocationHandler implements IHandler {

    static Location getLocation(String input, @Nullable CommandSender sender) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (input.equalsIgnoreCase("here")) {
                return player.getLocation();
            } else if (input.equalsIgnoreCase("there")) {
                return player.getTargetBlock(null, 50).getLocation();
            }
        }

        String[] contents = input.split(",", 4);
        double[] xyz = new double[3];

        World world = Bukkit.getWorld(contents[0]);
        if (world == null) {
            throw new NullPointerException("No world by that name could be found");
        }

        for (int i = 0; i < contents.length - 1; i++) {
            xyz[i] = Double.parseDouble(contents[i + 1]); // offset by 1 because of world at index 0
        }

        return new Location(world, xyz[0], xyz[1], xyz[2]);
    }

    @Nonnull
    @Override
    public Location instantiateInstance(String input, Class clazz, @Nullable CommandSender sender) {
        return getLocation(input, sender); // separate method so that related commands can get to it
    }

    @Nonnull
    @Override
    public Class<?> getRelevantClass() {
        return Location.class;
    }
}

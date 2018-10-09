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
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IMaterialHandler implements IHandler {

    static Material getMaterial(String input) {
        Material material = Material.matchMaterial(input);

        if (material == null) {
            throw new IllegalArgumentException("Could not find any matching materials for \"" + input + "\"");
        } else {
            return material;
        }
    }

    @NotNull
    @Override
    public Material instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) {
        return getMaterial(input); // separate method so other handlers can access
    }

    @NotNull
    @Override
    public Class<?> getRelevantClass() {
        return Material.class;
    }
}

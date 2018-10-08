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
import org.bukkit.material.MaterialData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IMaterialDataHandler implements IHandler {

    @NotNull
    @Override
    public MaterialData instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) {
        String[] contents = input.split(":", 2);
        Material material = IMaterialHandler.getMaterial(contents[0]);
        byte data = (byte) IPrimitivesHandler.getPrimitive(contents[1], byte.class);

        return new MaterialData(material, data);
    }

    @NotNull
    @Override
    public Class<?> getRelevantClass() {
        return MaterialData.class;
    }
}

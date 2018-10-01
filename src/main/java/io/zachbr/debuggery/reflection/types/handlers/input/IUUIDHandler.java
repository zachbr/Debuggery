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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class IUUIDHandler implements IHandler {

    @Nonnull
    @Override
    public UUID instantiateInstance(String input, Class clazz, @Nullable CommandSender sender) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException ex) {
            if (sender instanceof Player) {
                Entity entity = IEntityHandler.getEntity(input, sender);

                if (entity != null) {
                    return entity.getUniqueId();
                }
            }

            throw ex; // re-throw if we still couldn't make it work
        }
    }

    @Nonnull
    @Override
    public Class<?> getRelevantClass() {
        return UUID.class;
    }
}

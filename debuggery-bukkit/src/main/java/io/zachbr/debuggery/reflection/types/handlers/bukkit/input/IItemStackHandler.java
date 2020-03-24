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
import io.zachbr.debuggery.reflection.types.handlers.base.platform.PlatformSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IItemStackHandler implements IHandler {

    static @NotNull ItemStack getItemStack(String input, @Nullable PlatformSender<?> sender) {
        if (sender != null && sender.rawSender() instanceof Player player) {
            if (input.equalsIgnoreCase("this")) {
                return player.getInventory().getItemInMainHand();
            }
        }

        // try creating a new itemstack from material
        return new ItemStack(IMaterialHandler.getMaterial(input));
    }

    @Override
    public @NotNull ItemStack instantiateInstance(String input, Class<?> clazz, @Nullable PlatformSender<?> sender) {
        return getItemStack(input, sender);
    }

    @Override
    public @NotNull Class<?> getRelevantClass() {
        return ItemStack.class;
    }
}

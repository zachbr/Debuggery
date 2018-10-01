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

import io.zachbr.debuggery.reflection.types.handlers.base.IPolymorphicHandler;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IEnumHandler implements IPolymorphicHandler {

    static <T extends Enum<T>> T getEnumValue(String input, Class clazz) {
        return Enum.valueOf((Class<T>) clazz, input.toUpperCase());
    }

    @Nonnull
    @Override
    public Enum instantiateInstance(String input, Class clazz, @Nullable CommandSender sender) {
        return getEnumValue(input, clazz); // separated so other handlers can access
    }

    @Nonnull
    @Override
    public Class<?> getRelevantClass() {
        return Enum.class;
    }
}

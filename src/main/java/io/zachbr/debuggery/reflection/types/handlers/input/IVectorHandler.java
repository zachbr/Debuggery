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
import io.zachbr.debuggery.util.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IVectorHandler implements IHandler {

    @Override
    public @NotNull Object instantiateInstance(String input, Class<?> clazz, @Nullable CommandSender sender) {
        double[] parsed = StringUtil.parseDoublesFromString(input, 3);

        return new Vector(parsed[0], parsed[1], parsed[2]);
    }

    @Override
    public @NotNull Class<?> getRelevantClass() {
        return Vector.class;
    }
}

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

package io.zachbr.debuggery.reflection.types.handlers.output;

import io.zachbr.debuggery.reflection.types.TypeHandler;
import io.zachbr.debuggery.reflection.types.handlers.base.OHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class OMapHandler implements OHandler {

    @Nullable
    @Override
    public String getFormattedOutput(Object object) {
        final Map map = (Map) object;
        if (map.isEmpty()) {
            return null; // we don't want to display anything if the map is empty
        }

        StringBuilder returnString = new StringBuilder();

        for (Object obj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            final TypeHandler typeHandler = TypeHandler.getInstance();

            returnString
                    .append("{")
                    .append(typeHandler.getOutputFor(entry.getKey())).append(", ")
                    .append(typeHandler.getOutputFor(entry.getValue()))
                    .append("}\n");
        }

        return returnString.toString();
    }

    @NotNull
    @Override
    public Class<?> getRelevantClass() {
        return Map.class;
    }
}

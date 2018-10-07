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
import java.lang.reflect.Array;

public class OArrayHandler {

    public OArrayHandler(TypeHandler typeHandler) {
        // loop through all supported classes and register them to the handler
        Class[] supportedClasses = {Object[].class, byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class, boolean[].class, char[].class};
        for (Class clazz : supportedClasses) {
            OHandler handler = new OHandler() {
                @Nullable
                @Override
                public String getFormattedOutput(Object object) {
                    return getFormattedArray(object);
                }

                @NotNull
                @Override
                public Class<?> getRelevantClass() {
                    return clazz;
                }
            };

            typeHandler.registerHandler(handler);
        }
    }

    @Nullable
    private String getFormattedArray(Object object) {
        // Easier than checking every single primitive type
        if (object.getClass().getComponentType().isPrimitive()) {
            int length = Array.getLength(object);
            Object[] newArray = new Object[length];

            for (int i = 0; i < length; i++) {
                newArray[i] = Array.get(object, i);
            }

            object = newArray;
        }

        StringBuilder returnString = new StringBuilder("{");
        boolean first = true;

        for (Object entry : (Object[]) object) {
            final TypeHandler typeHandler = TypeHandler.getInstance();

            if (first) {
                returnString.append(typeHandler.getOutputFor(entry));
                first = false;
            } else {
                returnString.append(", ").append(typeHandler.getOutputFor(entry));
            }
        }

        return returnString.append("}").toString();
    }
}

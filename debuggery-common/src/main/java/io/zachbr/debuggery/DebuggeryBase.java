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

package io.zachbr.debuggery;

import io.zachbr.debuggery.reflection.MethodMapProvider;
import io.zachbr.debuggery.reflection.ReflectionChain;
import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

abstract class DebuggeryBase {
    private static final boolean DEBUG_MODE = Boolean.getBoolean("debuggery.debug");
    private final MethodMapProvider methodMapProvider;
    private final TypeHandler typeHandler;
    private final Logger logger;

    DebuggeryBase(Logger logger) {
        this.logger = logger;
        this.methodMapProvider = new MethodMapProvider();
        this.typeHandler = new TypeHandler(getLogger());
    }

    public final Logger getLogger() {
        return this.logger;
    }

    abstract String getPluginVersion();

    abstract String getPlatformName();

    abstract String getPlatformVersion();

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    /**
     * Gets system information as an array of lines
     *
     * @return system information
     */
    public @NotNull String[] getSystemInfo() {
        List<String> out = new ArrayList<>();

        out.add("Debuggery Ver: " + getPluginVersion());
        out.add("Server Impl: " + getPlatformName());
        out.add("Server Ver: " + getPlatformVersion());
        out.add("Java Runtime: " + System.getProperty("java.runtime.version"));
        out.add("Operating System: " + System.getProperty("os.name") + " "
                + System.getProperty("os.version") + " "
                + "(" + System.getProperty("os.arch") + ")");

        return out.toArray(new String[0]);
    }

    public final void printSystemInfo() {
        for (String line : getSystemInfo()) {
            logger.debug(line);
        }
    }

    public final MethodMapProvider getMethodMapProvider() {
        return this.methodMapProvider;
    }

    public final TypeHandler getTypeHandler() {
        return this.typeHandler;
    }

    // todo - better solutions elsewhere
    public ReflectionChain.Result performReflectiveChain(String[] inputArgs, Object initialInstance)
            throws IllegalAccessException,InputException, InvocationTargetException {
        Objects.requireNonNull(inputArgs);
        Objects.requireNonNull(initialInstance);

        ReflectionChain chain = new ReflectionChain(methodMapProvider, typeHandler, inputArgs, initialInstance);
        chain.chain();
        return chain.getResult();
    }
}

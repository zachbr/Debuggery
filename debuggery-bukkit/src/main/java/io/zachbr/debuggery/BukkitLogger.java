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

public class BukkitLogger implements Logger {
    private final java.util.logging.Logger pluginLogger;

    BukkitLogger(java.util.logging.Logger logger) {
        this.pluginLogger = logger;
    }

    @Override
    public void info(String str) {
        pluginLogger.info(str);
    }

    @Override
    public void warn(String str) {
        pluginLogger.warning(str);
    }

    @Override
    public void err(String str) {
        pluginLogger.severe(str);
    }

    @Override
    public void debug(String str) {
        pluginLogger.info("[DEBUG] " + str);
    }
}

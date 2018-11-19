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

public class TestLoggerImpl implements Logger {
    private final boolean DEBUG_ENABLED = Boolean.getBoolean("debuggery.debug");

    @Override
    public void info(String str) {
        System.out.println("[INFO] " + str);
    }

    @Override
    public void warn(String str) {
        System.out.println("[WARN] " + str);
    }

    @Override
    public void err(String str) {
        System.out.println("[ERROR] " + str);
    }

    @Override
    public void debug(String str) {
        if (!DEBUG_ENABLED) {
            return;
        }

        System.out.println("[DEBUG] " + str);
    }
}

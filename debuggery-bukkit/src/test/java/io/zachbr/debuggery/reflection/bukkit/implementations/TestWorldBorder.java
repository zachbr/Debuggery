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

package io.zachbr.debuggery.reflection.bukkit.implementations;

import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class TestWorldBorder implements WorldBorder {
    private final Location center;
    private final int size;
    private final int damageAmount;

    public TestWorldBorder(int size, int damageAmount, Location center) {
        this.size = size;
        this.damageAmount = damageAmount;
        this.center = center;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getSize() {
        return this.size;
    }

    @Override
    public void setSize(double newSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSize(double newSize, long seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getCenter() {
        return this.center;
    }

    @Override
    public void setCenter(Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCenter(double x, double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDamageBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDamageBuffer(double blocks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDamageAmount() {
        return this.damageAmount;
    }

    @Override
    public void setDamageAmount(double damage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWarningTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWarningTime(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWarningDistance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWarningDistance(int distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInside(Location location) {
        throw new UnsupportedOperationException();
    }
}

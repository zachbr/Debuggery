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

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class TestBlockData implements BlockData {
    private final Material material;
    private final String asString;

    public TestBlockData(Material material, String asString) {
        this.material = material;
        this.asString = asString;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getAsString() {
        return asString;
    }

    @Override
    public BlockData merge(BlockData blockData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean matches(BlockData blockData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported!", ex);
        }
    }
}

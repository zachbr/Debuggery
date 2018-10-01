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

package io.zachbr.debuggery.reflection.types.implementations;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import javax.annotation.Nonnull;
import java.util.*;

public class TestInventory implements Inventory {
    private final List<ItemStack> contents;
    private final String name;
    private final String title;
    private final int size;

    public TestInventory(List<ItemStack> contents, String name, String title, int size) {
        this.contents = contents;
        this.name = name;
        this.title = title;
        this.size = size;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getMaxStackSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxStackSize(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.contents.get(index);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.contents.add(index, item);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack[] getContents() {
        return this.contents.toArray(new ItemStack[contents.size()]);
    }

    @Override
    public void setContents(ItemStack[] items) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack[] getStorageContents() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(ItemStack item) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int first(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int firstEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.contents.clear();
    }

    @Override
    public List<HumanEntity> getViewers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public InventoryType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InventoryHolder getHolder() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ListIterator<ItemStack> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException();
    }
}

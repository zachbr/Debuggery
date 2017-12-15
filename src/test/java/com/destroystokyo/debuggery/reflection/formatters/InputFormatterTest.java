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

package com.destroystokyo.debuggery.reflection.formatters;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionDefault;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class InputFormatterTest {

    @Test
    public void testPrimitives() throws InputException {
        Class[] inputTypes = {
                byte.class,
                short.class,
                int.class,
                long.class,
                float.class,
                double.class,
                boolean.class,
                char.class
        };

        String[] input = {
                "127",
                "15",
                "11612",
                "5512512",
                ".0451",
                "2.254",
                "true",
                "§"
        };

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        // Java will autobox away our primitives, test against wrappers
        assertTrue(output[0] instanceof Byte);
        assertTrue(output[1] instanceof Short);
        assertTrue(output[2] instanceof Integer);
        assertTrue(output[3] instanceof Long);
        assertTrue(output[4] instanceof Float);
        assertTrue(output[5] instanceof Double);
        assertTrue(output[6] instanceof Boolean);
        assertTrue(output[7] instanceof Character);

        // Finally, let's make sure the values are correct
        // Yes, I am aware some of these casts are redundant, deal with it
        assertTrue(output[0].equals((byte) 127));
        assertTrue(output[1].equals((short) 15));
        assertTrue(output[2].equals((int) 11612));
        assertTrue(output[3].equals((long) 5512512));
        assertTrue(output[4].equals((float) .0451));
        assertTrue(output[5].equals((double) 2.254));
        assertTrue(output[6].equals((boolean) true));
        assertTrue(output[7].equals((char) '§'));
    }

    @Test
    public void testValueFromEnum() throws InputException {
        Class[] inputTypes = {
                WeatherType.class,
                EquipmentSlot.class,
                MainHand.class,
                PermissionDefault.class
        };

        String[] input = {
                "downfall",
                "HeAd",
                "lEfT",
                "NOT_OP"
        };

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        assertTrue(output[0] instanceof WeatherType);
        assertTrue(output[1] instanceof EquipmentSlot);
        assertTrue(output[2] instanceof MainHand);
        assertTrue(output[3] instanceof PermissionDefault);

        // Finally, let's make sure the values are correct
        assertTrue(output[0] == WeatherType.DOWNFALL);
        assertTrue(output[1] == EquipmentSlot.HEAD);
        assertTrue(output[2] == MainHand.LEFT);
        assertTrue(output[3] == PermissionDefault.NOT_OP);
    }

    @Test
    public void testMaterial() throws InputException {
        Class[] inputTypes = {Material.class, Material.class, Material.class};
        String[] input = {"gold_block", "DIAMOND_SWORD", "blaze_ROD"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Material);
        }

        // Finally, let's make sure the values are correct
        assertTrue(output[0] == Material.GOLD_BLOCK);
        assertTrue(output[1] == Material.DIAMOND_SWORD);
        assertTrue(output[2] == Material.BLAZE_ROD);
    }

    @Test
    public void testItemStack() throws InputException {
        Class[] inputTypes = {ItemStack.class};
        String[] input = {"diamond"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof ItemStack);
        }

        // Finally, let's make sure the values are correct
        assertTrue(((ItemStack) output[0]).getType() == Material.DIAMOND);
    }

    @Test
    public void testMaterialData() throws InputException {
        Class[] inputTypes = {MaterialData.class};
        String[] input = {"diamond_spade:24"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof MaterialData);
        }

        // Finally, let's make sure the values are correct
        assertTrue(((MaterialData) output[0]).getItemType() == Material.DIAMOND_SPADE);
    }

    @Test
    public void testGameMode() throws InputException {
        Class[] inputTypes = {GameMode.class, GameMode.class};
        String[] input = {"1", "adventure"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof GameMode);
        }

        // Finally, let's make sure the values are correct
        assertTrue(output[0] == GameMode.CREATIVE);
        assertTrue(output[1] == GameMode.ADVENTURE);
    }

    @Test
    public void testDifficulty() throws InputException {
        Class[] inputTypes = {Difficulty.class, Difficulty.class};
        String[] input = {"3", "peaceful"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Difficulty);
        }

        // Finally, let's make sure the values are correct
        assertTrue(output[0] == Difficulty.HARD);
        assertTrue(output[1] == Difficulty.PEACEFUL);
    }

    @Test
    public void testBukkitClasses() throws InputException {
        Class[] inputTypes = {Class[].class};
        String[] input = {"Zombie,Creeper,Pig"};

        Object[] output = InputFormatter.getTypesFromInput(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertTrue(inputTypes.length == output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Class[]);
        }

        // Finally, let's make sure the values are correct
        Class[] classes = (Class[]) output[0];
        assertTrue(classes[0].equals(Zombie.class));
        assertTrue(classes[1].equals(Creeper.class));
        assertTrue(classes[2].equals(Pig.class));
    }

}

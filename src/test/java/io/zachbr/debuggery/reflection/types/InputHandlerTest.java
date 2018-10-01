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

package io.zachbr.debuggery.reflection.types;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionDefault;
import org.junit.Test;

import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

public class InputHandlerTest {

    @SuppressWarnings("RedundantCast")
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

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

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
        assertEquals(output[0], ((byte) 127));
        assertEquals(output[1], ((short) 15));
        assertEquals(output[2], ((int) 11612));
        assertEquals(output[3], ((long) 5512512));
        assertEquals(output[4], ((float) .0451));
        assertEquals(output[5], ((double) 2.254));
        assertEquals(output[6], ((boolean) true));
        assertEquals(output[7], ((char) '§'));
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

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        assertTrue(output[0] instanceof WeatherType);
        assertTrue(output[1] instanceof EquipmentSlot);
        assertTrue(output[2] instanceof MainHand);
        assertTrue(output[3] instanceof PermissionDefault);

        // Finally, let's make sure the values are correct
        assertSame(output[0], WeatherType.DOWNFALL);
        assertSame(output[1], EquipmentSlot.HEAD);
        assertSame(output[2], MainHand.LEFT);
        assertSame(output[3], PermissionDefault.NOT_OP);
    }

    @Test
    public void testMaterial() throws InputException {
        Class[] inputTypes = {Material.class, Material.class, Material.class};
        String[] input = {"gold_block", "DIAMOND_SWORD", "blaze_ROD"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Material);
        }

        // Finally, let's make sure the values are correct
        assertSame(output[0], Material.GOLD_BLOCK);
        assertSame(output[1], Material.DIAMOND_SWORD);
        assertSame(output[2], Material.BLAZE_ROD);
    }

    @Test
    public void testArrayItemStacks() throws InputException {
        Class[] inputTypes = {ItemStack[].class};
        String[] input = {"diamond,sand,diamond_sword"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // ensure length of input is the same as output
        assertEquals(inputTypes.length, output.length);

        // ensure output is of proper type
        for (Object object : output) {
            assertTrue(object instanceof ItemStack[]);
        }

        ItemStack[] itemStackArr = (ItemStack[]) output[0];

        // verify types in array
        assertSame(itemStackArr[0].getType(), Material.DIAMOND);
        assertSame(itemStackArr[1].getType(), Material.SAND);
        assertSame(itemStackArr[2].getType(), Material.DIAMOND_SWORD);
    }

    @Test
    public void testItemStack() throws InputException {
        Class[] inputTypes = {ItemStack.class};
        String[] input = {"diamond"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof ItemStack);
        }

        // Finally, let's make sure the values are correct
        assertSame(((ItemStack) output[0]).getType(), Material.DIAMOND);
    }

    @Test
    public void testMaterialData() throws InputException {
        Class[] inputTypes = {MaterialData.class};
        String[] input = {"diamond_shovel:24"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof MaterialData);
        }

        // Finally, let's make sure the values are correct
        assertSame(((MaterialData) output[0]).getItemType(), Material.DIAMOND_SHOVEL);
    }

    @Test
    public void testGameMode() throws InputException {
        Class[] inputTypes = {GameMode.class, GameMode.class};
        String[] input = {"1", "adventure"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof GameMode);
        }

        // Finally, let's make sure the values are correct
        assertSame(output[0], GameMode.CREATIVE);
        assertSame(output[1], GameMode.ADVENTURE);
    }

    @Test
    public void testDifficulty() throws InputException {
        Class[] inputTypes = {Difficulty.class, Difficulty.class};
        String[] input = {"3", "peaceful"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Difficulty);
        }

        // Finally, let's make sure the values are correct
        assertSame(output[0], Difficulty.HARD);
        assertSame(output[1], Difficulty.PEACEFUL);
    }

    @Test
    public void testBukkitClasses() throws InputException {
        Class[] inputTypes = {Class[].class};
        String[] input = {"Zombie,Creeper,Pig"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // First let's make sure we didn't lose anything, or get anything
        assertEquals(inputTypes.length, output.length);

        // Next let's make sure everything is the right type
        for (Object object : output) {
            assertTrue(object instanceof Class[]);
        }

        // Finally, let's make sure the values are correct
        Class[] classes = (Class[]) output[0];
        assertEquals(classes[0], Zombie.class);
        assertEquals(classes[1], Creeper.class);
        assertEquals(classes[2], Pig.class);
    }

    @Test
    public void testCollection() throws InputException {
        Class[] inputTypes = {List.class, Set.class, Collection.class, Queue.class, Vector.class};
        String[] input = {"Material:Grass,Stone,Dirt", "SkullType:Wither,Zombie,Creeper", "PortalType:Nether,Ender",
        "TreeType:Redwood,RED_MUSHROOM,dark_oak", "GameMode:Creative,Survival,Adventure"};

        Object[] output = TypeHandler.getInstance().instantiateTypes(inputTypes, Arrays.asList(input), null);

        // verify we got the types we requested
        assertTrue(output[0] instanceof List);
        assertTrue(output[1] instanceof Set);
        assertTrue(output[2] instanceof Collection);
        assertTrue(output[3] instanceof Queue);
        assertTrue(output[4] instanceof Vector);

        BiFunction<Collection, Object[], Boolean> testAllPresent = (collection, testers) -> {
            boolean passes = true;
            for (Object tester : testers) {
                if (!collection.contains(tester)) {
                    System.out.println("CANNOT FIND REQUESTED TYPE: " + tester);
                    passes = false;
                }
            }

            return passes;
        };

        // verify list contents
        List list = (List) output[0];
        Material[] expectedMats = {Material.GRASS, Material.STONE, Material.DIRT};
        assertSame(expectedMats.length, list.size());
        assertTrue(testAllPresent.apply(list, expectedMats));

        // verify set contents
        Set set = (Set) output[1];
        SkullType[] expectedSkulls = { SkullType.WITHER, SkullType.ZOMBIE, SkullType.CREEPER};
        assertSame(expectedSkulls.length, set.size());
        assertTrue(testAllPresent.apply(set, expectedSkulls));

        // verify collection contents
        Collection collection = (Collection) output[2];
        PortalType[] expectedPortTypes = {PortalType.ENDER, PortalType.NETHER};
        assertSame(expectedPortTypes.length, collection.size());
        assertTrue(testAllPresent.apply(collection, expectedPortTypes));

        // verify queue contents
        Queue queue = (Queue) output[3];
        TreeType[] expectedClasses = {TreeType.REDWOOD, TreeType.RED_MUSHROOM, TreeType.DARK_OAK};
        assertSame(expectedClasses.length, queue.size());
        assertTrue(testAllPresent.apply(queue, expectedClasses));

        // verify vector contents
        Vector vector = (Vector) output[4];
        GameMode[] expectedGameModes = {GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE};
        assertSame(expectedGameModes.length, vector.size());
        assertTrue(testAllPresent.apply(vector, expectedGameModes));
    }

}

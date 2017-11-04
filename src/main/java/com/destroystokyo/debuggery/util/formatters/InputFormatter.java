/*
 * This file is part of Debuggery.
 *
 * Debuggery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * distributed with this repository.
 *
 * Debuggery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Debuggery. If not, see <http://www.gnu.org/licenses/>.
 */

package com.destroystokyo.debuggery.util.formatters;

import com.destroystokyo.debuggery.util.PlatformUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles formatting the arguments we send to method invocation
 */
public class InputFormatter {

    @Nonnull
    public static Object[] getTypesFromInput(Class[] classes, String[] input, @Nullable CommandSender sender) throws InputException {
        List<Object> out = new ArrayList<>();

        for (int i = 0; i < classes.length; i++) {
            out.add(getTypeForClass(classes[i], input[i], sender));
        }

        return out.toArray();
    }

    @Nullable
    private static Object getTypeForClass(Class clazz, String input, @Nullable CommandSender sender) throws InputException {
        if (clazz == null) {
            throw new IllegalArgumentException("Cannot determine input type for null class");
        }

        if (clazz.equals(String.class)) {
            return input;
        } else if (clazz.isPrimitive()) {
            return getPrimitive(clazz, input);
        } else if (clazz.equals(Class.class)) {
            return getBukkitClass(input);
        } else if (clazz.equals(Material.class)) {
            return getMaterial(input);
        } else if (clazz.equals(MaterialData.class)) {
            return getMaterialData(input);
        } else if (clazz.equals(Location.class)) {
            return getLocation(input, sender);
        } else if (clazz.equals(Entity.class)) {
            return getEntity(input, sender);
        } else if (clazz.equals(GameMode.class)) {
            return getGameMode(input);
        } else if (clazz.equals(Difficulty.class)) {
            return getDifficulty(input);
        } else if (Enum.class.isAssignableFrom(clazz)) { // Do not use for all enum types, lacks magic value support
            return getValueFromEnum(clazz, input);
        } else if (clazz.equals(ItemStack.class)) {
            return getItemStack(input, sender);
        } else if (clazz.equals(Class[].class)) {
            return getBukkitClasses(input);
        }

        return null; // TODO
    }

    private static Object getPrimitive(Class clazz, String input) throws InputException {
        try {
            if (input == null) {
                throw new NumberFormatException();
            }

            if (clazz.equals(byte.class)) {
                return Byte.valueOf(input);
            } else if (clazz.equals(short.class)) {
                return Short.valueOf(input);
            } else if (clazz.equals(int.class)) {
                return Integer.valueOf(input);
            } else if (clazz.equals(long.class)) {
                return Long.valueOf(input);
            } else if (clazz.equals(float.class)) {
                return Float.valueOf(input);
            } else if (clazz.equals(double.class)) {
                return Double.valueOf(input);
            } else if (clazz.equals(boolean.class)) {
                return Boolean.valueOf(input);
            } else if (clazz.equals(char.class)) {
                return input.charAt(0);
            }
        } catch (NumberFormatException ex) {
            throw new InputException(ex);
        }

        throw new AssertionError("Java added another primitive type!");
    }

    @Nonnull
    private static <T extends Enum<T>> T getValueFromEnum(Class clazz, String input) throws InputException {
        try {
            //noinspection unchecked
            return Enum.valueOf((Class<T>) clazz, input.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InputException(ex);
        }
    }

    @Nonnull
    private static Material getMaterial(String input) {
        return Material.matchMaterial(input);
    }

    @Nonnull
    private static ItemStack getItemStack(String input, CommandSender sender) {
        if (sender instanceof Player) {
            if (input.equalsIgnoreCase("this")) {
                return ((Player) sender).getInventory().getItemInMainHand();
            }
        }
        return new ItemStack(getMaterial(input));
    }

    @Nonnull
    private static MaterialData getMaterialData(String input) throws InputException {
        String[] contents = input.split(":", 2);
        Material material = getMaterial(contents[0]);
        byte data = (byte) getPrimitive(byte.class, contents[1]);

        //noinspection deprecation
        return new MaterialData(material, data);
    }

    @Nonnull
    private static Location getLocation(String input, CommandSender sender) throws InputException {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (input.equalsIgnoreCase("here")) {
                return player.getLocation();
            } else if (input.equalsIgnoreCase("there")) {
                return player.getTargetBlock(null, 50).getLocation();
            }
        }

        String[] contents = input.split(",", 4);
        double[] xyz = new double[3];
        World world = Bukkit.getWorld(contents[0]);

        try {
            for (int i = 1; i < contents.length; i++) {
                xyz[i - 1] = Double.parseDouble(contents[i]);
            }
        } catch (NumberFormatException ex) {
            throw new InputException(ex);
        }

        try {
            return new Location(world, xyz[0], xyz[1], xyz[2]);
        } catch (IllegalArgumentException ex) {
            throw new InputException(ex);
        }
    }

    @Nonnull
    private static GameMode getGameMode(String input) throws InputException {
        try {
            int val = Integer.parseInt(input);
            //noinspection deprecation
            return GameMode.getByValue(val);
        } catch (NumberFormatException ignored) {
        }

        return getValueFromEnum(GameMode.class, input);
    }

    @Nonnull
    private static Difficulty getDifficulty(String input) throws InputException {
        try {
            int val = Integer.parseInt(input);
            //noinspection deprecation
            return Difficulty.getByValue(val);
        } catch (NumberFormatException ignored) {
        }

        return getValueFromEnum(Difficulty.class, input);
    }

    @Nonnull
    private static Class[] getBukkitClasses(String input) throws InputException {
        List<Class> classList = new ArrayList<>();
        String[] classNames = input.split(",");

        for (String className : classNames) {
            classList.add(getBukkitClass(className));
        }

        return classList.toArray(new Class[classList.size()]);
    }

    @Nonnull
    private static Class getBukkitClass(String input) throws InputException {
        // This is only used for entities right now, so we can save some drama and just search those packages
        final String[] searchPackages = {"org.bukkit.entity", "org.bukkit.entity.minecart"};
        String normalized = Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
        if (normalized.endsWith(".class")) {
            normalized = normalized.substring(0, normalized.length() - 6);
        }

        Class clazz = null;
        for (String packageName : searchPackages) {
            try {
                clazz = Class.forName(packageName + "." + normalized);
            } catch (ClassNotFoundException ignored) {
            }

            if (clazz != null) {
                return clazz;
            }
        }

        throw new InputException(new ClassNotFoundException(normalized + " not present in Bukkit entity namespace"));
    }

    @Nullable
    private static Entity getEntity(String input, @Nullable CommandSender sender) throws InputException {
        Entity target;
        if (sender instanceof Player) {
            if (input.equalsIgnoreCase("that")) {
                target = PlatformUtil.getEntityPlayerLookingAt((Player) sender, 25, 1.5D);

                if (target != null) {
                    return target;
                }
            } else if (input.equalsIgnoreCase("me")) {
                return ((Player) sender);
            }
        }

        Location loc = getLocation(input, sender);
        return PlatformUtil.getEntityNearestTo(loc, 25, 1.5D);
    }
}

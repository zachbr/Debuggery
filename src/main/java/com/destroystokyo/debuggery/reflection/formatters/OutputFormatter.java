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

import org.bukkit.OfflinePlayer;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.messaging.Messenger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Handles formatting the objects we get back from method invocation
 */
public class OutputFormatter {

    @Nullable
    public static String getOutput(@Nullable Object object) {

        if (object == null) {
            return null;
        }

        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Collection) {
            return handleCollection((Collection) object);
        } else if (object instanceof Map) {
            return handleMap((Map) object);
        } else if (object.getClass().isArray()) {
            return handleArray(object);
        } else if (object instanceof Entity) {
            return handleEntity((Entity) object);
        } else if (object instanceof OfflinePlayer) {
            return handleOfflinePlayer((OfflinePlayer) object);
        } else if (object instanceof BlockState) {
            return handleBlockState((BlockState) object);
        } else if (object instanceof Inventory) {
            return handleInventory((Inventory) object);
        } else if (object instanceof WorldBorder) {
            return handleWorldBorder((WorldBorder) object);
        } else if (object instanceof CommandSender) {
            return handleCommandSender((CommandSender) object);
        } else if (object instanceof Messenger) {
            return handleMessenger((Messenger) object);
        } else if (object instanceof HelpMap) {
            return handleHelpMap((HelpMap) object);
        } else {
            return String.valueOf(object);
        }
    }

    @Nonnull
    private static String handleOfflinePlayer(OfflinePlayer player) {
        if (player instanceof Player) {
            return player.toString();
        } else {
            return "[" + player.getName() + ":" + player.getUniqueId() + "]";
        }
    }

    @Nonnull
    private static String handleWorldBorder(WorldBorder border) {
        return "[size=" + border.getSize() + " " +
                "center=" + border.getCenter().getBlockX() + "," + border.getCenter().getBlockY() + "," + border.getCenter().getBlockZ() + " " +
                "dmgAmt=" + border.getDamageAmount() + "]";
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    private static String handleBlockState(BlockState blockState) {
        return "[" + blockState.getData() + ", " +
                "blockData=" + blockState.getBlockData().getAsString() + ", " +
                "light=" + blockState.getLightLevel() + ", " +
                blockState.getLocation() + "]";
    }

    @Nonnull
    private static String handleHelpMap(HelpMap helpMap) {
        StringBuilder returnString = new StringBuilder("[");

        for (HelpTopic topic : helpMap.getHelpTopics()) {
            returnString.append("{").append(topic.getName()).append(", ").append(topic.getShortText()).append("}\n");
        }

        return returnString.append("]").toString();
    }

    @Nonnull
    private static String handleMessenger(Messenger messenger) {
        String incomingBanner = "-- Incoming Channels --\n";
        String incomingChannels = handleCollection(messenger.getIncomingChannels());
        String outgoingBanner = "\n-- Outgoing Channels --\n";
        String outgoingChannels = handleCollection(messenger.getOutgoingChannels());

        return incomingBanner + incomingChannels + outgoingBanner + outgoingChannels;
    }

    @Nonnull
    private static String handleCommandSender(CommandSender sender) {
        return sender.getName();
    }

    @Nonnull
    private static String handleInventory(Inventory inventory) {
        final String basicInfo = "name=" + inventory.getName() + ", title=" + inventory.getTitle()
                + ", size=" + inventory.getSize() + "\n";

        return basicInfo + handleArray(inventory.getContents());

    }

    @Nonnull
    private static String handleArray(Object array) {
        // Easier than checking every single primitive type
        if (array.getClass().getComponentType().isPrimitive()) {
            int length = Array.getLength(array);
            Object[] newArray = new Object[length];

            for (int i = 0; i < length; i++) {
                newArray[i] = Array.get(array, i);
            }

            array = newArray;
        }

        StringBuilder returnString = new StringBuilder("{");
        boolean first = true;

        for (Object entry : (Object[]) array) {
            if (first) {
                returnString.append(getOutput(entry));
                first = false;
            } else {
                returnString.append(", ").append(getOutput(entry));
            }
        }

        return returnString.append("}").toString();
    }

    @Nonnull
    private static String handleMap(Map map) {
        StringBuilder returnString = new StringBuilder();

        for (Object object : map.entrySet()) {
            Map.Entry entry = (Map.Entry) object;

            returnString.append("{").append(getOutput(entry.getKey())).append(", ").append(getOutput(entry.getValue())).append("}\n");
        }

        return returnString.toString();
    }


    @Nonnull
    private static String handleCollection(Collection collection) {
        return handleArray(collection.toArray());
    }

    @Nonnull
    private static String handleEntity(Entity entity) {
        String out = entity.toString();
        final boolean hasTags = out.endsWith("}");
        final String idStr = "id=" + entity.getEntityId();

        if (hasTags) {
            // strip off ending brace and append
            out = out.substring(0, out.length() - 1);
            out += ", " + idStr + "}";
        } else {
            // just append
            out += "{" + idStr + "}";
        }


        return out;
    }
}

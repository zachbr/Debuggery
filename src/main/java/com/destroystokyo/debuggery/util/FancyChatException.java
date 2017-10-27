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

package com.destroystokyo.debuggery.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Contains the needed methods to send hover event stack traces to the client.
 *
 * Has to be kept separate and never called from a CraftBukkit server.
 */
public class FancyChatException {

    /**
     * Sends a fancy hover text stack trace message to the client
     *
     * @param sender target to send to
     * @param errorMessage message displayed in chat
     * @param throwable throwable to format and display on hover
     */
    public static void sendFancyChatException(CommandSender sender, String errorMessage, Throwable throwable) {
        BaseComponent fancyException = formatException(errorMessage, throwable);
        sender.spigot().sendMessage(fancyException);
    }

    /**
     * Formats a throwable and error message into a BaseComponent to send
     *
     * @param errorMessage message displayed in chat
     * @param throwable throwable to format and display on hover
     * @return BaseComponent ready to send
     */
    @Nonnull
    private static BaseComponent formatException(String errorMessage, Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));

        TextComponent component = new TextComponent(ChatColor.RED + errorMessage);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                new TextComponent(writer.toString().replaceAll("\t", "    ").replaceAll("\r", ""))
        }));

        return component;
    }
}

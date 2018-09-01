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

package com.destroystokyo.debuggery.reflection.types.implementations;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public class TestHelpTopic extends HelpTopic {

    public TestHelpTopic(String commandName, String helpMsg, String permNode) {
        this.name = commandName;
        this.fullText = this.shortText = helpMsg;
        this.amendedPermission = permNode;
    }

    @Override
    public boolean canSee(CommandSender player) {
        throw new UnsupportedOperationException();
    }
}

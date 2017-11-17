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

package com.destroystokyo.debuggery.reflection.formatters.implementations;

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

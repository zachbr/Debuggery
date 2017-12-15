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

package com.destroystokyo.debuggery.commands;

import com.destroystokyo.debuggery.commands.base.CommandReflection;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockCommand extends CommandReflection {

    public BlockCommand() {
        super("dblock", "debuggery.block", true, Block.class);
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 50);

        if (args.length == 0) {
            player.sendMessage(block.toString());
            return true;
        }

        return doReflectionLookups(sender, args, block);
    }
}

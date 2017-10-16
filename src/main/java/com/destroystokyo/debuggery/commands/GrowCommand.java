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

package com.destroystokyo.debuggery.commands;

import com.destroystokyo.debuggery.commands.base.CommandBase;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class GrowCommand extends CommandBase {
    private static final Set<Material> GROWABLES = Sets.newHashSet(Material.CROPS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.DIRT);

    public GrowCommand() {
        super("dgrow", "debuggery.grow", true);
    }

    @Override
    protected boolean commandLogic(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 10);

        if (args.length == 0) {
            if (!growBlock(block, false)) {
                sender.sendMessage(ChatColor.RED + "This " + block.getType() + " block is not a recognized type");
                sender.sendMessage(ChatColor.RED + "You can override this by adding \"force\"");
            }

            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "force":
                    sender.sendMessage(ChatColor.RED + "WARNING: " + ChatColor.AQUA + "forcing data onto " + ChatColor.GOLD + block.getType());
                    this.growBlock(block, true);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown argument!");
                    break;
            }
        }

        return true;
    }

    @Override
    protected boolean helpLogic(CommandSender sender, String[] args) {
        sender.sendMessage("Allows you to grow crops to their ripened state.");
        sender.sendMessage("Just look at a crop and enter the command.");
        return true;
    }

    @Override
    protected Collection<String> getTabCompletions() {
        return Collections.singletonList("force");
    }

    private boolean growBlock(Block block, boolean force) {
        if (!force && !GROWABLES.contains(block.getType())) {
            return false;
        }

        this.updateGrowth(block);
        return true;
    }

    @SuppressWarnings("deprecation")
    private void updateGrowth(Block block) {
        if (block.getType() == Material.DIRT) {
            block.setData((byte) 0);
            block.setType(Material.GRASS);
        } else if (block.getData() != CropState.RIPE.getData()) {
            block.setData(CropState.RIPE.getData());
        }
    }
}

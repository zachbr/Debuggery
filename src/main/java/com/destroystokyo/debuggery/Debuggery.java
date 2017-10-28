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

package com.destroystokyo.debuggery;

import com.destroystokyo.debuggery.commands.*;
import com.destroystokyo.debuggery.commands.base.CommandBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Debuggery extends JavaPlugin {
    private final Map<String, CommandBase> commands = new HashMap<>();

    @Override
    public void onEnable() {
        this.registerCommands();
    }

    private void registerCommands() {
        // :(
        BlockCommand block = new BlockCommand();
        ChunkCommand chunk = new ChunkCommand();
        DebuggeryCommand debuggery = new DebuggeryCommand(this);
        EntityCommand entity = new EntityCommand();
        ItemCommand item = new ItemCommand();
        PlayerCommand player = new PlayerCommand();
        ServerCommand server = new ServerCommand();
        WorldCommand world = new WorldCommand();

        commands.put(block.getName(), block);
        commands.put(chunk.getName(), chunk);
        commands.put(debuggery.getName(), debuggery);
        commands.put(entity.getName(), entity);
        commands.put(item.getName(), item);
        commands.put(player.getName(), player);
        commands.put(server.getName(), server);
        commands.put(world.getName(), world);

        commands.values().forEach(c -> this.getCommand(c.getName()).setExecutor(c));
    }

    public Map<String, CommandBase> getAllCommands() {
        return Collections.unmodifiableMap(commands);
    }
}

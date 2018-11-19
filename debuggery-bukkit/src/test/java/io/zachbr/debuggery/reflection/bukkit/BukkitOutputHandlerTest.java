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

package io.zachbr.debuggery.reflection.bukkit;

import io.zachbr.debuggery.Logger;
import io.zachbr.debuggery.TestLoggerImpl;
import io.zachbr.debuggery.reflection.bukkit.implementations.*;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import io.zachbr.debuggery.reflection.types.handlers.bukkit.BootstrapBukkitHandlers;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.util.EulerAngle;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BukkitOutputHandlerTest {
    private final Logger logger = new TestLoggerImpl();
    private final TypeHandler typeHandler = new TypeHandler(logger);

    public BukkitOutputHandlerTest() {
        BootstrapBukkitHandlers.init(typeHandler, logger);
    }

    @Test
    public void testOfflinePlayer() {
        String name = "TestUser";
        UUID uuid = UUID.fromString("6c780b81-d087-485e-8786-b0a500d7c224");

        OfflinePlayer player = new TestOfflinePlayer(name, uuid);

        String out = typeHandler.getOutputFor(player);

        assertNotNull(out);
        assertTrue(out.contains(name));
        assertTrue(out.contains(uuid.toString()));
    }

    @Test
    public void testWorldBorder() {
        int size = 500;
        int damageAmt = 5;
        Location loc = TestLocation.LOC_ZERO;

        WorldBorder border = new TestWorldBorder(size, damageAmt, loc);

        String out = typeHandler.getOutputFor(border);

        assertNotNull(out);
        assertTrue(out.contains(String.valueOf(size)));
        assertTrue(out.contains(String.valueOf(damageAmt)));

        assertTrue(out.contains(String.valueOf(loc.getBlockZ())));
        assertTrue(out.contains(String.valueOf(loc.getBlockY())));
        assertTrue(out.contains(String.valueOf(loc.getBlockZ())));
    }

    @Test
    public void testBlockState() {
        MaterialData data = new MaterialData(Material.DIAMOND, (byte) 12);
        byte lightLevel = 5;
        Location loc = TestLocation.LOC_ZERO;

        BlockData blockData = new TestBlockData(Material.STONE, "minecraft:stone");
        BlockState blockState = new TestBlockState(data, lightLevel, loc, true, blockData);

        String out = typeHandler.getOutputFor(blockState);

        assertNotNull(out);
        assertTrue(out.contains(data.toString()));
        assertTrue(out.contains(blockData.getAsString()));
        assertTrue(out.contains(String.valueOf(lightLevel)));
        assertTrue(out.contains(loc.toString()));
    }

    @Test
    public void testHelpMap() {
        HelpTopic topicTacos = new TestHelpTopic("taco", "Makes tacos", "taco.taco");
        HelpTopic topicDebuggery = new TestHelpTopic("debuggery", "Exposes API stuffs", "d.g");

        HelpMap helpMap = new TestHelpMap(topicTacos, topicDebuggery);

        String out = typeHandler.getOutputFor(helpMap);

        assertNotNull(out);
        assertTrue(out.contains(topicTacos.getName()));
        assertTrue(out.contains(topicTacos.getShortText()));
        assertTrue(out.contains(topicDebuggery.getName()));
        assertTrue(out.contains(topicDebuggery.getShortText()));
    }

    @Test
    public void testMessenger() {
        String debuggeryIn = "Debuggery In";
        String debuggeryOut = "Debuggery Out";
        String tacoInOut = "Taco Listener";
        String stuffIn = "Other stuff";
        String stuffOut = "Something here";

        Set<String> messengersIn = new HashSet<>();
        Set<String> messengersOut = new HashSet<>();

        messengersIn.add(debuggeryIn);
        messengersIn.add(tacoInOut);
        messengersIn.add(stuffIn);

        messengersOut.add(debuggeryOut);
        messengersOut.add(tacoInOut);
        messengersOut.add(stuffOut);

        Messenger messenger = new TestMessenger(messengersIn, messengersOut);

        String out = typeHandler.getOutputFor(messenger);

        assertNotNull(out);

        String[] sections = out.split("}\\n");
        String incoming = sections[0];
        String outgoing = sections[1];

        assertTrue(incoming.contains(debuggeryIn));
        assertTrue(incoming.contains(tacoInOut));
        assertTrue(incoming.contains(stuffIn));

        assertTrue(outgoing.contains(debuggeryOut));
        assertTrue(outgoing.contains(tacoInOut));
        assertTrue(outgoing.contains(stuffOut));

    }

    @Test
    public void testCommandSender() {
        String name = "Debuggery";

        CommandSender sender = new TestCommandSender(false, name);

        String out = typeHandler.getOutputFor(sender);

        assertNotNull(out);
        assertTrue(out.contains(name));
    }

    @Test
    public void testEulerAngle() {
        double x = 3.55;
        double y = 12.414;
        double z = 98.41245;

        EulerAngle angle = new EulerAngle(x, y, z);
        String out = typeHandler.getOutputFor(angle);

        assertNotNull(out);
        assertTrue(out.contains(String.valueOf(x)));
        assertTrue(out.contains(String.valueOf(y)));
        assertTrue(out.contains(String.valueOf(z)));
    }
}

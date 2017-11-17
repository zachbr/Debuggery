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

package com.destroystokyo.debuggery.reflection.formatters;

import com.destroystokyo.debuggery.reflection.formatters.implementations.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.messaging.Messenger;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OutputFormatterTest {

    @Test
    public void testNullInput() {
        assertNull(OutputFormatter.getOutput(null));
    }

    @Test
    public void testOfflinePlayer() {
        String name = "TestUser";
        UUID uuid = UUID.fromString("6c780b81-d087-485e-8786-b0a500d7c224");

        OfflinePlayer player = new TestOfflinePlayer(name, uuid);

        String out = OutputFormatter.getOutput(player);

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

        String out = OutputFormatter.getOutput(border);

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

        BlockState blockState = new TestBlockState(data, lightLevel, loc);

        String out = OutputFormatter.getOutput(blockState);

        assertNotNull(out);
        assertTrue(out.contains(data.toString()));
        assertTrue(out.contains(String.valueOf(data.getItemTypeId())));
        assertTrue(out.contains(String.valueOf(lightLevel)));
        assertTrue(out.contains(loc.toString()));
    }

    @Test
    public void testHelpMap() {
        HelpTopic topicTacos = new TestHelpTopic("taco", "Makes tacos", "taco.taco");
        HelpTopic topicDebuggery = new TestHelpTopic("debuggery", "Exposes API stuffs", "d.g");

        HelpMap helpMap = new TestHelpMap(topicTacos, topicDebuggery);

        String out = OutputFormatter.getOutput(helpMap);

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

        String out = OutputFormatter.getOutput(messenger);

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

        String out = OutputFormatter.getOutput(sender);

        assertNotNull(out);
        assertTrue(out.contains(name));
    }

    @Test
    public void testArray() {
        // Test Objects first
        CommandSender[] senderArray = {
                new TestCommandSender(false, "console"),
                new TestCommandSender(false, "someone"),
                new TestCommandSender(false, "choco")
        };

        String out = OutputFormatter.getOutput(senderArray);

        assertNotNull(out);
        for (CommandSender sender : senderArray) {
            assertTrue(out.contains(sender.getName()));
        }

        // Now test primitives, since we do some special handling there
        int[] intArray = {1, 2, 3, 4, 5};

        out = OutputFormatter.getOutput(intArray);
        assertNotNull(out);

        for (int val : intArray) {
            assertTrue(out.contains(String.valueOf(val)));
        }
    }

    @Test
    public void testMap() {
        String tacoKey = "taco";
        String tacoVal = "tacos are great";
        String potatoKey = "potato";
        String potatoVal = "potatoes are bad";

        Map<String, String> map = new HashMap<>();
        map.put(tacoKey, tacoVal);
        map.put(potatoKey, potatoVal);

        String out = OutputFormatter.getOutput(map);

        assertNotNull(out);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertTrue(out.contains(entry.getKey()));
            assertTrue(out.contains(entry.getValue()));
        }
    }

    @Test
    public void testCollection() {
        // Bet you don't guess what song just came on
        String[] values = {
                "Buy it",
                "use it",
                "break it",
                "fix it",
                "Trash it",
                "change it",
                "mail - upgrade it",
                "Charge it",
                "point it",
                "zoom it,",
                "press it",
                "Snap it",
                "work it",
                "quick - erase it",
                "Write it",
                "cut it",
                "paste it",
                "save it",
                "Load it",
                "check it",
                "quick - rewrite it",
                "Plug it",
                "play it",
                "burn it",
                "rip it",
                "Drag and drop it",
                "zip - unzip it",
                "Lock it",
                "fill it",
                "call it",
                "find it",
                "View it",
                "code it",
                "jam - unlock it",
                "Surf it",
                "scroll it",
                "pause it",
                "click it",
                "Cross it",
                "crack it",
                "switch - update it",
                "Name it",
                "read it",
                "tune it",
                "print it",
                "Scan it",
                "send it",
                "fax - rename it",
                "Touch it",
                "bring it",
                "pay it",
                "watch it",
                "Turn it",
                "leave it",
                "start - format it"
        };
        List<String> collection = Arrays.asList(values);

        String out = OutputFormatter.getOutput(collection);

        assertNotNull(out);

        for (String val : values) {
            assertTrue(out.contains(val));
        }
    }
}

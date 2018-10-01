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

package io.zachbr.debuggery.reflection.types.implementations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.*;

import java.util.Set;

public class TestMessenger implements Messenger {
    private final Set<String> in;
    private final Set<String> out;

    public TestMessenger(Set<String> in, Set<String> out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public boolean isReservedChannel(String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerOutgoingPluginChannel(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterOutgoingPluginChannel(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterOutgoingPluginChannel(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getOutgoingChannels() {
        return this.out;
    }

    @Override
    public Set<String> getOutgoingChannels(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getIncomingChannels() {
        return this.in;
    }

    @Override
    public Set<String> getIncomingChannels(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistrationValid(PluginMessageListenerRegistration registration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isIncomingChannelRegistered(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOutgoingChannelRegistered(Plugin plugin, String channel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispatchIncomingMessage(Player source, String channel, byte[] message) {
        throw new UnsupportedOperationException();
    }
}

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

package io.zachbr.debuggery.reflection.types.handlers.output;

import io.zachbr.debuggery.reflection.types.TypeHandler;
import io.zachbr.debuggery.reflection.types.handlers.base.OHandler;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OMessengerHandler implements OHandler {

    @Override
    public @Nullable String getFormattedOutput(Object object) {
        final Messenger messenger = (Messenger) object;
        final TypeHandler instance = TypeHandler.getInstance();

        String incomingBanner = "-- Incoming Channels --\n";
        String incomingChannels = instance.getOutputFor(messenger.getIncomingChannels()); // pass to collection handling
        String outgoingBanner = "\n-- Outgoing Channels --\n";
        String outgoingChannels = instance.getOutputFor(messenger.getOutgoingChannels()); // pass to collection handling

        return incomingBanner + incomingChannels + outgoingBanner + outgoingChannels;
    }

    @Override
    public @NotNull Class<?> getRelevantClass() {
        return Messenger.class;
    }
}

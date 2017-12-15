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

package com.destroystokyo.debuggery.reflection.formatters.implementations;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class TestCommandSender implements CommandSender {
    private final boolean op;
    private final String name;

    public TestCommandSender(boolean isOp, String name) {
        this.op = isOp;
        this.name = name;
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(String[] messages) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Server getServer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPermissionSet(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        return this.op;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }
}

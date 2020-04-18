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

package io.zachbr.debuggery.reflection.bukkit.implementations;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class TestOfflinePlayer implements OfflinePlayer {
    private final String name;
    private final UUID uuid;

    public TestOfflinePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public boolean isOnline() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public boolean isBanned() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWhitelisted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getPlayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getFirstPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPlayedBefore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastLogin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastSeen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }
}

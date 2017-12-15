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

import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestHelpMap implements HelpMap {
    private List<HelpTopic> helpTopics = new ArrayList<>();

    public TestHelpMap(List<HelpTopic> helpTopics) {
        this.helpTopics = helpTopics;
    }

    public TestHelpMap(HelpTopic... topics) {
        helpTopics.addAll(Arrays.asList(topics));
    }

    @Override
    public HelpTopic getHelpTopic(String topicName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return helpTopics;
    }

    @Override
    public void addTopic(HelpTopic topic) {
        helpTopics.add(topic);
    }

    @Override
    public void clear() {
        helpTopics.clear();
    }

    @Override
    public void registerHelpTopicFactory(Class<?> commandClass, HelpTopicFactory<?> factory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getIgnoredPlugins() {
        throw new UnsupportedOperationException();
    }
}

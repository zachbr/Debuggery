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

package io.zachbr.debuggery.reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to allow querying a java class for methods based on their names
 */
public class MethodMap {
    public static final MethodMap EMPTY = new MethodMap();
    private final Map<String, Method> backingMap = new HashMap<>();
    private final Class<?> mappedClass;

    private MethodMap() {
        mappedClass = this.getClass();
    }

    MethodMap(@NotNull Class<?> clazz) {
        this.mappedClass = Objects.requireNonNull(clazz);
        for (Method method : clazz.getMethods()) {
            final String identifier = ReflectionUtil.getMethodId(method);
            backingMap.put(identifier, method);
        }
    }

    /**
     * Gets a method by its identifier
     *
     * @param identifier String identifier from {@link ReflectionUtil#getMethodId(Method)}
     * @return the associated method or null if it does not exist
     */
    public @Nullable Method getById(String identifier) {
        return backingMap.get(identifier);
    }

    /**
     * Gets whether this method map contains any entries for the specified id
     *
     * @param identifier String identifier from {@link ReflectionUtil#getMethodId(Method)}
     * @return True if this method map contains a method for the identifier
     */
    public boolean containsId(String identifier) {
        return backingMap.containsKey(identifier);
    }

    /**
     * Gets all methods that are mapped by this method map
     *
     * @return all methods
     */
    public @NotNull Set<Method> getAllMethods() {
        return new HashSet<>(backingMap.values());
    }

    /**
     * Gets all identifiers for this method map
     *
     * @return all identifiers
     */
    public @NotNull Set<String> getAllIds() {
        return new HashSet<>(backingMap.keySet());
    }

    /**
     * Gets the class this map corresponds to
     *
     * @return corresponding class
     */
    public @NotNull Class<?> getMappedClass() {
        return this.mappedClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        MethodMap other = (MethodMap) o;
        return backingMap.equals(other.backingMap);
    }

    @Override
    public int hashCode() {
        return 67 * backingMap.hashCode();
    }

    @Override
    public String toString() {
        return String.format("MethodMap{class: %s, elements: %d}",
                this.mappedClass,
                this.backingMap.size()
        );
    }
}

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

package io.zachbr.debuggery.reflection.chain;

import io.zachbr.debuggery.Logger;
import io.zachbr.debuggery.reflection.MethodMapProvider;
import io.zachbr.debuggery.reflection.types.TypeHandler;
import io.zachbr.debuggery.reflection.types.handlers.base.platform.PlatformSender;

import java.util.Objects;

// todo - is this going to stick around?
public class ReflectionChainFactory {
    final TypeHandler typeHandler;
    final MethodMapProvider methodMapProvider;
    final Logger logger;

    public ReflectionChainFactory(TypeHandler handler, MethodMapProvider provider, Logger logger) {
        this.typeHandler = handler;
        this.methodMapProvider = provider;
        this.logger = logger;
    }

    public ReflectionResult runChain(String[] args, Object initialInstance, PlatformSender sender) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(initialInstance);

        ReflectionChain chain = new ReflectionChain(this, args, initialInstance, sender);
        chain.runChain();

        return chain.getResult();
    }
}

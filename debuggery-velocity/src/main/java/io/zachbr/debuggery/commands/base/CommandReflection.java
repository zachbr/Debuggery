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

package io.zachbr.debuggery.commands.base;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import io.zachbr.debuggery.DebuggeryVelocity;
import io.zachbr.debuggery.reflection.MethodMap;
import io.zachbr.debuggery.reflection.MethodMapProvider;
import io.zachbr.debuggery.reflection.chain.ReflectionResult;
import io.zachbr.debuggery.reflection.types.InputException;
import io.zachbr.debuggery.reflection.types.handlers.base.platform.PlatformSender;
import io.zachbr.debuggery.util.*;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

// todo - Really really refactor to -common, this was just a few changes
/**
 * Base class for all commands that use reflection to dig into Velocity's API
 */
public abstract class CommandReflection extends CommandBase {
    private final DebuggeryVelocity debuggery;
    private final MethodMapProvider mapCache;
    private MethodMap availableMethods = MethodMap.EMPTY;

    protected CommandReflection(String name, String permission, boolean requiresPlayer, Class<?> clazz, DebuggeryVelocity plugin) {
        super(name, permission, requiresPlayer);
        this.debuggery = plugin;
        this.mapCache = plugin.getMethodMapProvider();
        updateReflectionClass(clazz);
    }

    @Override
    protected void helpLogic(@NotNull CommandSource sender, @NotNull String[] args) {
        sender.sendMessage(TextComponent.of("Uses reflection to call API methods built into Bukkit."));
        sender.sendMessage(TextComponent.of("Try using the tab completion to see all available subcommands."));
    }

    @Override
    public List<String> tabCompleteLogic(@NotNull CommandSource sender, @NotNull String[] args) {
        List<String> arguments = Arrays.asList(args);
        MethodMap reflectionMap = this.availableMethods;

        return CommandUtil.getReflectiveCompletions(arguments, reflectionMap, mapCache);
    }

    /**
     * Handles all the reflection based command logic
     *
     * @param sender   sender to send information to
     * @param args     command arguments
     * @param instance instance of the class type
     * @return true if handled successfully
     */
    protected boolean doReflectionLookups(@NotNull CommandSource sender, @NotNull String[] args, Object instance) {
        // 0 args just return info on object itself
        if (args.length == 0) {
            String result = getOutputStringFor(instance);
            if (result != null) {
                sender.sendMessage(TextComponent.of(result));
            }
            return true;
        }

        // more than 0 args, start chains
        Class<?> activeClass = availableMethods.getMappedClass();
        if (!activeClass.isInstance(instance)) {
            throw new IllegalArgumentException("Instance is of type: " + instance.getClass().getSimpleName() + "but was expecting: " + activeClass.getSimpleName());
        }

        final String inputMethod = args[0];
        if (!availableMethods.containsId(inputMethod)) {
            sender.sendMessage(TextComponent.of("Unknown or unavailable method").color(TextColor.RED));
            return true;
        }

        PlatformSender<?> platformSender = new PlatformSender<>(sender);
        ReflectionResult chainResult = debuggery.runReflectionChain(args, instance, platformSender);
        switch (chainResult.getType()) {
            case SUCCESS:
                notifySenderOfSuccess(sender, chainResult);
                break;
            case INPUT_ERROR:
            case UNHANDLED_EXCEPTION:
                notifySenderOfException(sender, chainResult);
                break;
            case NULL_REFERENCE:
            case UNKNOWN_REFERENCE:
            case ARG_MISMATCH:
                notifySenderOfResultReason(sender, chainResult);
                break;
            default:
                throw new IllegalArgumentException("Unhandled switch case for result of type: " + chainResult.getType());
        }

        return true;
    }

    private void notifySenderOfException(@NotNull CommandSource sender, @NotNull ReflectionResult chainResult) {
        Throwable ex = chainResult.getException();
        Objects.requireNonNull(ex);

        String errorMessage = ex instanceof InputException ? "Exception deducing proper types from your input!" : "Exception invoking method - See console for more details!";
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();

        if (sender instanceof Player) {
            sendFancyChatException(sender, errorMessage, cause);
        } else {
            sender.sendMessage(TextComponent.of(errorMessage).color(TextColor.RED));
        }

        cause.printStackTrace();
    }

    private void notifySenderOfResultReason(CommandSource sender, ReflectionResult chainResult) {
        Objects.requireNonNull(chainResult.getReason());
        sender.sendMessage(TextComponent.of(chainResult.getReason()));
    }

    private void notifySenderOfSuccess(CommandSource sender, ReflectionResult chainResult) {
        String output = getOutputStringFor(chainResult.getEndingInstance());
        if (output != null) {
            sender.sendMessage(TextComponent.of(output));
        }
    }

    /**
     * Updates the locally cached reflection class
     *
     * @param typeIn class type to cache a reflection map for
     */
    protected void updateReflectionClass(Class<?> typeIn) {
        if (availableMethods.getMappedClass() != typeIn) {
            availableMethods = mapCache.getMethodMapFor(typeIn);
        }
    }

    /**
     * Convenience method to run objects past the TypeHandler
     *
     * @param object Object to get String output for
     * @return textual description of Object
     */
    protected @Nullable String getOutputStringFor(@Nullable Object object) {
        return debuggery.getTypeHandler().getOutputFor(object);
    }

    private void sendFancyChatException(@NotNull CommandSource source, @NotNull String msg, @NotNull Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));

        TextComponent component = TextComponent.of(msg)
                .color(TextColor.RED)
                .hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT,
                        TextComponent.of(writer.toString().replaceAll("\t", "    ")
                                .replaceAll("\r", ""))));

        source.sendMessage(component);
    }
}

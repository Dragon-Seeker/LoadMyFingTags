package io.wispforest.lmft.utils;

import io.wispforest.lmft.LMFTCommon;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.function.Function;
import java.util.stream.Stream;

public class ClickEventUtils {
    private static final Function<String, @Nullable ClickEvent> urlClickEventFactory = findConstructor();

    private static Function<String, @Nullable ClickEvent> findConstructor() {
        var lookup = MethodHandles.publicLookup();

        var clickEventClass = ClickEvent.class;

        final var oldHandle = MethodHandleUtils.findConstructor(lookup, clickEventClass, ClickEvent.Action.class, String.class);

        if (oldHandle != null) {
            return s -> MethodHandleUtils.wrapForErrors("addUrlToStyleOld", () -> oldHandle.invoke(ClickEvent.Action.OPEN_URL, s));
        }

        var innerUrlClass = MethodHandleUtils.findClass(
            Stream.of("OpenUrl", "class_10608")
                .map(innerClass -> clickEventClass.getName() + "$" + innerClass)
                .toArray(String[]::new)
        );

        if (innerUrlClass != null) {
            final var newerHandle = MethodHandleUtils.findConstructor(lookup, innerUrlClass, URI.class);

            if (newerHandle != null) {
                return s -> MethodHandleUtils.wrapForErrors("addUrlToStyleNew", () -> newerHandle.invoke(URI.create(s)));
            }
        }

        LMFTCommon.LOGGER.warn(LMFTCommon.PREFIX + "Unable to find handle to create click event for possible error messages");

        return s -> null;
    }

    public static Style addUrlToStyle(Style style, String url) {
        var event = urlClickEventFactory.apply(url);

        return (event != null) ? style.withClickEvent(event) : null;
    }
}

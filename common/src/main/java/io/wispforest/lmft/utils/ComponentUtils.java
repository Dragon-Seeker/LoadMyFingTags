package io.wispforest.lmft.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ComponentUtils {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Nullable
    private static final Function<String, MutableComponent> mutableComponentEventFactory = findConstructor();

    private static Function<String, MutableComponent> findConstructor() {
        var staticConstructor = MethodHandleUtils.findStaticMethod(lookup, Component.class, MethodType.methodType(MutableComponent.class, String.class), "literal", "method_43470", "m_237113_");

        if (staticConstructor != null) {
            return s -> MethodHandleUtils.wrapForErrors("createMutableComponentNew", () -> (MutableComponent) staticConstructor.invoke(s));
        }

        var clazz = MethodHandleUtils.findClass("net.minecraft.network.chat.TextComponent", "net.minecraft.text.LiteralText", "net.minecraft.class_2585", "net.minecraft.src.C_5025_");

        if (clazz != null) {
            var constructor = MethodHandleUtils.findConstructor(lookup, clazz, String.class);

            if (constructor != null) {
                return s -> MethodHandleUtils.wrapForErrors("createMutableComponentOld", () -> (MutableComponent) constructor.invoke(s));
            }
        }

        return null;
    }

    public static boolean canCreateMessages() {
        return mutableComponentEventFactory != null;
    }

    @Nullable
    public static MutableComponent createMutableComponent(String text) {
        return mutableComponentEventFactory != null ? mutableComponentEventFactory.apply(text) : null;
    }

    //--

    @Nullable
    private static final BiConsumer<MutableComponent, Component> appendMethodCall = createAppendHandle();

    private static BiConsumer<MutableComponent, Component> createAppendHandle() {
        var method = MethodHandleUtils.findMethod(lookup, MutableComponent.class, MethodType.methodType(MutableComponent.class, Component.class), "append", "method_10852", "m_7220_");

        return method != null
            ? (component, sibling) -> MethodHandleUtils.wrapForErrors("append", () -> (MutableComponent) method.invoke(component, sibling))
            : null;
    }

    public static MutableComponent append(MutableComponent component, Component sibling) {
        if (appendMethodCall != null) appendMethodCall.accept(component, sibling);

        return component;
    }

    //--

    private static final BiConsumer<MutableComponent, UnaryOperator<Style>> styleOperatorMethodCall = createStyleOperatorMethod();

    private static BiConsumer<MutableComponent, UnaryOperator<Style>> createStyleOperatorMethod() {
        var method = MethodHandleUtils.findMethod(lookup, MutableComponent.class, MethodType.methodType(MutableComponent.class, UnaryOperator.class), "withStyle", "styled", "method_27694", "m_130938_");

        return method != null
            ? (component, operator) -> MethodHandleUtils.wrapForErrors("withStyle", () -> (MutableComponent) method.invoke(component, operator))
            : null;
    }

    public static MutableComponent withStyle(MutableComponent component, UnaryOperator<Style> style) {
        if (styleOperatorMethodCall != null) styleOperatorMethodCall.accept(component, style);

        return component;
    }

    //--

    private static final BiConsumer<MutableComponent, ChatFormatting[]> styleMethodCall = createStyleMethod();

    private static BiConsumer<MutableComponent, ChatFormatting[]> createStyleMethod() {
        var method = MethodHandleUtils.findMethod(lookup, MutableComponent.class, MethodType.methodType(MutableComponent.class, ChatFormatting[].class), "withStyle", "formatted", "method_27695", "m_130944_");

        return method != null
            ? (component, operator) -> MethodHandleUtils.wrapForErrors("withStyle", () -> (MutableComponent) method.invoke(component, operator))
            : null;
    }

    public static MutableComponent withStyle(MutableComponent component, ChatFormatting... formats) {
        if (styleMethodCall != null) styleMethodCall.accept(component, formats);

        return component;
    }
}

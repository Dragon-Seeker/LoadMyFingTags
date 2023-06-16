package io.wispforest.lmft.mixin;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFTCommon;
import io.wispforest.lmft.ducks.TagGroupLoaderAccessorDuck;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin<T> implements TagGroupLoaderAccessorDuck {

    @Unique private static final ThreadLocal<Identifier> currentTagId = ThreadLocal.withInitial(() -> new Identifier("", ""));

    @Inject(method = "method_32841", at = @At("HEAD"))
    private static void saveTagId(Function function, Function function2, Map map, Identifier identifier, Tag.Builder builder, CallbackInfo ci){
        currentTagId.set(identifier);
    }

    @Override
    public Identifier getCurrentId() {
        return currentTagId.get();
    }
}

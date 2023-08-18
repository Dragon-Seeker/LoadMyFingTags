package io.wispforest.lmft.forge.mixin;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin<T> {

    @Unique private static final Logger LOGGER = LogUtils.getLogger();
    @Unique private static final ThreadLocal<Identifier> currentTagId = ThreadLocal.withInitial(() -> new Identifier("", ""));

    @Inject(method = "resolveAll(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventTagsFromFailingToLoad(TagEntry.ValueGetter<T> valueGetter, List<TagGroupLoader.TrackedEntry> list, CallbackInfoReturnable<Either<Collection<TagGroupLoader.TrackedEntry>, Collection<T>>> cir, HashSet builder, List<TagGroupLoader.TrackedEntry> list2){
        if(list2.isEmpty()) return;

        LOGGER.error(
                "[Load My Fucking Tags] Couldn't load certain entries with the tag {}: {}",
                currentTagId.get(),
                list2.stream().map(Objects::toString).collect(Collectors.joining(", "))
        );

        list2.clear();

        LMFTCommon.areTagsCooked = true;
    }

    @Inject(method = "lambda$build$6", at = @At("HEAD"))
    private void saveTagId(TagEntry.ValueGetter valueGetter, Map map, Identifier id, TagGroupLoader.TagDependencies dependencies, CallbackInfo ci){
        currentTagId.set(id);
    }
}

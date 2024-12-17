package io.wispforest.lmft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(TagLoader.class)
public class TagGroupLoaderMixin<T> {

    @Unique private static final Logger LOGGER = LogUtils.getLogger();
    @Unique private static final ThreadLocal<ResourceLocation> currentTagId = ThreadLocal.withInitial(() -> ResourceLocation.fromNamespaceAndPath("", ""));

    @Inject(method = "tryBuildTag(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private void preventTagsFromFailingToLoad(TagEntry.Lookup<T> valueGetter, List<TagLoader.EntryWithSource> list, CallbackInfoReturnable<Either<Collection<TagLoader.EntryWithSource>, Collection<T>>> cir, @Local(ordinal = 1) List<TagLoader.EntryWithSource> list2){
        if(list2.isEmpty()) return;

        LOGGER.error(
                "[Load My F***ing Tags] Couldn't load certain entries with the tag {}: {}",
                currentTagId.get(),
                list2.stream().map(Objects::toString).collect(Collectors.joining(", "))
        );

        list2.clear();

        LMFTCommon.areTagsCooked = true;
    }

    @Inject(method = { "method_51476", "lambda$build$6" }, at = @At("HEAD"), require = 1, allow = 1)
    private void saveTagId(TagEntry.Lookup lookup, Map map, ResourceLocation id, TagLoader.SortingEntry sortingEntry, CallbackInfo ci){
        currentTagId.set(id);
    }
}

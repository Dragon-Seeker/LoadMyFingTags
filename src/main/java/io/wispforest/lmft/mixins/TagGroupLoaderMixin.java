package io.wispforest.lmft.mixins;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFT;
import net.minecraft.tag.TagEntry;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
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

    @Unique
    private static final Logger LOGGER = LogManager.getLogger("LMFT-TagGroupLoader");

    @Unique
    private static final ThreadLocal<Identifier> currentTagId = ThreadLocal.withInitial(() -> new Identifier("", ""));

    @Inject(method = "method_43952", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventTagsFromFailingToLoad(TagEntry.ValueGetter<T> valueGetter, List<TagGroupLoader.TrackedEntry> list, CallbackInfoReturnable<Either<Collection<TagGroupLoader.TrackedEntry>, Collection<T>>> cir, ImmutableSet.Builder builder, List<TagGroupLoader.TrackedEntry> list2){
        if(!list2.isEmpty()){
            LOGGER.error(
                "[Load My Fucking Tags] Couldn't load certain entries with the tag {}: {}",
                currentTagId.get(),
                list2.stream().map(Objects::toString).collect(Collectors.joining(", "))
            );

            list2.clear();

            LMFT.areTagsCooked = true;
        }
    }

    @Inject(method = "method_32841", at = @At("HEAD"))
    private void saveTagId(TagEntry.ValueGetter valueGetter, Map map, Identifier identifier, List list, CallbackInfo ci){
        currentTagId.set(identifier);
    }
}

package io.wispforest.lmft.mixins;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFT;
import io.wispforest.lmft.ducks.TagGroupLoaderAccessorDuck;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mixin(Tag.Builder.class)
public class TagBuilderMixin<T> {

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private static final TagGroupLoader<?> DUMMY_LOADER = new TagGroupLoader<>(identifier -> Optional.empty(), "");

    @Inject(method = "build", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventTagsFromFailingToLoad(Function<Identifier, Tag<T>> tagGetter, Function<Identifier, T> objectGetter, CallbackInfoReturnable<Either<Collection<Tag.TrackedEntry>, Tag<T>>> cir, ImmutableSet.Builder builder, List list){
        if(!list.isEmpty()){
            LOGGER.error(
                "[Load My Fucking Tags] Couldn't load certain entries with the tag {}: {}",
                ((TagGroupLoaderAccessorDuck)DUMMY_LOADER).getCurrentId(),
                list.stream().map(Objects::toString).collect(Collectors.joining(", "))
            );

            list.clear();

            LMFT.areTagsCooked = true;
        }
    }
}

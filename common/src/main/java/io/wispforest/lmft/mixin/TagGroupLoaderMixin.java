package io.wispforest.lmft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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

import java.util.List;
import java.util.Map;

@Mixin(value = TagLoader.class, priority = 800)
public abstract class TagGroupLoaderMixin<T> {
    @WrapOperation(method = "tryBuildTag(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean preventTagsFromFailingToLoad(List list2, Operation<Boolean> original){
        LMFTCommon.handleAndLogInvalidEntries(list2);

        return original.call(list2);
    }

    @Inject(method = { "method_51476", "lambda$build$6" }, at = @At("HEAD"), require = 1, allow = 1)
    private void saveTagId(CallbackInfo ci, @Local(ordinal = 0) ResourceLocation id){
        LMFTCommon.setTagId(id);
    }
}

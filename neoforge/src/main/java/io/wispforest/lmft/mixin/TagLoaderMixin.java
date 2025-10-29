package io.wispforest.lmft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Pseudo
@Mixin(targets = {
    "net/minecraft/tags/TagLoader",               // Mojmap      / 1.16.2 and up
}, priority = 800, remap = false)
public abstract class TagLoaderMixin {

    @WrapOperation(method = {
        "tryBuildTag(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",             // Mojmap        / 1.21.2 and up
        "build(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",                   // Mojmap        / 1.19 - 1.21.1
    }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), require = 0, allow = 1, remap = false)
    private boolean preventTagsFromFailingToLoad(List list2, Operation<Boolean> original){
        LMFTCommon.handleAndLogInvalidEntries(list2);

        return original.call(list2);
    }

    @Inject(method = {
        "lambda$build$6*",  // Neo Mojmap / 1.20 and up
    }, at = @At("HEAD"), require = 0, allow = 1, remap = false)
    private void saveTagId(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) ResourceLocation id){
        LMFTCommon.setTagId(id);
    }
}

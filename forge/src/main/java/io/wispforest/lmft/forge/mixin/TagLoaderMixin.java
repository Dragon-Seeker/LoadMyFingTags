package io.wispforest.lmft.forge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.resources.ResourceLocation;
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
        "build(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",                   // Mojmap        / 1.19 - 1.21.1
        "m_215978_(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",            // Forge SRG     / 1.19 - 1.20.4
    }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), require = 0, allow = 1, remap = false)
    private boolean preventTagsFromFailingToLoad(List list2, Operation<Boolean> original){
        LMFTCommon.handleAndLogInvalidEntries(list2);

        return original.call(list2);
    }

    @Inject(method = {
        "lambda$build$6*",  // Neo Mojmap / 1.20 and up
    }, at = @At("HEAD"), require = 0, allow = 1, remap = false)
    private void saveTagId1(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) ResourceLocation id){
        LMFTCommon.setTagId(id);
    }

    @Inject(method = {
        "m_284005_",       // Forge SRG  / 1.20 - 1.20.4
        "m_215981_",       // Forge SRG  / 1.19 to 1.19.4
        "m_144533_"        // Forge SRG  / 1.18.2
    }, at = @At("HEAD"), require = 0, allow = 1, remap = false)
    private static void saveTagId2(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) ResourceLocation id){
        LMFTCommon.setTagId(id);
    }
}

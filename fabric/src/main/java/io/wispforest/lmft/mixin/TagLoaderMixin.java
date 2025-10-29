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
    "net/minecraft/class_3503",                   // Fabric Inter.
}, priority = 800, remap = false)
public abstract class TagLoaderMixin {

    @WrapOperation(method = {
        "method_43952(Lnet/minecraft/class_3497$class_7474;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;",           // Fabric Inter. / 1.19 and up
    }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), require = 0, allow = 1, remap = false)
    private boolean preventTagsFromFailingToLoad(List list2, Operation<Boolean> original) {
        LMFTCommon.handleAndLogInvalidEntries(list2);

        return original.call(list2);
    }

    @Inject(method = {
        "method_51476",    // Fabric     / 1.20 and up
        "method_32841",    // Fabric     / 1.18.2 to 1.19.4
    }, at = @At("HEAD"), require = 0, allow = 1, remap = false)
    private void saveTagId(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) ResourceLocation id) {
        LMFTCommon.setTagId(id);
    }
}

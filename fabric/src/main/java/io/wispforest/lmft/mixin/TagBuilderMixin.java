package io.wispforest.lmft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.lmft.LMFTCommon;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

// 1.13 - 1.18.2
@Pseudo
@Mixin(targets = {
    "net/minecraft/class_3494$class_3495",  // Fabric Inter.
})
public abstract class TagBuilderMixin {

    @WrapOperation(method = {
        "method_26782",     // Fabric Inter.
    }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), require = 0, allow = 1, remap = false)
    private boolean preventTagsFromFailingToLoad(List list2, Operation<Boolean> original){
        LMFTCommon.handleAndLogInvalidEntries(list2);

        return original.call(list2);
    }
}

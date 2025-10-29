package io.wispforest.lmft.forge.mixin;

import io.wispforest.lmft.LMFTCommon;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Pseudo
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = {
        "reloadResources",
        "m_129861_"
    }, at = @At("HEAD"), remap = false)
    private void startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        LMFTCommon.liftCookedTagState();
    }
}

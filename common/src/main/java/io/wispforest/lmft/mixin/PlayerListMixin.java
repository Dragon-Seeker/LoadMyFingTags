package io.wispforest.lmft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(
        method = "placeNewPlayer",
        at = @At(value = "NEW", target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;")
    )
    private void hookOnPlayerConnect(CallbackInfo ci, @Local(argsOnly = true) ServerPlayer player) {
        LMFTCommon.sendMessage(player);
    }

    @Inject(
        method = "reloadResources",
        at = @At(value = "NEW", target = "Lnet/minecraft/network/protocol/common/ClientboundUpdateTagsPacket;")
    )
    private void hookOnDataPacksReloaded(CallbackInfo ci) {
        ((PlayerList) (Object) this).getPlayers().forEach(LMFTCommon::sendMessage);
    }
}

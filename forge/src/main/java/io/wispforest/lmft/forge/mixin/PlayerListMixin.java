package io.wispforest.lmft.forge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.lmft.LMFTCommon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(
        method = {
            "placeNewPlayer*",
            "m_11261_*"
        },
        at = @At(value = "NEW", target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;"),
        remap = false,
        require = 1,
        allow = 1
    )
    private void hookOnPlayerConnect(CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) ServerPlayer player) {
        LMFTCommon.sendMessage(player);
    }

    @Inject(
        method = {
            "reloadResources*",
            "m_11315_*"
        },
        at = {
            @At(value = "NEW", target = "Lnet/minecraft/network/protocol/game/ClientboundUpdateTagsPacket;"),
            @At(value = "NEW", target = "Lnet/minecraft/network/protocol/common/ClientboundUpdateTagsPacket;")
        },
        remap = false,
        require = 1,
        allow = 1
    )
    private void hookOnDataPacksReloaded(CallbackInfo ci) {
        ((PlayerList) (Object) this).getPlayers().forEach(LMFTCommon::sendMessage);
    }
}

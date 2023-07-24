package io.wispforest.lmft.forge;

import io.wispforest.lmft.LMFTCommon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(LMFTCommon.MODID)
public class LMFTForge {
    public LMFTForge() {
        LMFTCommon.init(FMLPaths.CONFIGDIR.get());

        MinecraftForge.EVENT_BUS
                .addListener((PlayerEvent.PlayerLoggedInEvent e) -> LMFTCommon.sendMessage(e.getPlayer()));
    }
}

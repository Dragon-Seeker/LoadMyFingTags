package io.wispforest.lmft.forge;

import io.wispforest.lmft.LMFTCommon;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(LMFTCommon.MODID)
public class LMFTForge {
    public LMFTForge() {
        LMFTCommon.init(FMLPaths.CONFIGDIR.get());

        NeoForge.EVENT_BUS
                .addListener((PlayerEvent.PlayerLoggedInEvent e) -> LMFTCommon.sendMessage(e.getEntity()));
    }
}

package io.wispforest.lmft.forge;

import io.wispforest.lmft.LMFTCommon;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(LMFTCommon.MODID)
public class LMFTForge {
    public LMFTForge() {
        LMFTCommon.init(FMLPaths.CONFIGDIR.get());

        NeoForge.EVENT_BUS.addListener((OnDatapackSyncEvent e) -> e.getRelevantPlayers().forEach(LMFTCommon::sendMessage));
    }
}

package io.wispforest.lmft.fabric;

import io.wispforest.lmft.LMFTCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class LMFTFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        LMFTCommon.init(FabricLoader.getInstance().getConfigDir());

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> LMFTCommon.areTagsCooked = false);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> LMFTCommon.sendMessage(player));
    }
}

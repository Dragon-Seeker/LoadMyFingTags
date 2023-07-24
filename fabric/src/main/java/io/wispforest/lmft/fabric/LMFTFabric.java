package io.wispforest.lmft.fabric;

import io.wispforest.lmft.LMFTCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class LMFTFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        LMFTCommon.init(FabricLoader.getInstance().getConfigDir());

        ServerPlayConnectionEvents.JOIN.register((h, s, c) -> LMFTCommon.sendMessage(h.getPlayer()));
    }
}

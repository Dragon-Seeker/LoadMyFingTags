package io.wispforest.lmft.fabric;

import io.wispforest.lmft.LMFTCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class LMFTFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        LMFTCommon.init();

        ServerPlayConnectionEvents.JOIN.register((h, s, c) -> LMFTCommon.sendMessage(h.getPlayer()));
    }
}

package io.wispforest.lmft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class LMFTCommon {

    public static final String MODID = "lmft";

    public static boolean areTagsCooked = false;

    public static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");

    public static void init() {}

    public static void sendMessage(PlayerEntity entity){
        if(LMFTCommon.areTagsCooked && !LMFTCommon.disableIngameError){
            entity.sendMessage(
                    new LiteralText("[Load My Fucking Tags]: It seems that some tags are a bit cooked, Look at the Logs for more Info if certain Functions are broken!")
                            .formatted(Formatting.RED, Formatting.BOLD),
                    false
            );
        }
    }
}

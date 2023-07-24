package io.wispforest.lmft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LMFTCommon {

    public static final String MODID = "lmft";

    public static boolean areTagsCooked = false;

    public static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");

    public static void init() {}

    public static void sendMessage(PlayerEntity entity){
        if(LMFTCommon.areTagsCooked && !LMFTCommon.disableIngameError){
            entity.sendMessage(
                    Text.literal("[Load My F***ing Tags]: It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. Click me for more info about this feature.")
                            .styled(style -> {
                                return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Dragon-Seeker/LoadMyFingTags/blob/ae915c6453f0537e12604e3b0c3d0c378b2dfd88/info.md"));
                            })
                            .formatted(Formatting.RED, Formatting.BOLD),
                    false
            );
        }
    }
}

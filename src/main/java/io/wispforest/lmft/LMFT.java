package io.wispforest.lmft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LMFT implements ModInitializer {

	public static final String MODID = "lmft";

	public static boolean areTagsCooked = false;

	public static boolean outputErrorMessageIngame = Boolean.getBoolean("lmft.error_output");

	public static final Text errorText = Text.literal("[Load My Fucking Tags]: It seems that some tags are a bit cooked, Look at the Logs for more Info if certain Functions are broken!").formatted(Formatting.RED, Formatting.BOLD);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if(areTagsCooked && !outputErrorMessageIngame){
				handler.player.sendMessage(errorText, false);
			}
		});
	}
}

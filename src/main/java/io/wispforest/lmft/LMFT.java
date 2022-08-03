package io.wispforest.lmft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LMFT implements ModInitializer {

	private static final Logger LOGGER = LogManager.getLogger(LMFT.class);

	public static final String MODID = "lmft";

	public static boolean areTagsCooked = false;

	public static boolean disableOutputErrorMessageIngame = Boolean.getBoolean("lmft.disable_error_output");

	public static final Text errorText = Text.literal("[Load My Fucking Tags]: It seems that some tags are a bit cooked, Look at the Logs for more Info if certain Functions are broken!").formatted(Formatting.RED, Formatting.BOLD);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if(areTagsCooked && !disableOutputErrorMessageIngame){
				handler.player.sendMessage(errorText, false);
			}
		});
	}

	public static void tagsAreCooked(Identifier identifier){
		areTagsCooked = true;

		LOGGER.info("[{}]{}", MODID, identifier);
	}
}

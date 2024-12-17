package io.wispforest.lmft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LMFTCommon {

    public static final String MODID = "lmft";

    public static boolean areTagsCooked = false;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = LogUtils.getLogger();

    private static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");
    private static boolean showToOnlyPrivileged = false;

    public static void init(Path configPath) {
        File configFile = new File(configPath + File.separator + "lmft.json");

        JsonObject configObject = null;

        //Based on builtinServer multi loader implementation
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("[LMFT]: Unable to find needed config file, will attempt to create such.");

                configObject = new JsonObject();
                configObject.addProperty("disableIngameError", false);
                configObject.addProperty("showToOnlyPrivileged", false);

                try (FileWriter writer = new FileWriter(configFile)) {  writer.write(GSON.toJson(configObject)); }
            } else {
                configObject = GSON.fromJson(Files.readString(configFile.toPath()), JsonObject.class);

                if(configObject == null) throw new IOException("[LMFT]: The config file was not found!");

                LOGGER.info("[LMFT]: Loaded Config File!");
            }
        } catch (IOException exception) {
            LOGGER.error("[LMFT]: Unable to create the needed config file, using default values!", exception);
        } catch (JsonSyntaxException exception) {
            LOGGER.error("[LMFT]: Unable to read the needed config file, using default values!", exception);
        }

        if(configObject != null) {
            if (configObject.has("disableIngameError")) {
                disableIngameError = disableIngameError || configObject.get("disableIngameError").getAsBoolean();
            }

            if (configObject.has("showToOnlyPrivileged")) {
                showToOnlyPrivileged = configObject.get("showToOnlyPrivileged").getAsBoolean();
            }
        }
    }

    public static void sendMessage(Player entity){
        if (!LMFTCommon.areTagsCooked || LMFTCommon.disableIngameError) return;

        if (LMFTCommon.showToOnlyPrivileged && entity.getPermissionLevel() <= 0) return;

        entity.displayClientMessage(
                Component.empty()
                        .append(Component.literal("[Load My F***ing Tags]: It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                        .append(Component.literal("Click me for more info about this feature.")
                                .withStyle(ChatFormatting.AQUA)
                                .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Dragon-Seeker/LoadMyFingTags/blob/3961e898550c4d996199bea0fa408a61e87e8dba/info.md")))),
                false
        );
    }
}

package io.wispforest.lmft;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class LMFTCommon {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "lmft";

    public static boolean areTagsCooked = false;

    public static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");

    public static void init(Path configPath) {
        File configFile = new File(configPath + File.separator + "lmft.json");

        JsonObject configObject = null;

        //Based on builtinServer multi loader implementation
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("[LMFT]: Unable to find needed config file, will attempt to create such.");

                configObject = new JsonObject();
                configObject.addProperty("disableIngameError", false);

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

        if(configObject != null && configObject.has("disableIngameError")) disableIngameError = disableIngameError || configObject.get("disableIngameError").getAsBoolean();
    }

    public static void sendMessage(PlayerEntity entity){
        if(!LMFTCommon.areTagsCooked || LMFTCommon.disableIngameError) return;

        entity.sendMessage(
                Text.literal("[Load My F***ing Tags]: It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. Click me for more info about this feature.")
                        .styled(style -> {
                            return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Dragon-Seeker/LoadMyFingTags/blob/3961e898550c4d996199bea0fa408a61e87e8dba/info.md"));
                        })
                        .formatted(Formatting.RED, Formatting.BOLD),
                false
        );
    }
}

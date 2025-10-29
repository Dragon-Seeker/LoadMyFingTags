package io.wispforest.lmft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import io.wispforest.lmft.utils.ClickEventUtils;
import io.wispforest.lmft.utils.ComponentUtils;
import io.wispforest.lmft.utils.PathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.slf4j.Log4jLogger;
import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.apache.logging.slf4j.SLF4JServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.wispforest.lmft.utils.ComponentUtils.*;

public class LMFTCommon {

    private static boolean areTagsCooked = false;

    public static final String MODID = "lmft";
    public static final String PREFIX = "[Load My F***ing Tags]: ";

    public static Logger LOGGER = LoggerFactory.getLogger(LMFTCommon.class);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");
    private static boolean showToOnlyPrivileged = false;

    private static boolean hasBeenLoaded = false;

    static {
        init();
    }

    public static void init() {
        if (hasBeenLoaded) return;

        hasBeenLoaded = true;

        var path = PathUtils.getConfigPath();

        JsonObject configObject = null;

        if (path != null) {
            var configFile = new File(path + File.separator + "lmft.json");

            //Based on builtinServer multi loader implementation
            try {
                if (configFile.createNewFile()) {
                    LOGGER.info(PREFIX + "Unable to find needed config file, will attempt to create such.");

                    configObject = new JsonObject();
                    configObject.addProperty("disableIngameError", false);
                    configObject.addProperty("showToOnlyPrivileged", false);

                    try (var writer = new FileWriter(configFile)) { writer.write(GSON.toJson(configObject)); }
                } else {
                    configObject = GSON.fromJson(Files.readString(configFile.toPath()), JsonObject.class);

                    if(configObject == null) throw new IOException("The config file for 'lmft' was not found!");

                    LOGGER.info(PREFIX + "Loaded Config File!");
                }
            } catch (IOException exception) {
                LOGGER.error(PREFIX + "Unable to create the needed config file, using default values!", exception);
            } catch (JsonSyntaxException exception) {
                LOGGER.error(PREFIX + "Unable to read the needed config file, using default values!", exception);
            }
        } else {
            LOGGER.error(PREFIX + "Unable to locate any path needed to load the config file, using default values!");
        }

        if(configObject == null) return;

        if (configObject.has("disableIngameError")) {
            disableIngameError = disableIngameError || configObject.get("disableIngameError").getAsBoolean();
        }

        if (configObject.has("showToOnlyPrivileged")) {
            showToOnlyPrivileged = configObject.get("showToOnlyPrivileged").getAsBoolean();
        }
    }

    public static void sendMessage(Player entity){
        if (!areTagsCooked) return;

        LOGGER.error(PREFIX + "It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. ");

        if (disableIngameError || (showToOnlyPrivileged && entity.hasPermissions(1))) return;

        if (canCreateMessages()) {
            var message = createMutableComponent("");

            append(
                message,
                withStyle(createMutableComponent(PREFIX + "It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. "), ChatFormatting.RED, ChatFormatting.BOLD)
            );

            append(
                message,
                withStyle(
                    withStyle(createMutableComponent("Click me for more info about this feature."), ChatFormatting.AQUA),
                    style -> ClickEventUtils.addUrlToStyle(style, "https://github.com/Dragon-Seeker/LoadMyFingTags/blob/3961e898550c4d996199bea0fa408a61e87e8dba/info.md")
                )
            );

            entity.displayClientMessage(message, false);
        }
    }

    private static final ThreadLocal<String> CURRENT_LOADING_TAG_ENTRY = ThreadLocal.withInitial(() -> "lmft:unknown_tag_id");

    public static void handleAndLogInvalidEntries(List<?> invalidEntries) {
        if(invalidEntries.isEmpty()) return;

        var id = CURRENT_LOADING_TAG_ENTRY.get();

        if (id == null) id = "lmft:unknown_tag_id";

        LOGGER.warn(PREFIX + "Couldn't load certain entries within the tag {}: {}",
            id,
            invalidEntries.stream().map(Objects::toString).collect(Collectors.joining(", "))
        );

        invalidEntries.clear();

        areTagsCooked = true;
    }

    public static void liftCookedTagState() {
        areTagsCooked = false;
    }

    public static void setTagId(ResourceLocation id) {
        CURRENT_LOADING_TAG_ENTRY.set(id.toString());
    }
}

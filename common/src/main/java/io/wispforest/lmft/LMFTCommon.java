package io.wispforest.lmft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LMFTCommon {

    private static boolean areTagsCooked = false;

    public static final String MODID = "lmft";
    public static final String PREFIX = "[Load My F***ing Tags]:";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");
    private static boolean showToOnlyPrivileged = false;

    private static boolean hasBeenLoaded = false;

    private static String cookedTagsMessage = "It seems that some tags are a bit 'cooked'. The logs will contain more info on what entries were not added to each tag.";
    private static String cookedTagsLinkMessage = "Click me for more info about this feature.";

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
        if (!areTagsCooked || disableIngameError || (showToOnlyPrivileged && entity.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))) return;

        entity.displayClientMessage(
            Component.empty()
                .append(
                    Component.literal(PREFIX)
                        .append(Component.translatableWithFallback("lmft.cooked_tags_message", cookedTagsMessage))
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
                ).append(
                    Component.translatableWithFallback("lmft.cooked_tags_link_message", cookedTagsLinkMessage)
                        .withStyle(ChatFormatting.AQUA)
                        .withStyle(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/Dragon-Seeker/LoadMyFingTags/blob/3961e898550c4d996199bea0fa408a61e87e8dba/info.md"))))
                ),
            false
        );
    }

    private static final ThreadLocal<Identifier> CURRENT_LOADING_TAG_ENTRY = ThreadLocal.withInitial(() -> Identifier.fromNamespaceAndPath("", ""));

    public static void handleAndLogInvalidEntries(List<?> invalidEntries) {
        if(invalidEntries.isEmpty()) return;

        var id = CURRENT_LOADING_TAG_ENTRY.get();

        if (id == null) id = Identifier.fromNamespaceAndPath("lmft", "unknown_tag_id");

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

    public static void setTagId(Identifier id) {
        CURRENT_LOADING_TAG_ENTRY.set(id);
    }
}

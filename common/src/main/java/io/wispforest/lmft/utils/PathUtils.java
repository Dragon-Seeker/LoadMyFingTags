package io.wispforest.lmft.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;

public class PathUtils {

    @Nullable
    public static Path getConfigPath() {
        var lookup = MethodHandles.publicLookup();

        var path = getFabricConfigPath(lookup);

        if (path == null) path = getNeoforgeConfigPath(lookup);
        if (path == null) path = getForgeConfigPath(lookup);

        return path;
    }

    @Nullable
    private static Path getFabricConfigPath(MethodHandles.Lookup lookup) {
        try {
            var fLoaderClass = MethodHandleUtils.findClass("net.fabricmc.loader.api.FabricLoader");

            if (fLoaderClass == null) return null;

            var instanceMethod = lookup.findStatic(fLoaderClass, "getInstance", MethodType.methodType(fLoaderClass));
            var configMethod = lookup.findVirtual(fLoaderClass, "getConfigDir", MethodType.methodType(Path.class));

            return MethodHandleUtils.wrapForErrors("getFabricConfigPath", () -> configMethod.invoke(instanceMethod.invoke()));
        } catch (IllegalAccessException | NoSuchMethodException ignored) {}

        return null;
    }

    @Nullable
    private static Path getNeoforgeConfigPath(MethodHandles.Lookup lookup) {
        try {
            var fmlPathsClass = MethodHandleUtils.findClass("net.neoforged.fml.loading.FMLPaths");

            if (fmlPathsClass == null) return null;

            var instanceMethod = lookup.findStaticVarHandle(fmlPathsClass, "CONFIGDIR", fmlPathsClass);
            var configMethod = lookup.findVirtual(fmlPathsClass, "get", MethodType.methodType(Path.class));

            return MethodHandleUtils.wrapForErrors("getNeoforgeConfigPath", () -> configMethod.invoke(instanceMethod.get()));
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException ignored) {}

        return null;
    }

    @Nullable
    private static Path getForgeConfigPath(MethodHandles.Lookup lookup) {
        try {
            var fmlPathsClass = MethodHandleUtils.findClass("net.minecraftforge.fml.loading.FMLPaths");

            if (fmlPathsClass == null) return null;

            var instanceMethod = lookup.findStaticVarHandle(fmlPathsClass, "CONFIGDIR", fmlPathsClass);
            var configMethod = lookup.findVirtual(fmlPathsClass, "get", MethodType.methodType(Path.class));

            return MethodHandleUtils.wrapForErrors("getForgeConfigPath", () -> configMethod.invoke(instanceMethod.get()));
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException ignored) {}

        return null;
    }
}

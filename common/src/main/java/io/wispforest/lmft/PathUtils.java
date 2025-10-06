package io.wispforest.lmft;

import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;

public class PathUtils {

    @Nullable
    public static Path getConfigPath() {
        var lookup = MethodHandles.publicLookup();

        var path = getFabricConfigPath(lookup);

        if (path == null) path = getNeoforgePath(lookup);
        if (path == null) path = getForgePath(lookup);

        return path;
    }

    private static Class<?> findClass(String targetName) throws ClassNotFoundException {
        return Class.forName(targetName, false, PathUtils.class.getClassLoader());
    }


    @Nullable
    private static Path getFabricConfigPath(MethodHandles.Lookup lookup) {
        try {
            var fLoaderClass = findClass("net.fabricmc.loader.api.FabricLoader");

            var instanceMethod = lookup.findStatic(fLoaderClass, "getInstance", MethodType.methodType(fLoaderClass));
            var configMethod = lookup.findVirtual(fLoaderClass, "getConfigDir", MethodType.methodType(Path.class));

            try {
                var fLoader = instanceMethod.invoke();

                return (Path) configMethod.invoke(fLoader);
            } catch (Throwable e) {
                LMFTCommon.LOGGER.error("Unable to get config path on Fabric as an error occured: ", e);
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException ignored) {}

        return null;
    }

    @Nullable
    private static Path getNeoforgePath(MethodHandles.Lookup lookup) {
        try {
            var fmlPathsClass = findClass("net.neoforged.fml.loading.FMLPaths");

            var instanceMethod = lookup.findStaticVarHandle(fmlPathsClass, "CONFIGDIR", fmlPathsClass);
            var configMethod = lookup.findVirtual(fmlPathsClass, "get", MethodType.methodType(Path.class));

            try {
                var CONFIGDIR = instanceMethod.get();

                return (Path) configMethod.invoke(CONFIGDIR);
            } catch (Throwable e) {
                LMFTCommon.LOGGER.error("Unable to get config path on Neoforge as an error occured: ", e);
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException ignored) {}

        return null;
    }

    @Nullable
    private static Path getForgePath(MethodHandles.Lookup lookup) {
        try {
            var fmlPathsClass = findClass("net.minecraftforge.fml.loading");

            var instanceMethod = lookup.findStaticVarHandle(fmlPathsClass, "CONFIGDIR", fmlPathsClass);
            var configMethod = lookup.findVirtual(fmlPathsClass, "get", MethodType.methodType(Path.class));

            try {
                var CONFIGDIR = instanceMethod.get();

                return (Path) configMethod.invoke(CONFIGDIR);
            } catch (Throwable e) {
                LMFTCommon.LOGGER.error("Unable to get config path on Forge as an error occured: ", e);
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException ignored) {}

        return null;
    }
}

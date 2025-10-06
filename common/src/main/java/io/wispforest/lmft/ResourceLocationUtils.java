package io.wispforest.lmft;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiFunction;

public class ResourceLocationUtils {
    private static final BiFunction<String, String, ResourceLocation> resourceLocationFactory;

    static {
        var resourceLocationConstructor = getResourceLocationConstructor();

        resourceLocationFactory = (namespace, path) -> {
            try {
                return (ResourceLocation) resourceLocationConstructor.invokeExact(namespace, path);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        return resourceLocationFactory.apply(namespace, path);
    }

    private static MethodHandle getResourceLocationConstructor() {
        var lookup = MethodHandles.publicLookup();

        var mt = MethodType.methodType(ResourceLocation.class, String.class, String.class);

        var method = findStatic(lookup, ResourceLocation.class, mt, "fromNamespaceAndPath", "of", "method_60655", "m_egmlfvuu");

        if (method == null) {
            mt = MethodType.methodType(void.class, String.class, String.class);

            try {
                method = lookup.findConstructor(ResourceLocation.class, mt);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to locate ResourceLocation method needed to construct them in LMFT due to an error: ", e);
            } catch (NoSuchMethodException ignored) {}
        }

        if (method == null) throw new IllegalArgumentException("Unable to locate ResourceLocation method needed to construct them in LMFT");

        return method;
    }

    @Nullable
    private static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> clazz, MethodType mt, String ...methodNames) {
        for (var methodName : methodNames) {
            try {
                return lookup.findStatic(clazz, methodName, mt);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to locate ResourceLocation method needed to construct them in LMFT due to an error: ", e);
            } catch (NoSuchMethodException ignored) {}
        }

        return null;
    }
}

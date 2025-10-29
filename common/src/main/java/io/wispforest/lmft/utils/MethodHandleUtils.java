package io.wispforest.lmft.utils;

import io.wispforest.lmft.LMFTCommon;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

public class MethodHandleUtils {

    public static MethodHandle findMethod(MethodHandles.Lookup lookup, Class<?> clazz, MethodType mt, String ...methodNames) {
        return findHandle(lookup, MethodHandles.Lookup::findVirtual, clazz, mt, methodNames);
    }

    @Nullable
    public static MethodHandle findStaticMethod(MethodHandles.Lookup lookup, Class<?> clazz, MethodType mt, String ...methodNames) {
        return findHandle(lookup, MethodHandles.Lookup::findStatic, clazz, mt, methodNames);
    }

    public static MethodHandle findStaticConstructor(MethodHandles.Lookup lookup, Class<?> clazz, List<Class<?>> parameterTypes, String ...methodNames) {
        return findStaticMethod(lookup, clazz, MethodType.methodType(clazz, parameterTypes), methodNames);
    }

    public static MethodHandle findConstructor(MethodHandles.Lookup lookup, Class<?> clazz, Class<?>... parameterTypes) {
        return findHandle(lookup, MethodHandles.Lookup::findConstructor, clazz, MethodType.methodType(void.class, parameterTypes));
    }

    public static Class<?> findClass(String ...targetNames)  {
        for (var targetName : targetNames) {
            try {
                return Class.forName(targetName, false, PathUtils.class.getClassLoader());
            } catch (ClassNotFoundException ignored) {}
        }

        return null;
    }

    public static MethodHandle findHandle(MethodHandles.Lookup lookup, NamedHandleLookup handleLookup, Class<?> clazz, MethodType mt, String ...methodNames) {
        for (var methodName : methodNames) {
            var handle = findHandle(lookup, handleLookup.generic(methodName), clazz, mt);

            if (handle != null) return handle;
        }

        return null;
    }

    public static MethodHandle findHandle(MethodHandles.Lookup lookup, HandleLookup handleLookup, Class<?> clazz, MethodType mt) {
        try {
            return handleLookup.findHandle(lookup, clazz, mt);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(LMFTCommon.PREFIX + "Unable to locate the a method handle for " + clazz.getName() + "  due to an error: ", e);
        } catch (ReflectiveOperationException ignored) {}

        return null;
    }

    public interface NamedHandleLookup {
        MethodHandle findHandle(MethodHandles.Lookup lookup, Class<?> clazz, String methodName, MethodType mt) throws ReflectiveOperationException;

        default HandleLookup generic(String methodName) {
            return (lookup, clazz, mt) -> NamedHandleLookup.this.findHandle(lookup, clazz, methodName, mt);
        }
    }

    public interface HandleLookup {
        MethodHandle findHandle(MethodHandles.Lookup lookup, Class<?> clazz, MethodType mt) throws ReflectiveOperationException;
    }

    public static <T> @Nullable T wrapForErrors(String methodName, MethodHandleExecution execution) {
        try {
            return (T) execution.get();
        } catch (Throwable e) {
            LMFTCommon.LOGGER.error(LMFTCommon.PREFIX + "Unable to invoke '{}' as an error occurred: ", methodName, e);
        }

        return null;
    }

    public interface MethodHandleExecution {
        Object get() throws Throwable;
    }
}

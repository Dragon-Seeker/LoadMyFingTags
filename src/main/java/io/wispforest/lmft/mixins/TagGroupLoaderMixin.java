package io.wispforest.lmft.mixins;

import io.wispforest.lmft.ducks.TagGroupLoaderAccessorDuck;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin<T> implements TagGroupLoaderAccessorDuck {

    @Unique
    private static final ThreadLocal<Identifier> currentTagId = ThreadLocal.withInitial(() -> new Identifier("",""));

    @Inject(method = "method_32841", at = @At("HEAD"))
    private static void saveTagId(Function function, Function function2, Map map, Identifier identifier, Tag.Builder builder, CallbackInfo ci){
        currentTagId.set(identifier);
    }

    @Override
    public Identifier getCurrentId() {
        return currentTagId.get();
    }
}

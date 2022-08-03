package io.wispforest.lmft.mixins;

import com.google.common.collect.Multimap;
import io.wispforest.lmft.ducks.TagGroupLoaderAccessorDuck;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin<T> implements TagGroupLoaderAccessorDuck {

    @Unique
    private static Identifier currentTagId;

    @Inject(method = "method_32838", at = @At("HEAD"))
    private static void saveTagId(Map map, Multimap multimap, Set set, Function function, Function function2, Map map2, Identifier identifier, CallbackInfo ci){
        currentTagId = identifier;
    }

    @Override
    public Identifier getCurrentId() {
        return currentTagId;
    }
}

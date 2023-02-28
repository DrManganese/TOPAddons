package io.github.drmanganese.topaddons.client;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FluidColors {

    private static final Map<TextureAtlasSprite, Integer> AVERAGE_COLORS = hashMapWithDefault(FluidColorExtraction::extractAvgColorFromTexture);
    private static final Map<TextureAtlasSprite, Integer> TOP_LEFT_COLORS = hashMapWithDefault(FluidColorExtraction::extractTopLeftColorFromTexture);
    private static final Map<String, Integer> OVERRIDE_COLORS = new HashMap<>();

    public static void resetMaps() {
        AVERAGE_COLORS.clear();
        TOP_LEFT_COLORS.clear();
    }

    @SuppressWarnings("ConstantConditions")
    public static int getForFluid(Fluid fluid, ForgeAddon.FluidColorAlgorithm algorithm) {
        final TextureAtlasSprite texture = FluidColorExtraction.getStillFluidTextureSafe(fluid);
        if (texture == null) return -16776995;

        return switch (algorithm) {
            case AVERAGE_COLOR -> AVERAGE_COLORS.get(texture);
            case TOP_LEFT_COLOR -> TOP_LEFT_COLORS.get(texture);
            default -> throw new IllegalArgumentException("Illegal Fluid Color Algorithm");
        };
    }

    public static Optional<Integer> getForgeColor(FluidStack fluidStack) {
        return Optional.of(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack)).filter(i -> i != -1);
    }

    public static Optional<Integer> getOverrideColor(Fluid fluid) {
        return Optional.ofNullable(OVERRIDE_COLORS.get(ForgeRegistries.FLUIDS.getKey(fluid).toString()));
    }

    private static Map<TextureAtlasSprite, Integer> hashMapWithDefault(Function<TextureAtlasSprite, Integer> defaultFunction) {
        return new HashMap<>() {
            @Override
            public Integer get(Object key) {
                if (!containsKey(key))
                    put((TextureAtlasSprite) key, defaultFunction.apply((TextureAtlasSprite) key));
                return super.get(key);
            }
        };
    }
}

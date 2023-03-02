package io.github.drmanganese.topaddons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class FluidColorExtraction {

    private static final Fluid DEFAULT = Fluids.WATER;

    static int extractTopLeftColorFromTexture(TextureAtlasSprite sprite) {
        final int abgr = sprite.getPixelRGBA(0, 0, 0);
        return (0xff << 24) | (red(abgr) << 16) | (green(abgr) << 8) | blue(abgr);
    }

    static int extractAvgColorFromTexture(TextureAtlasSprite sprite) {
        final int width = sprite.getWidth();
        final int n = width * width;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                final int abgr = sprite.getPixelRGBA(0, x, y);
                r += Math.pow(red(abgr), 2);
                g += Math.pow(green(abgr), 2);
                b += Math.pow(blue(abgr), 2);
            }
        }
        return (0xff << 24) | (sqrt(r / n) << 16) | (sqrt(g / n) << 8) | sqrt(b / n);
    }

    private static TextureAtlasSprite getStillFluidTexture(Fluid fluid) {
        return Minecraft.getInstance()
            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(IClientFluidTypeExtensions.of(fluid).getStillTexture());
    }

    @SuppressWarnings("ConstantConditions")
    // In the rare case that a fluid returns a null sprite (should be impossible) we return water's texture
    public static TextureAtlasSprite getStillFluidTextureSafe(Fluid fluid) {
        final TextureAtlasSprite stillFluidTexture = getStillFluidTexture(fluid);
        return stillFluidTexture == null ? getStillFluidTexture(DEFAULT) : stillFluidTexture;
    }

    private static int red(int abgr) {
        return abgr & 0xff;
    }

    private static int green(int abgr) {
        return abgr >> 8 & 0xff;
    }

    private static int blue(int abgr) {
        return abgr >> 16 & 0xff;
    }

    private static int sqrt(int i) {
        return (int) Math.sqrt(i);
    }
}

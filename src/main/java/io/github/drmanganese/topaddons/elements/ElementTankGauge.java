package io.github.drmanganese.topaddons.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import io.github.drmanganese.topaddons.addons.AddonForestry;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;

public class ElementTankGauge implements IElement {

    public ElementTankGauge(ByteBuf buf) {
    }

    @Override
    public void render(int x, int y) {
        GlStateManager.disableBlend();
        FluidStack stack = new FluidStack(FluidRegistry.WATER, 1255);
        Fluid fluid = stack.getFluid();
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        ResourceLocation fluidStill = fluid.getStill();
        TextureAtlasSprite fluidStillSprite = null;
        if (fluidStill != null) {
            fluidStillSprite = textureMap.getTextureExtry(fluidStill.toString());
        }
        if (fluidStillSprite == null) {
            fluidStillSprite = textureMap.getMissingSprite();
        }

        int fluidColor = fluid.getColor(stack);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setGLColorFromInt(fluidColor);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        //fluidstack
    }

    @Override
    public int getID() {
        return AddonForestry.ELEMENT_TANK;
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GlStateManager.color(red, green, blue, 1.0F);
    }
}

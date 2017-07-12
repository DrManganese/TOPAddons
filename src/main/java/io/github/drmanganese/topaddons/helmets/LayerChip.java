package io.github.drmanganese.topaddons.helmets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.reference.EnumChip;

import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

@SideOnly(Side.CLIENT)
public class LayerChip implements LayerRenderer<EntityPlayer> {


    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (!helmet.isEmpty()) {
            if (helmet.getItem() instanceof ItemArmor && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor) helmet.getItem()).getClass()) && helmet.hasTagCompound() && helmet.getTagCompound().hasKey(PROBETAG)) {
                EnumChip chip = AddonManager.SPECIAL_HELMETS.get(((ItemArmor) helmet.getItem()).getClass());
                if (chip != null) {
                    TextureAtlasSprite sprite = chip.getSprite();

                    GlStateManager.pushMatrix();
                    GlStateManager.rotate(player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks, 0, -1, 0);
                    GlStateManager.rotate(player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks - 270, 0, 1, 0);
                    GlStateManager.rotate(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks, 0, 0, 1);

                    GlStateManager.translate(0, -player.getDefaultEyeHeight(), 0);
                    if (player.isSneaking())
                        GlStateManager.translate(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.rotate(90F, 0F, 1F, 0F);
                    GlStateManager.rotate(180F, 1F, 0F, 0F);
                    chip.translateAndScale();
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    GlStateManager.enableBlend();
                    renderSpriteIn3D(Tessellator.getInstance(), sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV(), sprite.getIconWidth(), sprite.getIconHeight(), chip.getThickness());
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private void renderSpriteIn3D(Tessellator tesselator, float minU, float minV, float maxU, float maxV, int width, int height, float thickness) {
        BufferBuilder buffer = tesselator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(0.0D, 0.0D, 0.0D).tex(maxU, maxV).normal(0, 0, 1).endVertex();
        buffer.pos(1.0D, 0.0D, 0.0D).tex(minU, maxV).normal(0, 0, 1).endVertex();
        buffer.pos(1.0D, 1.0D, 0.0D).tex(minU, minV).normal(0, 0, 1).endVertex();
        buffer.pos(0.0D, 1.0D, 0.0D).tex(maxU, minV).normal(0, 0, 1).endVertex();
        tesselator.draw();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(0.0D, 1.0D, (0.0F - thickness)).tex(maxU, minV).normal(0, 0, -1).endVertex();
        buffer.pos(1.0D, 1.0D, (0.0F - thickness)).tex(minU, minV).normal(0, 0, -1).endVertex();
        buffer.pos(1.0D, 0.0D, (0.0F - thickness)).tex(minU, maxV).normal(0, 0, -1).endVertex();
        buffer.pos(0.0D, 0.0D, (0.0F - thickness)).tex(maxU, maxV).normal(0, 0, -1).endVertex();
        tesselator.draw();
        float halfXPixel = 0.5F * (maxU - minU) / (float)width;
        float halfYPixel = 0.5F * (maxV - minV) / (float)height;
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        int k;
        float x;
        float xOff;

        for (k = 0; k < width; ++k)
        {
            x = (float)k / (float)width;
            xOff = maxU + (minU - maxU) * x - halfXPixel;
            buffer.pos(x, 0.0D, (0.0F - thickness)).tex(xOff, maxV).normal(-1, 0, 0).endVertex();
            buffer.pos(x, 0.0D, 0.0D).tex(xOff, maxV).normal(-1, 0, 0).endVertex();
            buffer.pos(x, 1.0D, 0.0D).tex(xOff, minV).normal(-1, 0, 0).endVertex();
            buffer.pos(x, 1.0D, (0.0F - thickness)).tex(xOff, minV).normal(-1, 0, 0).endVertex();
        }

        tesselator.draw();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        float f9;

        for (k = 0; k < width; ++k)
        {
            x = (float)k / (float)width;
            xOff = maxU + (minU - maxU) * x - halfXPixel;
            f9 = x + 1.0F / (float)width;
            buffer.pos(f9, 1.0D, (0.0F - thickness)).tex(xOff, minV).normal(1, 0, 0).endVertex();
            buffer.pos(f9, 1.0D, 0.0D).tex(xOff, minV).normal(1, 0, 0).endVertex();
            buffer.pos(f9, 0.0D, 0.0D).tex(xOff, maxV).normal(1, 0, 0).endVertex();
            buffer.pos(f9, 0.0D, (0.0F - thickness)).tex(xOff, maxV).normal(1, 0, 0).endVertex();
        }

        tesselator.draw();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (k = 0; k < height; ++k)
        {
            x = (float)k / (float)height;
            xOff = maxV + (minV - maxV) * x - halfYPixel;
            f9 = x + 1.0F / (float)height;
            buffer.pos(0.0D, f9, 0.0D).tex(maxU, xOff).normal(0, 1, 0).endVertex();
            buffer.pos(1.0D, f9, 0.0D).tex(minU, xOff).normal(0, 1, 0).endVertex();
            buffer.pos(1.0D, f9, (0.0F - thickness)).tex(minU, xOff).normal(0, 1, 0).endVertex();
            buffer.pos(0.0D, f9, (0.0F - thickness)).tex(maxU, xOff).normal(0, 1, 0).endVertex();
        }

        tesselator.draw();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (k = 0; k < height; ++k)
        {
            x = (float)k / (float)height;
            xOff = maxV + (minV - maxV) * x - halfYPixel;
            buffer.pos(1.0D, x, 0.0D).tex(minU, xOff).normal(0, -1, 0).endVertex();
            buffer.pos(0.0D, x, 0.0D).tex(maxU, xOff).normal(0, -1, 0).endVertex();
            buffer.pos(0.0D, x, (0.0F - thickness)).tex(maxU, xOff).normal(0, -1, 0).endVertex();
            buffer.pos(1.0D, x, (0.0F - thickness)).tex(minU, xOff).normal(0, -1, 0).endVertex();
        }

        tesselator.draw();
    }
}

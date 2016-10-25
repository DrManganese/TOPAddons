package io.github.drmanganese.topaddons.reference;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumChip {
    STANDARD("chip", -0.625F, -1.84F, -0.3F, 1.25F, 1F/32F),
    SPECTACLES("spectacles", -.1935F, -1.719F, -.315F, .62F, 1F/64F),
    EYE_RIGHT("eye_right", -.3F, -1.795F, -.251F, .85F, 1F/32F),
    IC2("ic2", -0.625F, -1.765F, -0.3F, 1.25F, 1F/32F);

    private final String texture;
    private final float xTranslate, yTranslate, zTranslate;
    private final float scale, thickness;

    @SideOnly(Side.CLIENT)
    TextureAtlasSprite sprite;


    EnumChip(String texture, float xTranslate, float yTranslate, float zTranslate, float scale, float thickness) {
        this.texture = texture;
        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
        this.zTranslate = zTranslate;
        this.scale = scale;
        this.thickness = thickness;
    }

    public String getTexture() {
        return texture;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getSprite() {
        return sprite;
    }

    @SideOnly(Side.CLIENT)
    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public float getThickness() {
        return thickness;
    }

    @SideOnly(Side.CLIENT)
    public void translateAndScale() {
        GlStateManager.translate(xTranslate, yTranslate, zTranslate);
        GlStateManager.scale(scale, scale, scale);
    }
}

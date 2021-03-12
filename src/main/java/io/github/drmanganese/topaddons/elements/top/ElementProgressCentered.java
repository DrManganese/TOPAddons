package io.github.drmanganese.topaddons.elements.top;

import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A progress bar with its text centered.
 */
public class ElementProgressCentered implements IElement {

    private final long current, max;
    private final IProgressStyle style;
    private final Optional<TranslationTextComponent> override;
    private int id;

    public ElementProgressCentered(int id, long current, long max, IProgressStyle style, @Nullable String override) {
        this.id = id;
        this.current = current;
        this.max = max;
        this.style = style;
        this.override = Optional.ofNullable(override).map(TranslationTextComponent::new);
    }

    public ElementProgressCentered(PacketBuffer buf) {
        this.current = buf.readLong();
        this.max = buf.readLong();
        this.style = ElementHelper.readProgressStyleFromBuffer(buf);
        final boolean hasOverride = buf.readBoolean();
        this.override = Optional.ofNullable(hasOverride ? buf.readString() : null).map(TranslationTextComponent::new);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        ElementHelper.drawBox(matrixStack, x, y, style.getWidth(), style.getHeight(), style.getBackgroundColor(), 1, style.getBorderColor());
        if (current > 0 && max > 0) {
            final int dx = (int) Math.min((current * (style.getWidth() - 2) / max), style.getWidth() - 2);

            if (style.getAlternatefilledColor() == style.getFilledColor()) {
                ElementHelper.drawBox(matrixStack, x + 1, y + 1, dx, style.getHeight() - 2, style.getFilledColor(), 0, style.getFilledColor());
            } else {
                for (int i = 0; i < dx; i++) {
                    ElementHelper.drawVerticalLine(matrixStack, x + i + 1, y + 1, style.getHeight() - 2, i % 2 == 0 ? style.getFilledColor() : style.getAlternatefilledColor());
                }
            }
        }

        if (style.isShowText()) {
            final String text = override.map(TranslationTextComponent::getString).orElse(style.getPrefix() + current + style.getSuffix());
            final int textWidth = Minecraft.getInstance().fontRenderer.getStringWidth(text);
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(matrixStack, text, x + (style.getWidth() - textWidth) / 2.0F, y + 2, 0xffffffff);
        }
    }

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeLong(this.current);
        buf.writeLong(this.max);
        ElementHelper.writeProgressStyleToBuffer(style, buf);
        buf.writeBoolean(override.isPresent());
        override.map(TranslationTextComponent::getKey).ifPresent(buf::writeString);
    }
}

package io.github.drmanganese.topaddons.util;

import io.github.drmanganese.topaddons.capabilities.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;
import io.github.drmanganese.topaddons.elements.top.ElementProgressCentered;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public final class InfoHelper {

    /**
     * "<tt>prefix</tt>: <tt>info</tt>"
     */
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info) {
        return probeInfo.text(CompoundText.createLabelInfo(prefix + ":", " " + info));
    }

    public static IProbeInfo itemStackBackground(IProbeInfo probeInfo, PlayerEntity player, ItemStack itemStack, int color) {
        return probeInfo.element(new ElementItemStackBackground(ElementSync.getId("itemstack_background", player), itemStack, color, new ItemStyle()));
    }

    public static IProbeInfo progressCentered(IProbeInfo probeInfo, PlayerEntity player, int current, int max, IProgressStyle progressStyle, @Nullable String override) {
        return probeInfo.element(new ElementProgressCentered(ElementSync.getId("centered_progress", player), current, max, progressStyle, override));
    }

    public static IProbeInfo progressCenteredScaled(IProbeInfo probeInfo, PlayerEntity player, int current, int max, int scale, IProgressStyle progressStyle, @Nullable String override) {
        return progressCentered(probeInfo, player, scale * current / max, scale, progressStyle, override);
    }
}

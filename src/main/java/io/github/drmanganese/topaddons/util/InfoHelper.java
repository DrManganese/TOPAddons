package io.github.drmanganese.topaddons.util;

import io.github.drmanganese.topaddons.capabilities.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;

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
}

package io.github.drmanganese.topaddons.util;

import io.github.drmanganese.topaddons.addons.top.elements.ElementItemStackBackground;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

    public static IProbeInfo itemStackBackground(IProbeInfo probeInfo, Player player, ItemStack itemStack, int color) {
        return probeInfo.element(new ElementItemStackBackground(itemStack, color, new ItemStyle()));
    }
}

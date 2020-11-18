package io.github.drmanganese.topaddons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;

public final class PlayerHelper {

    @ObjectHolder("theoneprobe:probe")
    private static Item PROBE;

    public static ProbeMode getProbeMode(PlayerEntity player) {
        if (Config.extendedInMain.get() && player.getHeldItemMainhand().getItem() == PROBE) {
            return ProbeMode.EXTENDED;
        } else {
            return player.isSneaking() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
        }
    }
}

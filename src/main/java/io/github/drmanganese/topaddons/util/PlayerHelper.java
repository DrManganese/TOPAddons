package io.github.drmanganese.topaddons.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;

import java.util.Arrays;

public final class PlayerHelper {

    @ObjectHolder("theoneprobe:probe")
    private static Item PROBE;

    public static ProbeMode getProbeMode(Player player) {
        if (Config.extendedInMain.get() && player.getMainHandItem().getItem() == PROBE) {
            return ProbeMode.EXTENDED;
        } else {
            return player.isCrouching() ? ProbeMode.EXTENDED : ProbeMode.NORMAL;
        }
    }

    public static boolean isPlayerHolding(Player player, Item item) {
        return Arrays.stream(InteractionHand.values()).map(player::getItemInHand).map(ItemStack::getItem).anyMatch(item::equals);
    }
}

package io.github.drmanganese.topaddons.util;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.capabilities.IClientCfgCapability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;

public final class PlayerHelper {

    public static boolean isHolding(EntityPlayer player, Item item) {
        for (EnumHand hand : EnumHand.values()) {
            if (player.getHeldItem(hand).getItem() == item) {
                return true;
            }
        }

        return false;
    }

    public static IClientCfgCapability getSync(EntityPlayer player) {
        return player.getCapability(TOPAddons.CLIENT_CFG_CAP, null);
    }
}

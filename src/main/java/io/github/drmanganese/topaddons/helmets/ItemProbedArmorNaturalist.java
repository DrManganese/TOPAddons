package io.github.drmanganese.topaddons.helmets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import io.github.drmanganese.topaddons.api.ItemArmorProbed;

import forestry.api.core.IArmorNaturalist;
import forestry.core.PluginCore;

public class ItemProbedArmorNaturalist extends ItemArmorProbed implements IArmorNaturalist {

    public ItemProbedArmorNaturalist() {
        super(ArmorMaterial.LEATHER, "naturalist_helmet_probe", PluginCore.items.spectacles);
    }

    @Override
    public boolean canSeePollination(EntityPlayer player, ItemStack armor, boolean doSee) {
        return armorType == EntityEquipmentSlot.HEAD;
    }
}

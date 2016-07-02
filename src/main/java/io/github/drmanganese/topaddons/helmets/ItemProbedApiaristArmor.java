package io.github.drmanganese.topaddons.helmets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import io.github.drmanganese.topaddons.api.ItemArmorProbed;

import forestry.api.apiculture.IArmorApiarist;
import forestry.api.core.IArmorNaturalist;
import forestry.apiculture.PluginApiculture;

public class ItemProbedApiaristArmor extends ItemArmorProbed implements IArmorNaturalist, IArmorApiarist{

    public ItemProbedApiaristArmor() {
        super(ArmorMaterial.LEATHER, "apiarist_helmet_probe", PluginApiculture.items.apiaristHat);
    }

    @Override
    public boolean protectEntity(EntityLivingBase entity, ItemStack armor, String cause, boolean doProtect) {
        return true;
    }

    @Override
    public boolean canSeePollination(EntityPlayer player, ItemStack armor, boolean doSee) {
        return armorType == EntityEquipmentSlot.HEAD;
    }
}

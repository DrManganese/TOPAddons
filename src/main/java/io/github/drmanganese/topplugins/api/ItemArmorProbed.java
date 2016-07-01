package io.github.drmanganese.topplugins.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import io.github.drmanganese.topplugins.reference.Reference;

import java.util.List;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class ItemArmorProbed extends ItemArmor {

    private final String name;
    public final ItemArmor parent;

    public ItemArmorProbed(ArmorMaterial materialIn, String name, ItemArmor parent) {
        super(materialIn, 0, EntityEquipmentSlot.HEAD);
        this.name = name;
        this.parent = parent;

        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        setUnlocalizedName(getRegistryName().toString());
    }

    @Nonnull
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MOD_ID + ":textures/armor/" + name + ".png";
    }

    @Override
    public void getSubItems(@Nonnull Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack stack = new ItemStack(itemIn);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(PROBETAG, 1);
        stack.setTagCompound(tag);
        subItems.add(stack);
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

}

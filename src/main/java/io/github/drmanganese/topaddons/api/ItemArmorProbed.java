package io.github.drmanganese.topaddons.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import io.github.drmanganese.topaddons.reference.Reference;

import java.util.List;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

/**
 * Superclass for helmets (or other armor too I guess) with probe support, i.e. see the TOP HUD
 * without holding the Probe.
 */
public class ItemArmorProbed extends ItemArmor {

    /**  Unlocalized name for textures and localization */
    private final String name;
    /** Parent class of the helmet, needed for the crafting recipe */
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
    /**
     * The NBT integer tag "theoneprobe" ({@link PROBETAG}) tells The One Probe that this helmet
     * allows the TOP HUD to be shown when worn
     */
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

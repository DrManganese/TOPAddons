package io.github.drmanganese.topaddons.helmets;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mcjty.theoneprobe.items.ModItems;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class ProbedHelmetCrafting implements IRecipe {

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean helmet = false;
        boolean probe = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && !helmet) {
                    if (Config.Helmets.allHelmetsProbable && !Config.Helmets.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !Config.Helmets.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor)stack.getItem()).getClass())) {
                        helmet = !stack.hasTagCompound() || !stack.getTagCompound().hasKey(PROBETAG);
                    }
                } else if (stack.getItem() == ModItems.probe && !probe) {
                    probe = true;
                } else {
                    return false;
                }
            }
        }

        return helmet && probe;
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack helmet = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor) {
                    helmet = stack.copy();
                }
            }
        }

        if (helmet != null) {
            if (helmet.hasTagCompound()) {
                helmet.getTagCompound().setInteger(PROBETAG, 1);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger(PROBETAG, 1);
                helmet.setTagCompound(tag);
            }

            return helmet;
        }

        return null;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}

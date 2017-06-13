package io.github.drmanganese.topaddons.helmets;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.config.HelmetConfig;

import javax.annotation.Nonnull;

import mcjty.theoneprobe.items.ModItems;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class UnprobedHelmetCrafting implements IRecipe {

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean helmet = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && !helmet) {
                    if (HelmetConfig.allHelmetsProbable && !HelmetConfig.neverCraftList.contains(stack.getItem().getRegistryName().toString()) && !HelmetConfig.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !HelmetConfig.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor) stack.getItem()).getClass())) {
                        helmet = stack.hasTagCompound() && stack.getTagCompound().hasKey(PROBETAG);
                    }
                } else {
                    return false;
                }
            }
        }

        return helmet;

    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack helmet = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
                    if (HelmetConfig.allHelmetsProbable && !HelmetConfig.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !HelmetConfig.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor) stack.getItem()).getClass())) {
                        helmet = stack.copy();
                    }
                }
            }
        }

        if (!helmet.isEmpty()) {
            helmet.getTagCompound().removeTag(PROBETAG);
            return helmet;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        ret.set(0, new ItemStack(ModItems.probe));
        return ret;
    }
}

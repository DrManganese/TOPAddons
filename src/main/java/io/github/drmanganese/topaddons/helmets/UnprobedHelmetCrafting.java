package io.github.drmanganese.topaddons.helmets;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mcjty.theoneprobe.items.ModItems;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class UnprobedHelmetCrafting implements IRecipe {

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean helmet = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && !helmet) {
                    if (Config.Helmets.allHelmetsProbable && !Config.Helmets.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !Config.Helmets.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor)stack.getItem()).getClass())) {
                        helmet = stack.hasTagCompound() && stack.getTagCompound().hasKey(PROBETAG);
                    }
                } else {
                    return false;
                }
            }
        }

        return helmet;

    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack helmet = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
                    if (Config.Helmets.allHelmetsProbable && !Config.Helmets.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !Config.Helmets.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor)stack.getItem()).getClass())) {
                        helmet = stack.copy();
                    }
                }
            }
        }

        if (helmet != null ) {
            helmet.getTagCompound().removeTag(PROBETAG);;
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
        ItemStack[] ret = new ItemStack[inv.getSizeInventory()];
        ret[0] = new ItemStack(ModItems.probe, 1);
        return ret;
    }
}

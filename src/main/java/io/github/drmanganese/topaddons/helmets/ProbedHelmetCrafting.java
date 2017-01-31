package io.github.drmanganese.topaddons.helmets;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.config.Config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import mcjty.theoneprobe.items.ModItems;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class ProbedHelmetCrafting implements IRecipe {

    public static List<String> neverCraftList = Arrays.asList("neotech:electricArmorHelmet",
            "minecraft:diamond_helmet", "minecraft:golden_helmet", "minecraft:iron_helmet",
            "theoneprobe:diamond_helmet_probe", "theoneprobe:gold_helmet_probe", "theoneprobe:iron_helmet_probe",
            "enderio:darkSteel_helmet");

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean helmet = false;
        boolean probe = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && !helmet) {
                    if (Config.Helmets.allHelmetsProbable && !ProbedHelmetCrafting.neverCraftList.contains(stack.getItem().getRegistryName().toString()) && !Config.Helmets.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !Config.Helmets.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor)stack.getItem()).getClass())) {
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

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack helmet = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
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
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}

package io.github.drmanganese.topaddons.helmets;

import io.github.drmanganese.topaddons.config.ModConfig;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class RecipeHelmetDeprobify extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @GameRegistry.ObjectHolder("theoneprobe:probe")
    private static Item PROBE = null;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, World worldIn) {
        boolean helmet = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (ModConfig.allHelmetsProbifiable
                        && itemStack.getItem() instanceof ItemArmor
                        && ((ItemArmor) itemStack.getItem()).armorType == EntityEquipmentSlot.HEAD
                        && !ModConfig.helmetBlacklist.contains(itemStack.getItem().getRegistryName().toString())
                        && !ModConfig.NEVER_PROBIFY.contains(itemStack.getItem().getRegistryName().toString())
                        && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(PROBETAG)) {
                    if (helmet) return false;
                    helmet = true;
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
            if (!stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey(PROBETAG)) {
                helmet = stack.copy();
                break;
            }
        }

        helmet.getTagCompound().removeTag(PROBETAG);
        if (helmet.getTagCompound().hasNoTags()) helmet.setTagCompound(null);
        return helmet;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height > 0;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        remainingItems.set(0, new ItemStack(PROBE));
        return remainingItems;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}

package io.github.drmanganese.topaddons.helmets;

import io.github.drmanganese.topaddons.config.ModConfig;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class RecipeHelmetProbify extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final ItemStack probe;

    public RecipeHelmetProbify(@Nonnull ItemStack probe) {
        this.probe = probe;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, World worldIn) {
        boolean probe = false;
        boolean helmet = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.isItemEqual(this.probe)) {
                    if (probe) return false;
                    probe = true;
                } else if (ModConfig.allHelmetsProbifiable
                        && itemStack.getItem() instanceof ItemArmor
                        && ((ItemArmor) itemStack.getItem()).armorType == EntityEquipmentSlot.HEAD
                        && !ModConfig.helmetBlacklist.contains(itemStack.getItem().getRegistryName().toString())
                        && !ModConfig.NEVER_PROBIFY.contains(itemStack.getItem().getRegistryName().toString())
                        && (!itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey(PROBETAG))) {
                    if (helmet) return false;
                    helmet = true;
                }
            }
        }

        return probe && helmet;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack helmet = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemArmor) {
                helmet = itemStack.copy();
            }
        }

        if (helmet.hasTagCompound()) {
            helmet.getTagCompound().setInteger(PROBETAG, 1);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(PROBETAG, 1);
            helmet.setTagCompound(tag);
        }

        return helmet;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Factory implements IRecipeFactory {

        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            final ItemStack probe = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "probe"), context);
            return new RecipeHelmetProbify(probe);
        }
    }
}

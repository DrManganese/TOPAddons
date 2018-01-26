package io.github.drmanganese.topaddons.helmets.recipes;

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

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.config.HelmetConfig;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class RecipeHelmetProbing extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final ItemStack probeItem;

    public RecipeHelmetProbing(ItemStack probeItem) {
        this.probeItem = probeItem;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, World worldIn) {
        boolean helmet = false;
        boolean probe = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && !helmet) {
                    if (HelmetConfig.allHelmetsProbable && !HelmetConfig.neverCraftList.contains(stack.getItem().getRegistryName().toString()) && !HelmetConfig.helmetBlacklistSet.contains(stack.getItem().getRegistryName()) || !HelmetConfig.allHelmetsProbable && AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor) stack.getItem()).getClass())) {
                        helmet = !stack.hasTagCompound() || !stack.getTagCompound().hasKey(PROBETAG);
                    }
                } else if (stack.isItemEqual(this.probeItem) && !probe) {
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

        if (!helmet.isEmpty()) {
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
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
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
            return new RecipeHelmetProbing(probe);
        }
    }
}

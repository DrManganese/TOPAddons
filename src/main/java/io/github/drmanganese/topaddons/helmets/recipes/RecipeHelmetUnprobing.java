package io.github.drmanganese.topaddons.helmets.recipes;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.config.HelmetConfig;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import static mcjty.theoneprobe.items.ModItems.PROBETAG;

public class RecipeHelmetUnprobing extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final ItemStack probeItem;

    public RecipeHelmetUnprobing(ItemStack probeItem) {
        this.probeItem = probeItem;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, World worldIn) {
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
    public boolean canFit(int width, int height) {
        return width * height >= 1;
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
        ret.set(0, this.probeItem.copy());
        return ret;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            final ItemStack probe = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "probe"), context);
            return new RecipeHelmetUnprobing(probe);
        }

    }
}

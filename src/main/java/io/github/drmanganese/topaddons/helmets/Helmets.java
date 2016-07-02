package io.github.drmanganese.topaddons.helmets;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.github.drmanganese.topaddons.addons.AddonManager;

import mcjty.theoneprobe.items.AddProbeRecipe;

public class Helmets {

    public static void registerHelmets() {
        AddonManager.helmets.forEach(GameRegistry::register);
    }

    public static void registerRecipes() {
        AddonManager.helmets.forEach(helmet -> GameRegistry.addRecipe(new AddProbeRecipe(helmet.parent, helmet)));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        AddonManager.helmets.forEach(helmet -> ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation(helmet.getRegistryName().toString(), "inventory")));
    }
}

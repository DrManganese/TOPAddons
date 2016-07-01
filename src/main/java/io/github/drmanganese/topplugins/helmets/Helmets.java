package io.github.drmanganese.topplugins.helmets;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.github.drmanganese.topplugins.plugins.PluginManager;

import mcjty.theoneprobe.items.AddProbeRecipe;

public class Helmets {

    public static void registerHelmets() {
        PluginManager.helmets.forEach(GameRegistry::register);
    }

    public static void registerRecipes() {
        PluginManager.helmets.forEach(helmet -> GameRegistry.addRecipe(new AddProbeRecipe(helmet.parent, helmet)));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        PluginManager.helmets.forEach(helmet -> ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation(helmet.getRegistryName().toString(), "inventory")));
    }
}

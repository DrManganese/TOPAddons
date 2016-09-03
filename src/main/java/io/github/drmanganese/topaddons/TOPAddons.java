package io.github.drmanganese.topaddons;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

import io.github.drmanganese.topaddons.helmets.ProbedHelmetCrafting;
import io.github.drmanganese.topaddons.helmets.UnprobedHelmetCrafting;
import io.github.drmanganese.topaddons.proxy.IProxy;
import io.github.drmanganese.topaddons.reference.Reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, acceptedMinecraftVersions = "[1.9.4,1.10.2]", dependencies = "after:TheOneProbe;after:forestry;after:tconstruct;after:bloodmagic;after:StorageDrawers")
public class TOPAddons {

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());

        AddonManager.preInit(event);
        if (AddonManager.ADDONS.size() > 0) {
            TOPRegistrar.register();
        }

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.addRecipe(new ProbedHelmetCrafting());
        GameRegistry.addRecipe(new UnprobedHelmetCrafting());
        RecipeSorter.register("topaddons:helmet", ProbedHelmetCrafting.class, RecipeSorter.Category.SHAPELESS, "");
        RecipeSorter.register("topaddons:remhelmet", UnprobedHelmetCrafting.class, RecipeSorter.Category.SHAPELESS, "");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}

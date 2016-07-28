package io.github.drmanganese.topaddons.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topaddons.Config;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.addons.AddonManager;
import io.github.drmanganese.topaddons.helmets.Helmets;

public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());

        AddonManager.preInit(event);
        if (AddonManager.ADDONS.size() > 0) {
            TOPRegistrar.register();
        }

        if (AddonManager.HELMETS.size() > 0) {
            Helmets.registerHelmets();
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (AddonManager.HELMETS.size() > 0) {
            Helmets.registerRecipes();
        }
    }

}

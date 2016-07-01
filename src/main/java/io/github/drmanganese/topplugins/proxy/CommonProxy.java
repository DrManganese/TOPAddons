package io.github.drmanganese.topplugins.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topplugins.Plugins;
import io.github.drmanganese.topplugins.helmets.Helmets;
import io.github.drmanganese.topplugins.plugins.PluginManager;

public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        PluginManager.preInit(event);
        if (PluginManager.plugins.size() > 0) {
            Plugins.register();
        }
        if (PluginManager.helmets.size() > 0) {
            Helmets.registerHelmets();
        }

    }

    @Override
    public void init(FMLInitializationEvent event) {
        PluginManager.init(event);
        if (PluginManager.helmets.size() > 0) {
            Helmets.registerRecipes();
        }
    }
}

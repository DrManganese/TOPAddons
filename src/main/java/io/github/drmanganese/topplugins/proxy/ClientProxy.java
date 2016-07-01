package io.github.drmanganese.topplugins.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topplugins.helmets.Helmets;
import io.github.drmanganese.topplugins.plugins.PluginManager;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (PluginManager.helmets.size() > 0) {
            Helmets.registerModels();
        }
    }
}

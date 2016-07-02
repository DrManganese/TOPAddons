package io.github.drmanganese.topaddons.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topaddons.helmets.Helmets;
import io.github.drmanganese.topaddons.addons.AddonManager;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (AddonManager.helmets.size() > 0) {
            Helmets.registerModels();
        }
    }
}

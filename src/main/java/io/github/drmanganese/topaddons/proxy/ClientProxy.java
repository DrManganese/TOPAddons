package io.github.drmanganese.topaddons.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topaddons.addons.AddonManager;
import io.github.drmanganese.topaddons.helmets.Helmets;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        if (AddonManager.HELMETS.size() > 0) {
            Helmets.registerModels();
        }
    }
}

package io.github.drmanganese.topaddons.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.addons.AddonManager;
import io.github.drmanganese.topaddons.helmets.Helmets;

@SideOnly(Side.CLIENT)
public final class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (AddonManager.HELMETS.size() > 0) {
            Helmets.registerModels();
        }
    }
}

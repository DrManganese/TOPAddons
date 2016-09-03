package io.github.drmanganese.topaddons.proxy;


import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
    void preInit(FMLPreInitializationEvent event);

    void postInit(FMLPostInitializationEvent event);
}

package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = "required-after:theoneprobe;")
public class TOPAddons {

    public static Logger logger;

    @Mod.Instance
    public static TOPAddons INSTANCE = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        AddonLoader.loadAddons(event.getAsmData());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        TOPRegistrar.register();
    }
}

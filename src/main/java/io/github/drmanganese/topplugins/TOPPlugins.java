package io.github.drmanganese.topplugins;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topplugins.plugins.PluginManager;
import io.github.drmanganese.topplugins.reference.Reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = "after:TheOneProbe")
public class TOPPlugins {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("theoneprobe")) {
            PluginManager.preInit(event);
            if (PluginManager.plugins.size() > 0) {
                Plugins.register();
            }
        }
    }
}

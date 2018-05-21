package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.capabilities.DefaultElementSyncCapability;
import io.github.drmanganese.topaddons.capabilities.IElementSyncCapability;
import io.github.drmanganese.topaddons.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
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

    @CapabilityInject(IElementSyncCapability.class)
    public static final Capability<IElementSyncCapability> SYNC_CAP = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        CapabilityManager.INSTANCE.register(IElementSyncCapability.class, new DefaultElementSyncCapability.Storage(), DefaultElementSyncCapability::new);

        PacketHandler.init();

        AddonLoader.loadAddons(event.getAsmData());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        TOPRegistrar.register();
    }
}

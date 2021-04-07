package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.addons.industrialforegoing.IndustrialForegoingAddon;
import io.github.drmanganese.topaddons.addons.storagedrawers.StorageDrawersAddon;
import io.github.drmanganese.topaddons.addons.thermal.ThermalExpansionAddon;
import io.github.drmanganese.topaddons.addons.vanilla.VanillaAddon;
import io.github.drmanganese.topaddons.capabilities.ClientCfgCapability;
import io.github.drmanganese.topaddons.capabilities.ElementSyncCapability;
import io.github.drmanganese.topaddons.capabilities.UnsupportedCapFactory;
import io.github.drmanganese.topaddons.capabilities.VolatileStorage;
import io.github.drmanganese.topaddons.commands.TopAddonsCommands;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TopAddons.MOD_ID)
@Mod.EventBusSubscriber
public final class TopAddons {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "topaddons";

    @CapabilityInject(ElementSyncCapability.class)
    public static Capability<ElementSyncCapability> ELT_SYNC_CAP;

    @CapabilityInject(ClientCfgCapability.class)
    public static Capability<ClientCfgCapability> CLIENT_CFG_CAP;

    public TopAddons() {
        registerAddons();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEnqueueIMC);
    }

    private void registerAddons() {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        AddonRegistry.registerAddon(ForgeAddon::new);
        AddonRegistry.registerAddon(VanillaAddon::new);
        AddonRegistry.registerAddon(StorageDrawersAddon::new, "storagedrawers");
        AddonRegistry.registerAddon(IndustrialForegoingAddon::new, "industrialforegoing");
        AddonRegistry.registerAddon(ThermalExpansionAddon::new, "thermal_expansion");
        AddonRegistry.registerAddon(BloodMagicAddon::new, "bloodmagic");
        stopwatch.stop();
        LOGGER.debug("Registered {} addons in {}", AddonRegistry.getAddonStream().count(), stopwatch);
    }

    void onCommonSetup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ElementSyncCapability.class, new VolatileStorage<>(), new UnsupportedCapFactory<>());
        CapabilityManager.INSTANCE.register(ClientCfgCapability.class, new VolatileStorage<>(), new UnsupportedCapFactory<>());
        PacketHandler.init();
    }

    void onEnqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopRegistrar::new);
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        TopAddonsCommands.register(event.getServer().getCommandManager().getDispatcher());
    }
}

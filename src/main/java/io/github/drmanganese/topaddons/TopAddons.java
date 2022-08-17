package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.addons.thermal.ThermalExpansionAddon;
import io.github.drmanganese.topaddons.addons.vanilla.VanillaAddon;
import io.github.drmanganese.topaddons.capabilities.ClientCfgCapability;
import io.github.drmanganese.topaddons.commands.TopAddonsCommands;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TopAddons.MOD_ID)
@Mod.EventBusSubscriber
public final class TopAddons {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "topaddons";

    public static final Capability<ClientCfgCapability> CLIENT_CFG_CAP = CapabilityManager.get(new CapabilityToken<>(){});;

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
        AddonRegistry.registerAddon(ThermalExpansionAddon::new, "thermal_expansion");
        stopwatch.stop();
        LOGGER.debug("Registered {} addons in {}", AddonRegistry.getAddonStream().count(), stopwatch);
    }

    void onCommonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    void onEnqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopRegistrar::new);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        TopAddonsCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ClientCfgCapability.class);
    }
}

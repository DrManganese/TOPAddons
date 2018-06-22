package io.github.drmanganese.topaddons.proxy;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.config.ModConfig;
import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;

public class ClientProxy extends CommonProxy {

    @Override
    public void updateConfigs(Configuration config, List<IAddonConfig> addons) {
        addons.forEach(a -> a.updateConfigs(config, Side.CLIENT));
        ModConfig.updateHelmetConfig(config);

        Map<String, Object> syncMap = ModConfig.updateSync(config);
        if (Minecraft.getMinecraft().world != null) {
            PacketHandler.sendClientCfg(syncMap);
        }

        if (TOPAddons.CONFIG.hasChanged()) {
            TOPAddons.CONFIG.save();
        }
    }
}

package io.github.drmanganese.topaddons.proxy;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.config.ModConfig;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class CommonProxy implements IProxy {

    @Override
    public void updateConfigs(Configuration config, List<IAddonConfig> addons) {
        addons.forEach(a -> a.updateConfigs(config, Side.SERVER));
        ModConfig.updateHelmetConfig(config);
        if (TOPAddons.CONFIG.hasChanged()) {
            TOPAddons.CONFIG.save();
        }
    }
}

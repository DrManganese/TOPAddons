package io.github.drmanganese.topaddons.api;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collections;
import java.util.Map;

public interface IAddonConfig {

    void updateConfigs(Configuration config, Side side);

    default Map<String, Object> updateSyncedConfigs(Configuration config) {
        return Collections.emptyMap();
    }

}

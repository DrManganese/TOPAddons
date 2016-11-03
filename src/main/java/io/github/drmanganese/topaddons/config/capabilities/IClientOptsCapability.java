package io.github.drmanganese.topaddons.config.capabilities;

import java.util.Map;

import javax.annotation.Nonnull;

/**
 * A {@link net.minecraftforge.common.capabilities.Capability} linked to an
 * {@link net.minecraft.entity.player.EntityPlayer} that stores player-specific options i.e.
 * turning off functionality, modifying behaviour/appearance.
 * This approach is necessary because TheOneProbe does all its logic for what to display on the
 * server-side.
 */
public interface IClientOptsCapability {

    boolean getBoolean(String option);

    int getInt(String option);

    void setOption(String option, boolean value);

    void setOption(String option, int value);

    void setAll(@Nonnull Map<String, Integer> options);

    Map<String, Integer> getAll();
}

package io.github.drmanganese.topaddons.addons.forge;

import com.google.common.collect.Maps;
import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.IAddonConfigProviders;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.ModConfig;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.forge.ElementTankGauge;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@TOPAddon(dependency = "forge", fancyName = "Forge", order = 0)
public class AddonForge implements IAddonBlocks, IAddonElements, IAddonConfig, IAddonConfigProviders {

    public static boolean tankRounded;
    public static int tankBackgroundColor;
    public static int tankBorderColor;
    public static List<String> tankModBlacklist;
    public static int machineProgressBackgroundColor;
    public static int machineProgressBorderColor;

    @Override
    public void registerElements(ITheOneProbe probe) {
        ElementSync.registerElement(probe, "tank_gauge", ElementTankGauge::new);
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of(TileEntity.class, FluidCapInfo.INSTANCE);
    }

    @Nonnull
    @Override
    public ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return ImmutableMap.of(TileEntity.class, FluidCapInfo.INSTANCE);
    }

    @Override
    public void updateConfigs(Configuration config, Side side) {
        tankModBlacklist = Arrays.asList(config.get("forge", "tankModBlacklist", new String[]{"endertanks", "enderio"}, "Tank gauge modid blacklist", Property.Type.MOD_ID).getStringList());

        if (side == Side.CLIENT) {
            tankRounded = config.get("forge.client", "tankRounded", false, "").getBoolean();
            tankBackgroundColor = ModConfig.getColor(config, "forge.client", "tankBackgroundColor", "#55555555", "Background color for the TOPAddons fluid gauge");
            tankBorderColor = ModConfig.getColor(config, "forge.client", "tankBorderColor", "#FF555555", "Border color for the TOPAddons fluid gauge");
            machineProgressBackgroundColor = ModConfig.getColor(config, "general", "machineProgressBackgroundColor", "#55555555", "Background color for TOPAddons progress bars");
            machineProgressBorderColor = ModConfig.getColor(config, "general", "machineProgressBorderColor", "#FF555555", "Border color for TOPAddons progress bars");
        }
    }

    @Override
    public Map<String, Object> updateSyncedConfigs(Configuration config) {
        final Map<String, Object> syncedConfigs = Maps.newHashMap();
        syncedConfigs.put("showGauge", config.get("forge.client", "showGauge", "Both", "", new String[]{"Both", "TOP Addons", "The One Probe"}).getString());
        syncedConfigs.put("machineProgressBackgroundColor", ModConfig.getColor(config, "forge.client", "machineProgressBackgroundColor", "#99000011", "Background color for TOPAddons progress bars"));
        syncedConfigs.put("machineProgressBorderColor", ModConfig.getColor(config, "forge.client", "machineProgressBorderColor", "#FF555555", "Border color for TOPAddons progress bars"));
        return syncedConfigs;
    }
}

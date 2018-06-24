package io.github.drmanganese.topaddons.addons.storagedrawers;

import io.github.drmanganese.topaddons.addons.storagedrawers.tiles.InfoDrawer;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.IAddonConfigProviders;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import com.google.common.collect.ImmutableMap;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "storagedrawers")
public class AddonStorageDrawers implements IAddonBlocks, IAddonConfig, IAddonConfigProviders {

    private static final InfoDrawer DRAWER_INFO = new InfoDrawer();

    public static boolean hideConcealed;

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of(TileEntityDrawers.class, DRAWER_INFO);
    }

    @Override
    public void updateConfigs(Configuration config, Side side) {
        hideConcealed = config.get("storagedrawers", "hideConcealed", false, "Hide contents when drawer is concealed.").getBoolean();
    }

    @Nonnull
    @Override
    public ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return ImmutableMap.of(TileEntityDrawers.class, DRAWER_INFO);
    }
}

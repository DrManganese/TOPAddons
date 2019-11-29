package io.github.drmanganese.topaddons.addons.storagedrawers;

import io.github.drmanganese.topaddons.addons.storagedrawers.tiles.InfoDrawer;
import io.github.drmanganese.topaddons.api.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "storagedrawers")
public class AddonStorageDrawers implements IAddonBlocks, IAddonConfig, IAddonConfigProviders {

    private static final InfoDrawer DRAWER_INFO = new InfoDrawer();

    public static boolean hideConcealed;

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMultimap.of(TileEntityDrawers.class, DRAWER_INFO);
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

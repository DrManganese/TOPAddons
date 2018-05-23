package io.github.drmanganese.topaddons.addons.forge;

import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.forge.ElementTankGauge;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "forge", fancyName = "Forge", order = 0)
public class AddonForge implements IAddonBlocks, IAddonElements {

    @Override
    public void registerElements(ITheOneProbe probe) {
        ElementSync.registerElement(probe, "tank_gauge", ElementTankGauge::new);
    }

    @Override
    public String getID() {
        return Reference.MOD_ID + ":forge";
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of(TileEntity.class, FluidCapInfo.INSTANCE);
    }
}

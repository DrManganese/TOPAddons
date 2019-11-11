package io.github.drmanganese.topaddons.addons.forestry.tiles;

import forestry.cultivation.inventory.InventoryPlanter;
import forestry.cultivation.tiles.TilePlanter;
import io.github.drmanganese.topaddons.api.ITileInfo;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TilePlanterInfo implements ITileInfo<TilePlanter> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TilePlanter tile) {
        TileFarmInfo.addFertilizerInfo(probeInfo, probeMode, player, tile, tile::getStoredFertilizerScaled, InventoryPlanter.SLOT_FERTILIZER);
    }
}

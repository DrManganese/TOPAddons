package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.world.TreeFluidExtractorTile;

import java.awt.Color;

/**
 * Log breaking progress.
 */
public class TreeFluidExtractorInfo implements ITileInfo<TreeFluidExtractorTile> {

    private static final Color WOOD = new Color(Material.WOOD.getMaterialMapColor().colorValue);
    private static final Color DARKER_WOOD = WOOD.darker();

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TreeFluidExtractorTile tile) {
        BlockPos facing = hitData.getPos().offset(tile.getFacing().getOpposite());
        for (TreeFluidExtractorTile.WoodLodProgress woodLodProgress : TreeFluidExtractorTile.WoodLodProgress.woodLodProgressList) {
            if (woodLodProgress.getBlockPos().equals(facing)) {
                probeInfo.progress(100 * woodLodProgress.getProgress() / 8, 100, probeInfo.defaultProgressStyle()
                        .prefix("Log: ")
                        .suffix("%")
                        .borderColor(0xff777777)
                        .filledColor(DARKER_WOOD.hashCode())
                        .alternateFilledColor(WOOD.hashCode())
                );
            }
        }
    }
}

package io.github.drmanganese.topaddons.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;


public interface IAddonBlocks extends IProbeInfoProvider {

    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     */
    @Override
    default void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        final TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null && getTiles().containsKey(tile.getClass())) {
            //noinspection unchecked
            getTiles().get(tile.getClass()).getInfo(mode, probeInfo, player, world, blockState, data, (tile.getClass().cast(tile)));
        }

        final Block block = blockState.getBlock();
        if (getBlocks().containsKey(block)) {
            getBlocks().get(block).getInfo(mode, probeInfo, player, world, blockState, data);
        }

        //TODO Info based on predicates
    }

    /**
     * @return Map of {@link TileEntity} classes this addon provides info for.
     */
    @Nonnull
    default ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of();
    }

    /**
     * @return Map of {@link Block} instances this addon provides info for.
     */
    @Nonnull
    default ImmutableMap<Block, IBlockInfo> getBlocks() {
        return ImmutableMap.of();
    }
}

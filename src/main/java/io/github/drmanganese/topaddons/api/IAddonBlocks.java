package io.github.drmanganese.topaddons.api;

import io.github.drmanganese.topaddons.reference.Reference;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

public interface IAddonBlocks extends IProbeInfoProvider {

    @Nonnull
    @Override
    default String getID() {
        return Reference.MOD_ID + ":" + this.getClass().getDeclaredAnnotation(TOPAddon.class).dependency();
    }

    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     */
    @Override
    default void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        //Get the tileentity's class and superclasses. All of these are TileEntity or children of TileEntity.
        final TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            List<Class<?>> classes = ClassUtils.getAllSuperclasses(tile.getClass());
            classes.add(tile.getClass());

            for (Class class_ : classes) {
                if (getTiles().containsKey(class_)) {
                    //noinspection unchecked
                    getTiles().get(class_).getInfo(mode, probeInfo, player, world, blockState, data, (TileEntity) class_.cast(tile));
                }
            }
        }

        final Block block = blockState.getBlock();
        if (getBlocks().containsKey(block)) {
            getBlocks().get(block).getInfo(mode, probeInfo, player, world, blockState, data);
        }
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

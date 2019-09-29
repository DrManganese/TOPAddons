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
            final List<Class<?>> tileClasses = ClassUtils.getAllSuperclasses(tile.getClass());
            tileClasses.add(tile.getClass());

            for (Class class_ : tileClasses) {
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

        final List<Class<?>> blockClasses = ClassUtils.getAllSuperclasses(block.getClass());
        blockClasses.add(block.getClass());

        for (Class blockClass : blockClasses) {
            if (getBlockClasses().containsKey(blockClass)) {
                getBlockClasses().get(blockClass).getInfo(mode, probeInfo, player, world, blockState, data);
            }
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

    /**
     * @return Map of {@link Block} classes this addon provides info for.
     */
    @Nonnull
    default ImmutableMap<Class<? extends Block>, IBlockInfo> getBlockClasses() {
        return ImmutableMap.of();
    }
}

package io.github.drmanganese.topaddons.addons.bloodmagic.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.*;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;

import javax.annotation.Nonnull;

public class TileAlchemicalReactorInfo implements ITileInfo<TileAlchemicalReactionChamber> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull TileAlchemicalReactionChamber tile) {
        final ItemStack arcTool = tile.getStackInSlot(TileAlchemicalReactionChamber.ARC_TOOL_SLOT);
        if (!arcTool.isEmpty())
            probeInfo
                .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                .text(CompoundText.create().label("{*topaddons.bloodmagic:arc_tool*}: "))
                .item(arcTool);

        if (tile.currentProgress > 0)
            probeInfo.progress(
                (int) (100 * tile.currentProgress),
                100,
                Styles.machineProgress(player).filledColor(0xff51D6C9).alternateFilledColor(0xff18a7ae)
            );
    }
}

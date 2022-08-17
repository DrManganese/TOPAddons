package io.github.drmanganese.topaddons.addons.bloodmagic.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;

import javax.annotation.Nonnull;

public class TileAlchemicalReactorInfo implements ITileInfo<TileAlchemicalReactionChamber> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull TileAlchemicalReactionChamber tile) {
        final ItemStack arcTool = tile.getItem(TileAlchemicalReactionChamber.ARC_TOOL_SLOT);
        if (!arcTool.isEmpty())
            probeInfo
                .horizontal(Styles.CENTERED)
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
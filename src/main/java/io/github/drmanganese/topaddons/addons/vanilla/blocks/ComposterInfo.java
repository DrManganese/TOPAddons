package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

import com.google.common.collect.Streams;
import mcjty.theoneprobe.api.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ComposterInfo implements IBlockInfo {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData) {
        final int compostLevel = blockState.getValue(ComposterBlock.LEVEL);
        final IProgressStyle progressStyle = Styles.machineProgress(player)
            .filledColor(0xff523c18)
            .alternateFilledColor(0xff4a3018)
            .suffix("/8").prefix("")
            .alignment(ElementAlignment.ALIGN_CENTER);
        probeInfo.progress(compostLevel, 8, progressStyle);

        final Set<Item> heldCompostables = Streams.stream(player.getHandSlots())
            .map(ItemStack::getItem)
            .filter(ComposterBlock.COMPOSTABLES::containsKey)
            .collect(Collectors.toUnmodifiableSet());

        if (!heldCompostables.isEmpty()) {
            final IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(5));
            heldCompostables.forEach(i ->
                hori.vertical(probeInfo.defaultLayoutStyle().spacing(-3).alignment(ElementAlignment.ALIGN_CENTER))
                    .item(new ItemStack(i))
                    .text(String.format("%.0f%%", 100 * ComposterBlock.COMPOSTABLES.getFloat(i)))
            );
        }
    }
}

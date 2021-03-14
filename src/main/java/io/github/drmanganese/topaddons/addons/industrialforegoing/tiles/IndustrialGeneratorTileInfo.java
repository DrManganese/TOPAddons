package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import com.buuz135.industrial.block.tile.IndustrialGeneratorTile;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public class IndustrialGeneratorTileInfo implements ITileInfo<IndustrialGeneratorTile<?>> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull IndustrialGeneratorTile<?> tile) {
        probeInfo.text(CompoundText.createLabelInfo("Generating: ", tile.getEnergyProducedEveryTick()).label(" FE/t"));
    }
}

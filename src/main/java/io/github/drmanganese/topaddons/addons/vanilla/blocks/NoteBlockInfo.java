package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.addons.vanilla.VanillaAddon;
import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import org.apache.commons.lang3.StringUtils;

public class NoteBlockInfo implements IBlockInfo {

    private static final String[] NOTES = new String[]{
            "F\u266f/G\u266d", "G", "G\u266f/A\u266d", "A", "A\u266f/B\u266d", "B", "C", "C\u266f/D\u266d", "D", "D\u266f/E\u266d", "E", "F"
    };

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData) {
        if (VanillaAddon.showPitch.get()) {
            final String instrumentName = StringUtils.capitalize(blockState.get(NoteBlock.INSTRUMENT).name());
            InfoHelper.textPrefixed(probeInfo, "{*topaddons.vanilla:instrument*}", instrumentName);
            InfoHelper.textPrefixed(probeInfo, "{*topaddons.vanilla:pitch*}", NOTES[blockState.get(NoteBlock.NOTE) % 12]);
        }
    }
}

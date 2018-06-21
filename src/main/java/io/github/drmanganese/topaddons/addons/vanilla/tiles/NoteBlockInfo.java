package io.github.drmanganese.topaddons.addons.vanilla.tiles;

import io.github.drmanganese.topaddons.addons.vanilla.AddonVanilla;
import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;

public class NoteBlockInfo implements ITileInfo<TileEntityNote> {

    private static final String[] NOTES = new String[]{
            "F\u266f/G\u266d", "G", "G\u266f/A\u266d", "A", "A\u266f/B\u266d", "B", "C", "C\u266f/D\u266d", "D", "D\u266f/E\u266d", "E", "F"
    };

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntityNote tile) {
        if (AddonVanilla.showPitch) {
            probeInfo.text("{*topaddons.vanilla:pitch*}: " + NOTES[tile.note % 12]);
        }
    }
}

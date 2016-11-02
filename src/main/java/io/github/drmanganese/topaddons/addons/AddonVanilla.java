package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.capabilities.ModCapabilities;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "Forge", fancyName = "Vanilla")
public class AddonVanilla extends AddonBlank {

    private static final String[] NOTES = new String[]{
            "F\u266f/G\u266d", "G", "G\u266f/A\u266d", "A", "A\u266f/B\u266d", "B", "C", "C\u266f/D\u266d", "D", "D\u266f/E\u266d", "E", "F"

    };

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEntityNote && player.getCapability(ModCapabilities.OPTIONS, null).getBoolean("showPitch")) {
            textPrefixed(probeInfo, "Pitch", NOTES[((TileEntityNote) tile).note % 12], TextFormatting.AQUA);

            Material material = world.getBlockState(data.getPos().down()).getMaterial();
            String instrument;
            if (material == Material.ROCK) {
                instrument = "Bass drum";
            } else if (material == Material.SAND) {
                instrument = "Snare drum";
            } else if (material == Material.GLASS) {
                instrument = "Clicks and sticks";
            } else if (material == Material.WOOD) {
                instrument = "Bass guitar";
            } else {
                instrument = "Piano/Harp";
            }

            textPrefixed(probeInfo, "Instrument", instrument, TextFormatting.AQUA);
        }
    }
}

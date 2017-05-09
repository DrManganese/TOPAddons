package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "forge", fancyName = "Vanilla")
public class AddonVanilla extends AddonBlank {

    private static final String[] NOTES = new String[]{
            "F\u266f/G\u266d", "G", "G\u266f/A\u266d", "A", "A\u266f/B\u266d", "B", "C", "C\u266f/D\u266d", "D", "D\u266f/E\u266d", "E", "F"
    };

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEntityNote && player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("showPitch")) {
            textPrefixed(probeInfo, "{*topaddons.vanilla:pitch*}", NOTES[((TileEntityNote) tile).note % 12]);

            Material material = world.getBlockState(data.getPos().down()).getMaterial();
            String instrument;
            if (material == Material.ROCK) {
                instrument = "{*topaddons.vanilla:rock*}";
            } else if (material == Material.SAND) {
                instrument = "{*topaddons.vanilla:sand*}";
            } else if (material == Material.GLASS) {
                instrument = "{*topaddons.vanilla:glass*}";
            } else if (material == Material.WOOD) {
                instrument = "{*topaddons.vanilla:wood*}";
            } else {
                instrument = "{*topaddons.vanilla:else*}";
            }

            textPrefixed(probeInfo, "{*topaddons.vanilla:instrument*}", instrument);
        }
    }
}

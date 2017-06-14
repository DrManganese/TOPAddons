package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.Config;

import java.util.Collections;
import java.util.List;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

@TOPAddon(dependency = "Forge", fancyName = "Vanilla")
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

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityAnimal) {
            if (mode == ProbeMode.EXTENDED || ((EntityAnimal) entity).isBreedingItem(player.getHeldItem(EnumHand.MAIN_HAND)) || ((EntityAnimal) entity).isBreedingItem(player.getHeldItem(EnumHand.OFF_HAND))) {
                int age = ((EntityAnimal) entity).getGrowingAge();
                int mins = age / 1200;
                int secs = age / 20 - mins * 60;
                if (age > 0 && Config.Vanilla.breedingCooldown) {
                    textPrefixed(probeInfo, "{*topaddons.vanilla:nobreed*}", String.format(mins == 0 ? "%d\"" : "%d'%d\"", mins == 0 ? secs : mins, secs), TextFormatting.RED);
                } else if (age < 0) {
                    textPrefixed(probeInfo, "{*topaddons.vanilla:adultin*}", String.format(mins == 0 ? "%d\"" : "%d'%d\"", mins == 0 ? -secs : -mins, -secs));
                }
            }
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList(new IBlockDisplayOverride() {
            @Override
            public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() == Blocks.END_PORTAL) {
                    if (Tools.show(mode, mcjty.theoneprobe.config.Config.getRealConfig().getShowModName())) {
                        probeInfo.horizontal()
                                .vertical()
                                .text(Blocks.END_PORTAL_FRAME.getLocalizedName())
                                .text(MODNAME + Tools.getModName(Blocks.END_PORTAL));
                    } else {
                        probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                .text(Blocks.END_PORTAL_FRAME.getLocalizedName());
                    }
                    return true;
                }
                return false;
            }
        });
    }
}

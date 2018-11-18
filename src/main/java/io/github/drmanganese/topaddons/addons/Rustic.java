package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import rustic.common.blocks.crops.BlockAppleSeeds;
import rustic.common.blocks.crops.BlockGrapeLeaves;
import rustic.common.blocks.crops.BlockGrapeStem;
import rustic.common.blocks.crops.BlockStakeCrop;

import java.util.Collections;
import java.util.List;

import static mcjty.theoneprobe.api.TextStyleClass.*;

@TOPAddon(dependency = "rustic")
public class Rustic extends AddonBlank {

    @ObjectHolder("rustic:grape_stem")
    private static final Block GRAPE_STEM = null;

    @ObjectHolder("rustic:grape_leaves")
    private static final Block GRAPE_LEAVES = null;

    @ObjectHolder("rustic:apple_seeds")
    private static final Block APPLE_SEEDS = null;

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        //Crop growth for rustic crops
        return Collections.singletonList((mode, probeInfo, player, world, blockState, data) -> {
            if (blockState.getBlock() instanceof BlockStakeCrop || blockState.getBlock() == GRAPE_LEAVES || blockState.getBlock() == GRAPE_STEM || blockState.getBlock() == APPLE_SEEDS) {
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .item(data.getPickBlock())
                            .vertical()
                            .itemLabel(data.getPickBlock())
                            .text(MODNAME + Tools.getModName(blockState.getBlock()));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(data.getPickBlock())
                            .itemLabel(data.getPickBlock());
                }

                int age = 0;
                int maxAge = 3;

                if (blockState.getBlock() instanceof BlockStakeCrop) {
                    age = blockState.getValue(BlockStakeCrop.AGE);
                }

                if (blockState.getBlock() == APPLE_SEEDS) {
                    age = blockState.getValue(BlockAppleSeeds.AGE);
                    //Max value for apple seeds is actually 1, but at max age the sapling hasn't formed yet
                    maxAge = 2;
                }

                if (blockState.getBlock() == GRAPE_LEAVES) {
                    age = blockState.getValue(BlockGrapeLeaves.GRAPES) ? 1 : 0;
                    maxAge = 1;
                }

                if (blockState.getBlock() == GRAPE_STEM) {
                    age = blockState.getValue(BlockGrapeStem.AGE);
                }

                if (age == maxAge) {
                    probeInfo.text(OK + "Fully grown");
                } else {
                    probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
                }

                return true;
            }

            return false;
        });
    }
}

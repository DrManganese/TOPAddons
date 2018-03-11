package io.github.drmanganese.topaddons.addons.subaddons.binniesmods;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.styles.ProgressStyleExtraTreesMachine;

import java.util.Collections;
import java.util.List;

import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.machines.power.ComponentProcess;
import binnie.extratrees.blocks.BlockHops;
import binnie.extratrees.machines.brewery.BreweryLogic;
import binnie.extratrees.machines.distillery.DistilleryLogic;
import binnie.extratrees.machines.fruitpress.FruitPressLogic;
import binnie.extratrees.machines.lumbermill.LumbermillLogic;
import com.google.common.collect.Lists;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import static mcjty.theoneprobe.api.TextStyleClass.*;

public class SubAddonTrees extends AddonBlank {

    @ObjectHolder("extratrees:hops")
    private static final BlockCrops HOPS = null;

    private static final List<Class<? extends ComponentProcess>> machineProcesses = Lists.newArrayList(
            LumbermillLogic.class,
            DistilleryLogic.class,
            FruitPressLogic.class,
            BreweryLogic.class
    );

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileEntityMachine) {
            Machine machine = ((TileEntityMachine) tile).getMachine();

            if (machine != null) {
                for (Class<? extends ComponentProcess> process : machineProcesses) {
                    if (machine.hasComponent(process)) {
                        ComponentProcess logic = machine.getComponent(process);
                        if (logic.isInProgress()) {
                            probeInfo.progress(Math.round(logic.getProgress()), 100, new ProgressStyleExtraTreesMachine(process));
                        }

                        break;
                    }
                }
            }
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, blockState, data) -> {
            //noinspection ConstantConditions
            if (blockState.getBlock() == HOPS && blockState.getValue(BlockHops.HALF) == BlockHops.HopsHalf.UP) {
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .item(data.getPickBlock())
                            .vertical()
                            .text(data.getPickBlock().getDisplayName())
                            .text(MODNAME + Tools.getModName(HOPS));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(data.getPickBlock())
                            .text(data.getPickBlock().getDisplayName());
                }

                IBlockState belowState = world.getBlockState(data.getPos().down());
                int age = ((BlockHops) blockState.getBlock()).getAge(belowState);
                int maxAge = ((BlockHops) blockState.getBlock()).getMaxAge();
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

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() == HOPS && blockState.getValue(BlockHops.HALF) == BlockHops.HopsHalf.UP) {
            config.showCropPercentage(IProbeConfig.ConfigMode.NOT);
        }
    }
}

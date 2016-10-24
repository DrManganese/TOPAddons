package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Names;

import ic2.core.block.comp.Energy;
import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.block.machine.tileentity.TileEntityCanner;
import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.block.wiring.TileEntityElectricBlock;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;

@TOPAddon(dependency = "IC2")
public class AddonIndustrialCraft2 extends AddonBlank {

    @Override
    public void addTankNames() {
        Names.tankNamesMap.put(TileEntityGeoGenerator.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileEntityCanner.class, new String[]{"Input", "Output"});
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            if (tile instanceof TileEntityStandardMachine) {
                TileEntityStandardMachine machine = (TileEntityStandardMachine) tile;
                double energyStorage = machine.defaultEnergyStorage * 2 + machine.upgradeSlot.extraEnergyStorage;
                probeInfo.progress((int) machine.getEnergy(), (int) (machine.getEnergy() > energyStorage ? machine.getEnergy() : energyStorage), probeInfo.defaultProgressStyle()
                        .suffix(" EU")
                        .filledColor(Config.rfbarFilledColor)
                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(NumberFormat.COMPACT));

                textPrefixed(probeInfo, "Consumption", machine.energyConsume + " EU/t");
                textPrefixed(probeInfo, "Progress", (Math.round(((TileEntityStandardMachine) tile).getProgress()  * 100)) + "%");
            }

            if (tile instanceof TileEntitySolarGenerator) {
                if (((TileEntitySolarGenerator) tile).skyLight == 0F) {
                    probeInfo.text(TextFormatting.RED + "Sky Obstructed/Too Dark");
                }
            }

            if (tile instanceof TileEntityElectricBlock) {
                Energy energy = ((TileEntityElectricBlock) tile).energy;
                probeInfo.progress((int) energy.getEnergy(), (int) energy.getCapacity(), probeInfo.defaultProgressStyle()
                        .suffix(" EU")
                        .filledColor(Config.rfbarFilledColor)
                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(NumberFormat.COMPACT));
            }
        }
    }
}

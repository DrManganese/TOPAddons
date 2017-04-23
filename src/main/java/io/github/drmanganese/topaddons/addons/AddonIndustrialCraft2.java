package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Names;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import java.util.HashMap;
import java.util.Map;

import ic2.core.block.TileEntityHeatSourceInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.block.machine.tileentity.TileEntityCanner;
import ic2.core.block.machine.tileentity.TileEntityFermenter;
import ic2.core.block.machine.tileentity.TileEntityPump;
import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.armor.ItemArmorSolarHelmet;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;

@TOPAddon(dependency = "ic2", order = 0)
public class AddonIndustrialCraft2 extends AddonBlank {

    @Override
    public void addTankNames() {
        Names.tankNamesMap.put(TileEntityGeoGenerator.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileEntityCanner.class, new String[]{"Input", "Output"});
    }

    @Override
    public boolean hasSpecialHelmets() {
        return true;
    }

    @Override
    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        Map<Class<? extends ItemArmor>, EnumChip> map = new HashMap<>();
        map.put(ItemArmorNanoSuit.class, EnumChip.IC2);
        map.put(ItemArmorQuantumSuit.class, EnumChip.IC2);
        map.put(ItemArmorHazmat.class, EnumChip.IC2);
        map.put(ItemArmorSolarHelmet.class, EnumChip.IC2);
        map.put(ItemArmorHazmat.class, EnumChip.IC2);
        return map;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            if (tile instanceof TileEntityStandardMachine) {
                TileEntityStandardMachine machine = (TileEntityStandardMachine) tile;
                double energyStorage = machine.defaultEnergyStorage * 2 + machine.upgradeSlot.extraEnergyStorage;
                euBar(probeInfo, (int) machine.getEnergy(),(int) (machine.getEnergy() > energyStorage ? machine.getEnergy() : energyStorage));
                if (mode == ProbeMode.EXTENDED) {
                    textPrefixed(probeInfo, "Consumption", machine.energyConsume + " EU/t");
                }

                if (player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("ic2Progress") && machine.getProgress() > 0 || mode == ProbeMode.EXTENDED) {
                    probeInfo.progress((long) (machine.getProgress()*100), 100, new ProgressStyleTOPAddonGrey().prefix("Progress: ").suffix("%").filledColor(0xffaaaaaa).alternateFilledColor(0xff888888));
                }
            }

            if (tile instanceof TileEntitySolarGenerator) {
                if (((TileEntitySolarGenerator) tile).skyLight == 0F) {
                    probeInfo.text(TextFormatting.RED + "Sky Obstructed/Too Dark");
                }
            }

            if (tile instanceof TileEntityElectricBlock) {
                Energy energy = ((TileEntityElectricBlock) tile).energy;
                euBar(probeInfo, (int) energy.getEnergy(), (int) energy.getCapacity());
            }

            if (tile instanceof TileEntityTeleporter) {
                BlockPos pos = ((TileEntityTeleporter) tile).getTarget();
                textPrefixed(probeInfo, "Destination", ((TileEntityTeleporter) tile).hasTarget() ? String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()) : "none");
            }

            if (tile instanceof TileEntityTerra) {
                if (!((TileEntityTerra) tile).tfbpSlot.isEmpty())
                    textPrefixed(probeInfo, "Blueprint", ((TileEntityTerra) tile).tfbpSlot.get().getDisplayName().substring(7), TextFormatting.AQUA);
                else
                    textPrefixed(probeInfo, "Blueprint", "None", TextFormatting.AQUA);
            }

            if (tile instanceof TileEntityHeatSourceInventory) {
                textPrefixed(probeInfo, "Transmitting", ((TileEntityHeatSourceInventory) tile).gettransmitHeat() + " hU");
                textPrefixed(probeInfo, "Buffer", ((TileEntityHeatSourceInventory) tile).getHeatBuffer() + " hU");
                textPrefixed(probeInfo, "Max transfer", ((TileEntityHeatSourceInventory) tile).getMaxHeatEmittedPerTick() + " hU");
            }

            if (tile instanceof TileEntityFermenter) {
                TileEntityFermenter fermenter = (TileEntityFermenter) tile;
                probeInfo.progress(Math.round(100 * fermenter.getGuiValue("heat")), 100, new ProgressStyleTOPAddonGrey().prefix("Conversion: ").suffix("%").alternateFilledColor(0xFFE12121).filledColor(0xFF871414));
                probeInfo.progress(Math.round(100 * fermenter.getGuiValue("progress")), 100, new ProgressStyleTOPAddonGrey().prefix("Waste: ").suffix("%").alternateFilledColor(0xFF0E760E).filledColor(0xFF084708));
            }

            if (tile instanceof TileEntityPump) {
                euBar(probeInfo, (int) ((TileEntityPump) tile).getEnergy(), 40);
            }
        }
    }

    private void euBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(energy, capacity, probeInfo.defaultProgressStyle()
                .suffix(" EU")
                .filledColor(Config.rfbarFilledColor)
                .alternateFilledColor(Config.rfbarAlternateFilledColor)
                .borderColor(Config.rfbarBorderColor)
                .numberFormat(NumberFormat.COMPACT));
    }
}

package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.addons.crossmod.AdvGensXIC2;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Colors;

import java.awt.Color;
import java.text.DecimalFormat;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.config.Config;
import net.bdew.generators.config.Tuning;
import net.bdew.generators.controllers.PoweredController;
import net.bdew.generators.controllers.exchanger.TileExchangerController;
import net.bdew.generators.controllers.steam.TileSteamTurbineController;
import net.bdew.generators.controllers.syngas.TileSyngasController;
import net.bdew.generators.controllers.turbine.TileTurbineController;
import net.bdew.generators.modules.forgeOutput.BlockForgeOutput$;
import net.bdew.generators.modules.rfOutput.BlockRfOutput$;
import net.bdew.generators.modules.turbine.BlockTurbine;
import net.bdew.lib.data.mixins.DataSlotNumeric;
import net.bdew.lib.multiblock.block.BlockOutput;
import scala.collection.Iterator;
import scala.reflect.ClassTag$;
import static mcjty.theoneprobe.api.TextStyleClass.PROGRESS;

@TOPAddon(dependency = "advgenerators")
public class AddonAdvancedGenerators extends AddonBlank {

    @Override
    public void addFluidColors() {
        Colors.FLUID_NAME_COLOR_MAP.put("syngas", new Color(73, 94, 15).hashCode());
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileTurbineController) {
            TileTurbineController controller = (TileTurbineController) tile;

            boolean rfOutput = false, euOutput = false;
            final Iterator<BlockOutput> it = ((PoweredController) controller).getModuleBlocks(ClassTag$.MODULE$.<BlockOutput>apply(BlockOutput.class)).valuesIterator();
            while (it.hasNext()) {
                final BlockOutput blockOutput = it.next();
                if (blockOutput instanceof BlockRfOutput$ || blockOutput instanceof BlockForgeOutput$) {
                    rfOutput = true;
                }

                euOutput = TOPAddons.ic2Loaded && AdvGensXIC2.isEuOutput(blockOutput);
            }

            if (euOutput && !rfOutput) {
                AddonIndustrialCraft2.euBar(probeInfo, (int) (controller.power().stored() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")), (int) (controller.power().capacity() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")));
                textPrefixed(probeInfo, "{*topaddons:generating*}", (int) (controller.outputAverage().average() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")) + "/" + (int) ((float) (((DataSlotNumeric) controller.maxMJPerTick()).value()) * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")) + " EU/t");
            } else {
                if (Config.getRealConfig().getRFMode() == 1) {
                    probeInfo.progress((int) controller.power().stored(), (int) controller.power().capacity(),
                            probeInfo.defaultProgressStyle()
                                    .suffix("RF")
                                    .filledColor(Config.rfbarFilledColor)
                                    .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(Config.rfFormat));
                } else {
                    probeInfo.text(PROGRESS + "RF: " + ElementProgress.format((int) controller.power().stored(), Config.rfFormat, "RF"));
                }
                textPrefixed(probeInfo, "{*topaddons:generating*}", (int) (controller.outputAverage().average() * Tuning.getSection("Power").getFloat("RF_MJ_Ratio")) + "/" + (int) ((float) (((DataSlotNumeric) controller.maxMJPerTick()).value()) * Tuning.getSection("Power").getFloat("RF_MJ_Ratio")) + " RF/t");
            }
            textPrefixed(probeInfo, "{*topaddons.advgenerators:turbines*}", String.valueOf(((PoweredController) controller).getModuleBlocks(ClassTag$.MODULE$.apply(BlockTurbine.class)).size()));

            FluidTankInfo fuel = controller.fuel().getInfo();
            AddonForge.addTankElement(probeInfo, "Fuel", fuel, mode, player);
            textPrefixed(probeInfo, "{*topaddons:generating*}", new DecimalFormat("#.##").format(controller.fuelPerTickAverage().average()) + "/" + new DecimalFormat("#.##").format(((DataSlotNumeric) controller.fuelPerTick()).value()) + " mB/t");
        }

        if (tile instanceof TileSteamTurbineController) {
            TileSteamTurbineController controller = (TileSteamTurbineController) tile;

            boolean rfOutput = false, euOutput = false;
            final Iterator<BlockOutput> it = ((PoweredController) controller).getModuleBlocks(ClassTag$.MODULE$.<BlockOutput>apply(BlockOutput.class)).valuesIterator();
            while (it.hasNext()) {
                final BlockOutput blockOutput = it.next();
                if (blockOutput instanceof BlockRfOutput$ || blockOutput instanceof BlockForgeOutput$) {
                    rfOutput = true;
                }

                euOutput = TOPAddons.ic2Loaded && AdvGensXIC2.isEuOutput(blockOutput);
            }

            if (euOutput && !rfOutput) {
                AddonIndustrialCraft2.euBar(probeInfo, (int) (controller.power().stored() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")), (int) (controller.power().capacity() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")));
                textPrefixed(probeInfo, "{*topaddons:generating*}", (int) (controller.outputAverage().average() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")) + "/" + (int) ((double) ((DataSlotNumeric) controller.maxMJPerTick()).value() * Tuning.getSection("Power").getFloat("EU_MJ_Ratio")) + " EU/t");
            } else {
                if (Config.getRealConfig().getRFMode() == 1) {
                    probeInfo.progress((int) controller.power().stored(), (int) controller.power().capacity(),
                            probeInfo.defaultProgressStyle()
                                    .suffix("RF")
                                    .filledColor(Config.rfbarFilledColor)
                                    .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(Config.rfFormat));
                } else {
                    probeInfo.text(PROGRESS + "RF: " + ElementProgress.format((int) controller.power().stored(), Config.rfFormat, "RF"));
                }
                textPrefixed(probeInfo, "{*topaddons:generating*}", (int) (controller.outputAverage().average() * Tuning.getSection("Power").getFloat("RF_MJ_Ratio")) + "/" + (int) ((double) (((DataSlotNumeric) controller.maxMJPerTick()).value()) * Tuning.getSection("Power").getFloat("RF_MJ_Ratio")) + " RF/t");
            }
            textPrefixed(probeInfo, "{*topaddons.advgenerators:turbines*}", String.valueOf(((PoweredController) controller).getModuleBlocks(ClassTag$.MODULE$.apply(BlockTurbine.class)).size()));

            FluidTankInfo fuel = controller.steam().getInfo();
            AddonForge.addTankElement(probeInfo, "Fuel", fuel, mode, player);
            textPrefixed(probeInfo, "{*topaddons:consumption*}", new DecimalFormat("#.##").format(controller.steamAverage().average()) + " mB/t");
            textPrefixed(probeInfo, "{*topaddons.advgenerators:speed*}", new DecimalFormat("#").format(((DataSlotNumeric) controller.speed()).value()) + " RPM");
        }

        if (tile instanceof TileSyngasController) {
            TileSyngasController controller = (TileSyngasController) tile;

            AddonForge.addTankElement(probeInfo, "Water Tank", controller.waterTank().getInfo(), mode, player);
            AddonForge.addTankElement(probeInfo, "Syngas Tank", controller.syngasTank().getInfo(), mode, player);

            IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2));
            IProbeInfo labels = hori.vertical(probeInfo.defaultLayoutStyle().spacing(4));
            IProbeInfo bars = hori.vertical();
            final double deltaHeat = controller.avgHeatDelta().average();

            labels.text(TextStyleClass.LABEL + "{*topaddons.advgenerators:heat*}:");
            bars.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2))
                    .progress((int) Math.round((double) ((DataSlotNumeric) controller.heat()).value()), (int) controller.cfg().maxHeat(), probeInfo.defaultProgressStyle()
                            .filledColor(0xffd62b1e)
                            .alternateFilledColor(0xffd62b1e)
                            .suffix("/" + (int) controller.cfg().maxHeat() + " HU")
                            .width(63))
                    .text(((deltaHeat > 0D) ? TextFormatting.GREEN : (deltaHeat < 0D) ? TextFormatting.RED : TextFormatting.RESET) + new DecimalFormat("#.##").format(deltaHeat) + " HU/t");

            labels.text(TextStyleClass.LABEL + "{*topaddons.advgenerators:carbon*}:");
            bars.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2))
                    .progress((int) Math.round(100 * (double) ((DataSlotNumeric) controller.carbonBuffer()).value() / controller.cfg().internalTankCapacity()), 100, probeInfo.defaultProgressStyle()
                            .filledColor(0xff222222)
                            .alternateFilledColor(0xff222222)
                            .suffix("%")
                            .width(63))
                    .text(new DecimalFormat("#.##").format(controller.avgCarbonUsed().average()) + " C/t");

            if (mode == ProbeMode.EXTENDED) {
                hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(0xff222222));
                for (ItemStack itemStack : controller.inventory().inv()) {
                    if (!itemStack.isEmpty()) hori.item(itemStack);
                }
            }

            labels.text(TextStyleClass.LABEL + "{*topaddons.advgenerators:steam*}:");
            bars.progress((int) Math.round(100 * (double) ((DataSlotNumeric) controller.steamBuffer()).value() / controller.cfg().internalTankCapacity()), 100, probeInfo.defaultProgressStyle()
                    .filledColor(0xffdddddd)
                    .alternateFilledColor(0xffdddddd)
                    .suffix("%")
                    .width(63));

            textPrefixed(probeInfo, "{*topaddons.advgenerators:production*}", new DecimalFormat("#.##").format(controller.avgSyngasProduced().average()) + " mB/t");

        }

        if (tile instanceof TileExchangerController) {
            TileExchangerController controller = (TileExchangerController) tile;

            final double deltaHeat = controller.heatLoss().average();
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2))
                    .text(TextStyleClass.LABEL + "{*topaddons.advgenerators:heat*}:")
                    .progress((int) Math.round((double) ((DataSlotNumeric) controller.heat()).value()), (int) controller.cfg().maxHeat(), probeInfo.defaultProgressStyle()
                            .filledColor(0xffd62b1e)
                            .alternateFilledColor(0xffd62b1e)
                            .suffix("/" + (int) controller.cfg().maxHeat() + " HU")
                            .width(70))
                    .text(new DecimalFormat("-#.##").format(deltaHeat) + " HU/t");

            textPrefixed(probeInfo, "{*topaddons.advgenerators:max_heat_transfer*}", new DecimalFormat("#.##").format(((DataSlotNumeric) controller.maxHeatTransfer()).value()) + " HU");
            textPrefixed(probeInfo, "{*topaddons.advgenerators:fluid_consumption*}", new DecimalFormat("#.##").format(controller.inputRate().average()) + " mB/t");
            textPrefixed(probeInfo, "{*topaddons.advgenerators:fluid_production*}", new DecimalFormat("#.##").format(controller.outputRate().average()) + " mB/t");
        }

        if (TOPAddons.ic2Loaded) AdvGensXIC2.euOutputInfo(probeInfo, tile);
    }
}
package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import io.github.drmanganese.topaddons.Util;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Names;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import ic2.api.crops.ICropTile;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IKineticSource;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.recipe.ISemiFluidFuelManager;
import ic2.api.recipe.Recipes;
import ic2.core.block.BlockIC2Fence;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityHeatSourceInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.Fluids;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.generator.tileentity.TileEntityGenerator;
import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import ic2.core.block.generator.tileentity.TileEntityKineticGenerator;
import ic2.core.block.generator.tileentity.TileEntityRTGenerator;
import ic2.core.block.generator.tileentity.TileEntitySemifluidGenerator;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.block.generator.tileentity.TileEntityStirlingGenerator;
import ic2.core.block.generator.tileentity.TileEntityWaterGenerator;
import ic2.core.block.generator.tileentity.TileEntityWindGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityStirlingKineticGenerator;
import ic2.core.block.machine.tileentity.TileEntityAdvMiner;
import ic2.core.block.machine.tileentity.TileEntityBlastFurnace;
import ic2.core.block.machine.tileentity.TileEntityCanner;
import ic2.core.block.machine.tileentity.TileEntityCentrifuge;
import ic2.core.block.machine.tileentity.TileEntityCondenser;
import ic2.core.block.machine.tileentity.TileEntityFermenter;
import ic2.core.block.machine.tileentity.TileEntityFluidBottler;
import ic2.core.block.machine.tileentity.TileEntityFluidDistributor;
import ic2.core.block.machine.tileentity.TileEntityFluidRegulator;
import ic2.core.block.machine.tileentity.TileEntityInduction;
import ic2.core.block.machine.tileentity.TileEntityLiquidHeatExchanger;
import ic2.core.block.machine.tileentity.TileEntityMagnetizer;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.machine.tileentity.TileEntityMiner;
import ic2.core.block.machine.tileentity.TileEntityPatternStorage;
import ic2.core.block.machine.tileentity.TileEntityReplicator;
import ic2.core.block.machine.tileentity.TileEntityScanner;
import ic2.core.block.machine.tileentity.TileEntitySolarDestiller;
import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.block.machine.tileentity.TileEntitySteamGenerator;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.block.personal.TileEntityPersonalChest;
import ic2.core.block.personal.TileEntityTradeOMat;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.block.reactor.tileentity.TileEntityReactorChamberElectric;
import ic2.core.block.wiring.TileEntityChargepadBlock;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.init.MainConfig;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.armor.ItemArmorSolarHelmet;
import ic2.core.ref.ItemName;
import ic2.core.util.ConfigUtil;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;

@TOPAddon(dependency = "ic2", order = 0)
public class AddonIndustrialCraft2 extends AddonBlank {

    private static final Map<Class<? extends TileEntityBaseGenerator>, Double> MULTIPLIERS = new HashMap<>();
    private static final Map<Class<? extends TileEntityBaseGenerator>, Function<TileEntityBaseGenerator, Double>> OUTPUTS = new HashMap<>();

    static {
        MULTIPLIERS.put(TileEntitySolarGenerator.class, ConfigUtil.getDouble(MainConfig.get(), "balance/energy/generator/solar"));
        MULTIPLIERS.put(TileEntityGenerator.class, (double) ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator"));
        MULTIPLIERS.put(TileEntityRTGenerator.class, (double) ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/radioisotope"));
        MULTIPLIERS.put(TileEntityWaterGenerator.class, ConfigUtil.getDouble(MainConfig.get(), "balance/energy/generator/water"));
        MULTIPLIERS.put(TileEntityWindGenerator.class, ConfigUtil.getDouble(MainConfig.get(), "balance/energy/generator/wind"));


        OUTPUTS.put(TileEntitySolarGenerator.class, tile -> ((TileEntitySolarGenerator) tile).skyLight * MULTIPLIERS.get(TileEntitySolarGenerator.class));
        OUTPUTS.put(TileEntityGenerator.class, tile -> 10.0D * MULTIPLIERS.get(TileEntityGenerator.class));
        OUTPUTS.put(TileEntityGeoGenerator.class, tile -> 20.0D);
        OUTPUTS.put(TileEntityRTGenerator.class, tile -> {
            TileEntityRTGenerator RTGGenerator = (TileEntityRTGenerator) tile;
            int counter = 0;
            for (int i = 0; i < RTGGenerator.fuelSlot.size(); ++i) {
                if (!RTGGenerator.fuelSlot.isEmpty(i)) {
                    ++counter;
                }
            }
            if (counter == 0) {
                return 0.0D;
            } else {
                return Math.pow(2.0D, (double) (counter - 1)) * MULTIPLIERS.get(TileEntityRTGenerator.class);
            }
        });
        OUTPUTS.put(TileEntitySemifluidGenerator.class, tile -> {
            FluidStack fluidStack = tile.getComponent(Fluids.class).getFluidTank("fluid").getFluid();
            if (fluidStack != null) {
                ISemiFluidFuelManager.BurnProperty property = Recipes.semiFluidGenerator.getBurnProperty(fluidStack.getFluid());
                if (property != null && fluidStack.amount >= property.amount) {
                    return property.power;
                }
            }
            return 0.0D;
        });
        OUTPUTS.put(TileEntityWaterGenerator.class, tile -> {
            TileEntityWaterGenerator waterGenerator = ((TileEntityWaterGenerator) tile);
            if (waterGenerator.fuel <= 0) {
               return EnergyNet.instance.getNodeStats(EnergyNet.instance.getTile(tile.getWorld(), tile.getPos())).getEnergyOut();
            } else if (waterGenerator.fuelSlot.get().getItem().hasContainerItem(waterGenerator.fuelSlot.get())) {
                return 1.0D;
            } else {
                return 2.0D;
            }
        });
        OUTPUTS.put(TileEntityWindGenerator.class, tile -> {
            try {
                return EnergyNet.instance.getNodeStats(EnergyNet.instance.getTile(tile.getWorld(), tile.getPos())).getEnergyOut();
            } catch (NullPointerException e) {
                return 0.0D;
            }
        });
    }

    @Override
    public void addTankNames() {
        Names.tankNamesMap.put(TileEntityGeoGenerator.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileEntityCanner.class, new String[]{"Input", "Output"});
        Names.tankNamesMap.put(TileEntityLiquidHeatExchanger.class, new String[]{"Hot liquid", "Cold liquid"});
        Names.tankNamesMap.put(TileEntityCondenser.class, new String[]{"Gas", "Liquid"});
        Names.tankNamesMap.put(TileEntityReactorChamberElectric.class, new String[]{"Cold liquid", "Hot liquid"});
        Names.tankNamesMap.put(TileEntityStirlingKineticGenerator.class, new String[]{"Cold liquid", "Hot liquid"});
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

        if (tile instanceof TileEntityBlock) {
            TileEntityBlock tileBlock = (TileEntityBlock) tile;
            //EU Bar
            if (tileBlock.hasComponent(Energy.class)) {
                Energy energy = tileBlock.getComponent(Energy.class);
                euBar(probeInfo, (int) energy.getEnergy(), (int) energy.getCapacity());
            }

            //Tank gauges
            if (tileBlock.hasComponent(Fluids.class)) {
                Iterator<Fluids.InternalFluidTank> tanks = tileBlock.getComponent(Fluids.class).getAllTanks().iterator();
                int i = 0;
                while (tanks.hasNext()) {
                    AddonForge.addTankElement(probeInfo, tile.getClass(), tanks.next(), i, mode, player);
                    i++;
                }
            }
        }

        if (tile instanceof TileEntityStandardMachine) {
            TileEntityStandardMachine machine = (TileEntityStandardMachine) tile;
            //Progress bars -- only show when the machine has work
            boolean hasWork = false;
            try {
                hasWork = !machine.inputSlot.isEmpty();
            } catch (NullPointerException e) {
                if (tile instanceof TileEntityFluidBottler) {
                    hasWork = !((TileEntityFluidBottler) tile).fillInputSlot.isEmpty() || !((TileEntityFluidBottler) tile).drainInputSlot.isEmpty();
                }
            }
            if (hasWork) {
                progressBar(probeInfo, (int) (machine.getProgress() * 100), 0xffaaaaaa, 0xff888888);
            }

            //Energy consumption
            if (mode == ProbeMode.EXTENDED) {
                textPrefixed(probeInfo, "{*topaddons:consumption*}", machine.energyConsume + " EU/t");
            }
        }

        if (tile instanceof TileEntityInduction) {
            TileEntityInduction machine = (TileEntityInduction) tile;
            //Heat percentage
            probeInfo.progress((int) Math.floor(machine.heat / 100), 100, probeInfo.defaultProgressStyle().prefix("Heat: ").suffix("%").filledColor(0xffd62b1e).alternateFilledColor(0xffd62b1e));

            //Progress bar -- only show when the machine has work
            if (!machine.inputSlotA.isEmpty() || !machine.inputSlotB.isEmpty()) {
                progressBar(probeInfo, Math.min(machine.progress / 40, 100), 0xffaaaaaa, 0xff888888);
            }

            //Energy consumption
            if (mode == ProbeMode.EXTENDED) {
                textPrefixed(probeInfo, "{*topaddons:consumption*}", "15 EU/t");
            }
        }

        if (tile instanceof TileEntityCentrifuge) {
            TileEntityCentrifuge machine = (TileEntityCentrifuge) tile;
            //Heat percentage -- only show when the machine has work or when heat > 0
            if (!machine.inputSlot.isEmpty() || machine.heat > 0) {
                probeInfo.progress(machine.heat, machine.workheat, probeInfo.defaultProgressStyle().filledColor(0xffd62b1e).alternateFilledColor(0xffd62b1e).showText(false).height(8));
            }
        }

        if (tile instanceof TileEntityBlastFurnace) {
            TileEntityBlastFurnace machine = (TileEntityBlastFurnace) tile;
            //Heat percentage -- only show when the machine has work or when heat > 0
            if (!machine.inputSlot.isEmpty()) {
                progressBar(probeInfo, (int) (machine.getGuiValue("progress") * 100), 0xffaaaaaa, 0xff888888);
            }

            if (!machine.inputSlot.isEmpty() || machine.heat > 0) {
                probeInfo.progress(machine.heat, TileEntityBlastFurnace.maxHeat, probeInfo.defaultProgressStyle().filledColor(0xffd62b1e).alternateFilledColor(0xffd62b1e).showText(false).height(8));
            }

        }

        if (tile instanceof TileEntityMiner) {
            TileEntityMiner miner = (TileEntityMiner) tile;
            if (miner.drillSlot.isEmpty()) {
                probeInfo.text(TextStyleClass.ERROR + "{*topaddons.ic2:miner_no_drill*}");
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.ic2:miner_drill*}: ").item(miner.drillSlot.get());
            }

            if (miner.pipeSlot.isEmpty()) {
                probeInfo.text(TextStyleClass.ERROR + "{*topaddons.ic2:miner_no_pipes*}");
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.ic2:miner_pipes*}: ").item(miner.pipeSlot.get());
            }

            if (miner.scannerSlot.isEmpty()) {
                textPrefixed(probeInfo, "{*topaddons.ic2:miner_scanner*}", "{*topaddons:none*}");
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.ic2:miner_scanner*}: ").item(miner.scannerSlot.get()).text(miner.scannerSlot.get().getDisplayName());
            }
        }

        if (tile instanceof TileEntityAdvMiner) {
            TileEntityAdvMiner miner = (TileEntityAdvMiner) tile;
            if (miner.scannerSlot.isEmpty()) {
                probeInfo.text(TextStyleClass.ERROR + "{*topaddons.ic2:miner_no_scanner*}");
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.ic2:miner_scanner*}: ").item(miner.scannerSlot.get()).text(miner.scannerSlot.get().getDisplayName());
            }
        }

        if (tile instanceof TileEntityElectricBlock) {
            TileEntityElectricBlock tileElectric = (TileEntityElectricBlock) tile;
            //Storage block redstone mode
            if (mode == ProbeMode.EXTENDED) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(Items.REDSTONE))
                        .text(tileElectric.getRedstoneMode().replaceFirst(".+:\\s", ""));
            }
            //Maximum EU/t
            textPrefixed(probeInfo, "{*topaddons.ic2:max_output*}", new DecimalFormat("##.#").format(tileElectric.getOutputEnergyUnitsPerTick()) + " EU/t");
        }

        if (tile instanceof IKineticSource) {
            textPrefixed(probeInfo, "{*topaddons.ic2:buffer*}", new DecimalFormat("##.#").format(((IKineticSource) tile).maxrequestkineticenergyTick(((TileEntityBlock) tile).getFacing())) + " kU");
        }

        if (tile instanceof TileEntitySolarGenerator) {
            if (((TileEntitySolarGenerator) tile).skyLight == 0F) {
                probeInfo.text(TextFormatting.RED + "{*topaddons.ic2:no_sky*}");
            }
        }

        if (tile instanceof TileEntitySolarDestiller) {
            if (TileEntitySolarGenerator.getSkyLight(world, data.getPos()) == 0F) {
                probeInfo.text(TextFormatting.RED + "{*topaddons.ic2:no_sky*}");
            }
        }

        if (tile instanceof TileEntityTeleporter) {
            BlockPos pos = ((TileEntityTeleporter) tile).getTarget();
            textPrefixed(probeInfo, "{*topaddons.ic2:destination*}", ((TileEntityTeleporter) tile).hasTarget() ? String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()) : "none");
        }

        if (tile instanceof TileEntityTerra) {
            if (!((TileEntityTerra) tile).tfbpSlot.isEmpty()) {
                textPrefixed(probeInfo, "{*topaddons.ic2:blueprint*}", ((TileEntityTerra) tile).tfbpSlot.get().getDisplayName().substring(7));
            } else {
                textPrefixed(probeInfo, "{*topaddons.ic2:blueprint*}", "{*topaddons:none*}");
            }
        }

        if (tile instanceof TileEntityHeatSourceInventory) {
            textPrefixed(probeInfo, "{*topaddons.ic2:transmitting*}", ((TileEntityHeatSourceInventory) tile).gettransmitHeat() + " hU");
            textPrefixed(probeInfo, "{*topaddons.ic2:buffer*}", ((TileEntityHeatSourceInventory) tile).getHeatBuffer() + " hU");
            textPrefixed(probeInfo, "{*topaddons.ic2:max_transfer*}", ((TileEntityHeatSourceInventory) tile).getMaxHeatEmittedPerTick() + " hU");
        }

        if (tile instanceof TileEntityFermenter) {
            TileEntityFermenter fermenter = (TileEntityFermenter) tile;
            probeInfo.progress(Math.round(100 * fermenter.getGuiValue("heat")), 100, new ProgressStyleTOPAddonGrey().prefix("Conversion: ").suffix("%").alternateFilledColor(0xFFE12121).filledColor(0xFF871414));
            probeInfo.progress(Math.round(100 * fermenter.getGuiValue("progress")), 100, new ProgressStyleTOPAddonGrey().prefix("Waste: ").suffix("%").alternateFilledColor(0xFF0E760E).filledColor(0xFF084708));
        }

        if (tile instanceof TileEntityBaseGenerator) {
            if (OUTPUTS.containsKey(tile.getClass())) {
                textPrefixed(probeInfo, "{*topaddons:generating*}", new DecimalFormat("#.##").format((double) OUTPUTS.get(tile.getClass()).apply((TileEntityBaseGenerator) tile)) + " EU/t");
            }
        }

        if (tile instanceof TileEntityStirlingGenerator) {
            //EU Production
            textPrefixed(probeInfo, "{*topaddons:generating*}", new DecimalFormat("##.##").format(((TileEntityStirlingGenerator) tile).production) + " EU/t");
        }

        if (tile instanceof TileEntityKineticGenerator) {
            //EU Production
            textPrefixed(probeInfo, "{*topaddons:generating*}", new DecimalFormat("##.##").format(((TileEntityKineticGenerator) tile).getproduction()) + " EU/t");
        }

        if (tile instanceof IReactor || (tile instanceof IReactorChamber && ((IReactorChamber) tile).getReactorInstance() != null)) {
            IReactor reactor = tile instanceof IReactorChamber ? ((IReactorChamber) tile).getReactorInstance() : (IReactor) tile;

            if (reactor.isFluidCooled()) {
                textPrefixed(probeInfo, "{*topaddons.ic2:heat_production*}", new DecimalFormat("#.##").format(((TileEntityNuclearReactorElectric) reactor.getCoreTe()).EmitHeat) + " hU/t");
            } else {
                textPrefixed(probeInfo, "{*topaddons:generating*}", new DecimalFormat("#.##").format(reactor.getReactorEUEnergyOutput()) + " EU/t");
            }

            final float ratio = 1.0F * reactor.getHeat() / reactor.getMaxHeat();
            final int color = Util.blendColors(0xff00bc00, 0xffed190d, Math.min(1, ratio));
            probeInfo.progress(reactor.getHeat(), Math.max(reactor.getHeat(), reactor.getMaxHeat()), probeInfo.defaultProgressStyle().suffix("\u00b0C").filledColor(color).alternateFilledColor(color));
        }

        if (tile instanceof TileEntityChargepadBlock && tile.getDistanceSq(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) <= 0.75D) {
            IProbeInfo hori = probeInfo.horizontal();
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof IElectricItem) {
                        hori.item(stack);
                    }
                }
            }
        }

        if (tile instanceof TileEntityScanner) {
            TileEntityScanner scanner = (TileEntityScanner) tile;
            if (!scanner.isEmpty()) {
                textPrefixed(probeInfo, "{*topaddons.ic2:uu_scanning*}", scanner.inputSlot.get().getDisplayName());
                progressBar(probeInfo, scanner.progress / 33, 0xffaaaaaa, 0xff888888);
            }
        }

        if (tile instanceof TileEntityPatternStorage) {
            textPrefixed(probeInfo, "{*topaddons.ic2:uu_stored_patterns*}", String.valueOf(((TileEntityPatternStorage) tile).getPatterns().size()));
        }

        if (tile instanceof TileEntityReplicator) {
            TileEntityReplicator replicator = (TileEntityReplicator) tile;

            if (replicator.getMode() != TileEntityReplicator.Mode.STOPPED) {
                int multiplier = 1;
                char prefix = Character.MIN_VALUE;
                if (replicator.patternUu < 0.002) {
                    multiplier = 1000000;
                    prefix = '\u00b5';
                } else if (replicator.patternUu < 5) {
                    multiplier = 1000;
                    prefix = 'm';
                }

                textPrefixed(probeInfo, "{*topaddons.ic2:uu_replicating*}", replicator.pattern.getDisplayName());
                textPrefixed(probeInfo, "{*topaddons.ic2:mode*}", "{*" + (replicator.getMode() == TileEntityReplicator.Mode.SINGLE ? "ic2.Replicator.gui.info.single" : "ic2.Replicator.gui.info.repeat") + "*}");
                probeInfo.progress((int) (replicator.uuProcessed * multiplier), (int) (replicator.patternUu * multiplier), probeInfo.defaultProgressStyle().suffix(" " + prefix + 'B'));
                //TODO micro, milli formatting for bucket and eu
            }
        }

        if (tile instanceof TileEntityMatter) {
            TileEntityMatter tileMatter = (TileEntityMatter) tile;
            if (tileMatter.amplificationIsAvailable()) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.ic2:uu_amplifier*}:").item(tileMatter.amplifierSlot.get());
            }
        }

        if (tile instanceof TileEntityPersonalChest) {
            textPrefixed(probeInfo, "{*topaddons.ic2:owner*}", ((TileEntityPersonalChest) tile).getOwner() != null ? ((TileEntityPersonalChest) tile).getOwner().getName() : "");
        }

        if (tile instanceof TileEntityFluidDistributor) {
            probeInfo.text(TextStyleClass.LABEL + "{*ic2.FluidDistributor.gui.mode.info*} " + TextStyleClass.INFO + (((TileEntityFluidDistributor) tile).getActive() ? "{*ic2.FluidDistributor.gui.mode.concentrate*}" : "{*ic2.FluidDistributor.gui.mode.distribute*}"));
        }

        if (tile instanceof TileEntityFluidRegulator) {
            TileEntityFluidRegulator regulator = (TileEntityFluidRegulator) tile;
            textPrefixed(probeInfo, "{*topaddons.ic2:mb_rate*}", regulator.getoutputmb() + " mB" + regulator.getmodegui());
        }

        if (tile instanceof TileEntitySteamGenerator) {
            TileEntitySteamGenerator generator = (TileEntitySteamGenerator) tile;
            textPrefixed(probeInfo, "Output Fluid", "{*" + generator.getOutputFluidName() + "*}");
            textPrefixed(probeInfo, "Output Rate", generator.getOutputMB() + " mB/t");
            probeInfo.progress((int) generator.getCalcification(), 100, probeInfo.defaultProgressStyle()
                    .backgroundColor(0xff007dfd)
                    .filledColor(0xffbebfbe)
                    .alternateFilledColor(0xffbebfbe)
                    .prefix("Calcification: ")
                    .suffix(new DecimalFormat("##.#").format(generator.getCalcification()) + "%")
                    .numberFormat(NumberFormat.NONE));
        }

        if (tile instanceof TileEntityMagnetizer) {
            if (!BlockIC2Fence.hasMetalShoes(player)) {
                probeInfo.text(TextStyleClass.OBSOLETE + "{*ic2.Magnetizer.gui.noMetalShoes*}");
            }
        }

        if (tile instanceof TileEntityBlock && mode == ProbeMode.EXTENDED) {
            if (((TileEntityBlock) tile).hasComponent(Energy.class)) {
                Energy energy = ((TileEntityBlock) tile).getComponent(Energy.class);
                if (!energy.getSourceDirs().isEmpty()) {
                    textPrefixed(probeInfo, "{*topaddons.ic2:power_tier*}", String.valueOf(energy.getSourceTier()));
                } else if (!energy.getSinkDirs().isEmpty()) {
                    textPrefixed(probeInfo, "{*topaddons.ic2:power_tier*}", String.valueOf(energy.getSinkTier()));
                }
            }
        }

        if (tile instanceof ICropTile) {
            ICropTile crop = (ICropTile) tile;
            if (crop.getCrop() != null) {
                textPrefixed(probeInfo, "{*topaddons.forestry:maturity*}", TextStyleClass.WARNING + String.valueOf(100 * crop.getCurrentSize() / crop.getCrop().getMaxSize()) + "%");
                if (hodlingCropnalyzer(player)) {
                    textPrefixed(probeInfo, "{*topaddons.ic2:crop_nutrient*}", String.format("%d/%d", crop.getStorageNutrients(), 100));
                    textPrefixed(probeInfo, "{*topaddons.ic2:crop_water*}", String.format("%d/%d", crop.getStorageWater(), 200));
                    textPrefixed(probeInfo, "{*topaddons.ic2:crop_weedex*}", String.format("%d/%d", crop.getStorageWeedEX(), 100));
                    textPrefixed(probeInfo, "{*topaddons.ic2:crop_growth*}", String.format("%d/%d", crop.getGrowthPoints(), 300));
                } else if (crop.getScanLevel() > 0) {
                    textPrefixed(probeInfo, "{*topaddons.ic2:crop_name*}", "{*" + crop.getCrop().getUnlocalizedName() + "*}");
                    if (crop.getScanLevel() >= 2) {
                        textPrefixed(probeInfo, "Tier", Util.getRomanNumeral(crop.getCrop().getProperties().getTier()));
                    }
                    if (crop.getScanLevel() >= 4) {
                        textPrefixed(probeInfo, "Growth", String.valueOf(crop.getStatGrowth()));
                        textPrefixed(probeInfo, "Gain", String.valueOf(crop.getStatGain()));
                        textPrefixed(probeInfo, "Resis.", String.valueOf(crop.getStatResistance()));
                    }
                }
            }
        }

        if (tile instanceof TileEntityTradeOMat) {
            TileEntityTradeOMat trader = (TileEntityTradeOMat) tile;
            if (trader.getOwner() != null) {
                textPrefixed(probeInfo, "{*topaddons.ic2:owner*}", trader.getOwner().getName());
            }
        }
    }

    private boolean hodlingCropnalyzer(EntityPlayer player) {
        for (EnumHand enumHand : EnumHand.values()) {
            ItemStack heldStack = player.getHeldItem(enumHand);
            if (heldStack.getItem() == ItemName.cropnalyzer.getInstance() && heldStack.getItemDamage() < heldStack.getMaxDamage()) {
                return true;
            }
        }

         return false;
    }

    public static void euBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(energy, capacity, probeInfo.defaultProgressStyle()
                .suffix("EU")
                .filledColor(Config.rfbarFilledColor)
                .alternateFilledColor(Config.rfbarAlternateFilledColor)
                .borderColor(Config.rfbarBorderColor)
                .numberFormat(NumberFormat.COMPACT));
    }
}

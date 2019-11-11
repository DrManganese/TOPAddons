package io.github.drmanganese.topaddons.addons.forestry.tiles;

import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmInventory;
import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.forestry.ElementFarm;
import io.github.drmanganese.topaddons.styles.Styles;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidTank;

import forestry.api.farming.FarmDirection;
import forestry.api.farming.IFarmLogic;
import forestry.farming.logic.*;
import forestry.farming.multiblock.IFarmControllerInternal;
import forestry.farming.multiblock.MultiblockLogicFarm;
import forestry.farming.tiles.TileFarm;
import forestry.farming.tiles.TileFarmGearbox;
import forestry.farming.tiles.TileFarmValve;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TileFarmInfo implements ITileInfo<TileFarm> {

    private static final Map<Class<? extends IFarmLogic>, Integer> SOIL_COLORS = new HashMap<>();

    static {
        SOIL_COLORS.put(FarmLogicOrchard.class, 0x99221002);
        SOIL_COLORS.put(FarmLogicGourd.class, 0x99734b2d);
        SOIL_COLORS.put(FarmLogicSucculent.class, 0x99dbd3a0);
        SOIL_COLORS.put(FarmLogicCocoa.class, 0x9957431a);
        SOIL_COLORS.put(FarmLogicArboreal.class, 0x99221002);
        SOIL_COLORS.put(FarmLogicReeds.class, 0x99dbd3a0);
        SOIL_COLORS.put(FarmLogicMushroom.class, 0x996f6369);
        SOIL_COLORS.put(FarmLogicPeat.class, 0x99221b17);
        SOIL_COLORS.put(FarmLogicInfernal.class, 0x99544033);
        SOIL_COLORS.put(FarmLogicEnder.class, 0x99dddfa5);
        SOIL_COLORS.put(FarmLogicCrops.class, 0x99734b2d);
    }

    public static void addFertilizerInfo(IProbeInfo probeInfo, ProbeMode probeMode, EntityPlayer player, ISidedInventory tile, Function<Integer, Integer> getStoredFertilizerScaled, int fertilizerSlot) {
        if (probeMode == ProbeMode.EXTENDED) {
            final int fertilizer = getStoredFertilizerScaled.apply(16);
            if (fertilizer > 0) {
                IProbeInfo hori = probeInfo.horizontal(Styles.horiCentered());
                if (!tile.getStackInSlot(fertilizerSlot).isEmpty()) {
                    hori.item(tile.getStackInSlot(fertilizerSlot));
                }
                hori.progress(fertilizer, 16, Styles.machineProgress(player).filledColor(0xffd90f00).alternateFilledColor(0xffcc0e00).width(81).showText(false));
            }
        }
    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileFarm tile) {
        final MultiblockLogicFarm logic = tile.getMultiblockLogic();
        final IFarmControllerInternal controller = logic.getController();
        if (logic.isConnected()) {
            if (!(tile instanceof TileFarmGearbox)) {
                TileFarmGearbox gearBox = (TileFarmGearbox) controller.getComponents().stream()
                        .filter(TileFarmGearbox.class::isInstance)
                        .findFirst().orElse(null);

                if (gearBox != null) {
                    if (Config.getDefaultConfig().getRFMode() == 1) {
                        probeInfo.progress(gearBox.getEnergyManager().getEnergyStored(), gearBox.getEnergyManager().getMaxEnergyStored(),
                                probeInfo.defaultProgressStyle()
                                        .suffix("RF")
                                        .filledColor(Config.rfbarFilledColor)
                                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                        .borderColor(Config.rfbarBorderColor)
                                        .numberFormat(Config.rfFormat));
                    } else {
                        probeInfo.text(TextStyleClass.PROGRESS + "RF: " + ElementProgress.format(gearBox.getEnergyManager().getEnergyStored(), Config.rfFormat, "RF"));
                    }
                }
            }

            addFertilizerInfo(probeInfo, probeMode, player, tile, controller::getStoredFertilizerScaled, 20);

            if (!(tile instanceof TileFarmValve)) {
                final IFluidTank tank = controller.getTankManager().getTank(0);
                FluidCapInfo.INSTANCE.gauge(probeInfo, probeMode, player, tank.getFluid(), tank.getCapacity(), "Farm tank");
            }

            EnumFacing facing = player.getHorizontalFacing();
            NonNullList<ItemStack> farmIcons = NonNullList.withSize(5, ItemStack.EMPTY);
            int[] farmColors = new int[4];
            ItemStack socket = controller.getSocket(0);
            farmIcons.set(4, !socket.isEmpty() ? socket : new ItemStack(Blocks.BARRIER));
            for (int i = 0; i < 4; i++) {
                farmIcons.set(i, controller.getFarmLogic(FarmDirection.getFarmDirection(facing)).getIconItemStack());
                farmColors[i] = SOIL_COLORS.getOrDefault(controller.getFarmLogic(FarmDirection.getFarmDirection(facing)).getClass(), 0x55000000);
                facing = facing.rotateY();
            }

            probeInfo.element(new ElementFarm(ElementSync.getId("farm", player), facing.getName(), farmIcons, farmColors));
        }
    }
}

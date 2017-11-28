package io.github.drmanganese.topaddons.addons.subaddons.binniesmods;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import io.github.drmanganese.topaddons.Util;
import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.elements.binnies.ElementFlowerColor;

import java.util.Collections;
import java.util.List;

import binnie.botany.api.gardening.IBlockSoil;
import binnie.botany.api.genetics.IFlower;
import binnie.botany.api.genetics.IFlowerColor;
import binnie.botany.blocks.BlockFlower;
import binnie.botany.blocks.BlockSoil;
import binnie.botany.items.ItemSoilMeter;
import binnie.botany.tile.TileEntityFlower;
import binnie.core.genetics.AlleleHelper;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

public class SubAddonBotany extends AddonBlank {

    @ObjectHolder("botany:soil_meter")
    private static final ItemSoilMeter SOIL_METER = null;

    @ObjectHolder("botany:flower")
    private static final BlockFlower FLOWER_BLOCK = null;

    @ObjectHolder("botany:soil")
    private static final BlockSoil SOIL = null;

    @SuppressWarnings("UnusedReturnValue")
    private IProbeInfo addColorBox(IProbeInfo probeInfo, IFlowerColor color, EntityPlayer player) {
        return probeInfo.element(new ElementFlowerColor(getElementId(player, "flowerColor"), color.getColor(false))).text(color.getColorName());
    }

    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileEntityFlower) {
            IFlower flower = ((TileEntityFlower) tile).getFlower();

            if (flower.isAnalyzed()) {
                textPrefixed(probeInfo, "Age", Integer.toString(flower.getAge()));

                IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2))
                        .text(TextStyleClass.LABEL + "Color:");
                addColorBox(hori, flower.getGenome().getPrimaryColor(), player);
                hori.text("and");
                addColorBox(hori, flower.getGenome().getSecondaryColor(), player);

                hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(2))
                        .text(TextStyleClass.LABEL + "Stem:");
                addColorBox(hori, flower.getGenome().getStemColor(), player);

                if (mode == ProbeMode.EXTENDED) {
                    textPrefixed(probeInfo, "Temp. Tolerance", AlleleHelper.getAllele(flower.getGenome().getToleranceTemperature()).getAlleleName());
                    textPrefixed(probeInfo, "Moist. Tolerance", AlleleHelper.getAllele(flower.getGenome().getToleranceMoisture()).getAlleleName());
                    textPrefixed(probeInfo, "pH Tolerance", AlleleHelper.getAllele(flower.getGenome().getTolerancePH()).getAlleleName());
                }
            } else if (mode == ProbeMode.EXTENDED) {
                probeInfo.text(TextStyleClass.OBSOLETE + "{*for.gui.unknown*}");
            }

            IBlockState blockStateBelow = world.getBlockState(data.getPos().down());

            //noinspection ConstantConditions
            if (Util.isHoldingItem(player, SOIL_METER) && blockStateBelow.getBlock() == SOIL) {
                IBlockSoil soil = (IBlockSoil) blockStateBelow.getBlock();

                IProbeInfo soilInfo = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(
                        MapColor.DIRT.colorValue + 0xff000000))
                        .item(SOIL.getPickBlock(blockStateBelow, null, world, data.getPos().down(), player))
                        .vertical();
                textPrefixed(soilInfo, "{*botany.moisture*}", soil.getMoisture(world, data.getPos().down()).getLocalisedName(true));
                textPrefixed(soilInfo, "{*botany.ph*}", soil.getPH(world, data.getPos().down()).getLocalisedName(true));
            }
        }

        if (blockState.getBlock() instanceof IBlockSoil && Util.isHoldingItem(player, SOIL_METER)) {
            IBlockSoil soil = (IBlockSoil) blockState.getBlock();
            textPrefixed(probeInfo, "{*botany.moisture*}", soil.getMoisture(world, data.getPos()).getLocalisedName(true));
            textPrefixed(probeInfo, "{*botany.ph*}", soil.getPH(world, data.getPos()).getLocalisedName(true));
            if (soil.resistsWeeds(world, data.getPos())) {
                probeInfo.text(TextStyleClass.OK + "{*botany.soil.weedkiller*}");
            }
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, blockState, data) -> {
            //noinspection ConstantConditions
            if (blockState.getBlock() == FLOWER_BLOCK) {
                TileEntityFlower tile = (TileEntityFlower) world.getTileEntity(data.getPos());
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .item(tile.getItemStack())
                            .vertical()
                            .text(tile.getItemStack().getDisplayName())
                            .text(MODNAME + Tools.getModName(FLOWER_BLOCK));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(tile.getItemStack())
                            .text(tile.getItemStack().getDisplayName());
                }

                return true;
            }

            return false;
        });
    }
}

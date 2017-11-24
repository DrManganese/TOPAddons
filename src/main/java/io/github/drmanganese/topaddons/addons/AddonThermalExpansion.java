package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Colors;

import cofh.thermalexpansion.block.TileRSControl;
import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import cofh.thermalexpansion.block.machine.TileMachineBase;
import cofh.thermalexpansion.block.storage.TileCell;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

@TOPAddon(dependency = "thermalexpansion")
public class AddonThermalExpansion extends AddonBlank {

    // Progress bar colours for TE machines, each pair is a tier (Basic->Resonant)
    private static final int[][] PROGRESS_COLOURS = {
            {0xffaaaaaa, 0xff9f9f9f},
            {0xff8e9a95, 0xff7c8781},
            {0xffd6d031, 0xffc3b514},
            {0xffd96020, 0xffbf4413},
            {0xff318080, 0xff245c5c}
    };

    @Override
    public void addFluidColors() {
        Colors.FLUID_NAME_COLOR_MAP.put("crude_oil", 0xff232323);
        Colors.FLUID_NAME_COLOR_MAP.put("redstone", 0xff8a0a09);
        Colors.FLUID_NAME_COLOR_MAP.put("glowstone", 0xffc6af07);
        Colors.FLUID_NAME_COLOR_MAP.put("ender", 0xff11605e);
        Colors.FLUID_NAME_COLOR_MAP.put("pyrotheum", 0xffddbb1e);
        Colors.FLUID_NAME_COLOR_MAP.put("cryotheum", 0xff0ebeca);
        Colors.FLUID_NAME_COLOR_MAP.put("aerotheum", 0xff787654);
        Colors.FLUID_NAME_COLOR_MAP.put("petrotheum", 0xff191310);
        Colors.FLUID_NAME_COLOR_MAP.put("mana", 0xff095277);
        Colors.FLUID_NAME_COLOR_MAP.put("steam", 0xff838582);
        Colors.FLUID_NAME_COLOR_MAP.put("creosote", 0xff515502);
        Colors.FLUID_NAME_COLOR_MAP.put("coal", 0xff393939);
        Colors.FLUID_NAME_COLOR_MAP.put("refined_oil", 0xffc36a1f);
        Colors.FLUID_NAME_COLOR_MAP.put("refined_fuel", 0xffc3a515);
        Colors.FLUID_NAME_COLOR_MAP.put("sap", 0xff7e5215);
        Colors.FLUID_NAME_COLOR_MAP.put("syrup", 0xff863903);
        Colors.FLUID_NAME_COLOR_MAP.put("resin", 0xffb57c04);
        Colors.FLUID_NAME_COLOR_MAP.put("tree_oil", 0xffb49645);
        Colors.FLUID_NAME_COLOR_MAP.put("mushroom_stew", 0xffc8a98f);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileMachineBase) {
            TileMachineBase machine = (TileMachineBase) tile;
            // Progress bar with percentage and RF/t
            if (machine.isActive) {
                IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(5));
                progressBar(hori, machine.getScaledProgress(100), PROGRESS_COLOURS[machine.getLevel()][0], PROGRESS_COLOURS[machine.getLevel()][1]);
                hori.text(TextStyleClass.LABEL + "@ " + TextStyleClass.INFO + machine.getInfoEnergyPerTick() + " RF/t");
            }
        }

        if (tile instanceof TileDynamoBase) {
            TileDynamoBase dynamo = (TileDynamoBase) tile;
            // Dynamo power output
            if (dynamo.isActive) {
                textPrefixed(probeInfo, "Power output", String.format("%d RF/t", dynamo.getInfoEnergyPerTick()));
            }
        }

        if (tile instanceof TileCell) {
            TileCell cell = (TileCell) tile;
            // Maximum power in- and outputs with the blue and orange icons from the gui
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .icon(new ResourceLocation("cofh:textures/gui/elements/info_input.png"), 0, 0, 10, 10, probeInfo.defaultIconStyle().textureHeight(10).textureWidth(10).width(15).height(10))
                    .text(TextStyleClass.LABEL + String.format("%d RF/t", cell.amountRecv));
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .icon(new ResourceLocation("cofh:textures/gui/elements/info_output.png"), 0, 0, 10, 10, probeInfo.defaultIconStyle().textureHeight(10).textureWidth(10).width(15).height(10))
                    .text(TextStyleClass.LABEL + String.format("%d RF/t", cell.amountSend));
        }

        if (tile instanceof TileRSControl) {
            if (!(((TileRSControl) tile).redstoneControlOrDisable())) {
                probeInfo.text(TextFormatting.RED + "Disabled");
            }
        }
    }
}

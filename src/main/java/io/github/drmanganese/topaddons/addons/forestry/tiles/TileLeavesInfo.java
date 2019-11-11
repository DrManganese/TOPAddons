package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.core.utils.GeneticsUtil;

import java.text.DecimalFormat;

public class TileLeavesInfo implements ITileInfo<TileLeaves> {

    @GameRegistry.ObjectHolder("forestry:naturalist_helmet")
    public static Item SPECTACLES;

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileLeaves tile) {
        if (tile.hasFruit()) {
            if (tile.getRipeness() >= 1.0F) {
                probeInfo.text(TextStyleClass.OK + "Fully ripe");
            } else {
                InfoHelper.textPrefixed(probeInfo, "Ripeness", TextFormatting.YELLOW + DecimalFormat.getPercentInstance().format(tile.getRipeness()));
            }

            if (tile.isPollinated() && GeneticsUtil.hasNaturalistEye(player)) {
                probeInfo.text(TextStyleClass.OK + "Pollinated");
            }

            InfoHelper.textPrefixed(probeInfo, "Fruit", tile.getNanny().getGenome().getActiveAllele(EnumTreeChromosome.FRUITS).getAlleleName());
        }
    }
}

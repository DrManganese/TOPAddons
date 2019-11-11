package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.ProgressStyleForestryMultiColored;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.apiculture.tiles.TileBeeHousingBase;

//TODO show frames on apiary
public class TileBeeHousingInfo implements ITileInfo<TileBeeHousingBase> {

    private static String getSpecies(IBeeGenome genome) {
        StringBuilder builder = new StringBuilder()
                .append(genome.getPrimary().getAlleleName());
        if (!genome.getPrimary().equals(genome.getSecondary())) {
            builder.append(" - ").append(genome.getSecondary().getAlleleName());
        }
        return builder.toString();
    }

    @SuppressWarnings("ConstantConditions")
    public static void getInfo(IProbeInfo probeInfo, IBeeHousingInventory housing, IBeekeepingLogic logic) {
        final ItemStack queen = housing.getQueen();
        final int progress = logic.getBeeProgressPercent();

        if (!queen.isEmpty() && logic.getBeeProgressPercent() > 0) {
            final IBee queendividual = BeeManager.beeRoot.getMember(queen);
            probeInfo.horizontal(Styles.horiCentered())
                    .item(queen)
                    .progress(progress, 100, new ProgressStyleForestryMultiColored(progress));
            if (BeeManager.beeRoot.getType(queen) == EnumBeeType.QUEEN) {

                InfoHelper.textPrefixed(probeInfo, "Princess", getSpecies(queendividual.getGenome()));
                InfoHelper.textPrefixed(probeInfo, "Drone", getSpecies(queendividual.getMate()));
            }
        }

    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileBeeHousingBase tile) {
        getInfo(probeInfo, tile.getBeeInventory(), tile.getBeekeepingLogic());
    }
}

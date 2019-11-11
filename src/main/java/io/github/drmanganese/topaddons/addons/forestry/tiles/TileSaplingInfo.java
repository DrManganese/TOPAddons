package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.genetics.IAllele;
import forestry.arboriculture.genetics.alleles.AlleleFruits;
import forestry.arboriculture.tiles.TileSapling;

public class TileSaplingInfo implements ITileInfo<TileSapling> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileSapling tile) {

        ITree tree = tile.getTree();
        if (tree != null && probeMode == ProbeMode.EXTENDED) {
            IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff000000 + MapColor.WOOD.colorValue));
            if (tree.isAnalyzed()) {
                ITreeGenome genome = tree.getGenome();

                if (!tree.isPureBred(EnumTreeChromosome.SPECIES)) {
                    InfoHelper.textPrefixed(vert, "Hybrid", genome.getPrimary().getAlleleName() + " - " + genome.getSecondary().getAlleleName(), TextFormatting.BLUE);
                }

                InfoHelper.textPrefixed(vert, "Sappiness", genome.getActiveAllele(EnumTreeChromosome.SAPPINESS).getAlleleName(), TextFormatting.GOLD);
                InfoHelper.textPrefixed(vert, "Maturation", genome.getActiveAllele(EnumTreeChromosome.MATURATION).getAlleleName(), TextFormatting.RED);
                InfoHelper.textPrefixed(vert, "Height", genome.getActiveAllele(EnumTreeChromosome.HEIGHT).getAlleleName(), TextFormatting.LIGHT_PURPLE);
                InfoHelper.textPrefixed(vert, "Girth", String.format("%sx%s", tree.getGirth(), tree.getGirth()), TextFormatting.AQUA);
                InfoHelper.textPrefixed(vert, "Fertility", genome.getActiveAllele(EnumTreeChromosome.FERTILITY).getAlleleName(), TextFormatting.YELLOW);
                InfoHelper.textPrefixed(vert, "Yield", genome.getActiveAllele(EnumTreeChromosome.YIELD).getAlleleName());
                InfoHelper.textPrefixed(vert, "Carbonization", String.valueOf(genome.getSecondary().getWoodProvider().getCarbonization()), TextFormatting.DARK_GRAY);
                if (genome.getFireproof()) {
                    probeInfo.text(TextFormatting.RED + "Fireproof");
                }

                IAllele fruit = genome.getActiveAllele(EnumTreeChromosome.FRUITS);
                if (fruit != AlleleFruits.fruitNone) {
                    InfoHelper.textPrefixed(vert, "Fruit", (!tree.canBearFruit() ? TextFormatting.STRIKETHROUGH : "") + genome.getFruitProvider().getDescription(), TextFormatting.GREEN);
                }
            } else {
                vert.text(TextStyleClass.OBSOLETE + "Not Analyzed");
            }
        }
    }
}

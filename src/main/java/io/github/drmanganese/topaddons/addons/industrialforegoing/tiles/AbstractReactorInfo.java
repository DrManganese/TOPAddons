package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.generator.AbstractReactorTile;

import java.text.DecimalFormat;

/**
 * Reactor efficiency and production rate.
 */
public class AbstractReactorInfo implements ITileInfo<AbstractReactorTile> {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.#%");

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, AbstractReactorTile tile) {
        if (!tile.isPaused()) {
            InfoHelper.textPrefixed(probeInfo, "Efficiency", FORMAT.format(Math.max(0, tile.getEfficiency())));
            probeInfo.text(TextStyleClass.LABEL + "Producing " + TextStyleClass.INFO + tile.getProducedAmountItem() + TextStyleClass.LABEL + " mB/item");
        }
    }
}

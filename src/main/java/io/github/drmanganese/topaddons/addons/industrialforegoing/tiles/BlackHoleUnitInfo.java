package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementSimpleProgressCentered;
import io.github.drmanganese.topaddons.styles.SimpleProgressStyle;
import io.github.drmanganese.topaddons.util.Formatting;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.misc.BlackHoleUnitTile;

import java.text.DecimalFormat;

/**
 * Total count, stack count and fill percentage.
 */
public class BlackHoleUnitInfo implements ITileInfo<BlackHoleUnitTile> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, BlackHoleUnitTile tile) {
        final ItemStack stack = tile.getItemStack().copy();
        if (!stack.isEmpty()) {
            final int amount = tile.getAmount();
            final int maxSize = stack.getMaxStackSize();
            final int remainder = amount % maxSize;
            final int quotient = (amount - remainder) / maxSize;

            stack.setCount(1);
            IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff555555).spacing(0));

            vert.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .item(stack)
                    .vertical(probeInfo.defaultLayoutStyle().spacing(0))
                    .text(stack.getDisplayName())
                    .text(TextStyleClass.LABEL + "[" + (amount >= maxSize ? Formatting.SISuffix(quotient, "#") + "x" + maxSize + " + " : "") + remainder + "]");

            final String text = Formatting.SISuffix(amount) + " - " + new DecimalFormat("###.##").format(100.0F * amount / Integer.MAX_VALUE) + "%";

            vert.element(new ElementSimpleProgressCentered(ElementSync.getId("centered_progress", player),
                    amount, Integer.MAX_VALUE,
                    new SimpleProgressStyle().borderColor(0xff555555).fillColor(0xff454545).alternateFillColor(0xff353535),
                    text));
        }
    }
}

package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementSimpleProgressCentered;
import io.github.drmanganese.topaddons.styles.ProgressStyles;
import io.github.drmanganese.topaddons.util.Formatting;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.buuz135.industrial.tile.block.BlackHoleUnitBlock;
import com.buuz135.industrial.tile.misc.BlackHoleControllerTile;

import java.text.DecimalFormat;

/**
 * Total count and fill percentage for each BHU.
 */
public class BlackHoleUnitControllerInfo implements ITileInfo<BlackHoleControllerTile> {

    @GameRegistry.ObjectHolder("industrialforegoing:black_hole_unit")
    private static Block BLACK_HOLE_UNIT;

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, BlackHoleControllerTile tile) {
        IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff555555).spacing(0));
        for (int i = 0; i < tile.getStorage().getSlots(); i++) {

            ItemStack stack = tile.getStorage().getStackInSlot(i);
            if (!stack.isEmpty()) {
                ((BlackHoleUnitBlock) BLACK_HOLE_UNIT).getItemStack(stack);

                final ItemStack innerStack = ((BlackHoleUnitBlock) BLACK_HOLE_UNIT).getItemStack(stack).copy();
                if (!innerStack.isEmpty()) {
                    final int amount = ((BlackHoleUnitBlock) BLACK_HOLE_UNIT).getAmount(stack);

                    innerStack.setCount(1);
                    final String text = Formatting.SISuffix(amount) + " - " + new DecimalFormat("#.##").format(100.0F * amount / Integer.MAX_VALUE) + "%";
                    IProbeInfo innerVert = vert.vertical(probeInfo.defaultLayoutStyle().spacing(0));
                    innerVert.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(innerStack, probeInfo.defaultItemStyle().height(18))
                            .element(new ElementSimpleProgressCentered(ElementSync.getId("centered_progress", player),
                                    amount, Integer.MAX_VALUE,
                                    ProgressStyles.SIMPLE_PROGRESS,
                                    text));
                }
            }
        }

    }
}

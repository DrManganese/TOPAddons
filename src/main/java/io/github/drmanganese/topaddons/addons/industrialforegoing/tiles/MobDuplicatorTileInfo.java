package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.ObjectHolders;
import io.github.drmanganese.topaddons.addons.industrialforegoing.IndustrialForegoingAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobDuplicatorTile;
import com.buuz135.industrial.item.MobImprisonmentToolItem;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MobDuplicatorTileInfo implements ITileInfo<MobDuplicatorTile> {

    private static final ILayoutStyle LAYOUT_STYLE = new LayoutStyle().alignment(ElementAlignment.ALIGN_TOPLEFT).spacing(4);

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull MobDuplicatorTile tile) {
        IndustrialForegoingAddon.getFirstItemHandlerFromTile(tile)
            .map(iItemHandler -> IntStream.range(0, iItemHandler.getSlots()).mapToObj(iItemHandler::getStackInSlot))
            .orElse(Stream.empty())
            .filter(itemStack -> itemStack.getItem() == ObjectHolders.IndustrialForegoing.MOB_IMPRISONMENT_TOOL)
            .forEach(itemStack -> showMobImprisonmentItem(probeInfo, world, itemStack));
    }

    private static void showMobImprisonmentItem(IProbeInfo probeInfo, Level world, ItemStack itemStack) {
        final MobImprisonmentToolItem mobImprisonmentToolItem = (MobImprisonmentToolItem) ObjectHolders.IndustrialForegoing.MOB_IMPRISONMENT_TOOL;
        if (mobImprisonmentToolItem.containsEntity(itemStack)) {
            final Entity mob = mobImprisonmentToolItem.getEntityFromStack(itemStack, world, false, false);
            probeInfo
                .horizontal(LAYOUT_STYLE)
                .text(CompoundText.create().label("{*topaddons.industrialforegoing:entity*}: ").info(mob.getDisplayName()))
                .entity(mob);
        } else {
            probeInfo.text(CompoundText.createLabelInfo("{*topaddons.industrialforegoing:entity*}: ", "{*topaddons.forge:empty*}"));
        }
    }
}

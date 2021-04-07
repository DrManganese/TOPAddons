package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.ObjectHolders;
import io.github.drmanganese.topaddons.addons.industrialforegoing.IndustrialForegoingAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobDuplicatorTile;
import com.buuz135.industrial.item.MobImprisonmentToolItem;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MobDuplicatorTileInfo implements ITileInfo<MobDuplicatorTile> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull MobDuplicatorTile tile) {
        IndustrialForegoingAddon.getFirstItemHandlerFromTile(tile)
            .map(iItemHandler -> IntStream.range(0, iItemHandler.getSlots()).mapToObj(iItemHandler::getStackInSlot))
            .orElse(Stream.empty())
            .filter(itemStack -> itemStack.getItem() == ObjectHolders.IndustrialForegoing.MOB_IMPRISONMENT_TOOL)
            .forEach(itemStack -> showMobImprisonmentItem(probeInfo, world, itemStack));
    }

    private static void showMobImprisonmentItem(IProbeInfo probeInfo, World world, ItemStack itemStack) {
        final MobImprisonmentToolItem mobImprisonmentToolItem = (MobImprisonmentToolItem) ObjectHolders.IndustrialForegoing.MOB_IMPRISONMENT_TOOL;
        if (mobImprisonmentToolItem.containsEntity(itemStack)) {
            final Entity mob = mobImprisonmentToolItem.getEntityFromStack(itemStack, world, false);
            probeInfo
                .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_BOTTOMRIGHT).spacing(4))
                .text(CompoundText.create().label("{*topaddons.industrialforegoing:entity*}: ").info(mob.getDisplayName()))
                .entity(mob);
        } else {
            probeInfo.text(CompoundText.createLabelInfo("{*topaddons.industrialforegoing:entity*}: ", "{*topaddons.forge:empty*}"));
        }
    }
}

package io.github.drmanganese.topaddons.addons.storagedrawers.tiles;

import io.github.drmanganese.topaddons.addons.storagedrawers.StorageDrawersAddon;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.util.InfoHelper;
import io.github.drmanganese.topaddons.util.PlayerHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.chestContentsBorderColor;

public class DrawerInfo implements ITileInfo<TileEntityDrawers>, ITileConfigProvider {

    //∞
    private static final String INFINITY = "\u221e";

    private static final ILayoutStyle NO_SPACING = ILayoutStyle.createSpacing(0);
    private static final ILayoutStyle INVENTORY = ILayoutStyle.createSpacing(2).borderColor(chestContentsBorderColor);

    @Override
    public void getProbeConfig(IProbeConfig config, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        final TileEntityDrawers tile = Objects.requireNonNull((TileEntityDrawers) world.getTileEntity(data.getPos()));

        if (shouldShowInfo(PlayerHelper.getProbeMode(player), player) || hideInfoBecauseConcealed(tile))
            config.showChestContents(IProbeConfig.ConfigMode.NOT);
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull TileEntityDrawers tile) {
        if (!shouldShowInfo(probeMode, player) || hideInfoBecauseConcealed(tile)) return;

        final IDrawerGroup drawerGroup = tile.getGroup();
        final List<IDrawer> drawers = IntStream
            .range(0, drawerGroup.getDrawerCount())
            .mapToObj(drawerGroup::getDrawer)
            .filter(d -> !d.isEmpty())
            .collect(Collectors.toList());

        if (!drawers.isEmpty()) {
            final boolean hasVendingUpgrade = tile.getDrawerAttributes().isUnlimitedVending();
            final IProbeInfo vert = probeInfo.vertical(INVENTORY);
            drawers.forEach(drawer -> displayDrawerInfo(vert, drawer, hasVendingUpgrade));
        }

        // Only show stacks per slot when extended, regardless of alwaysShowExtended config
        if (probeMode == ProbeMode.EXTENDED) {
            final boolean hasUnlimitedStorage = tile.getDrawerAttributes().isUnlimitedStorage();
            final int storageMultiplier = tile.upgrades().getStorageMultiplier();
            final int stacksPerSlot = tile.getEffectiveDrawerCapacity() * storageMultiplier;
            InfoHelper.textPrefixed(probeInfo, "{*topaddons.storagedrawers:stack_limit*}", hasUnlimitedStorage ? INFINITY : stacksPerSlot + " (x" + storageMultiplier + ")");
        }
    }

    private static void displayDrawerInfo(IProbeInfo probeInfo, IDrawer drawer, Boolean hasVendingUpgrade) {
        final IProbeInfo horizontal = probeInfo.horizontal(NO_SPACING);
        final ItemStack stack = drawer.getStoredItemPrototype().copy();

        // Minimum of 0 to handle empty locked drawers
        stack.setCount(Math.max(1, drawer.getStoredItemCount()));
        horizontal.item(stack);

        final IProbeInfo vertical = horizontal.vertical(NO_SPACING);
        vertical.itemLabel(drawer.getStoredItemPrototype());
        vertical.text(
            CompoundText.create()
                .style(LABEL)
                .text(String.format("[%s]", hasVendingUpgrade ? INFINITY : getDetailedCount(drawer)))
        );
    }

    private static String getDetailedCount(IDrawer drawer) {
        final int count = drawer.getStoredItemCount();
        final int stackSize = drawer.getStoredItemStackSize();
        final int stacks = count / stackSize;
        final int remainder = count % stackSize;

        if (stackSize == 1 || count < stackSize) return String.valueOf(count);

        String detailedCount = String.format("%dx%d", stacks, stackSize);
        if (remainder > 0) detailedCount += String.format(" + %d", remainder);
        return detailedCount;
    }

    private static boolean hideInfoBecauseConcealed(TileEntityDrawers tile) {
        return tile.getDrawerAttributes().isConcealed() && StorageDrawersAddon.hideConcealed.get();
    }

    private static boolean shouldShowInfo(ProbeMode probeMode, PlayerEntity player) {
        return probeMode == ProbeMode.EXTENDED || Config.getSyncedBoolean(player, StorageDrawersAddon.alwaysShowExtended);
    }
}

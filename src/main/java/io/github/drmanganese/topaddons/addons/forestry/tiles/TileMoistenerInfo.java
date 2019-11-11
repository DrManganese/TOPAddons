package io.github.drmanganese.topaddons.addons.forestry.tiles;

import forestry.factory.inventory.InventoryMoistener;
import forestry.factory.tiles.TileMoistener;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.util.InfoHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileMoistenerInfo implements ITileInfo<TileMoistener> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileMoistener tile) {
        showSpeed(probeInfo, world, hitData);
        showProgress(probeInfo, player, tile);
    }

    private void showSpeed(IProbeInfo probeInfo, World world, IProbeHitData hitData) {
        int light = world.getLight(hitData.getPos().up());
        int speed;
        if (light > 11) {
            speed = 0;
        } else if (light >= 9) {
            speed = 1;
        } else if (light >= 7) {
            speed = 2;
        } else if (light >= 5) {
            speed = 3;
        } else {
            speed = 4;
        }
        InfoHelper.textPrefixed(probeInfo, "Speed", (speed == 0 ? TextStyleClass.WARNING.toString() : "") + speed);
    }

    private void showProgress(IProbeInfo probeInfo, EntityPlayer player, TileMoistener tile) {
        if (tile.isWorking()) {
            final ItemStack workStack = tile.getStackInSlot(InventoryMoistener.SLOT_WORKING);
            probeInfo
                    .horizontal(Styles.horiCentered())
                    .item(workStack)
                    .progress(
                            100 - tile.getConsumptionProgressScaled(100),
                            100,
                            Styles.machineProgress(player, "Decaying")
                                    .filledColor(0xff695d1c)
                                    .alternateFilledColor(0xff998728)
                                    .width(81)
                    );
        }

        if (tile.isProducing()) {
            final ItemStack productStack = tile.getStackInSlot(InventoryMoistener.SLOT_RESOURCE);
            probeInfo
                    .horizontal(Styles.horiCentered())
                    .item(productStack)
                    .progress(
                            100 - tile.getProductionProgressScaled(100),
                            100,
                            Styles.machineProgress(player, "Moistening")
                                    .filledColor(0xff341407)
                                    .alternateFilledColor(0xff240e05)
                                    .width(81)
                    );
        }
    }
}

package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

@TOPAddon(dependency = "hatchery")
public class AddonHatchery extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof EggNestTileEntity) {
            EggNestTileEntity nest = (EggNestTileEntity) tile;
            if (nest.getStackInSlot(0) != null) {
                probeInfo.text(TextStyleClass.OK + nest.getStackInSlot(0).getDisplayName());
                probeInfo.progress(Math.round(nest.getPercentage()), 100, new ProgressStyleTOPAddonGrey().suffix("%").prefix("Hatching: ").filledColor(0xffbfa565).alternateFilledColor(0xff8e7945));
            }
        } else if (tile instanceof NestPenTileEntity) {
            NestPenTileEntity pen = (NestPenTileEntity) tile;
            Entity entity = pen.storedEntity();
            if (entity != null) {
                probeInfo.text(entity.getDisplayName().getFormattedText());
                int nextDrop = pen.getTimeToNextDrop() / 20; //in seconds
                int minutes = nextDrop / 60;
                int seconds = nextDrop - minutes * 60;
                textPrefixed(probeInfo, "{*topaddons.hatchery:next_drop*}", minutes == 0 ? String.format("%d\"", seconds) : String.format("%d'%d\"", minutes, seconds));
                if (Loader.isModLoaded("chickens") && mode == ProbeMode.EXTENDED) {
                    AddonChickens.addChickensChickenInfo(probeInfo, entity);
                }
            }
        } else if (tile instanceof FeederTileEntity) {
            FeederTileEntity feeder = (FeederTileEntity) tile;
            probeInfo.progress(feeder.getSeedsInv(), feeder.getMaxSeedInv(), new ProgressStyleTOPAddonGrey().suffix(" feed left").filledColor(0xffbfa565).alternateFilledColor(0xff8e7945));
        }
    }
}

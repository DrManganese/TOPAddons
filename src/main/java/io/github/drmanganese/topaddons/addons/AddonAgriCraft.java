package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.Config;

import com.infinityraider.agricraft.api.misc.IAgriDisplayable;
import com.infinityraider.agricraft.tiles.TileEntityCrop;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;


@TOPAddon(dependency = "agricraft")
public class AddonAgriCraft extends AddonBlank {

	@Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

        TileEntity tileEntity = world.getTileEntity(data.getPos());
		if(tileEntity instanceof TileEntityCrop) {

			List<String> info = new ArrayList<>();
			List<String> revisedInfo;
			((IAgriDisplayable) tileEntity).addDisplayInfo(info);

			revisedInfo = getModeAppropriateData(info, mode);

			for(String line:revisedInfo) {
				probeInfo.text(line);
			}

		}
	}

	private List<String> getModeAppropriateData(List<String> info, ProbeMode mode) {
		List<String> revisedList = new ArrayList<>();
		//Two strings indicates an empty crop
		if(info.size() == 2) {
			revisedList.add(formatText(info.get(0)));
			revisedList.add(formatText(info.get(1)));
		}

		//Five strings indicates an unanalyzed crop
		if(info.size() == 5)
			for(int i = 0;i<info.size();i++) {
				if (i == 3 && mode == ProbeMode.NORMAL && Config.AgriCraft.extendedMode) continue;
				revisedList.add(formatText(info.get(i)));
			}
		//Seven strings indicates an analayzed crop
		if(info.size() == 7) {
			for(int i = 0; i<info.size();i++) {
				if((i == 3 || i == 4 || i == 5) && mode == ProbeMode.NORMAL && Config.AgriCraft.extendedMode) continue;
				revisedList.add(formatText(info.get(i)));
			}
		}
		return revisedList;
	}

	private String formatText(String line) {
		String[] text = line.split(":");
		if(text.length == 2) {
			return (TextFormatting.GREEN + text[0] + ":" + TextFormatting.RESET + text[1]);
		} else {
			return line;
		}
	}

}


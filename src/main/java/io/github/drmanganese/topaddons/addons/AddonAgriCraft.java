package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import com.infinityraider.agricraft.tiles.TileEntityCrop;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;


@TOPAddon(dependency = "agricraft")
public class AddonAgriCraft extends AddonBlank {

	private boolean extendedMode = true;

	@Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

        TileEntity tileEntity = world.getTileEntity(data.getPos());
		if(tileEntity instanceof TileEntityCrop) {

			List<String> info = new ArrayList<>();
			List<String> revisedInfo;
			((IAgriDisplayable) tileEntity).addDisplayInfo(info::add);

			revisedInfo = getModeAppropriateData(info, mode);

			for(String line:revisedInfo) {
				probeInfo.text(line);
			}

		}
	}

	@Override
	public void updateConfigs(Configuration config) {
		extendedMode = config.get("agricraft", "extendedMode", true, "Only show breeding stages in extended mode.").setLanguageKey("topaddons.config:agricraft_extended_mode").getBoolean();
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
				if (i == 3 && mode == ProbeMode.NORMAL && extendedMode) continue;
				revisedList.add(formatText(info.get(i)));
			}
		//Seven strings indicates an analayzed crop
		if(info.size() == 7) {
			for(int i = 0; i<info.size();i++) {
				if((i == 3 || i == 4 || i == 5) && mode == ProbeMode.NORMAL && extendedMode) continue;
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


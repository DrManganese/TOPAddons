package io.github.drmanganese.topaddons.addons.crossmod;

import net.minecraft.tileentity.TileEntity;

import io.github.drmanganese.topaddons.addons.AddonBlank;

import net.bdew.generators.modules.euOutput.BlockEuOutputBase;
import net.bdew.generators.modules.euOutput.TileEuOutputBase;
import net.bdew.lib.multiblock.block.BlockOutput;

import java.text.DecimalFormat;

import mcjty.theoneprobe.api.IProbeInfo;

public final class AdvGensXIC2 {

    public static boolean isEuOutput(BlockOutput blockOutput) {
        return blockOutput instanceof BlockEuOutputBase;

    }

    public static void euOutputInfo(IProbeInfo probeInfo, TileEntity tile) {
        if (tile instanceof TileEuOutputBase) {
            AddonBlank.textPrefixed(probeInfo, "{*topaddons.advgenerators:max_output*}", new DecimalFormat("#.##").format(((TileEuOutputBase) tile).maxOutput()) + " EU/t");
        }
    }
}

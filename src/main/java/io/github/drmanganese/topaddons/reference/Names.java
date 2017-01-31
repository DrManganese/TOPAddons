package io.github.drmanganese.topaddons.reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import io.github.drmanganese.topaddons.network.MessageAskTranslation;
import io.github.drmanganese.topaddons.network.PacketHandler;

import java.util.HashMap;
import java.util.Map;

public final class Names {

    public static final Map<Class<? extends TileEntity>, String[]> tankNamesMap = new HashMap<>();

    public static final Map<String, Class> clientConfigOptions = new HashMap<>();

    public static final Map<String, String> translations = new HashMap<>();

    static {
        clientConfigOptions.put("fluidGauge", Boolean.TYPE);
        clientConfigOptions.put("hideTOPTank", Boolean.TYPE);
        clientConfigOptions.put("forestryReasonCrouch", Boolean.TYPE);
        clientConfigOptions.put("showPitch", Boolean.TYPE);
        clientConfigOptions.put("ic2Progress", Boolean.TYPE);
    }

    public static String getTranslation(String unloc, EntityPlayer player) {
        if (!translations.containsKey(unloc)) {
            translations.put(unloc, unloc);
            PacketHandler.INSTANCE.sendTo(new MessageAskTranslation(unloc), (EntityPlayerMP) player);
        }

        return translations.get(unloc);
    }
}

package io.github.drmanganese.topaddons.elements;

import io.github.drmanganese.topaddons.TOPAddons;

import net.minecraft.entity.player.EntityPlayer;

import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.HashMap;
import java.util.Map;

/**
 * Element ids get desynced when the client and the server don't have the same mods enabled. (e.g. client has dimensions
 * mod but server has it disabled)
 * <br><br>
 * When an addon registers its element(s) the id returned by TOP is stored in a map, each element is then associated to
 * it by a specified string identifier.
 * <br><br>
 * When a player joins a server the contents of the map are sent to the server and stored as a (non-persistent)
 * capability. Now, instead of using an id stored on the server for the element, the info providers must use
 * {@link #getId} to send the correct id back to the client.
 */
public final class ElementSync {
    public static final Map<String, Integer> elementIdMap = new HashMap<>();

    public static void registerElement(ITheOneProbe probe, String elementName, IElementFactory elementFactory) {
        elementIdMap.put(elementName, probe.registerElementFactory(elementFactory));
    }

    public static int getId(String elementName, EntityPlayer player) {
        return player.getCapability(TOPAddons.SYNC_CAP, null).getElementId(elementName);
    }
}

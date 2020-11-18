package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.entity.player.PlayerEntity;

import mcjty.theoneprobe.api.IElementFactoryNew;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.HashMap;
import java.util.Map;

/**
 * Element ids get desynced when the client and the server don't have the same mods enabled. (e.g. client has dimensions
 * mod but server has it disabled)
 * <p>
 * When an addon registers its element(s) the id returned by TOP is stored in a map, each element is then associated to
 * it by a specified string identifier.
 * <p>
 * When a player joins a server the contents of the map are sent to the server and stored as a (non-persistent)
 * capability. Now, instead of using an id stored on the server for the element, the info providers must use
 * {@link #getId} to send the correct id back to the client.
 */
public final class ElementSync {
    public static final Map<String, Integer> elementIdMap = new HashMap<>();

    public static void registerElement(ITheOneProbe probe, String elementName, IElementFactoryNew elementFactory) {
        elementIdMap.put(elementName, probe.registerElementFactory(elementFactory));
    }

    public static int getId(String elementName, PlayerEntity player) {
        return player.getCapability(TopAddons.ELT_SYNC_CAP)
                .orElseThrow(RuntimeException::new)
                .getElementId(elementName);
    }
}

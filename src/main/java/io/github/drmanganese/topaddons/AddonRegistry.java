package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.api.IAddonConfig;

import net.minecraftforge.fml.ModList;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Instantiates and keeps track of TOP Addons.
 */
public final class AddonRegistry {

    private static final List<TopAddon> ADDONS = new LinkedList<>();

    /**
     * Registers and instantiates an addon if its dependent mods are loaded.
     *
     * @param addonFactory Addon class factory.
     * @param dependencies List of modids necessary for the addon.
     * @param rejectors    List of modids which should prevent the creation of the addon.
     */
    public static void registerAddon(Supplier<TopAddon> addonFactory, String[] dependencies, String[] rejectors) {
        if (verifyDependencies(dependencies, rejectors)) {
            final TopAddon addon = addonFactory.get();
            ADDONS.add(addon);
            TopAddons.LOGGER.info("Registered addon {}", addon.getFancyName());
        }
    }

    /**
     * Registers and instantiates an addon which has no dependencies.
     *
     * @param addonFactory Addon class factory.
     */
    public static void registerAddon(Supplier<TopAddon> addonFactory) {
        registerAddon(addonFactory, new String[0], new String[0]);
    }

    /**
     * Registers and instantiates an addon which has a single dependency.
     *
     * @param addonCreator Addon class factory.
     * @param dependency   Modid of the mode dependency.
     */
    public static void registerAddon(Supplier<TopAddon> addonCreator, String dependency) {
        registerAddon(addonCreator, ArrayUtils.toArray(dependency), new String[0]);
    }

    public static Stream<TopAddon> getAddonStream() {
        return ADDONS.stream();
    }

    public static Stream<IAddonConfig> getAddonConfigStream() {
        return getAddonStream().filter(IAddonConfig.class::isInstance).map(IAddonConfig.class::cast);
    }

    private static boolean verifyDependencies(final String[] dependencies, final String[] rejectors) {
        final ModList modList = ModList.get();
        final boolean allDepsFound = Arrays.stream(dependencies).allMatch(modList::isLoaded);
        final boolean noRejectorsFound = Arrays.stream(rejectors).noneMatch(modList::isLoaded);
        return allDepsFound && noRejectorsFound;
    }
}

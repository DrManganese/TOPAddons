package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import com.google.common.base.Strings;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class AddonLoader {

    public static final List<Object> ADDONS = new LinkedList<>();
    public static final List<IAddonConfig> CFG_ADDONS = new ArrayList<>();

    /**
     * Uses the FML ASMData Table to find classes with the {@link TOPAddon} annotation. If the addon's dependency is
     * found and loaded an instance of the addon is stored in {@link AddonLoader::ADDONS} for registration. The instance
     * is also stored in {@link AddonLoader::CFG_ADDONS} if the addon provides configuration options.
     *
     * @param asmData FML ASM table
     */
    static void loadAddons(ASMDataTable asmData) {
        final Logger logger = TOPAddons.LOGGER;

        //Retrieve all classes with the @TOPAddon annotation
        final Set<ASMDataTable.ASMData> candidates = asmData.getAll(TOPAddon.class.getName());
        logger.info("Found {} addon candidates.", candidates.size());

        for (ASMDataTable.ASMData candidate : candidates) {
            try {
                final Class addonClass = Class.forName(candidate.getClassName());
                final TOPAddon annotation = (TOPAddon) addonClass.getDeclaredAnnotation(TOPAddon.class);
                final String dependency = annotation.dependency();

                //Skip candidate if dependency is empty
                if (Strings.isNullOrEmpty(dependency)) {
                    logger.warn("Empty or null dependency for class {}.", candidate.getClassName());
                    continue;
                }

                //Instantiate the addon if dependencies are satisfied is loaded
                if (verifyDependencies(dependency, annotation.extraDeps())) {
                    final String fancyName = annotation.fancyName().equals("") ? getModName(dependency) : annotation.fancyName();

                    try {
                        final Object addon = addonClass.newInstance();
                        ADDONS.add(addon);
                        if (addon instanceof IAddonConfig) {
                            CFG_ADDONS.add((IAddonConfig) addon);
                        }
                        logger.info("Created addon {}.", fancyName);
                    } catch (InstantiationException e) {
                        logger.error("Addon {}'s class could not be instantiated: {}", fancyName, e.getCause());
                    } catch (IllegalAccessException e) {
                        logger.error("Addon {}'s constructor/class is not accessible: {}", fancyName, e.getCause());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //should not happen
            }
        }

        //Sort loaded addons by their annotations order parameter. Ensures order for info sorting.
        ADDONS.sort(Comparator.comparingInt(a -> a.getClass().getDeclaredAnnotation(TOPAddon.class).order()));
    }

    private static String getModName(String dependency) {
        return Loader.instance().getIndexedModList().get(dependency).getName();
    }

    /**
     * Compare main dependency and additional dependencies with loaded mods. See {@link TOPAddon#dependency()} and
     * {@link TOPAddon#extraDeps()} for rules.
     *
     * @param dependency Main addon dependency mod id.
     * @param extraDeps  Other dependency mod ids
     * @return <i>true</i> if dependencies are verified.
     */
    private static boolean verifyDependencies(String dependency, String[] extraDeps) {
        for (String dep : extraDeps) {
            if (dep.startsWith("!") && Loader.isModLoaded(dep.substring(1))) {
                return false;
            } else if (!Loader.isModLoaded(dep)) {
                return false;
            }
        }

        return Loader.isModLoaded(dependency);
    }
}

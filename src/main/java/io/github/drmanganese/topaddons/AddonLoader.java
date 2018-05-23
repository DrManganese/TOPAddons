package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import com.google.common.base.Strings;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class AddonLoader {

    public static final List<Object> ADDONS = new LinkedList<>();

    /**
     * Uses the FML ASMData Table to find classes with the {@link TOPAddon} annotation. If the addon's dependency is
     * found and loaded an instance of the addon is stored in {@link AddonLoader::ADDONS} and stored for registration.
     *
     * @param asmData FML ASM table
     */
    static void loadAddons(ASMDataTable asmData) {
        final Logger logger = TOPAddons.logger;

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
                        ADDONS.add(addonClass.newInstance());
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

        ADDONS.sort(Comparator.comparingInt(a -> a.getClass().getDeclaredAnnotation(TOPAddon.class).order()));
    }

    private static String getModName(String dependency) {
        return Loader.instance().getIndexedModList().get(dependency).getName();
    }

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

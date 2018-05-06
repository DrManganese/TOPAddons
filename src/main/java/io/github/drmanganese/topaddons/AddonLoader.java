package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import com.google.common.base.Strings;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;


public class AddonLoader {

    public static final Set<Object> ADDONS = new HashSet<>();

    /**
     * Uses the FML ASMData Table to find classes with the {@link TOPAddon} annotation. If the addon's dependency is
     * found and loaded an instance of the addon is stored in {@link AddonLoader::ADDONS} and stored for registration.
     * @param asmData FML ASM table
     */
    static void loadAddons(ASMDataTable asmData) {
        final Logger logger = TOPAddons.logger;

        //Retrieve all classes with the @TOPAddon annotation
        final Set<ASMDataTable.ASMData> candidates = asmData.getAll(TOPAddon.class.getName());
        logger.info("Found {} addon candidates.", candidates.size());

        for (ASMDataTable.ASMData candidate : candidates) {
            String dependency = (String) candidate.getAnnotationInfo().get("dependency");

            //Skip candidate if dependency is empty
            if (Strings.isNullOrEmpty(dependency)) {
                logger.warn("Empty or null dependency for class {}.", candidate.getClassName());
                continue;
            }

            //Instantiate the addon if dependency mod is loaded
            if (Loader.isModLoaded(dependency)) {
                try {
                    ADDONS.add(Class.forName(candidate.getClassName()).newInstance());
                    //TODO Fancy name
                    logger.info("Created addon {}.", dependency);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    //TODO Handle and display exceptions
                }
            }
        }
    }

}

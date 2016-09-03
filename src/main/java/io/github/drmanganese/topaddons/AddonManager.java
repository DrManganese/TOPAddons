package io.github.drmanganese.topaddons;

import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.drmanganese.topaddons.api.ITOPAddon;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.EnumChip;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddonManager {

    public static final List<ITOPAddon> ADDONS = new LinkedList<>();
    public static final Map<Class<? extends ItemArmor>, EnumChip> HELMETS = new HashMap<>();

    public static void preInit(FMLPreInitializationEvent event) {
        /* Get all classes with the {@link TOPAddon} annotation */
        Set<ASMDataTable.ASMData> asmDataSet = event.getAsmData().getAll(TOPAddon.class.getName());

        TOPAddons.LOGGER.info("Found {} addon candidates.", asmDataSet.size());

        for (ASMDataTable.ASMData asmData : asmDataSet) {
            Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
            String fancyName = (String) annotationInfo.get("fancyName");
            String dependency = (String) annotationInfo.get("dependency");
            int numHelmets = 0;
            boolean success = true;

            /** Throw exception if {@link TOPAddon#dependency()} was somehow not given */
            if (dependency == null || dependency.isEmpty()) {
                throw new IllegalArgumentException("No dependency specified for addon. Offender: " + asmData.getClassName());
            } else {
                if (Loader.isModLoaded(dependency)) {
                    /** Try to get a readable name for the addon from its dependency */
                    if (fancyName == null || fancyName.isEmpty()) {
                        for (ModContainer modContainer: Loader.instance().getModList()) {
                            if (modContainer.getModId().equals(dependency)) {
                                fancyName = modContainer.getName();
                            }
                        }
                    }

                    try {
                        Class<?> clazz = Class.forName(asmData.getClassName());
                        /** Does {@link clazz} implement/extend {@link ITOPAddon} */
                        if (ITOPAddon.class.isAssignableFrom(clazz)) {
                            ITOPAddon instance = (ITOPAddon) clazz.newInstance();
                            numHelmets = addHelmets(instance);
                            ADDONS.add(instance);
                        }
                    } catch (ClassNotFoundException e) {
                        TOPAddons.LOGGER.fatal("Classloader error while trying to create addon {}.", fancyName);
                        e.printStackTrace();
                        success = false;
                    } catch (InstantiationException e) {
                        TOPAddons.LOGGER.error("Addon {} couldn't be instantiated, does its class have a constructor?", fancyName);
                        e.printStackTrace();
                        success = false;
                    } catch (IllegalAccessException e) {
                        TOPAddons.LOGGER.error("Couldn't access constructor for addon {}.", fancyName);
                        e.printStackTrace();
                        success = false;
                    }

                    if (success) {
                        if (numHelmets > 0)
                            TOPAddons.LOGGER.info("Created addon {} with {} helmets.", fancyName, numHelmets);
                        else
                            TOPAddons.LOGGER.info("Created addon {}.", fancyName);
                    } else {
                        TOPAddons.LOGGER.fatal("Failed to create addon {}", fancyName);
                    }

                } else {
                    TOPAddons.LOGGER.info("Dependency ({}) for addon {} not found, skipping...", dependency, fancyName);
                }
            }
        }

        /** Sort alphabetically for client/server element IDs */
        Collections.sort(ADDONS, (o1, o2) -> o1.getClass().getName().compareToIgnoreCase(o2.getClass().getName()));

        /** Sorting order */
        Collections.sort(ADDONS, (o1, o2) -> {
            int order1 = o1.getClass().getAnnotation(TOPAddon.class).order();
            int order2 = o2.getClass().getAnnotation(TOPAddon.class).order();

            return Integer.compare(order1, order2);
        });
    }

    private static int addHelmets(ITOPAddon addon) {
        if (addon.hasHelmets()) {
            //Safe because duplicates are impossible
            HELMETS.putAll(addon.getHelmets());
        }

        return addon.getHelmets().size();
    }
}

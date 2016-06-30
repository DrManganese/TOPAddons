package io.github.drmanganese.topplugins.plugins;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import io.github.drmanganese.topplugins.TOPPlugins;
import io.github.drmanganese.topplugins.api.ITOPPlugin;
import io.github.drmanganese.topplugins.api.TOPPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PluginManager {

    public static List<ITOPPlugin> plugins = new ArrayList<>();

    public static void preInit(FMLPreInitializationEvent event) {
        /** Get all classes with the {@link TOPPlugin} annotation */
        Set<ASMDataTable.ASMData> asmDataSet = event.getAsmData().getAll(TOPPlugin.class.getName());

        TOPPlugins.LOGGER.info("Found {} candidates.", asmDataSet.size());

        for (ASMDataTable.ASMData asmData : asmDataSet) {
            Map<String, Object> annotationInfo = asmData.getAnnotationInfo();
            String fancyName = (String) annotationInfo.get("fancyName");
            String dependency = (String) annotationInfo.get("dependency");
            boolean success = true;

            /** Throw exception if {@link TOPPlugin#dependency()} was somehow not given */
            if (dependency == null || dependency.isEmpty()) {
                throw new IllegalArgumentException("No dependency specified for plugin. Offender: " + asmData.getClassName());
            } else {
                if (Loader.isModLoaded(dependency)) {
                    /** Try to get a readable name for the plugin from its dependency */
                    if (fancyName == null || fancyName.isEmpty()) {
                        for (ModContainer modContainer: Loader.instance().getModList()) {
                            if (modContainer.getModId().equals(dependency)) {
                                fancyName = modContainer.getName();
                            }
                        }
                    }

                    try {
                        Class<?> clazz = Class.forName(asmData.getClassName());
                        /** Does {@link clazz} implement/extend {@link ITOPPlugin} */
                        if (ITOPPlugin.class.isAssignableFrom(clazz)) {
                            ITOPPlugin instance = (ITOPPlugin) clazz.newInstance();
                            plugins.add(instance);
                        }
                    } catch (ClassNotFoundException e) {
                        TOPPlugins.LOGGER.fatal("Classloader error while trying to create plugin {}.", fancyName);
                        e.printStackTrace();
                        success = false;
                    } catch (InstantiationException e) {
                        TOPPlugins.LOGGER.error("Plugin {} couldn't be instantiated, does its class have a constructor?", fancyName);
                        e.printStackTrace();
                        success = false;
                    } catch (IllegalAccessException e) {
                        TOPPlugins.LOGGER.error("Couldn't access constructor for plugin {}.", fancyName);
                        e.printStackTrace();
                        success = false;
                    }

                    if (success) {
                        TOPPlugins.LOGGER.info("Created plugin {}", fancyName);
                    } else {
                        TOPPlugins.LOGGER.fatal("Failed to create plugin {}", fancyName);
                    }

                } else {
                    TOPPlugins.LOGGER.info("Dependency ({}) for plugin {} not found, skipping...", dependency, fancyName);
                }
            }
        }
    }
}

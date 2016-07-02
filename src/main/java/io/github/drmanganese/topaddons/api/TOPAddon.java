package io.github.drmanganese.topaddons.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TOPAddon {

    /**
     * @return The modid of the mod the plugin is written for, will be used to check if the mod is loaded
     */
    String dependency();

    /**
     * @return Optional readable plugin name
     */
    String fancyName() default "";

    /**
     * @return Optional localization key (might be used later for configs or something)
     */
    String unlocDescription() default "";
}

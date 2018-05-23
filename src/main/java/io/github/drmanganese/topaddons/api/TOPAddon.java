package io.github.drmanganese.topaddons.api;

import net.minecraftforge.fml.common.ModContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This defines an addon. Every class with this annotation will be instantiated and registered with The One Probe - if
 * its dependencies are satisfied.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TOPAddon {

    /**
     * The unique identifier of the mod that this addon requires.
     *
     * @return Dependant mod's id.
     */
    String dependency();

    /**
     * Optional array of additional modid dependencies. If a modid is preceded by <b>!</b> the addon should not be loaded if the
     * mod is loaded.
     *
     * @return Array of additional dependencies.
     */
    String[] extraDeps() default {};

    /**
     * Optional addon name to show instead of default mod's {@link ModContainer#getName()}.
     *
     * @return Addon's "fancy name".
     */
    String fancyName() default "";

    /**
     * Optional addon ordering, lower numbers have higher priority.
     *
     * @return Addon's order priority.
     */
    int order() default 10;
}

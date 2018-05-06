package io.github.drmanganese.topaddons.api;

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
}

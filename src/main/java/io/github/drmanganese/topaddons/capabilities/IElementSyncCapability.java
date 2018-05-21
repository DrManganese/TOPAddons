package io.github.drmanganese.topaddons.capabilities;

import java.util.Map;

public interface IElementSyncCapability {

    int getElementId(String name);

    void setElementIds(Map<String, Integer> elementIds);

    Map<String, Integer> getAllElementIds();
}

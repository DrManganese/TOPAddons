package io.github.drmanganese.topaddons.capabilities;

import java.util.HashMap;
import java.util.Map;

public final class ElementSyncCapability {

    private final Map<String, Integer> elementIdMap = new HashMap<>();

    public int getElementId(String name) {
        return this.elementIdMap.getOrDefault(name, -1);
    }

    public void setElementIds(Map<String, Integer> elementIds) {
        this.elementIdMap.clear();
        this.elementIdMap.putAll(elementIds);
    }

    public Map<String, Integer> getAllElementIds() {
        return this.elementIdMap;
    }
}

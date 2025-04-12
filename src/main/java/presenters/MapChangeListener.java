package presenters;

import maps.WorldMap;

@FunctionalInterface
public interface MapChangeListener {
    void mapChanged(WorldMap worldMap);
}

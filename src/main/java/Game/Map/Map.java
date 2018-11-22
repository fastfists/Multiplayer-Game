package Game.Map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Map {
    private Set<Tile> tiles;
    private String name;

    public Map(Set<Tile> tiles, String name) {
        this.tiles = tiles;
        this.name = name;
    }

    /**
     * Constructs a Map
     * Uses:
     * new Map(name, tile1, tile2, tile3)
     *
     * @param name  The name of the map
     * @param tiles an unspecified amount of tiles
     */
    public Map(String name, Tile... tiles) {
        this(new HashSet<Tile>(Arrays.asList(tiles)), name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(Set<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Adds a tile to the set of tiles if tile's uniqueId does belong to any other tile in the set of tiles.
     */
    public boolean addTile(Tile tile) {
        return tiles.add(tile);
    }

    /**
     * Removes a tile from the set of tiles using tile's uniqueId
     */
    public boolean removeTile(Tile tile) {
        return tiles.remove(tile);
    }
}
package Game.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static Game.FileUtilties.FileUtilities.getFilesFromDir;
import static Game.Map.MapUtilities.getCustomMapsDir;

/**
 * This class is used for de-serializing an object from a file which means
 * taking a file that has information of an object's state and turning
 * that back into an actual object in memory.
 */
public class MapReader {
    public static final Logger logger = Logger.getLogger(MapReader.class.getName());

    public static Map createMapFromJson(String jsonStr) {
        JsonObject rootJson = getJsonObject(jsonStr);
        String name = getNameFromJson(rootJson);
        HashSet<Tile> tiles = getTilesFromJson(rootJson);
        return new Map(name, tiles);
    }

    public static Map getCustomMap(String mapName) throws MapNotFoundException {
        HashSet<Map> maps = getCustomMaps();
        for (Map map : maps) {
            if (map.getName().equals(mapName)) {
                return map;
            }
        }
        throw new MapNotFoundException("Requested map not found.");
    }

    /**
     * This method is used for returning a HashSet of all maps in the custom maps directory.
     *
     * @return HashSet of Map objects that are serialized from files in CustomMaps directory.
     * If files could not be read from customMapsDir, an empty HashSet is returned.
     */
    public static HashSet<Map> getCustomMaps() {
        try {
            List<File> files = getFilesFromDir(getCustomMapsDir(), ".json");
            return MapReader.getSerializedMaps(files);
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public static Map createMapFromFile(File file) throws IOException {
        return createMapFromJson(Files.readString(file.toPath()));
    }

    public static HashSet<Map> getSerializedMaps(File... files) {
        return getSerializedMaps(Arrays.asList(files));
    }

    /**
     * This method allows for the use of a list of File objects to be serialized instead of using an array of File objects.
     *
     * @param files List of File objects to be serialized into Map objects.
     * @return An array of serialized Map objects from the files parameter.
     */
    public static HashSet<Map> getSerializedMaps(List<File> files) {
        HashSet<Map> maps = new HashSet<>();
        for (File file : files) {
            try {
                maps.add(createMapFromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return maps;
    }

    public static JsonObject getJsonObject(String jsonData) {
        JsonParser parserJson = new JsonParser();
        return parserJson.parse(jsonData).getAsJsonObject();
    }

    public static HashSet<Tile> getTilesFromJson(JsonObject rootJson) {
        HashSet<Tile> tiles = new HashSet<>();
        Iterator<JsonElement> tilesIter = rootJson.get("tiles").getAsJsonArray().iterator();
        while (tilesIter.hasNext()) {
            JsonObject tileJson = tilesIter.next().getAsJsonObject();
            String UID = getUniqueId(tileJson);
            String type = getType(tileJson);
            Point2D pos = getPosition(tileJson);
            tiles.add(new Tile(UID, type, pos));
        }
        return tiles;
    }

    public static String getUniqueId(JsonObject tileJson) {
        return tileJson.get("uniqueId").getAsString();
    }

    public static String getType(JsonObject tileJson) {
        return tileJson.get("type").getAsString();
    }

    public static Point2D getPosition(JsonObject tileJson) {
        JsonObject pos = tileJson.get("pos").getAsJsonObject();
        double x = pos.get("x").getAsDouble();
        double y = pos.get("y").getAsDouble();
        return new Point2D(x, y);
    }

    public static String getNameFromJson(JsonObject rootJson) {
        /* The replaceAll call removes " from the beginning and ending of the string. */
        return rootJson.get("name").toString().replaceAll("(?:^\")|(?:\"$)", "");
    }
}

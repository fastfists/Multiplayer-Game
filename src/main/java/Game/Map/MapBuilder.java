package Game.Map;


import Game.EntityType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.FXGL.getGameScene;
import static com.almasb.fxgl.app.FXGL.getGameWorld;


/**
 * Class that spawns in any entities that are needed to be
 * viewed by the player.
 */
public class MapBuilder {

    private ArrayList<PlayerScreen> screens;
    private double tileSize = 64;
    private static GameWorld game = getGameWorld();
    private Map map;

    public MapBuilder(Map map, double tileSize, PlayerScreen... screens) {
        this.map = map;
        this.tileSize = tileSize;
        this.screens = new ArrayList<PlayerScreen>(Arrays.asList(screens));
        map.getTiles().removeIf(tile -> tile.getType().equals("Blank")); // Too difficult to change maps
        getGameScene().addUINodes(screens);
    }

    public void addScreen(PlayerScreen screen){
        screens.add(screen);
    }

    /**
     * Clears the map of all tiles that are currently placed
     */
    public static void clearMap() {
        game.getEntitiesByType(EntityType.Wall).forEach(Entity::removeFromWorld);
        game.getEntitiesByType(EntityType.PowerUp).forEach(Entity::removeFromWorld);
        game.getEntitiesByType(EntityType.Tile).forEach(Entity::removeFromWorld);
    }

    public void update() {
        clearMap(); // only remove tile and Health Components
        for (Tile t : map.getTiles()) {
            Point2D tile = t.getPos().multiply(tileSize);
            for(PlayerScreen screen : screens){
                if (screen.contains(tile)) {
                    spawn(t.getType(), new SpawnData(tile).put("tileSize", tileSize).put("Time", Duration.seconds(3)).put("Strength", 5.0));
                }
            }
        }
        screens.forEach(PlayerScreen::onUpdate);
    }

    public void display(int screenNumber, Node... items){
        screens.get(screenNumber).addAll(items);
    }

    public double getTileSize() {
        return tileSize;
    }

    public void configureTileSize(double tileSize){
        tileSize = tileSize;
    }

    public void setMap(Map map) {
        this.map = map;
    }


}

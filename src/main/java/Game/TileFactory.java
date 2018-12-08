package Game;

import Game.components.FrictionComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileFactory implements EntityFactory {

    public Entities.EntityBuilder genericTile(SpawnData data) {
        return Entities.builder()
                .type(EntityType.Tile)
                .from(data)
                .with(new CollidableComponent(true));
    }

    @Spawns("wood")
    public Entity newWoodTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(-0.5)) // slows the car
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BROWN))
                .build();
    }

    @Spawns("ice")
    public Entity newIceTile(SpawnData data) {

        return genericTile(data)
                .with(new FrictionComponent(0)) // restores the cars drag to normal
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.BLUE))
                .build();
    }

    @Spawns("boost")
    public Entity newBoostTile(SpawnData data) {
        return genericTile(data)
                .with(new FrictionComponent(0.1)) // speeds up the car
                .viewFromNodeWithBBox(new Rectangle(50, 50, Color.GREEN))
                .build();
    }
}

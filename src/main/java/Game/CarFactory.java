package Game;

import Game.components.DamageComponent;
import Game.components.GunComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.app.DSLKt.texture;

public class CarFactory implements EntityFactory {

    @Spawns("Player1")
    public Entity spawnPlayer1(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 0));

        return Entities.builder()
                .type(EntityType.Player1)
                .from(data)
                .viewFromNodeWithBBox(texture("car.png", 64, 64/2))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new GunComponent("Bullet"))
                .with(new MovementComponent(0.05,0.95,0.80,2.5,0.5))
                .with(new HealthComponent(100))
                .with(new EffectComponent())
                .build();
    }

    @Spawns("Player2")
    public Entity spawnPlayer2(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 0));

        return Entities.builder()
                .type(EntityType.Player2)
                .from(data)
                .viewFromNodeWithBBox(texture("car.png", 64, 64/2))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new GunComponent("Bullet"))
                .with(new MovementComponent(0.05,0.95,0.80,2.5,0.5))
                .with(new HealthComponent(100))
                .with(new EffectComponent())
                .build();
    }

    @Spawns("Ball")
    public Entity newBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1.0f)); // makes it bounce off stuff
        Point2D velocity = data.get("velocity"); // gets the data injected from spawns() (look in game app implementation)
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));

        return Entities.builder()
                .type(EntityType.Ball)
                .from(data)
                .viewFromNodeWithBBox(new Circle(20))
                .with(physics)
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Point2D velocity = data.get("velocity");
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));

        return Entities.builder()
                .type(EntityType.Bullet)
                .from(data)
                .viewFromNodeWithBBox(new Circle(5))
                .with(new CollidableComponent(true))
                .with(new DamageComponent(20))
                .with(physics)
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }
}

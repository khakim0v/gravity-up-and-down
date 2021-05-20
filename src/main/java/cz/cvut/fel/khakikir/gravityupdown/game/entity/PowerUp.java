package cz.cvut.fel.khakikir.gravityupdown.game.entity;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapSprite;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

public class PowerUp extends MapSprite {
    private final PowerUpType type;

    public PowerUp(double x, double y, PowerUpType type) {
        super(x, y, getAssetPathForType(type));
        this.type = type;

        this.immovable = true;
    }

    public PowerUpType getType() {
        return type;
    }

    private static String getAssetPathForType(PowerUpType type) {
        return switch (type) {
            case Flip -> Registry.Image.POWER_UP_FLIP.getPath();
            case Bounce -> Registry.Image.POWER_UP_BOUNCE.getPath();
            case Smash -> Registry.Image.POWER_UP_SMASH.getPath();
            case Savepoint -> Registry.Image.POWER_UP_SAVEPOINT.getPath();
            case Stop -> Registry.Image.POWER_UP_STOP.getPath();
        };
    }
}

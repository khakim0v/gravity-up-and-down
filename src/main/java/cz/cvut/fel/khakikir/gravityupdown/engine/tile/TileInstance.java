package cz.cvut.fel.khakikir.gravityupdown.engine.tile;

import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapObject;

public class TileInstance extends MapObject {
    public TileInstance() {
        immovable = true;
        moves = false;
    }
}

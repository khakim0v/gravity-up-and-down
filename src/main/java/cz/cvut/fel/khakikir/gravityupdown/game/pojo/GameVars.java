package cz.cvut.fel.khakikir.gravityupdown.game.pojo;

import cz.cvut.fel.khakikir.gravityupdown.engine.util.EngineSave;
import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

public final class GameVars {
    public static final Registry.Level[] LEVELS = new Registry.Level[]{
            Registry.Level.LEVEL_0_THANKS,
            Registry.Level.LEVEL_0_INTRO,
            Registry.Level.LEVEL_0_POWERUP,
            Registry.Level.LEVEL_2
    };

    public static int LEVEL;
    public static LevelStats[] LEVEL_STATS = new LevelStats[LEVELS.length];
    //public static List<HighScore> HIGHSCORES;
    public static int SCORE = 0;
    //public static var SAVES;
    public static EngineSave AUTOSAVE;
    public static boolean RESUMED;
    public static SavePoint SAVEPOINT;

    private GameVars() {

    }
}

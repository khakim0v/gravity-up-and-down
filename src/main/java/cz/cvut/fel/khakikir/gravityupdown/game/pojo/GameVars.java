package cz.cvut.fel.khakikir.gravityupdown.game.pojo;

import cz.cvut.fel.khakikir.gravityupdown.game.util.Registry;

import java.util.HashMap;
import java.util.Map;

public final class GameVars {
    public static Registry.Level LEVEL;
    public static Map<Registry.Level, LevelStats> LEVEL_STATS = new HashMap<>();;
    //public static List<HighScore> HIGHSCORES;
    public static int SCORE = 0;
    //public static var SAVES;
    //public static var AUTOSAVE;
    public static boolean RESUMED;
    //public static SavePointState SAVEPOINT;

    private GameVars() {

    }
}

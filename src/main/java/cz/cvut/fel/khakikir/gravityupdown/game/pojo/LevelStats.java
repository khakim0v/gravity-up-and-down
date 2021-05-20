package cz.cvut.fel.khakikir.gravityupdown.game.pojo;

public class LevelStats {
    private static final int FLIP_VALUE = 1000;
    private static final int FLIP_KEPT_VALUE = 1500;
    private static final int BOUNCE_POWERUP_VALUE = 1000;
    private static final int BOUNCE_POWERUP_KEPT_VALUE = 1500;
    private static final int STOP_VALUE = 1000;
    private static final int STOP_KEPT_VALUE = 1500;
    private static final int AIR_JUMP_VALUE = 50;
    private static final int JUMP_VALUE = 25;
    private static final int BOUNCE_VALUE = 1;
    private static final int TIME_SECOND_VALUE = 500;
    private static final int SPIKE_VALUE = 250;

    public int level;
    public boolean levelPassed;
    public int levelTimeLimit;

    public int flipsCollected;
    public int flipsUsed;

    public int airJumps;
    public int jumps;
    public int bounces;

    public int bouncesCollected;
    public int bouncesUsed;

    public int stopsCollected;
    public int stopsUsed;

    public int spikesSmashed;

    public double elapsedTime;

    public int points;

    public LevelStats(int level) {
        this.level = level;
    }

    public static int calculatePoints(LevelStats stats, boolean calculateTime) {
        int points = 0;

        points += (stats.flipsCollected * FLIP_VALUE);
        points += ((stats.flipsCollected - stats.flipsUsed) * FLIP_KEPT_VALUE);

        points += (stats.bouncesCollected * BOUNCE_POWERUP_VALUE);
        points += ((stats.bouncesCollected - stats.bouncesUsed) * BOUNCE_POWERUP_KEPT_VALUE);

        points += (stats.stopsCollected * STOP_VALUE);
        points += ((stats.stopsCollected - stats.stopsUsed) * STOP_KEPT_VALUE);

        points += (stats.airJumps * AIR_JUMP_VALUE);
        points += (stats.jumps * JUMP_VALUE);
        points += (stats.bounces * BOUNCE_VALUE);
        points += (stats.spikesSmashed * SPIKE_VALUE);

        if (calculateTime) {
            var limit = stats.levelTimeLimit;
            var remaining = limit - stats.elapsedTime;

            points += Math.floor(remaining * TIME_SECOND_VALUE);
        }

        return points;
    }
}

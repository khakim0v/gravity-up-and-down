package cz.cvut.fel.khakikir.gravityupdown.game.audio;

public enum SFX {
    MUSIC_BIT("music_bit", "/music/bit.ogg"),
    DIE("die", "/sfx/die.wav");

    private final String name;
    private final String path;

    SFX(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}

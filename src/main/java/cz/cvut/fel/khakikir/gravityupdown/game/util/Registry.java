package cz.cvut.fel.khakikir.gravityupdown.game.util;

public final class Registry {
    public enum Level {
        LEVEL_0_INTRO("/data/level0_intro.tmx"),
        LEVEL_1("/data/level1_test.tmx");

        private final String path;

        Level(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum Sound {
        MUSIC_BIT("/music/bit.ogg"),
        SFX_DIE("/sfx/die.wav");

        private final String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum Image {
        BACKDROP("/images/backdrop.png"),
        CURSOR("/images/cursor.png"),
        PLAYER("/images/player_single.png"),
        SCANLINES("/images/scanlines.png"),
        TILESET("/images/tileset.png");

        private final String path;

        Image(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum Font {
        NOKIAFC("/fonts/nokiafc22.ttf");

        private final String path;

        Font(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private Registry() {
    }
}


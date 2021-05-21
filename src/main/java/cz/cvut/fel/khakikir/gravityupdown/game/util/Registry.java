package cz.cvut.fel.khakikir.gravityupdown.game.util;

public final class Registry {
    public enum Level {
        LEVEL_0_THANKS("/data/level0_thanks.tmx"),
        LEVEL_0_INTRO("/data/level0_intro.tmx"),
        LEVEL_0_POWERUP("/data/level0_powerup.tmx"),
        LEVEL_2("/data/level2.tmx");

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
        SFX_DIE("/sfx/die.wav"),
        SFX_BOUNCE("/sfx/bounce.wav"),
        SFX_HIT("/sfx/hit.wav"),
        SFX_JUMP("/sfx/jump.wav"),
        SFX_POWERUP_FLIP("/sfx/powerup_flip.wav"),
        SFX_POWERUP_SAVEPOINT("/sfx/powerup_savepoint.wav"),
        SFX_POWERUP_SMASH("/sfx/powerup_smash.wav"),
        SFX_SMASH("/sfx/smash.wav"),
        SFX_UI("/sfx/ui.wav"),
        SFX_LOGO("/sfx/logo.wav");

        private final String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum Image {
        // Main
        BACKDROP("/images/backdrop.png"),
        CURSOR("/images/cursor.png"),
        PLAYER("/images/player_single.png"),
        SCANLINES("/images/scanlines.png"),
        TILESET("/images/tileset.png"),
        BUTTON("/ui/button.png"),
        BUTTON_DISABLED("/ui/button_disabled.png"),
        INSTRUCTIONS("/images/instructions.png"),

        // HUD
        HUD_BAR("/images/hud_bar.png"),
        HUD_BAR_RED("/images/hud_bar_red.png"),
        HUD_BAR_TICKS("/images/hud_bar_ticks.png"),
        HUD_BAR_TICK_GREEN("/images/hud_bar_tick_green.png"),
        TIMER("/images/timer.png"),

        // PowerUps
        POWER_UP_FLIP("/images/powerup_flip.png"),
        POWER_UP_BOUNCE("/images/powerup_bounce.png"),
        POWER_UP_SMASH("/images/powerup_smash.png"),
        POWER_UP_SAVEPOINT("/images/powerup_savepoint.png"),
        POWER_UP_STOP("/images/powerup_stop.png");

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


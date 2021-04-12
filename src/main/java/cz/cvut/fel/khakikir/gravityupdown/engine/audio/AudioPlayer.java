package cz.cvut.fel.khakikir.gravityupdown.engine.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.util.HashMap;

public class AudioPlayer {
    private static final HashMap<String, Clip> clips = new HashMap<>();
    private static boolean isMuted;

    public static void load(String name, String path) {
        if (clips.containsKey(name)) return;
        try {
            InputStream is = AudioPlayer.class.getResourceAsStream(path);
            if (is == null) {
                System.out.printf("Audio resource '%s' wasn't found%n", name);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            Clip clip = AudioSystem.getClip();
            clip.open(dais);
            clips.put(name, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void play(String name) {
        if (isMuted) return;

        Clip c = clips.get(name);
        if (c != null) {
            if (c.isRunning()) c.stop();
            c.setFramePosition(0);
            c.start();
        }
    }

    public static void loop(String name, int start, int end) {
        if (isMuted) return;

        stop(name);
        clips.get(name).setLoopPoints(start, end);
        clips.get(name).setFramePosition(0);
        clips.get(name).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stop(String name) {
        Clip c = clips.get(name);
        if (c != null && c.isRunning()) {
            c.stop();
        }
    }
}

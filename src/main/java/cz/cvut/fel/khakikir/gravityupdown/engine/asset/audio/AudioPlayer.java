package cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio;

import com.github.trilarion.sound.vorbis.sampled.spi.VorbisAudioFileReader;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.ResourceException;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;

public class AudioPlayer {
    private static final HashMap<String, Clip> clips = new HashMap<>();

    public static void load(String name, String path) {
        if (clips.containsKey(name)) return;
        try {
            InputStream is = AudioPlayer.class.getResourceAsStream(path);
            if (is == null) {
                throw new ResourceException(
                        String.format("AudioPlayer: No audio resource with the name '%s' is found", name));
            }

            AudioInputStream ais;
            if (path.endsWith(".ogg")) {
                VorbisAudioFileReader vafr = new VorbisAudioFileReader();
                ais = vafr.getAudioInputStream(is);
            } else {
                ais = AudioSystem.getAudioInputStream(is);
            }

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
            throw new ResourceException(e);
        }
    }

    public static void play(String name) {
        Clip clip = getClip(name);
        stop(clip);

        clip.setFramePosition(0);
        clip.start();
    }

    public static void loop(String name) {
        Clip clip = getClip(name);
        stop(clip);

        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void loop(String name, int start, int end) {
        Clip clip = getClip(name);
        stop(clip);

        clip.setLoopPoints(start, end);
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void setMuted(boolean shouldMute) {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            Line[] lines = mixer.getSourceLines();
            for (Line line : lines) {
                BooleanControl bc = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                if (bc != null) {
                    bc.setValue(shouldMute);
                }
            }
        }
    }

    public static void stop(String name) {
        Clip clip = getClip(name);
        stop(clip);
    }

    private static void stop(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    private static Clip getClip(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            return clip;
        } else {
            throw new ResourceException(String.format("AudioPlayer: Can't find clip with the name '%s'", name));
        }
    }
}

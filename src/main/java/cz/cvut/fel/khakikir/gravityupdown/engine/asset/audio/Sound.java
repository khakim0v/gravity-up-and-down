package cz.cvut.fel.khakikir.gravityupdown.engine.asset.audio;

import com.github.trilarion.sound.vorbis.sampled.spi.VorbisAudioFileReader;
import cz.cvut.fel.khakikir.gravityupdown.engine.asset.ResourceException;

import javax.sound.sampled.*;
import java.io.InputStream;

public class Sound {
    private Clip clip;

    public static Sound load(String path) {
        Sound sound = new Sound();
        sound.loadPath(path);
        return sound;
    }

    public void loadPath(String path) {
        try {
            InputStream is = Sound.class.getResourceAsStream(path);
            if (is == null) {
                throw new ResourceException(
                        String.format("Sound: No audio resource with the name '%s' is found", path));
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
            this.clip = AudioSystem.getClip();
            this.clip.open(dais);
        } catch (Exception e) {
            throw new ResourceException(e);
        }
    }

    public void play() {
        if (clip != null) {
            //stop();

            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            stop();

            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void loop(int start, int end) {
        if (clip != null) {
            stop();

            clip.setLoopPoints(start, end);
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
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

    private void stop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}

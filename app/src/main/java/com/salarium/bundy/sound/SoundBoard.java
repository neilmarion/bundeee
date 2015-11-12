package com.salarium.bundy.sound;

import com.salarium.bundy.R;
import com.salarium.bundy.values.*;

import android.app.Activity;

public class SoundBoard {
    private final boolean MUTE = true;
    private Activity activity;
    private SoundPlayer soundPlayer;
    public SoundBoard(Activity activity) {
        this.activity = activity;
        soundPlayer = new SoundPlayer(activity);
    }

    public void playKeypadSoundEffect() {
        if (!MUTE) {
            soundPlayer.playSoundEffect1();
        }
    }

    public void playEnterSoundEffect() {
        if (!MUTE) {
            soundPlayer.playSoundEffect10();
        }
    }

    public void playErrorSoundEffect() {
        soundPlayer.playSoundEffect16();
    }

    public void playClockInSoundEffect() {
        soundPlayer.playSoundEffect14();
    }

    public void playClockOutSoundEffect() {
        soundPlayer.playSoundEffect15();
    }
}

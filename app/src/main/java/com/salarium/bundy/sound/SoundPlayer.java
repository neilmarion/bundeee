package com.salarium.bundy.sound;

import com.salarium.bundy.R;
import com.salarium.bundy.values.*;

import android.app.Activity;
import android.media.MediaPlayer;

public class SoundPlayer {
    private Activity activity;
    public SoundPlayer(Activity activity) {
        this.activity = activity;
    }

    public void playSoundEffect1() {
        play(R.raw.button);
    }

    public void playSoundEffect2() {
        play(R.raw.overdose);
    }

    public void playSoundEffect3() {
        play(R.raw.jewell);
    }

    public void playSoundEffect4() {
        play(R.raw.sound7);
    }

    public void playSoundEffect6() {
        play(R.raw.camera_click);
    }

    public void playSoundEffect7() {
        play(R.raw.eat_pills);
    }

    public void playSoundEffect8() {
        play(R.raw.shake_it_off);
    }

    public void playSoundEffect9() {
        play(R.raw.megaman);
    }

    public void playSoundEffect10() {
        play(R.raw.sound3);
    }

    public void playSoundEffect11() {
        play(R.raw.fire);
    }

    public void playSoundEffect12() {
        play(R.raw.sound11);
    }

    public void playSoundEffect13() {
        play(R.raw.open_it);
    }

    public void playSoundEffect14() {
        play(R.raw.clockin);
    }

    public void playSoundEffect15() {
        play(R.raw.clockout);
    }

    public void playSoundEffect16() {
        play(R.raw.fail);
    }

    private void play(int id) {
        MediaPlayer mediaPlayer = MediaPlayer.create(activity, id);
        mediaPlayer.setOnPreparedListener(
            new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mediaPlayer.setOnCompletionListener(
            new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }
}

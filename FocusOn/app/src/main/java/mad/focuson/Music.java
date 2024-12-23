package mad.focuson;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Music {
    private MediaPlayer mediaPlayer;
    private MediaPlayer bgMusicPlayer;
    private Context context;

    public Music(Context context) {
        this.context = context;
    }

    public void playTimerEndSound() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, R.raw.timer_end);
            if (mediaPlayer == null) {
                Log.e("Music", "Failed to create MediaPlayer for timer_end");
                return;
            }
            mediaPlayer.setOnCompletionListener(mp -> mp.release());
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("Music", "Error playing timer end sound", e);
        }
    }

    public void playBreakSound() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, R.raw.break_start);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> mp.release());
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        try {
            if (bgMusicPlayer != null) {
                bgMusicPlayer.release();
            }
            bgMusicPlayer = MediaPlayer.create(context, R.raw.timer_end);
            if (bgMusicPlayer != null) {
                bgMusicPlayer.setLooping(true);
                bgMusicPlayer.start();
            } else {
                Log.e("Music", "Failed to create MediaPlayer for background music");
            }
        } catch (Exception e) {
            Log.e("Music", "Error playing background music", e);
        }
    }

    public void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (bgMusicPlayer != null) {
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
    }
}

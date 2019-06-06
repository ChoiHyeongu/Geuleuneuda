package songpatechnicalhighschool.motivation.geuleuneuda;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Delayed;

public class InduceActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    public float speed = 6f;
    final float changeSize = 0.25f;
    private int beep = R.raw.beep;
    private boolean speedChanged = true;
    private PerfectLoopMediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_induce);

        //mediaPlayer = MediaPlayer.create(this, beep);
        //mediaPlayer.setVolume(1f, 1f);
        //mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(0.25f));
        //mediaPlayer.setLooping(true);

        playSound();

        Button button = findViewById(R.id.button);
        button.setOnClickListener((View) -> stopSound());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void playSound() {
        Handler delay = new Handler();

        player = PerfectLoopMediaPlayer.create(this, beep);
        player.start();

        PlaybackParams playbackParams = new PlaybackParams();

        playbackParams.setPitch(1f);
        playbackParams.setSpeed(0.25f);
        player.changeSpeed(playbackParams);
        Toast.makeText(this, "1f", Toast.LENGTH_SHORT).show();
        delay.postDelayed(()->playbackParams.setPitch(2f), 3000);
        playbackParams.setSpeed(2f);
        player.changeSpeed(playbackParams);
        Toast.makeText(this, "2f", Toast.LENGTH_SHORT).show();
        delay.postDelayed(()->playbackParams.setPitch(4f), 3000);
        playbackParams.setSpeed(4f);
        player.changeSpeed(playbackParams);
        Toast.makeText(this, "4f", Toast.LENGTH_SHORT).show();
    }

    private void changeplayerSpeed(float speed) {
    }

    public void stopSound() {
        player.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
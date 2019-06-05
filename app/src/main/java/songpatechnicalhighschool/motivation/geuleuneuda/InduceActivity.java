package songpatechnicalhighschool.motivation.geuleuneuda;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;

public class InduceActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    public int delayMillis = 1000;
    private int beep = R.raw.beep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_induce);



        playSound();

        Button button = findViewById(R.id.button);
        button.setOnClickListener((View) -> stopSound());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void playSound() {

    }

    public void stopSound() {
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

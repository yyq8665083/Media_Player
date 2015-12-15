package bai.yun.domain;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.music.R;

/**
 * @author Yun
 */
public class MainActivity extends Activity implements OnSeekBarChangeListener {

    private static final String MP3_MUSIC_FILE_NAME = "westlife_you_raise_me_up.mp3";

    private static final String MP3_MUSIC_SDCARD_PATH = "";

    private static final String MP3_MUSIC_RESOURCE_PATH = "/res/raw/";

    private static final String MP3_MUSIC_NETWORK_PATH = "";

    private RadioGroup radioGroup;

    private Button buttonPlay;

    private Button buttonPause;

    private Button buttonStop;

    private TextView currentPlayMusicPath;

    private SeekBar audioSeekBar = null;

    private boolean progressFlag = false;

    private MediaPlayer mediaPlayer;

    private Timer mTimer;

    private TimerTask mTimerTask;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        initControls();

        mediaPlayer = new MediaPlayer();

        registerButtonHanlder();

        audioSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initControls() {

        Resources res = getResources();

        tabHost = (TabHost) findViewById(R.id.tabhost);

        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_1))
                .setIndicator(res.getString(R.string.tab_1), res.getDrawable(R.drawable.pic_tab_1))
                .setContent(R.id.tabcontent_1));

        tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_2))
                .setIndicator(res.getString(R.string.tab_2), res.getDrawable(R.drawable.pic_tab_2))
                .setContent(R.id.tabcontent_2));

        tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_3))
                .setIndicator(res.getString(R.string.tab_3), res.getDrawable(R.drawable.pic_tab_3))
                .setContent(R.id.tabcontent_3));

        tabHost.setCurrentTab(0);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        buttonPlay = (Button) findViewById(R.id.button_start);

        buttonPause = (Button) findViewById(R.id.button_pause);

        buttonStop = (Button) findViewById(R.id.button_stop);

        currentPlayMusicPath = (TextView) findViewById(R.id.mp3_file_path);

        audioSeekBar = (SeekBar) findViewById(R.id.audio_seekbar);
    }

    private void registerButtonHanlder() {

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (mediaPlayer != null) {

                    mediaPlayer.reset();

                }
            }

        });

        buttonPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                playMusic();

            }
        });

        buttonPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                pauseMusic();

            }
        });

        buttonStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                stopMusic();

            }
        });
    }

    private void playMusic() {

        mediaPlayer.reset();

        String musicPath = "";

        switch (radioGroup.getCheckedRadioButtonId()) {

            case R.id.button_load_from_res:

                musicPath = MP3_MUSIC_RESOURCE_PATH + MP3_MUSIC_FILE_NAME;

                currentPlayMusicPath.setText(musicPath);

                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.westlife_you_raise_me_up);

                doPlayMusic(musicPath, true);

                break;

            case R.id.button_load_from_sdcard:

                musicPath = MP3_MUSIC_SDCARD_PATH + MP3_MUSIC_FILE_NAME;

                currentPlayMusicPath.setText(musicPath);

                doPlayMusic(musicPath, false);

                break;

            case R.id.button_load_from_url:

                musicPath = MP3_MUSIC_NETWORK_PATH + MP3_MUSIC_FILE_NAME;

                currentPlayMusicPath.setText(musicPath);

                doPlayMusic(musicPath, false);

                break;

        }

        Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();

    }

    private void doPlayMusic(String musicPath, boolean isResWay) {

        try {

            if (!isResWay) {

                mediaPlayer.setDataSource(musicPath);

                mediaPlayer.prepare();

            }

            audioSeekBar.setMax(mediaPlayer.getDuration());

            mTimer = new Timer();

            mTimerTask = new TimerTask() {

                @Override
                public void run() {

                    if (progressFlag)

                        return;

                    audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                }
            };

            mTimer.schedule(mTimerTask, 0, 10);

            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                    mTimer.cancel();

                    mTimerTask.cancel();

                    audioSeekBar.setProgress(0);

                    mediaPlayer.reset();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void pauseMusic() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.pause();

            Toast.makeText(MainActivity.this, "������ͣ", Toast.LENGTH_SHORT).show();

        } else {

            mediaPlayer.start();

            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

        }

    }

    private void stopMusic() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.reset();

            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

            mTimer.cancel();

            mTimerTask.cancel();

        }
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {

        mediaPlayer.seekTo(arg0.getProgress());

        progressFlag = false;

    }
}

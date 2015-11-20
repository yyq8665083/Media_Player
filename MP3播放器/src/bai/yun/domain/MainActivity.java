package bai.yun.domain;

import java.util.Timer;
import java.util.TimerTask;

import com.music.R;

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

/**
 * MP3播放界面实现
 * 
 * @author Yun
 *
 */
public class MainActivity extends Activity implements OnSeekBarChangeListener {

	// 定位播放的MP3文件名
	private static final String MP3_MUSIC_FILE_NAME = "you_raise_me_up.mp3";

	// 定义SD卡中存放MP3文件的路径
	private static final String MP3_MUSIC_SDCARD_PATH = "";

	// 定义资源方式加载MP3的路径
	private static final String MP3_MUSIC_RESOURCE_PATH = "/res/drawable/";

	// 定义网络MP3地址
	private static final String MP3_MUSIC_NETWORK_PATH = "";

	// 单选框
	private RadioGroup radioGroup;

	// 开始按钮
	private Button buttonPlay;

	// 暂停按钮
	private Button buttonPause;

	// 结束按钮
	private Button buttonStop;

	// 当前音乐播放的路径
	private TextView currentPlayMusicPath;

	// 定义进度条变量
	private SeekBar audioSeekBar = null;

	// 进度条拖动标志
	private boolean progressFlag = false;

	// 声明MediaPlayer变量
	private MediaPlayer mediaPlayer;

	// 声明定义器变量
	private Timer mTimer;

	private TimerTask mTimerTask;

	// 声明选项卡
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// 界面初始化
		initControls();

		// 初始化MediaPlayer对象
		mediaPlayer = new MediaPlayer();

		// 注册事件监听
		registerButtonHanlder();

		// SeekBar进度改变事件
		audioSeekBar.setOnSeekBarChangeListener(this);
	}

	/**
	 * 初始化界面控件
	 */
	@SuppressWarnings("deprecation")
	private void initControls() {

		// 获取资源
		Resources res = getResources();

		tabHost = (TabHost) findViewById(R.id.tabhost);

		// 加载选项卡
		tabHost.setup();

		// 创建标签，然后设置：标题/图标/框架内容
		//标签一
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_1))
				.setIndicator(res.getString(R.string.tab_1), res.getDrawable(R.drawable.pic_tab_1))
				.setContent(R.id.tabcontent_1));
		
		//标签二
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_2))
				.setIndicator(res.getString(R.string.tab_2), res.getDrawable(R.drawable.pic_tab_2))
				.setContent(R.id.tabcontent_2));
		
		//标签三
		tabHost.addTab(tabHost.newTabSpec(res.getString(R.string.tab_3))
				.setIndicator(res.getString(R.string.tab_3), res.getDrawable(R.drawable.pic_tab_3))
				.setContent(R.id.tabcontent_3));
		
		//设置当前活动选项卡
		tabHost.setCurrentTab(0);

		radioGroup = (RadioGroup) findViewById(R.id.radio_group);

		buttonPlay = (Button) findViewById(R.id.button_start);

		buttonPause = (Button) findViewById(R.id.button_pause);

		buttonStop = (Button) findViewById(R.id.button_stop);

		currentPlayMusicPath = (TextView) findViewById(R.id.mp3_file_path);

		audioSeekBar = (SeekBar) findViewById(R.id.audio_seekbar);
	}

	/**
	 * 注册事件的监听器
	 */
	private void registerButtonHanlder() {

		// 注册单选按钮单击事件
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// 重置MediaPlayer对象
				if (mediaPlayer != null) {

					mediaPlayer.reset();

				}
			}

		});

		// 注册开始按钮单击事件
		buttonPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 开始播放
				playMusic();

			}
		});

		// 注册暂停按钮单击事件
		buttonPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 暂停播放
				pauseMusic();

			}
		});

		// 注册停止按钮单击事件
		buttonStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 停止播放
				stopMusic();

			}
		});
	}

	/**
	 * 开始播放
	 */
	private void playMusic() {

		// 重置MediaPlayer
		mediaPlayer.reset();

		// 定义播放文件路径
		String musicPath = "";

		// 判断以不同的方式加载MP3资源文件
		switch (radioGroup.getCheckedRadioButtonId()) {

		// 从资源文件res/drawable加载MP3
		case R.id.button_load_from_res:

			// 构建MP3文件路径
			musicPath = MP3_MUSIC_RESOURCE_PATH + MP3_MUSIC_FILE_NAME;

			currentPlayMusicPath.setText(musicPath);

			// 通过传入位于res/drawable下的MP3资源，构建MediaPlayer对象
			mediaPlayer = MediaPlayer.create(MainActivity.this, R.drawable.you_raise_me_up);

			// 执行播放操作
			doPlayMusic(musicPath, true);

			break;

		// 从SD卡加载MP3
		case R.id.button_load_from_sdcard:

			// 构建MP3文件路径
			musicPath = MP3_MUSIC_SDCARD_PATH + MP3_MUSIC_FILE_NAME;

			currentPlayMusicPath.setText(musicPath);

			// 执行播放操作
			doPlayMusic(musicPath, false);

			break;

		case R.id.button_load_from_url:

			// 构建MP3文件路径
			musicPath = MP3_MUSIC_NETWORK_PATH + MP3_MUSIC_FILE_NAME;

			currentPlayMusicPath.setText(musicPath);

			// 执行播放操作
			doPlayMusic(musicPath, false);

			break;

		}

		// 提示当前状态
		Toast.makeText(MainActivity.this, "音乐正在播放", Toast.LENGTH_LONG).show();

	}

	/**
	 * 完成音乐的播放操作
	 * 
	 * @param musicPath
	 *            所播放MP3音乐的路径
	 * @param isResWay
	 *            是否为内部资源加载方式
	 * 
	 */
	private void doPlayMusic(String musicPath, boolean isResWay) {

		try {

			// 判断当前是否以内部资源方式加载MP3文件
			if (!isResWay) {

				// 设置要播放的MP3文件路径
				mediaPlayer.setDataSource(musicPath);

				// 进行播放前准备操作
				mediaPlayer.prepare();

			}

			// 设置进度条最大值为MP3音乐时长
			audioSeekBar.setMax(mediaPlayer.getDuration());

			// 用定时器记录播放速度
			mTimer = new Timer();

			mTimerTask = new TimerTask() {

				@Override
				public void run() {

					if (progressFlag)

						return;

					// 设置进度条为当前播放进度
					audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());

				}
			};

			mTimer.schedule(mTimerTask, 0, 10);

			// 开始播放
			mediaPlayer.start();

			// 注册播放完成事件监听器
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {

					// 当播放完成时给予提示
					Toast.makeText(MainActivity.this, "播放完成", Toast.LENGTH_SHORT).show();

					// 取消定时任务
					mTimer.cancel();

					mTimerTask.cancel();

					// 设置进度条为初始状态
					audioSeekBar.setProgress(0);

					// 重置MediaPlayer为初始状态
					mediaPlayer.reset();

				}
			});

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/**
	 * 暂停播放
	 */
	private void pauseMusic() {

		// 判断当前是否正在播放音乐
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {

			// 暂停播放
			mediaPlayer.pause();

			// 提示状态
			Toast.makeText(MainActivity.this, "播放暂停", Toast.LENGTH_SHORT).show();

		} else {

			// 继续播放
			mediaPlayer.start();

			// 提示状态
			Toast.makeText(MainActivity.this, "开始播放", Toast.LENGTH_SHORT).show();

		}

	}

	/**
	 * 停止播放
	 */
	private void stopMusic() {

		// 判断是否正在播放音乐
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {

			// 重置MediaPlayer到初始化状态
			mediaPlayer.reset();

			// 提示状态
			Toast.makeText(MainActivity.this, "停止播放", Toast.LENGTH_SHORT).show();

			mTimer.cancel();

			mTimerTask.cancel();

		}
	}

	/**
	 * 进度条改变事件
	 */
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

	}

	/**
	 * 开始拖动进度条事件
	 */
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {

	}

	/**
	 * 进度条拖动完成事件
	 */
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {

		// 多媒体播放器播放到进度条的位置
		mediaPlayer.seekTo(arg0.getProgress());

		progressFlag = false;

	}
}

package com.next.gplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class GPlayer extends Activity {

	private static final String TAG = "GPlayer";

	public static final int MSG_SS = 0x0100;
	public static final int MSG_FD = 0x0110;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams param;
	private GVideoView mGVideoView;
	public GHandler mHandler = null;
	public static String mVideoPath;
	public static VideoView mVideoView;
	public static int videoSchedule;
	public boolean key = true;
	MediaController mController;

	// ----------------------------
	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private TextView textSchedule;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	/** 当前缩放模式 */
	// private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private GestureDetector mGestureDetector;
	private MediaController mMediaController;

	float up_x = 0;
	float down_x = 0;
	boolean keyMove = false;

	// -------------------------------
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_player);
		
	
	 
	 
		
		
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		textSchedule = (TextView) findViewById(R.id.video_schedule);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		Log.e("JMQ","Audio"+mAudioManager.isMusicActive());
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mController = new MediaController(this);
		mVideoView.setMediaController(mController);
		Intent intent = getIntent();
		mVideoPath = intent.getStringExtra("playerpath");
		mVideoPath = intent.getStringExtra("path");
		if (mVideoPath != null) {
			mVideoView.setVideoPath(mVideoPath);
		}
		// mVideoView.prepare();
		mHandler = new GHandler();
		mVideoView.start();
		mVideoView.requestFocus();

		Log.d(TAG, "onCreate2" + System.currentTimeMillis());

		mGestureDetector = new GestureDetector(this, new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("JMQ", "onSingleTapUp");
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("JMQ", "onShowPress");
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {

				float mOldX = e1.getX(), mOldY = e1.getY();
				int y = (int) e2.getRawY();
				int x = (int) e2.getRawX();
				Display disp = getWindowManager().getDefaultDisplay();
				int windowWidth = disp.getWidth();
				int windowHeight = disp.getHeight();
				// if(Math.abs(x-mOldX)<Math.abs(y-mOldY))
				if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
				{
					Log.d("JMQ", "moldx1=" + mOldX);
					onVolumeSlide((mOldY - y) / windowHeight);
				} else if (mOldX < windowWidth / 5.0)// 左边滑动
					onBrightnessSlide((mOldY - y) / windowHeight);
				else if (mOldX > windowWidth / 5.0
						|| mOldX < windowWidth * 4.0 / 5)
					keyMove = true;
				// onScheduleSlide((x - mOldX) / windowWidth);

				return false;

			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("JMQ", "onLongPress");
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				Log.d("JMQ", "onFling");
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("JMQ", "onDown");
				return false;
			}
		});
	}

	private void showView() {
		mGVideoView = new GVideoView(getApplicationContext(), mHandler);
		// mGVideoView.setBackgroundResource(R.drawable.faceback_head);

		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		param = ((MyApplication) getApplication()).getMywmParams();

		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		param.format = 1;
		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		param.flags = param.flags
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		param.flags = param.flags
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

		param.alpha = 1.0f;

		param.gravity = Gravity.LEFT | Gravity.TOP;

		param.x = 0;
		param.y = 0;

		param.width = 540;
		param.height = 540;

		mGVideoView.setVideoPath(mVideoPath);
		Log.d("JMQ", "showView+++");

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			key = false;
			this.finish();

		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "start1" + System.currentTimeMillis());

		Log.d(TAG, "start2" + System.currentTimeMillis());
		showView();
		Log.d(TAG, "start3" + System.currentTimeMillis());
	}

	@Override
	public void onPause() {
		super.onPause();
		mVideoView.pause();
		if (key) {
			videoSchedule = mVideoView.getCurrentPosition();

			mGVideoView.seekTo(videoSchedule);

			mGVideoView.start();
			mWindowManager.addView(mGVideoView, param);
		}
		Log.d(TAG, "onPause" + System.currentTimeMillis());
	}

	@Override
	public void onRestart() {
		super.onRestart();
		mGVideoView.pause();
		videoSchedule = mGVideoView.getCurrentPosition();
		mWindowManager.removeView(mGVideoView);
		mVideoView.seekTo(videoSchedule);
		mVideoView.start();
		Log.d(TAG, "onRestart" + System.currentTimeMillis());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy:" + System.currentTimeMillis());
		if (key)
			mWindowManager.removeView(mGVideoView);
	}

	@Override
	protected void onStop() {

		super.onStop();
		Log.d(TAG, "onStop:" + System.currentTimeMillis());
	}

	public class GHandler extends Handler {

		public GHandler() {

		}

		@Override
		public void dispatchMessage(Message msg) {

			super.dispatchMessage(msg);

			switch (msg.what) {
			case GPlayer.MSG_SS: {
				Log.i("JMQ", "MSG_SS++++" + param.height);
				// if( param.width<800){
				// param.width= param.width+100;
				// param.height= param.width+100;
				//
				// mWindowManager.updateViewLayout(mGVideoView, param);
				// Log.i("JMQ", "MSG_SS++++"+param.height );
				// }
			}
				break;
			case GPlayer.MSG_FD:
				mVolumeBrightnessLayout.setVisibility(View.GONE);
				textSchedule.setVisibility(View.GONE);
				mOperationBg.setVisibility(View.VISIBLE);
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;
		boolean b_mov = false;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			up_x = 0;
			down_x = 0;
			endGesture();
			break;
		case MotionEvent.ACTION_MOVE:
			b_mov = true;

			down_x = event.getX();
			Log.d("JMQ", "ACTION_MOVE=" + down_x);
			break;
		case MotionEvent.ACTION_DOWN:
			
			up_x = event.getX();
			Log.e("JMQ", "ACTION_UP=" + up_x);
		}
		if (b_mov && keyMove) {
			Log.d("JMQ", "down_x-up_x=" + (down_x - up_x));
			if ((down_x - up_x) > 0) {

				onScheduleSlide(1);
			} else
				onScheduleSlide(-1);
			keyMove = false;
		}
		return super.onTouchEvent(event);
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;
		Message msg = new Message();
		msg.what = MSG_FD;
		mHandler.sendMessageDelayed(msg, 300);

	}

	/**
	 * 滑动改变进度 video_schedule
	 * 
	 * @param percent
	 */
	protected void onScheduleSlide(float f) {
		Log.d("JMQ", "f=" + f);
		videoSchedule = mVideoView.getCurrentPosition();
		int index = (int) (videoSchedule + f * mVideoView.getDuration() / 3600);
		Log.d("JMQ", "index=" + index);
		if (index > mVideoView.getDuration())
			index = mVideoView.getDuration();
		else if (index < 0)
			index = 0;
		mVideoView.seekTo(index);
		textSchedule.setVisibility(View.VISIBLE);
		mVolumeBrightnessLayout.setVisibility(View.VISIBLE);

		textSchedule.setText(index * 100 / mVideoView.getDuration() + "%");
		mOperationBg.setVisibility(View.GONE);
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.video_schedule).getLayoutParams().width
				* index / mVideoView.getDuration();
		mOperationPercent.setLayoutParams(lp);

	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			if (mOperationBg == null) {
				Log.d("JMQ", "mOperationBg=null");
			} else
				mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {

		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.00f)
			lpa.screenBrightness = 0.00f;
		getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	public void shanMenu(float x, float y) {
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		View v = new View(this);
		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		windowManager.addView(v, mParams);
	}

	@SuppressWarnings("unused")
	private boolean isLongPressed(float lastX, float lastY, float thisX,
			float thisY, long lastDownTime, long thisEventTime,
			long longPressTime) {
		float offsetX = Math.abs(thisX - lastX);
		float offsetY = Math.abs(thisY - lastY);
		long intervalTime = thisEventTime - lastDownTime;
		if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
			
			return true;
		}
		return false;
	}

}
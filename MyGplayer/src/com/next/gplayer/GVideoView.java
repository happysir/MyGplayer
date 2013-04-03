package com.next.gplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.VideoView;

public class GVideoView extends VideoView {

	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;

	public Handler mHandler;
	public static int WIDTH;
	public static int HEIGHT;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private static long mTime;
	private static float mX;
	private static float mY;
	private static boolean mStatic = true;

	int mode = NONE;
	float oldDist = 1f;
	PointF start = new PointF();
	PointF mid = new PointF();
	Context mContext;
	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	private WindowManager.LayoutParams wmParams = ((MyApplication) getContext()
			.getApplicationContext()).getMywmParams();

	public GVideoView(Context context, Handler mHandler) {
		super(context);
		mContext = context;
		this.mHandler = mHandler;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x = event.getRawX();
		y = event.getRawY() - 25;
		Log.i("JMQ", "currX" + x + "====currY" + y);

		// if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("JMQ", ".ACTION_DOWN");
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			if (mStatic) {
				mY = mTouchStartY;
				mX = mTouchStartX;
				mTime = System.currentTimeMillis();

				mStatic = false;
			}
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.i("JMQ", ".ACTION_POINTER_DOWN");

			// mTouchStartX = event.getX();
			// mTouchStartY = event.getY();
			// oldDist = this.spacing(event);
			// if (oldDist > 10f) {
			//
			// midPoint(mid, event);
			// mode = ZOOM;
			// }
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("JMQ", ".ACTION_MOVE");
			// WIDTH = 700;
			// HEIGHT =900;

			if (mode == DRAG) {
				updateViewPosition();
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10) {
					// wmParams.width=wmParams.width+50;
					// wmParams.height=wmParams.width+50;
					// wm.updateViewLayout(this, wmParams);
				}
			}
			break;

		case MotionEvent.ACTION_UP:

			isLongPressed(mX, mY, event.getX(), event.getY(), mTime,
					System.currentTimeMillis(), 2000);
			Log.e("JMQ", "ACTION_UP");
			mStatic = true;
			updateViewPosition();
			// change();
			mTouchStartX = mTouchStartY = 0;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			updateViewPosition();
			Log.i("JMQ", ".ACTION_POINTER_UP");
			mTouchStartX = mTouchStartY = 0;
			mode = NONE;
			break;
		}

		return true;
	}

	// private void change() {
	// // TODO Auto-generated method stub
	// Message msg = new Message();
	// msg.what=0x0100;
	//
	// // mHandler.sendEmptyMessage(0x0100);
	// Log.i("JMQ", "this.wmParams.height" + wmParams.height );
	// }

	private void updateViewPosition() {
		
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(this, wmParams);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int width = getDefaultSize(WIDTH, widthMeasureSpec);
	// int height = getDefaultSize(HEIGHT, heightMeasureSpec);
	// setMeasuredDimension(width, height);
	// }
	private boolean isLongPressed(float lastX, float lastY, float thisX,
			float thisY, long lastDownTime, long thisEventTime,
			long longPressTime) {
//		Log.e("JMQ", "lastX=" + lastX + "lastY=" + lastY + "thisX=" + thisX
//				+ "thisY=" + thisY);
//		Log.e("JMQ", "lastDownTime=" + lastDownTime + "lastDownTime="
//				+ lastDownTime + "thisEventTime=" + thisEventTime);

		float offsetX = Math.abs(thisX - lastX);
		float offsetY = Math.abs(thisY - lastY);
		long intervalTime = thisEventTime - lastDownTime;
		if (offsetX <= 30 && offsetY <= 30 && intervalTime >= longPressTime) {
			Intent intent = new Intent(mContext, GPlayer.class);

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);

			Log.e("JMQ", "StartActivity GPlayer");

			return true;
		}
		return false;
	}

}

package com.develop.view;

import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
//import android.graphics.AvoidXfermode.Mode;
import android.os.Handler;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.R;
import com.develop.PopStars.Util.Utils;


public class Firework {

	private final String TAG = this.getClass().getSimpleName();

	final float screenWidthMeasure = 720;
	private final static int BIG_DEFAULT_ELEMENT_COUNT = 200;
	private final static int MIDDLE_DEFAULT_ELEMENT_COUNT = 200;
	private int BIG_DEFAULT_DURATION = 3000;
	private final static float BIG_DEFAULT_LAUNCH_SPEED = 3;
	private final static float BIG_DEFAULT_ELEMENT_SIZE = 8;

	private final static int SMALL_DEFAULT_ELEMENT_COUNT = 8;
	private int SMALL_DEFAULT_DURATION = 1300;
	private final static float SMALL_DEFAULT_LAUNCH_SPEED = 18;
	private final static float SMALL_DEFAULT_ELEMENT_SIZE = 8;

	private final static float DEFAULT_WIND_SPEED = 6;
	private final static float DEFAULT_GRAVITY = 6;

	private Paint mPaint;

	private int count; // count of element
	private int duration;
	private int[] colors;
	private int color;

	private float launchSpeed;
	private float windSpeed;
	private float gravity;
	private int windDirection; // 1 or -1
	private Location location;
	private float elementSize;
	GameSoundPool sounds;
	private ValueAnimator animator;
	private float animatorValue;
	
	

	private ArrayList<Element> elements = new ArrayList<Element>();
	private AnimationEndListener listener;
	Context context;
	private int mode = 0;
	private float srceenWidth;
	private float screenHeight;
	private int bitmapColor[] = { R.drawable.light_blue,
			R.drawable.light_yellow, R.drawable.light_green,
			R.drawable.light_pink, R.drawable.light_red };

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setScreenSize(float width, float height) {
		srceenWidth = width;
		screenHeight = height;
	}

	public Firework(Location location, int windDirection, int mode,
			Context context, GameSoundPool sounds, int color, float width,
			float height) {
		srceenWidth = width;
		screenHeight = height;
		this.color = color;
		this.location = location;
		this.sounds = sounds;
		this.windDirection = windDirection;
		this.context = context;
		this.mode = mode;
		colors = baseColors;
		gravity = DEFAULT_GRAVITY;
		windSpeed = DEFAULT_WIND_SPEED;
		if (srceenWidth > 0) {
			BIG_DEFAULT_DURATION = (int) (srceenWidth / screenWidthMeasure * BIG_DEFAULT_DURATION);
			SMALL_DEFAULT_DURATION = (int) (srceenWidth / screenWidthMeasure * SMALL_DEFAULT_DURATION);
		}
		Log.d("zxc111", "Firework srceenWidth = " + srceenWidth
				+ " BIG_DEFAULT_DURATION = " + BIG_DEFAULT_DURATION);
		if (mode == 0) {// 大烟花
			count = BIG_DEFAULT_ELEMENT_COUNT;
			duration = BIG_DEFAULT_DURATION;
			launchSpeed = BIG_DEFAULT_LAUNCH_SPEED;
			elementSize = BIG_DEFAULT_ELEMENT_SIZE;
		} else if (mode == 1) {
			count = SMALL_DEFAULT_ELEMENT_COUNT;
			duration = SMALL_DEFAULT_DURATION;
			launchSpeed = SMALL_DEFAULT_LAUNCH_SPEED;
			elementSize = SMALL_DEFAULT_ELEMENT_SIZE;
		} else {
			count = MIDDLE_DEFAULT_ELEMENT_COUNT;
			duration = BIG_DEFAULT_DURATION;
			launchSpeed = BIG_DEFAULT_LAUNCH_SPEED;
			elementSize = BIG_DEFAULT_ELEMENT_SIZE;
		}
		init();
	}

	private float starSize = 15;

	private void init() {
		Random random = new Random(System.currentTimeMillis());
		// color = colors[random.nextInt(colors.length)];
		// 给每个火花设定一个随机的方向 0-360
		elements.clear();
		Log.d("zxc118", "Firework init mode = " + mode + " count = " + count);
		if (mode == 0) {
			for (int i = 0; i < count; i++) {
				color = bitmapColor[random.nextInt(bitmapColor.length)];
				InputStream is = context.getResources().openRawResource(color);
				Bitmap mBitmap = BitmapFactory.decodeStream(is);
				elements.add(new Element(color, Math.toRadians(random
						.nextInt(360)), random.nextFloat() * launchSpeed,
						mBitmap));
			}
		} else {
			float bitmapScale = 2;
			if (srceenWidth > 0) {
				bitmapScale = srceenWidth / screenWidthMeasure * bitmapScale;
			}
			for (int i = 0; i < count; i++) {
				InputStream is = context.getResources().openRawResource(color);
				Bitmap mBitmap = BitmapFactory.decodeStream(is);

				/*
				 * mBitmap = Utils.getBitmap(mBitmap, (int) ((int)
				 * mBitmap.getWidth() * bitmapScale), (int) bitmapScale *
				 * mBitmap.getHeight());
				 */
				Bitmap shapeBitmap = Utils.drawShapeBitmap(mBitmap,
						(int) (srceenWidth / screenWidthMeasure * starSize),
						"star");

				elements.add(new Element(color, Math.toRadians(random
						.nextInt(360)), random.nextFloat() * launchSpeed,
						shapeBitmap));
			}

		}
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		timeCount = 1;
		animatorValue = timeCount;
		// BlurMaskFilter maskFilter = new BlurMaskFilter(2,
		// BlurMaskFilter.Blur.NORMAL);
		// mPaint.setMaskFilter(maskFilter);

	}
	
	private float timeCount = 1;
	private final float dif = 0.00816f; 

	float startTime;
	private boolean needRemove =false;
	
	public boolean getRemove(){
		return needRemove;
	}
	
	 boolean isStart = false;
	public void fire() {
		animator = ValueAnimator.ofFloat(1, 0);
		animator.setDuration(duration);
		//从头开始动画
		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				animatorValue = (Float) valueAnimator.getAnimatedValue();
				Log.d("zxc55", "onAnimationUpdate animatorValue = "+animatorValue);
				// 计算每个火花的位置
				isStart = true;
				for (Element element : elements) {
					element.x = (float) (element.x
							+ Math.cos(element.direction) * element.speed
							* animatorValue + windSpeed * windDirection);
					element.y = (float) (element.y
							- Math.sin(element.direction) * element.speed
							* animatorValue + gravity * (1 - animatorValue));
				}
			}
		});
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.d("zxc118", "onAnimationEnd clear animator");
				listener.onAnimationEnd(Firework.this);
				needRemove = true;
			}
		});
		animator.start();
		if (mode == 0 && sounds != null) {
			sounds.playSound(9, 0);
		}
		
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setColors(int colors[]) {
		this.colors = colors;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void addAnimationEndListener(AnimationEndListener listener) {
		this.listener = listener;
	}
	
	private final int maxTime = 38;
	private int n =0;
	public void draw(Canvas canvas) {
		mPaint.setAlpha((int) (225 * animatorValue));
		n++;
		if(n>maxTime){
			listener.onAnimationEnd(Firework.this);
		}
		Log.d("zxc118", "onAnimationUpdate animatorValue = "+animatorValue+" n = "+n);
		for (Element element : elements) {
			canvas.drawBitmap(element.bitmap, location.x + element.x,
					location.y + element.y, mPaint);
		}
		if(n>2 && !isStart){
			updateLocation();
		}
	}

	public void updateLocation(){
		animatorValue-= dif;
		Log.d("zxc55", "onAnimationUpdate animatorValue = "+animatorValue);
		if(animatorValue<0){
			listener.onAnimationEnd(Firework.this);
		}
		for (Element element : elements) {
			element.x = (float) (element.x
					+ Math.cos(element.direction) * element.speed
					* animatorValue + windSpeed * windDirection);
			element.y = (float) (element.y
					- Math.sin(element.direction) * element.speed
					* animatorValue + gravity * (1 - animatorValue));
		}
	}
	public void release() {
		for (Element element : elements) {
			if (element.bitmap != null && element.bitmap.isRecycled()) {
				element.bitmap.recycle();
			}
		}
	}

	private static final int[] baseColors = { 0xFFFF43, 0x00E500, 0x44CEF6,
			0xFF0040, 0xFF00FFB7, 0x008CFF, 0xFF5286, 0x562CFF, 0x2C9DFF,
			0x00FFFF, 0x00FF77, 0x11FF00, 0xFFB536, 0xFF4618, 0xFF334B,
			0x9CFA18 };

	interface AnimationEndListener {
		void onAnimationEnd(Firework mFirework);
	}

	static class Location {
		public float x;
		public float y;

		public Location(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}

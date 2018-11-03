package com.develop.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.waps.extend.AppWall;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.Util.Utils;
import com.develop.PopStars.waps.MakeGlod;
import com.develop.constant.ConstantUtil;
import com.develop.object.Button;
import com.develop.object.TitleButton;
import com.develop.PopStars.MainActivity;
import com.develop.PopStars.R;
import com.umeng.analytics.MobclickAgent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

/*游戏开始前的界面类*/
public class StartView extends BaseView {

	private LinkedList<Firework> fireworks = new LinkedList<Firework>();
	private boolean hasAdv = false;
	private float text_x;
	private float text_y;
	private float button_x;
	private float button_y;
	private float button_y2;
	private float button_y3;
	private float best_y;
	private float best_x;
	private boolean isBtChange; // 按钮图片改变的标记
	private boolean isBtChange2;
	private boolean isBtChange3;

	private Bitmap text; // 文字图片
	private Bitmap background; // 文字图片
	private Bitmap button; // 按钮图片
	private Bitmap button2; // 按钮图片
	private Bitmap best_bitmap;
	//private Bitmap city; // 城市
	private Rect rect; // 绘制文字的区域
	ExplosionField mExplosionField;
	Context context;
	FireworkView fireworkView;
	boolean isPause = false;
	final int sourceBitmapHeight = 55;
	boolean isInit ;
	
	public void setPause(boolean pause){
		isPause = pause;
	}

	public StartView(Context context, GameSoundPool sounds,FireworkView fireworkView) {
		super(context, sounds);
		this.sounds = sounds;
		// TODO Auto-generated constructor stub
		this.context = context;
		this.fireworkView = fireworkView;
		// TODO Auto-generated constructor stub
		rect = new Rect();
		thread = new Thread(this);
		
	}

	public void setExplosionField(ExplosionField mExplosionField) {
		this.mExplosionField = mExplosionField;
		// mExplosionField.explode(null,context);
	}

	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}

	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		if(!isInit){
			isInit = true;
			initBitmap();
		}
		if (thread.isAlive()) {
			thread.start();
		} else {
			thread = new Thread(this);
			thread.start();
		}
		
	}

	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		
	}

	/*
	 * 爆炸烟花
	 */
	public void bombFireworks(){
		int rang_max = 100;
		int rang_min = 50;
		if(!isPause){
			//爆炸该视图
			 int rnag = (int) (Math.random()* rang_max)+rang_min;
			 int x = (int) (Math.random()* (screen_width-2*rnag))+rnag;
			 int y = (int) (Math.random()* (screen_height/3-rnag))+rnag;
			 lunchFireWork(x,y,0,0,0);
		}
	}

	// 响应触屏事件的方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d("zxc225", "StartdrawSelfiew onTouchEvent dddddddddddd");
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getPointerCount() == 1) {
			float x = event.getX();
			float y = event.getY();
			// 判断第一个按钮是否被按下
			if (newGameButton.isClick(x, y)) {
				sounds.playSound(4, 0);
				isBtChange = true;
				newGameButton.setButtonFlash(true);
				newGameButton.setFlashTime(10);
				Map<String, String> map_value = new HashMap<String, String>();
			    map_value.put("start_new_game", "start_new_game");
				MobclickAgent.onEvent(getContext(), "start_new_game", map_value);
				mainActivity.getHandler().postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mainActivity.toMainView();
					}
					
				}, 1000);
				
			}
			// 判断第二个按钮是否被按下
			else if (resumeGameButton.isClick(x, y) && Utils.getKey(context,ConstantUtil.STARKEY) == 1) {
				sounds.playSound(4, 0);
				isBtChange2 = true;
				mainActivity.setResume(true);
				resumeGameButton.setButtonFlash(true);
				resumeGameButton.setFlashTime(10);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("resumeGame","resumeGame");
				MobclickAgent.onEvent(getContext(), "resumeGame", map);
				mainActivity.getHandler().postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mainActivity.toMainView();
					}
					
				}, 1000);
				
			} else if (aboutGameButton.isClick(x, y)) {
				sounds.playSound(4, 0);
				isBtChange3 = true;
				//aboutGameButton.setButtonFlash(true);
				//aboutGameButton.setFlashTime(10);

				mainActivity.showIntrduceDialog();
				
			}else if(VoiceButton.isClick(x, y)){
				int voiceClick = Utils.getKeyDefault(getContext(), ConstantUtil.VOICEKEY);
				voiceClick = voiceClick == 0?1:0;
				Utils.saveKey(getContext(), ConstantUtil.VOICEKEY, voiceClick);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("voiceClice",""+voiceClick);
				MobclickAgent.onEvent(getContext(), "eventvoiceClice", map); 
				//mainActivity.showMenuDialog();
			}else if(advButton != null && advButton.isClick(x, y)){
				Intent appWallIntent = new Intent(mainActivity, MakeGlod.class);
				mainActivity.startActivity(appWallIntent);
				//mainActivity.showMenuDialog();
			}
				
				
			return true;
		}
		// 响应屏幕单点移动的消息
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			
			return true;
		}
		// 响应手指离开屏幕的消息
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			
			return true;
		}
		return false;
	}

//	final int BAR_HEIGHT = 80;
	final float POP_SCALE = 0.8f;
    float BUTTON_DIS = 60;
	final float BUTTON_WIDE_PRO = 0.5f;
	float button_scale;
	float popScale;
	//float cityScale;
	// 初始化图片资源方法
	int bestScore;
	float bestScale;
	float best_title_length;
	TitleButton bestScoreButton;
	
	TitleButton newGameButton;
	TitleButton resumeGameButton;
	TitleButton aboutGameButton;
	TitleButton VoiceButton;
	TitleButton advButton;
	Bitmap close_bitmap;
	Bitmap open_bitmap;
	Bitmap city;
	Bitmap advBitmapText;
	Bitmap advBitmap;
	final float screenWidthMeasure = 720;
	final int textSize = 40;
	@Override
	public void initBitmap() {
		BUTTON_DIS = (int) (BUTTON_DIS*screen_width/screenWidthMeasure);
		//菜单
		close_bitmap =  BitmapFactory.decodeResource(getContext().getResources(), R.drawable.close_voice);
		close_bitmap = Utils.getBitmap(close_bitmap,(int) (screen_width*0.1f),(int) (screen_width*0.1f));
		open_bitmap =  BitmapFactory.decodeResource(getContext().getResources(), R.drawable.open_voice);
		open_bitmap= Utils.getBitmap(open_bitmap,(int) (screen_width*0.1f),(int) (screen_width*0.1f));
		VoiceButton = new TitleButton(getContext().getResources());
		VoiceButton.init(""," ",screen_width-close_bitmap.getWidth(),BUTTON_DIS/2,close_bitmap.getWidth(),close_bitmap.getHeight(),close_bitmap,1);
		
		background = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.background_main);
		background = Utils.getBitmap(background,(int)screen_width,(int)screen_height);
		
		best_bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.announcement_bg);
		best_bitmap = Utils.getBitmap(best_bitmap,(int) (screen_width*0.5f),(int) (screen_width/screenWidthMeasure*sourceBitmapHeight));

		best_title_length = paint.measureText(context.getString(R.string.best_score));
		best_x = screen_width/2;
	
		int painSize = (int) (screen_width/screenWidthMeasure*textSize);
		
		// TODO Auto-generated method stub
		//background = BitmapFactory.decodeResource(getResources(),
		//		R.drawable.beijing);
		city = BitmapFactory.decodeResource(getResources(), R.drawable.bg_hiscore);
		city = Utils.getBitmap(city,(int)screen_width,(int)screen_width/city.getWidth()*city.getHeight());
		text = BitmapFactory.decodeResource(getResources(), R.drawable.popstar);
		popScale =(float) (screen_width / text.getWidth())*0.8f;
		text = Utils.getBitmap(text,(int) ((int)text.getWidth()*popScale),(int) ((int)text.getHeight()*popScale));
		button = BitmapFactory
				.decodeResource(getResources(), R.drawable.button);
		button_scale = screen_width / button.getWidth() * BUTTON_WIDE_PRO;
		button = Utils.getBitmap(button,(int) ((int)button.getWidth()*button_scale),(int) ((int)button.getHeight()*button_scale*0.9f));
		button2 = BitmapFactory
				.decodeResource(getResources(), R.drawable.button2);
		button2 =Utils.getBitmap(button2,(int) ((int)button2.getWidth()*button_scale),(int) ((int)button2.getHeight()*button_scale*0.9f));
		
		text_x = screen_width / 2 - text.getWidth() / 2;
		text_y = screen_height / 2-text.getHeight();
		
		best_y = BUTTON_DIS/2;
		bestScoreButton =new TitleButton(context.getResources());
		bestScoreButton.init(context.getString(R.string.best_score),""+bestScore,best_x,
				best_y,best_bitmap.getWidth(),best_bitmap.getHeight(),best_bitmap,painSize);
		// 按钮的放缩比例
		button_x = screen_width / 2;
		button_y = screen_height / 2;
		button_y2 = button_y + button.getHeight() + BUTTON_DIS;
		button_y3 = button_y2 + button.getHeight() + BUTTON_DIS;
		// 返回包围整个字符串的最小的一个Rect区域
		paint.setFakeBoldText(true); 
		
		newGameButton = new TitleButton(getContext().getResources());
		newGameButton.init("",""+getContext().getResources().getString(R.string.start_game),button_x,
				button_y,button.getWidth(),button.getHeight(),button,painSize);
		newGameButton.setPainColor(Color.WHITE);
		newGameButton.setFlashSpeed(200);
		
		resumeGameButton = new TitleButton(getContext().getResources());
		resumeGameButton.init("",""+getContext().getResources().getString(R.string.resume_game),button_x,
				button_y2,button.getWidth(),button.getHeight(),button,painSize);

		resumeGameButton.setFlashSpeed(200);
		if(Utils.getKey(context,ConstantUtil.STARKEY) ==0){
			//resumeGameButton.setBitmap(button2);
			resumeGameButton.setPainColor(getContext().getResources().getColor(R.color.text_gray_color));
			resumeGameButton.setAlpha(200);
			resumeGameButton.setEnable(false);
		}else{
			resumeGameButton.setPainColor(Color.WHITE);
		}

		aboutGameButton = new TitleButton(getContext().getResources());
		aboutGameButton.init("",""+getContext().getResources().getString(R.string.about_game),button_x,
				button_y3,button.getWidth(),button.getHeight(),button,painSize);
		aboutGameButton.setPainColor(Color.WHITE);
		aboutGameButton.setFlashSpeed(100);
		
		paint.setTextSize(screen_width/screenWidthMeasure*textSize);
		if(hasAdv) {
			advBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.double_click);
			float advBitmapScale = screen_width / screenWidthMeasure;
			advBitmap = Utils.getBitmap(advBitmap, (int) ((int) advBitmap.getWidth() * advBitmapScale), (int) ((int) advBitmap.getHeight() * advBitmapScale));
			advButton = new TitleButton(getContext().getResources());
			advButton.init("", "", screen_width - advBitmap.getWidth(),
					screen_height / 2, advBitmap.getWidth(), advBitmap.getHeight(), advBitmap, painSize);
			advButton.setBreath(true);
			advBitmapText = BitmapFactory.decodeResource(getResources(), R.drawable.text_free_get2);
			advBitmapText = Utils.getBitmap(advBitmapText, (int) ((int) advBitmapText.getWidth() * advBitmapScale), (int) ((int) advBitmapText.getHeight() * advBitmapScale));
		}
	}

	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if (text != null && !text.isRecycled()) {
			text.recycle();
		}
		if (button != null && !button.isRecycled()) {
			button.recycle();
		}
		if (best_bitmap != null && !best_bitmap.isRecycled()) {
			best_bitmap.recycle();
		}
		if (background != null &&!background.isRecycled()) {
			background.recycle();
		}
		if(close_bitmap != null && close_bitmap.isRecycled()){
			close_bitmap.recycle();
		}
		if(open_bitmap != null && open_bitmap.isRecycled()){
			open_bitmap.recycle();
		}
		if(city != null && city.isRecycled()){
			city.recycle();
		}
		if(advBitmap != null && advBitmap.isRecycled()){
			advBitmap.recycle();
		}
		if(advBitmapText != null && advBitmapText.isRecycled()){
			advBitmapText.recycle();
		}
		
		removeAllFireWork();
		/*if (!city.isRecycled()) {
			city.recycle();
		}*/
	}


	public void removeAllFireWork(){
		if(fireworks != null){
			for (Firework obj:fireworks){
	            obj.release();
	        }
			fireworks.clear();
		}
	}
	private boolean lock = false;;
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		int voiceClick = Utils.getKeyDefault(getContext(), ConstantUtil.VOICEKEY);
		if(voiceClick == 0){
			VoiceButton.setBitmap(close_bitmap);
		}else{
			VoiceButton.setBitmap(open_bitmap);
		}
		
		try {
			bestScore = Utils.getKey(context, ConstantUtil.BESTSCOREKEY);
			canvas = sfh.lockCanvas();
			lock = true;
			//Log.d("zxc225", "StartdrawSelfiew drawSelf");
			//canvas.drawColor(Color.BLACK);
			canvas.drawBitmap(background,0,0,paint);
			paint.setColor(Color.rgb(235, 161, 1));// 绘制背景色
			canvas.save();
			canvas.drawBitmap(city,0,0,paint);
			// canvas.scale(popScale,popScale, 0, 0);
			canvas.drawBitmap(text, text_x, text_y, paint);
			//canvas.drawBitmap(city, 0, screen_height - city.getHeight(), paint);
			bestScoreButton.setContent(""+bestScore);
			bestScoreButton.draw(canvas);
			newGameButton.draw(canvas);
			resumeGameButton.draw(canvas);
			aboutGameButton.draw(canvas);	
			VoiceButton.draw(canvas);
			if(hasAdv) {
				canvas.drawBitmap(advBitmapText, screen_width - advBitmapText.getWidth(), screen_height / 2 + advBitmap.getHeight() / 2, paint);
				advButton.draw(canvas);
			}
			for (int i =0 ; i<fireworks.size(); i++){
	            fireworks.get(i).draw(canvas);
	        }
			canvas.save();
			canvas.restore();
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null && lock)
				sfh.unlockCanvasAndPost(canvas);
			lock = false;
		}
	}

	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if(threadFlag && !isPause){
				long startTime = System.currentTimeMillis();
				drawSelf();
				long endTime = System.currentTimeMillis();
				try {
					if (endTime - startTime < 50)
						Thread.sleep(50 - (endTime - startTime));
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			}
		}
	}
	
	public void lunchFireWork(float x, float y, int direction,int mode,int color){
        final Firework firework = new Firework(new Firework.Location(x, y), direction,mode,context,sounds,color,screen_width,screen_height);
        firework.addAnimationEndListener(new Firework.AnimationEndListener() {
            @Override
            public void onAnimationEnd(Firework mFirework) {
                fireworks.remove(mFirework);
                mFirework.release();
            }
        });
        fireworks.add(firework);
        firework.fire();
        invalidate();
    }
	
}

package com.develop.view;

import java.util.LinkedList;
import java.util.List;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.Util.Utils;
import com.develop.biz.XingBiz;
import com.develop.biz.XingBiz.BombObject;
import com.develop.constant.ConstantUtil;
import com.develop.object.Xing;
import com.develop.ui.Guanka;
import com.develop.PopStars.MainActivity;
import com.develop.PopStars.R;
import com.develop.*;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MainView extends BaseView {
	private LinkedList<Firework> fireworks = new LinkedList<Firework>();
	private boolean isPlay; // 标记游戏运行状态
	private XingBiz xingbiz;
	private Bitmap background; // 背景图片
	private Guanka guanka;
	private int Game_State;
	private Context context;

	// private Bitmap city; // 城市

	private Bitmap button; // 按钮图片
	private Bitmap button2; // 按钮图片
	private float button_x;
	private float button_y;
	private float button_y2;
	private float button_y3;
	Rect rect;

	GameSoundPool sounds;
	FireworkView fireworkView;

	public MainView(Context context, GameSoundPool sounds,
			FireworkView fireworkView) {
		super(context, sounds);
		this.fireworkView = fireworkView;
		Log.d("zxc", "new MainView");
		this.context = context;
		thread = new Thread(this);
		this.sounds = sounds;
		isPlay = true;
		xingbiz = new XingBiz(getResources(), sounds, context, fireworkView,this);
		guanka = new Guanka(getResources(), this, sounds);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"test", Activity.MODE_PRIVATE);
		String zuigaofen = sharedPreferences.getString("zuigaofen", "");
		if (!zuigaofen.equals("")) {
			guanka.setZuigaofen(Integer.parseInt(zuigaofen));
		}
		rect = new Rect();
		// mExplosionField = ExplosionField.attach2Window((Activity) context);
		updateBlock();

	}

	public Context getMainContext() {
		return context;
	}


	float button_scale;
	final float BUTTON_WIDE_PRO = 0.5f;
	final float BUTTON_DIS = 40;
	private boolean isResume = false;
	private boolean isResumeLive = false;
	private int currentGuanKa;
	private int preScore;

	public void initBitmap() {
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.background);

		background = Utils.getBitmap(background, (int) screen_width,
				(int) screen_height);

		/*
		 * city = BitmapFactory.decodeResource(getResources(), R.drawable.city);
		 * float cityScale = (float) (screen_width / city.getWidth()); city =
		 * Utils.getBitmap(city, (int) ((int) city.getWidth() * cityScale),
		 * (int) cityScale * city.getHeight());
		 */

		button = BitmapFactory
				.decodeResource(getResources(), R.drawable.button);
		button_scale = screen_width / button.getWidth() * BUTTON_WIDE_PRO;
		button = Utils.getBitmap(button,
				(int) ((int) button.getWidth() * button_scale),
				(int) ((int) button.getHeight() * button_scale));
		button2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.button2);
		button2 = Utils.getBitmap(button2,
				(int) ((int) button2.getWidth() * button_scale),
				(int) ((int) button2.getHeight() * button_scale));
		// 按钮的放缩比例
		button_x = screen_width / 2 - button.getWidth() / 2;
		button_y = screen_height / 2 - button.getHeight();
		button_y2 = button_y + button.getHeight() + BUTTON_DIS;
		button_y3 = button_y2 + button.getHeight() + BUTTON_DIS;
		//updateBlock();
	}

	boolean isInit = false;

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	public boolean isDie(){
		if(guanka != null)
			return guanka.getIsDie();
		return true;
	}
	public void exitAndSave() {
		if (!xingbiz.isChuangguanwancheng())
			saveAll();
	}

	public void exitMenu() {
		mainActivity.getHandler().sendEmptyMessage(ConstantUtil.START_GAME);
	}

	public boolean getAllBlockInitFinash() {
		return xingbiz.getAllBlockInitFinash();
	}

	public void newGame() {
		Game_State = ConstantUtil.GAME_START;
	}

	public void setResume(boolean isResume) {
		this.isResume = isResume;
	}

	public void setResumeLive(boolean isResumeLive, int currentGuanKa,
			int preScore) {
		this.isResumeLive = isResumeLive;
		this.currentGuanKa = currentGuanKa;
		this.preScore = preScore;
	}

	public int getGuanKa() {
		return guanka.getGuanka();
	}

	public int getPreScore() {
		return guanka.getPreScore();
	}

	public void setResumeLive() {
		((MainActivity) context).setResumeLive(true, guanka.getGuanka());
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		if (!isInit) {
			isInit = true;
			Game_State = ConstantUtil.GAME_START;
			xingbiz.setScreenWH(screen_width, screen_height);
			guanka.setScreenWH(screen_width, screen_height);

			guanka.initbutton();
			xingbiz.setGuanka(guanka);
			initBitmap();
		} else {
			Game_State = ConstantUtil.GAME_ING;
		}
		if (thread.isAlive()) {
			thread.start();
		} else {
			thread = new Thread(this);
			thread.start();
		}
	}

	/*public void addScoreElement(int score, float x, float y) {
		if (xingbiz != null) {
			xingbiz.addScoreElement(score, x, y);
		}
	}*/

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		// release();
	}

	public void release() {
		xingbiz.release();
		if (!button.isRecycled()) {
			button.recycle();
		}
		if (!button2.isRecycled()) {
			button2.recycle();
		}
		if (!background.isRecycled()) {
			background.recycle();
		}
		removeAllFireWork();
		/*
		 * if (!city.isRecycled()) { city.recycle(); }
		 */
	}
	int time=0;
	private boolean lock = false;
	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			lock = true;
			canvas.save();
			// canvas.drawColor(Color.BLACK); // 绘制背景色
			canvas.drawBitmap(background, 0, 0, paint);
			// canvas.drawBitmap(city, 0, screen_height - city.getHeight(),
			// paint);
			canvas.restore();
			guanka.draw(canvas);
			xingbiz.draw(canvas);
			Log.d("zxc09","fireworks.size() = "+fireworks.size());
			for (Firework firework:fireworks){
				firework.draw(canvas);
	        }
		} catch (Exception err) {
			Log.d("zxc", "draw Exception " + threadFlag);
			err.printStackTrace();
		} finally {
			Log.d("zxc", "draw finally " + threadFlag);
			if (canvas != null && lock)
				sfh.unlockCanvasAndPost(canvas);
			lock = false;
		}
	};
	
	public void removeAllFireWork(){
		if(fireworks != null){
			for (Firework obj:fireworks){
	            obj.release();
	        }
			fireworks.clear();
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

	public void saveAll() {
		xingbiz.exitAndSave();
	}

	public void Logic() {
		switch (Game_State) {
		case ConstantUtil.GAME_START: {
			updateBomb = false;
			xingbiz.init();
			if (isResume) {
				xingbiz.resumeGame();
			} else {
				guanka.init();
				if (isResumeLive) {
					guanka.setGuanka(currentGuanKa);
					guanka.setMubiaofen(guanka.getmbf(guanka.getGuanka()));
					guanka.setRecoverLiveScore(preScore);
					guanka.setCurrentScore(preScore);
				}
			}
			Game_State = ConstantUtil.GAME_ING;
			break;
		}
		case ConstantUtil.GAME_NEXT: {
			updateBomb = false;
			xingbiz.init();
			Game_State = ConstantUtil.GAME_ING;
			break;
		}
		case ConstantUtil.GAME_ING: {
			xingbiz.initObject1();
			xingbiz.Logic();
			if (xingbiz.isChuangguanwancheng()) {
				Game_State = ConstantUtil.GAME_END;
			}
			break;
		}
		case ConstantUtil.GAME_END: {
			if (xingbiz.isShengliorshibai()) {
				if (guanka.getDefen() > guanka.getZuigaofen()) {
					guanka.setZuigaofen(guanka.getDefen());
					SharedPreferences mySharedPreferences = context
							.getSharedPreferences("test", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					editor.putString("zuigaofen", "" + guanka.getZuigaofen());
					editor.commit();
				}
				sounds.playSound(2, 0);
				Game_State = ConstantUtil.GAME_WAIT;
			} else {
				sounds.playSound(3, 0);
				Game_State = ConstantUtil.GAME_WAIT;
			}
			break;
		}
		case ConstantUtil.GAME_WAIT: {
			if (xingbiz.isChuangguanwancheng()) {
				if (xingbiz.isShengliorshibai()) {

				} else {
					guanka.setShibaishow(true);
				}
			} else {
				guanka.setStartshow(true);
			}
			break;
		}
		}
		guanka = xingbiz.getGuanka();

	};

	public void showMenuDialog() {
		((MainActivity) context).showMenuDialog();
	}

	boolean isBomb = false;

	boolean updateBomb = false;
	int n =0;
	public void delayUpdateBlockBomb(){
		if(!updateBomb){
			xingbiz.setFlash();
			Message msg = new Message();
			msg.what = ConstantUtil.BLOCK_BOMB_GAME;
			isBomb = true;
			((MainActivity) context).getHandler().sendMessageDelayed(msg,2000);
		}
		updateBomb = true;
	}
	
	private boolean dialog_die_show = false;
	public void updateBlockBomb() {
		if (isPause || ((MainActivity) context).getDialogIsShow()) {
			Message msg = new Message();
			msg.what = ConstantUtil.BLOCK_BOMB_GAME;
			((MainActivity) context).getHandler().sendMessageDelayed(msg,1000);
			return;
		}
		if (xingbiz != null && xingbiz.isChuangguanwancheng()
				&& xingbiz.hasLive()) {// 游戏完成点爆砖块
			dialog_die_show = false;
			if(isBomb){
				isBomb = false;
				xingbiz.setUnFlash();
			}
	        xingbiz.getNextBondStar();
			Message msg = new Message();
			msg.what = ConstantUtil.BLOCK_BOMB_GAME;
			
			((MainActivity) context).getHandler().sendMessageDelayed(msg,200);
		} else if (xingbiz.isChuangguanwancheng() && !xingbiz.hasLive()
				&& guanka.isSucess()) {
			dialog_die_show = false;
			Message msg = new Message();
			msg.what = ConstantUtil.UPDATE_NEXT;
			guanka.setShowGuanka(true);
			guanka.setCurrentScore(guanka.getDefen());
			((MainActivity) context).getHandler().sendMessageDelayed(msg,2000);
		} else if (xingbiz.isChuangguanwancheng() && guanka.getIsDie()
				&& !((MainActivity) context).dieDialogIsShow() && !dialog_die_show) {
			Message msg = new Message();
			dialog_die_show = true;
			msg.what = ConstantUtil.SHOW_DIEDIALOG;
			guanka.setCurrentScore(guanka.getDefen());
			((MainActivity) context).getHandler().sendMessageDelayed(msg,2000);
			//((MainActivity) context).showDieDialog();
		}
		
	}

	public void clearDoubleClickLabel() {
		xingbiz.clearDoubleClickLabel();
	}

	public void updateNext() {
		Game_State = ConstantUtil.GAME_NEXT;
	}

	public void updateBlock() {
		if (xingbiz.getBombNumber() > 0) {
			xingbiz.bombFireworks();
		}
		Message msg = new Message();
		msg.what = ConstantUtil.UPDATE_BOMB;
		((MainActivity) context).getHandler().sendMessageDelayed(msg, 300);
	}

	public int getGame_State() {
		return Game_State;
	}

	public void setGame_State(int game_State) {
		Game_State = game_State;
	}


	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			guanka.onDown(x, y);
			switch (Game_State) {
			case ConstantUtil.GAME_ING: {
				xingbiz.onDown(x, y);
				break;
			}
			case ConstantUtil.GAME_WAIT: {
				break;
			}
			}
			return true;
		}
		return false;
	}

	boolean isPause = false;

	public void setPause(boolean ispause) {
		isPause = ispause;
	}

	public void run() {
		while (threadFlag) {
			if (!isPause && !((MainActivity) context).getDialogIsShow()) {
				draw();
				Logic();
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			}
		}
	}
}

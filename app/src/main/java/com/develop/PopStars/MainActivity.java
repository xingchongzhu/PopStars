package com.develop.PopStars;


import java.util.HashMap;

import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.UpdatePointsListener;
import cn.waps.extend.AppDetail;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.Util.Utils;
import com.develop.PopStars.waps.MakeGlod;
import com.develop.constant.ConstantUtil;
import com.develop.view.CommomDialog;
import com.develop.view.CustomDialog;
import com.develop.view.ExplosionField;
import com.develop.view.FireworkView;
import com.develop.view.MainView;
import com.develop.view.ScoreMoveField;
import com.develop.view.StartView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class MainActivity extends Activity implements AppDetail.onAwardClickListener, UpdatePointsListener{

	int n;
	int score;
	private static final int REQUEST_CODE = 0; // 请求码

	private GameSoundPool sounds;
	private MainView mainView;
	private StartView startView;
	private int view =1;
	private boolean isResumeLive = false;
	private int currentGuanKa;
	private int preScore;
	boolean isResume = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ConstantUtil.TO_MAIN_VIEW) {
				toMainView();
			} else if (msg.what == ConstantUtil.START_GAME) {
				startview();
			} else if (msg.what == ConstantUtil.RESUME_GAME) {
				toMainView();
				Toast.makeText(MainActivity.this, "rsume", 1).show();
			} else if (msg.what == ConstantUtil.END_GAME) {
				endGame();
			}else if (msg.what == ConstantUtil.BLOCK_BOMB_GAME) {
				if(mainView != null){
					mainView.updateBlockBomb();
				}
			}else if (msg.what == ConstantUtil.UPDATE_SHOW_SCORE) {
				if(mainView != null){
					//mainView.updateCurrentScore();
				}
			}else if (msg.what == ConstantUtil.UPDATE_NEXT) {
				if(mainView != null){
					mainView.updateNext();
				}
			}else if (msg.what == ConstantUtil.UPDATE_FIREWORK) {
				if(mainView != null){
					//mainView.updateScore();
				}
			}else if (msg.what == ConstantUtil.UPDATE_BOMB) {
				if(mainView != null){
					mainView.updateBlock();
				}
			}else if (msg.what == ConstantUtil.SHOW_DIEDIALOG) {
				//Toast.makeText(MainActivity.this, "daieee", 1).show();
				showDieDialog();
			}else if(msg.what == ConstantUtil.WELCOME_SOUND){
				sounds.playSound(8, 0);
			}
		}
	};
	
	//点击两次
	final long DOUBLE_CLICK = 1500;
	long startTime;
	boolean isClick =false;
	boolean pause = false;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(view == 1){
			long endTime = System.currentTimeMillis();
			if(!isClick){
				isClick = true;
				startTime = System.currentTimeMillis();	
				Toast.makeText(this,this.getString(R.string.on_back_exit),0).show();
			}else{
				Log.d("zxc11","onBackPressed isClick = "+isClick);
				isClick = false;
				if(endTime - startTime <= DOUBLE_CLICK){
					endGame();
				}else{
					isClick = true;
					startTime = System.currentTimeMillis();	
					Toast.makeText(this,this.getString(R.string.on_back_exit),0).show();
				}
			}
			
		}else{
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}else if(mainView != null && mainView.isDie()){
				startview();
			}else{
				showAlertDialog();
			}
			//Toast.makeText(this,"游戏界面",0).show();
			
		}
		//super.onBackPressed();
		return;
	}
	
	public void showAlertDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(this.getResources().getString(R.string.message_hint));
		builder.setTitle(this.getResources().getString(R.string.hint));
		builder.setPositiveButton(this.getResources().getString(R.string.exit_save_game), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				mainView.exitAndSave();
				startview();
				dialog.dismiss();
				//设置你的操作事项
			}
		});

		builder.setNegativeButton(this.getResources().getString(R.string.exit_game),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						startview();
						dialog.dismiss();
						//UMGameAgent.onProfileSignOff();
						//mainView.exitMenu();
					}
				});
		dialog = builder.create();
		Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
       // window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
	}

	CustomDialog dialog;
	CustomDialog.Builder builder = null;
	public void showDieDialog() {
		final int glod = Utils.getKey(this, ConstantUtil.GOLDKEY);
		if(builder == null)
			builder = new CustomDialog.Builder(this);
		builder.setGlod(glod);
		builder.setMessage(this.getResources().getString(glod < ConstantUtil.RECOVERLIVE?R.string.glod_no_hint:R.string.glod_hint));
		builder.setTitle(this.getResources().getString(R.string.die));
		builder.setPositiveButton(this.getResources().getString(R.string.recover_life), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(glod > ConstantUtil.RECOVERLIVE){
					isResumeLive = true;
					currentGuanKa = mainView.getGuanKa();
					preScore = mainView.getPreScore();
					Message msg = new Message();
					msg.what = ConstantUtil.TO_MAIN_VIEW;
					Utils.saveKey(MainActivity.this, ConstantUtil.GOLDKEY, (glod-ConstantUtil.RECOVERLIVE));
					//MainActivity.this.getHandler().sendMessageDelayed(msg, 10);
					toMainView();
					dialog.dismiss();
					//UMGameAgent.use("glod_recover_live", 1 , 100);
				}else{
					builder.setMessage(MainActivity.this.getResources().getString(R.string.glod_no_hint));
				}
				
				//设置你的操作事项
			}
		});

		builder.setNegativeButton(this.getResources().getString(R.string.onece_again),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						toMainView();
					}
				});
		
		builder.setMakeScoreButton(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent appWallIntent = new Intent(MainActivity.this, MakeGlod.class);
				MainActivity.this.startActivity(appWallIntent);
				//AdInfo adInfo = AppConnect.getInstance(MainActivity.this).getAdInfo();
				//AppDetail.getInstanct().showAdDetail(MainActivity.this, adInfo);
			}
		});
		dialog = builder.create(R.layout.die_dialog_normal_layout);
		Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
		dialog.show();
		
	}
	
	public void showIntrduceDialog() {
		if(builder == null)
			builder = new CustomDialog.Builder(this);
				builder.setTitle(this.getResources().getString(R.string.introduce_game));
		
        builder.setFeedBack(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(MainActivity.this, "setFeedBack", 0).show();
				Log.d("zxc224", "setFeedBack");
				AppConnect.getInstance(MainActivity.this).showFeedback(MainActivity.this);
			}
		});
        
        builder.setcheckEdition(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(MainActivity.this, "setcheckEdition", 0).show();
				Log.d("zxc224", "setcheckEdition");
				AppConnect.getInstance(MainActivity.this).checkUpdate(MainActivity.this);
			}
		});
        dialog = builder.create(R.layout.dialog_about,1);
		Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        //window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
		dialog.show();
	}
	
	public boolean dieDialogIsShow(){
		if(dialog != null){
			if(dialog.isShowing()){
				return true;
			}
		}
		return false;
	}
	int singleClick ;
	int voiceClick;
	CommomDialog.Builder builderMenu = null;
	public void showMenuDialog() {
		if(builderMenu == null)
			builderMenu = new CommomDialog.Builder(this);
		builderMenu.setTitle(this.getResources().getString(R.string.introduce_title));
		builderMenu.setResumeButton(this.getResources().getString(R.string.continue_click), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		singleClick = Utils.getKey(this, ConstantUtil.SINGLEDOUBLEKEY);
		builderMenu.setDoubleButton(this.getResources().getString(singleClick == 0?R.string.single_click:R.string.double_click),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//dialog.dismiss();
				singleClick = singleClick == 0?1:0;
				builderMenu.setDoubleButtonTitle(MainActivity.this.getResources().getString(singleClick == 0?R.string.single_click:R.string.double_click));
				Utils.saveKey(MainActivity.this, ConstantUtil.SINGLEDOUBLEKEY, singleClick);
				builderMenu.setDoubleDrable(singleClick == 0?R.drawable.double_click1:R.drawable.double_click2);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("doubleClice",""+singleClick);
				MobclickAgent.onEvent(MainActivity.this, "eventDoubleClice", map);    
				if(singleClick ==0){
					if(mainView != null){
						mainView.clearDoubleClickLabel();
					}
				}
			}
		},singleClick == 0?R.drawable.double_click1:R.drawable.double_click2);
		
		voiceClick = Utils.getKeyDefault(this, ConstantUtil.VOICEKEY);
		builderMenu.setVoiceButton(this.getResources().getString(R.string.voice),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				voiceClick = voiceClick == 0?1:0;
				Utils.saveKey(MainActivity.this, ConstantUtil.VOICEKEY, voiceClick);
				builderMenu.setVoiceDrable(voiceClick == 0?R.drawable.close_voice:R.drawable.open_voice);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("voiceClice",""+voiceClick);
				MobclickAgent.onEvent(MainActivity.this, "eventvoiceClice", map); 
			}
		},voiceClick == 0?R.drawable.close_voice:R.drawable.open_voice);
		
		dialog = builderMenu.create(R.layout.dialog_commom);
		dialog.show();
	}


	boolean firstInit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 分数计算公式
		super.onCreate(savedInstanceState);
		MobclickAgent.onProfileSignIn("example_id");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
		sounds = new GameSoundPool(this);
		sounds.initGameSound();
		PermissionsUtil.requestPermission(this);
		
		handler.postDelayed(runnable, 1000);
		
		if(view ==1){
			startview();
		}
		firstInit = true;
		Message msg = new Message();
		msg.what = ConstantUtil.WELCOME_SOUND;
		handler.sendMessageDelayed(msg, 1000);
		
		//AppConnect.getInstance("b1aba42d7c592303d9468722b873677b", "waps", this);

		//UMGameAgent.setDebugMode(true);
        //UMGameAgent.init(this);
        
        //AppDetail.getInstanct().addAwardClickListener(this);
        // 预加载自定义广告内容（仅在使用了自定义广告、抽屉广告或迷你广告的情况，才需要添加）
     	//AppConnect.getInstance(this).initAdInfo();
     	
     	// 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
     	//AppConnect.getInstance(this).initPopAd(this);
     	
        // 带有默认参数值的在线配置，使用此方法，程序第一次启动使用的是"defaultValue"，之后再启动则是使用的服务器端返回的参数值
      //  String showAd = AppConnect.getInstance(this).getConfig("showAd", "defaultValue");
     //   Log.i("debug", "showAd = "+showAd);
    }
	
	FireworkView fireworkView;
	public void startview() {
		isClick =false;
		view = 1;

		if (startView == null) {
			startView = new StartView(this, sounds,fireworkView);
		}else{
			startView.release();
			startView = new StartView(this, sounds,fireworkView);
		}
		this.setContentView(startView);
		//addMiniAdv();
		//showDieDialog();
	}
	
	public boolean getDialogIsShow(){
		if(dialog !=null && dialog.isShowing())
			return true;
		return false;
	}

	public void setResumeLive(boolean isResumeLive,int currentGuanKa){
		this.isResumeLive = isResumeLive;
		this.currentGuanKa = currentGuanKa;
	}
	public void setResume(boolean isResume){
		this.isResume = isResume;
	}

	public void endGame() {
		if (startView != null) {
			startView.setThreadFlag(false);
			startView.release();
		}
		if (mainView != null) {
			mainView.setThreadFlag(false);
			mainView.release();
		}
		//Log.d("zxc","mainactivity endGame");
		this.finish();
	}

	public void toMainView() {
		isClick =false;
		view = 2;
		if (startView != null) {
			startView.release();
			startView = null;
		}
		
		if (mainView == null) {
			mainView = new MainView(this, sounds,fireworkView);
		}else{
			mainView.release();
			mainView = null;
			mainView = new MainView(this, sounds,fireworkView);
		}
		setContentView(mainView);
		mainView.setResume(isResume);
		mainView.setResumeLive(isResumeLive, currentGuanKa,preScore);

		isResume = false;
		isResumeLive = false;
		
		//addMiniAdv();
	}
	
	public void addMiniAdv(){
		LinearLayout adlayout = new LinearLayout(this);
		adlayout.setGravity(Gravity.TOP);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_TOP);
		this.addContentView(adlayout, layoutParams);
		AppConnect.getInstance(this).showMiniAd(this, adlayout, 10);// 10秒刷新一次
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//UMGameAgent.onResume(this);
		pause = false;
		if(startView != null && !firstInit){
			startView.setPause(false);
		}
		if(mainView != null && !firstInit){
			mainView.setPause(false);
		}
		if(view ==1 && !firstInit){
			startview();
		}
		firstInit = false;
		
		/*AppConnect.getInstance(this).setPopAdNoDataListener(new AppListener() {

			@Override
			public void onPopNoData() {
				Log.i("debug", "no adv to user");
			}

		});
		// 显示插屏广告
		AppConnect.getInstance(this).showPopAd(this);*/
		
		/*AdInfo adInfo = AppConnect.getInstance(this).getAdInfo();
		//AppDetail.getInstanct().showAdDetail(this, adInfo);
		
		SplashView mSplashView = new SplashView(this);
		// call after setContentView(R.layout.activity_sample);
		mSplashView.showSplashView(this, 6, R.drawable.default_img, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {
                Log.d("SplashView", "img clicked. actionUrl: " + actionUrl);
                Toast.makeText(MainActivity.this, "img clicked.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                Log.d("SplashView", "dismissed, initiativeDismiss: " + initiativeDismiss);
            }
        },adInfo);*/

        // call this method anywhere to update splash view data
        //SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://jkyeo.com");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//UMGameAgent.onPause(this);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mainView != null){
			mainView.release();
		}
		if(startView != null){
			startView.release();
		}
		AppConnect.getInstance(this).close();
		MobclickAgent.onKillProcess(this);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		pause = true;
		if(mainView != null){
			mainView.setPause(true);
		}
		if(startView != null){
			startView.setPause(true);
		}
		
	}
	// getter和setter方法
	// getter��setter����
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
			if (startView != null && !pause) {
				startView.bombFireworks();
			}
			handler.postDelayed(this, 2000);
		}
	};


	public void getUpdatePoints(String currencyName, int pointTotal) {
		final int glod = Utils.getKey(this, ConstantUtil.GOLDKEY);
		Utils.saveKey(MainActivity.this, ConstantUtil.GOLDKEY, (glod+pointTotal));
		if(builder != null)
			builder.setGlod(glod+pointTotal);
		Log.d("zxc117", "getUpdatePoints pointTotal = "+pointTotal);
		
	}

	public void getUpdatePointsFailed(String error) {

	}

	@Override
	public void updatAward(int score) {
		// TODO Auto-generated method stub
		AppConnect.getInstance(this).awardPoints(score, this);
	}
}

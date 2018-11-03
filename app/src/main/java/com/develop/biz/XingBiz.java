package com.develop.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.MainActivity;
import com.develop.PopStars.R;
import com.develop.PopStars.Util.StarObject;
import com.develop.PopStars.Util.Utils;
import com.develop.constant.ConstantUtil;
import com.develop.object.Button;
import com.develop.object.GameObject;
import com.develop.object.Xing;
import com.develop.object.moveScoreText;
import com.develop.ui.Guanka;
import com.develop.view.ExplosionAnimator;
import com.develop.view.ExplosionField;
import com.develop.view.Firework;
import com.develop.view.FireworkView;
import com.develop.view.GoodSocre;
import com.develop.view.MainView;
import com.develop.view.ScoreMoveAnimator;
import com.develop.view.ScoreMoveField;
import com.develop.view.GoodSocre.Location;
import com.umeng.analytics.MobclickAgent;

public class XingBiz {
	private Resources resources;
	private int hangshu;
	private int id;
	private Random random;
	protected float screen_width; // 屏幕的宽度
	protected float screen_height; // 屏幕的高度
	private boolean iscaozuo;
	private List<Xing> Mainxinglist = new ArrayList<Xing>();
	private List<Xing> currentSelectlist = new ArrayList<Xing>();
	private List<Xing> preSelectlist = new ArrayList<Xing>();
	private List<Xing> suplesStarslist = new ArrayList<Xing>();
	private Xing[][] stars= new Xing[ConstantUtil.MAXROW][ConstantUtil.MAXCLOUM];
	//private List<moveScoreText> moveScoreTexts = new ArrayList<moveScoreText>();
	private Guanka guanka;
	private boolean shengliorshibai;
	private boolean chuangguanwancheng = false;
	private int defen;
	private int xwidth, xheight;
	private boolean timepanduan = false;
	private GameSoundPool sounds;
	private Context context;
	private ScoreMoveField mScoreMoveField;
	private final int BROCK_NUMBER = 8;
	private MainView mMainView;
	FireworkView fireworkView;
	private List<GoodSocre> goodArray = new ArrayList<GoodSocre>();
	private List<BombObject> bombObjectArray = new ArrayList<BombObject>();

	public class BombObject{
		public BombObject(int id,float x,float y,int color){
			this.x = x;
			this.y = y;
			this.color = color;
		}
		public int id;
		public float x;
		public float y;
		public int color;
	}
	//Bitmap goodBoy;
	//Bitmap superGoodBoy;
	public XingBiz(Resources resources, GameSoundPool sounds, Context context,FireworkView fireworkView,MainView mMainView) {
		this.mMainView =mMainView;
		this.context = context;
		this.resources = resources;
		this.sounds = sounds;
		random = new Random();
		this.fireworkView = fireworkView;
		this.paint = new Paint();
		//goodBoy = BitmapFactory.decodeResource(resources, R.drawable.combo_awesome);
		//superGoodBoy = BitmapFactory.decodeResource(resources, R.drawable.combo_fantastic);
	}
	
	public void setScoreMoveField(ScoreMoveField mScoreMoveField) {
		this.mScoreMoveField = mScoreMoveField;
	}
	
	public void bombFireworks(float x,float y,int color){
		if(mMainView != null){
			mMainView.lunchFireWork(x,y,0,1,color);
		}
	}


	public void setScreenWH(float screen_width, float screen_height) {
		this.screen_width = screen_width;
		this.screen_height = screen_height;
		this.xwidth = (int) screen_width / 10;
		this.xheight = this.xwidth;
		
	}

	public void init() {
		Log.d("zxc119", "init ");
		for(Xing obj:Mainxinglist){
			obj.release();
		}
		currentFlashlist.clear();
		currentSelectlist.clear();
		this.Mainxinglist.clear();
		preSelectlist.clear();
		preSelectlist.clear();
		for(int r = 0;r<ConstantUtil.MAXROW;r++){
			for(int j = 0;j<ConstantUtil.MAXCLOUM;j++){
				stars[r][j] = null;
			}
		}
		for(GoodSocre obj:goodArray){
			obj.release();
		}
		goodArray.clear();
		hangshu = 0;
		isResetSpeed = false;
		bombNumber = 0;
		this.chuangguanwancheng = false;
		guanka.guankanext();
		isAutoNotic = false;
		startDownTime = System.currentTimeMillis();
		this.iscaozuo = false;
		
	}

	public boolean getAllBlockInitFinash(){
		if(hangshu >=20 && isAllStopDown() || isTouchBlock){
			return true;
		}
		return false;
	}
	
	private boolean isTouchBlock = false;
	// 初始化星星 每次初始化10个 一共十行
	public void initObject1() {
		if (hangshu < 20) {
			isTouchBlock = false;
			if (hangshu%2==0) {
				for (int i = 0; i <5;i++) {
					id++;
					float x = (float) (2*i) * screen_width / 10;
					float y = -random.nextInt(xheight / 2) - (hangshu * xheight);
					Xing xing = new Xing(resources);
					xing.setScreenWH(screen_width, screen_height);
					xing.setWH(xwidth, xheight);
					xing.init(id, x, y);
					Mainxinglist.add(xing);
				}
			} else {
				for (int i = 0; i <5;i++) {
					id++;
					float x = (float) (2*i+1) * screen_width / 10;
					float y = -random.nextInt(xheight / 2) - (hangshu * xheight);
					Xing xing = new Xing(resources);
					xing.setScreenWH(screen_width, screen_height);
					xing.setWH(xwidth, xheight);
					xing.init(id, x, y);
					Mainxinglist.add(xing);
				}
			}
			hangshu++;
		}
	}

	// 星星向下移动是否全部停止
	public boolean isAllStopDown() {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (obj.isIsyundongdown()) {
					return false;
				}
			}
		}

		return true;
	}

	// 星星向左移动是否全部停止
	public boolean isLeftAllStop() {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (obj.isIsyundongleft()) {
					return false;
				}
			}
		}

		return true;
	}
	
	/*public void drawMoveIncreaseScore(Canvas canvas){
		for(moveScoreText text:moveScoreTexts){
			if (text.isAlive()) {
				text.draw(canvas);
			}else{
				moveScoreTexts.remove(text);
				text.release();
			}
		}
	}*/

	boolean isResetSpeed = false;
	protected Paint paint; 			// 画笔对象
	double timeDis = 7000;
	boolean isAutoNotic = false;
	public void draw(Canvas canvas) {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				obj.draw(canvas);
			}
		}
		for(GoodSocre obj:goodArray){
			obj.draw(canvas);
		}
		if(System.currentTimeMillis() -startDownTime > timeDis && !chuangguanwancheng && !isAutoNotic){
			startDownTime = System.currentTimeMillis();
			autoNotice();
		}
	}

	public void lunchGoodScore(float x, float y,int mode){
        final GoodSocre mGoodSocre = new GoodSocre(new GoodSocre.Location(x, y),mode,context,sounds,screen_width,screen_height);
        mGoodSocre.addAnimationEndListener(new GoodSocre.AnimationEndListener() {
			@Override
			public void onAnimationEnd(GoodSocre score) {
				// TODO Auto-generated method stub
				goodArray.remove(score);
				score.release();
			}
        });
        goodArray.add(mGoodSocre);
        mGoodSocre.flash();
    }

	public void setFlash(){
		for(Xing obj:suplesStarslist){
			obj.setFlash(true);
		}
	}
	
	public void setUnFlash(){
		for(Xing obj:suplesStarslist){
			obj.setFlash(false);
		}
	}
	
	public List<Xing> getNextBondStar() {
		
		
		List<Xing> xin = new ArrayList<Xing>();
		if(suplesStarslist.size() <=0)
			return xin;
		if(bondAllRowStar != null && bondAllRowStar.getId() == suplesStarslist.get(0).getId()){
			sounds.playSound(9,0);
			for(Xing obj:suplesStarslist){
				obj.setAlive(false);
				bombFireworks(obj.getObject_x(),obj.getObject_y(),obj.getCurrentColor());
			}
			return xin;
		}
		sounds.playSound(6,0);
		suplesStarslist.get(0).setAlive(false);
		bombFireworks(suplesStarslist.get(0).getObject_x(),suplesStarslist.get(0).getObject_y(),suplesStarslist.get(0).getCurrentColor());
		suplesStarslist.remove(0);
		return xin;
	}
	
	public List<BombObject> getBombObjectArray(){
		return bombObjectArray;
	}

	final int BONDNUMBERMAX = 6;
	int bondNumber = 0;
	
	public int getBondNumber(){
		return bondNumber;
	}
	
	public void setBondNumber(int bondNumber){
		this.bondNumber = bondNumber;
	}
	
	public int getBombNumber(){
		return bombNumber;
	}

	private int bombNumber = 0;
	/*
	 * 爆炸烟花
	 */
	public void bombFireworks(){
		int rang_max = 100;
		int rang_min = 50;
		bombNumber--;
		if(mMainView != null){
			//爆炸该视图
			 int rnag = (int) (Math.random()* rang_max)+rang_min;
			 int x = (int) (Math.random()* (screen_width-2*rnag))+rnag;
			 int y = (int) (Math.random()* (screen_height/3-rnag))+rnag;
			 mMainView.lunchFireWork(x,y,0,0,2);
		}
	}
	
	public boolean hasLive() {
		boolean isLive = false;
		for (Xing obj : Mainxinglist) {
			if(obj.isAlive())
				isLive = true;;
		}
		return isLive;
	}
	
	public void resumeGame(){
		this.Mainxinglist.clear();
		hangshu = 20;
		this.chuangguanwancheng = false;
		isResetSpeed = false;
		this.iscaozuo = false;
		guanka.setZuigaofen(Utils.getKey(context, ConstantUtil.BESTSCOREKEY));
		guanka.setGuanka(Utils.getKey(context, ConstantUtil.GUANKAEKEY));
		guanka.setMubiaofen(guanka.getmbf(guanka.getGuanka()));
		guanka.setDefen(Utils.getKey(context, ConstantUtil.CURRENTSCOREKEY));
		guanka.setGold(Utils.getKey(context, ConstantUtil.GOLDKEY));
		guanka.setCurrentScore(guanka.getDefen());
		int n = 0;
		for(int i=0;i<ConstantUtil.MAXROW;i++){
			for(int j =0;j<ConstantUtil.MAXCLOUM;j++){
				Xing star = Utils.getObjectFromShare(context, ConstantUtil.STARKEY+(n++) 
						, screen_width, screen_height, xwidth, xheight, id++);
				if(star != null){
					Mainxinglist.add(star);
				}
			}
		}
		startDownTime = System.currentTimeMillis();
	}
	
	public void exitAndSave(){
		Thread thread=new Thread(new Runnable()  
        {  
            @Override  
            public void run(){
				int bestScore = guanka.getZuigaofen();
				if(guanka.getZuigaofen() < guanka.getDefen()){
					bestScore = guanka.getDefen();
				}
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("exitAndSave","exitAndSave");
				MobclickAgent.onEvent(mMainView.getContext(), "exitAndSave", map);    

				Utils.clearShare(context);
				Utils.saveKey(context, ConstantUtil.BESTSCOREKEY, bestScore);
				Utils.saveKey(context, ConstantUtil.GUANKAEKEY, guanka.getGuanka());
				Utils.saveKey(context, ConstantUtil.CURRENTSCOREKEY, guanka.getDefen());
				Utils.saveKey(context, ConstantUtil.GOLDKEY, guanka.getGlod());
				int n =0;
				Log.d("zxc119", "exitAndSave Mainxinglist.size = "+Mainxinglist.size());
				Utils.saveKey(context,ConstantUtil.STARKEY,1);//putString(ConstantUtil.STARKEY, objectStr);
				for(Xing obj:Mainxinglist){
	            	Utils.saveObjectToShare(context, new StarObject(obj.getObject_x(),obj.getObject_y()
	            			,obj.getColor(),obj.isAlive()), ConstantUtil.STARKEY+(n++));
	            	Log.d("zxc01", "exit and save "+" key = "+(ConstantUtil.STARKEY+n)+ " islive = "+obj.isAlive());
				}
            }  
        });  
        thread.start(); 
	}

	Xing bondAllRowStar = null;
	// 事物逻辑函数判断星星何时下落 向左移动
	public void Logic() {
		if (!iscaozuo) {
			if (isLeftAllStop()) {
				for (Xing obj : Mainxinglist) {
					if (obj.isAlive()) {
						obj.logic(Mainxinglist, sounds);

					}
				}
			}

			if (isAllStopDown()) {
				for (Xing obj : Mainxinglist) {
					if (obj.isAlive()) {
						this.Mainxinglist = obj.logicLeft(Mainxinglist);

					}
				}
			}
			if (isAllStopDown() && isLeftAllStop()) {
				if (!isNobody()) {
					int i = 0;
					for (Xing obj : Mainxinglist) {
						if (obj.isAlive()) {
							//guanka.setjiangli();
							i++;
							if (guanka.getDefen() >= guanka.getMubiaofen()) {
								chuangguanwancheng = true;
								shengliorshibai = true;
							} else {
								chuangguanwancheng = true;
								shengliorshibai = false;
							}
						}
					}
					guanka.setSyxing(i);
					if (hangshu > 0 && i == 0) {
						guanka.setSyxing(0);
						if (guanka.getDefen()>= guanka.getMubiaofen()) {
							chuangguanwancheng = true;
							shengliorshibai = true;
						} else {
							chuangguanwancheng = true;
							shengliorshibai = false;
						}
					}
					if (chuangguanwancheng)
						guanka.setjiangli();
					int n=0;
					for (Xing obj : Mainxinglist) {
						if(obj.isAlive()){
							int row = (int) ((screen_height-obj.getObject_y()-obj.getObject_height())/obj.getObject_height());
							int cloume = (int) (obj.getObject_x()/obj.getObject_width());
							n++;
							Log.d("zxc114", "row = "+row+" cloume = "+cloume+" n "+n);
							stars[row][cloume]=obj;
						}
					}
					if(chuangguanwancheng && shengliorshibai){
						bondNumber+=BONDNUMBERMAX;
					}
					suplesStarslist.clear();
					bondAllRowStar = null;
					boolean isAllRow =true;
					for(int r = ConstantUtil.MAXROW -1 ;r>=0;r--){
						isAllRow =true;
						Xing first = null;
						for(int j = ConstantUtil.MAXCLOUM - 1;j>=0;j--){
							if(stars[r][j] != null && stars[r][j].isAlive()){
								suplesStarslist.add(stars[r][j]);
								if(first == null)
									first = stars[r][j];
							}
							else if(first != null){
								isAllRow = false;
							}
						}
						if(isAllRow){
							for(int l = ConstantUtil.MAXCLOUM -1 ;l>=0;l--){
								if(stars[0][l] != null && stars[0][l].isAlive()){
									if(first != null && first.getObject_x() == stars[0][l].getObject_x()){
										bondAllRowStar = first;
									}
								}
							}
						}
					}
					/*if(bondAllRowStar != null){
						int row = (int) ((screen_height-bondAllRowStar.getObject_y()-bondAllRowStar.getObject_height())/bondAllRowStar.getObject_height());
						int cloume = (int) (bondAllRowStar.getObject_x()/bondAllRowStar.getObject_width());
						Log.d("zxc114", "row = "+row+" cloume = "+cloume);
					}*/
					Log.d("zxc001","suplesStarslist.size = "+suplesStarslist.size());

				}
				iscaozuo = true;
			}
		}
	}

	public boolean isTimepanduan() {
		return timepanduan;
	}

	public void setTimepanduan(boolean timepanduan) {
		this.timepanduan = timepanduan;
	}

	public boolean isChuangguanwancheng() {
		return chuangguanwancheng;
	}

	public void setChuangguanwancheng(boolean chuangguanwancheng) {
		this.chuangguanwancheng = chuangguanwancheng;
	}

	// 判断有没有星星可以消除
	public boolean isNobody() {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				obj.setIsxuanzhong(true);
				setXingList(obj);
				if (isSelectTwo()) {
					for (Xing obj2 : Mainxinglist) {
						if (obj2.isAlive()) {
							obj2.setIsxuanzhong(false);
						}
					}
					return true;
				} else {
					obj.setIsxuanzhong(false);
				}

			}
		}

		return false;
	}

	public int getSelectNumber() {
		int xzshuliang = 0;
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (obj.isIsxuanzhong()) {
					xzshuliang++;
				}
			}
		}
		return xzshuliang;
	}
	
	public void GooBoy() {

	}
	
	public void clearDoubleClickLabel(){
		for (Xing obj2 : Mainxinglist) {
			obj2.setClickTime(0);//点击一次
			obj2.setIsxuanzhong(false);
		}
	}
	
	public boolean isSameColorClick(Xing obj){
		for(Xing tmp:preSelectlist){
			if(obj.getId() == tmp.getId())
				return true;
		}
		return false;
	}

	private List<Xing> currentFlashlist = new ArrayList<Xing>();
	int max = 0;
	Xing autoNotice;
	public void autoNotice(){
		autoNotice = null;
		max = 0;
		for (Xing obj : Mainxinglist) {
			if(obj.isAlive()){
				currentFlashlist.clear();
				for (Xing obj1 : Mainxinglist) {
					if(obj1.isAlive()){
						obj1.setIsFlash(false);
					}
				}
				obj.setIsFlash(true);
				setXingFlashList(obj);
				int number = currentFlashlist.size();
				if(max < number){
					max = number;
					autoNotice = obj;
				}
			}
		}
		currentFlashlist.clear();
		for (Xing obj1 : Mainxinglist) {
			if(obj1.isAlive()){
				obj1.setIsFlash(false);
			}
		}
		if(autoNotice != null){
			setXingFlashList(autoNotice);
			for(Xing obj:currentFlashlist){
				//obj.setFlash(true);
				obj.setIsFlash(true);
			}
		}
		isAutoNotic = true;
		
	}

	double startDownTime;
	private int preClickColor =0;
	// 用户点击屏幕事件
	public void onDown(float x, float y) {
		if (iscaozuo) {
			boolean isDoubleClic = Utils.getKey(context, ConstantUtil.SINGLEDOUBLEKEY)==1?true:false;
			for (Xing obj : Mainxinglist) {
				if (obj.isAlive()) {
					if (x > obj.getObject_x()
							&& x < obj.getObject_x() + obj.getObject_width()
							&& y > obj.getObject_y()
							&& y < obj.getObject_y() + obj.getObject_height()) {
						currentSelectlist.clear();
						obj.setIsxuanzhong(true);
						currentSelectlist.add(obj);
						setXingList(obj);
						if (isSelectTwo()) {
							isAutoNotic = false;
							startDownTime = System.currentTimeMillis();
							for(Xing obj3:currentFlashlist){
								obj3.setIsFlash(false);
							}
							if(!isSameColorClick(obj)){
								for (Xing obj2 : Mainxinglist) {
									obj2.setClickTime(0);//点击一次
								}
							}
							if(isDoubleClic){
								for (Xing obj3 : Mainxinglist) {
									if(obj3.isIsxuanzhong()){
										obj3.setClickTime(obj3.getClickTime()+1);
									}
								}
								if(obj.getClickTime() ==1){
									sounds.playSound(1, 0);
								}
							}
							isTouchBlock = true;
							int selectNumber = getSelectNumber();
							if(isDoubleClic && obj.getClickTime() >1){
								sounds.playSound(6, 0);
								guanka.setBlockNumber(selectNumber);
								if(selectNumber >= ConstantUtil.SCORECOOL && selectNumber < ConstantUtil.SCOREGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,1);
								}
								if(selectNumber >= ConstantUtil.SCOREGOOD && selectNumber < ConstantUtil.VERYGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,2);
									sounds.playSound(11, 0);
									bombNumber+=1;
								}
								if(selectNumber >= ConstantUtil.VERYGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,3);
									sounds.playSound(11, 0);
									bombNumber+=3;
								}
								if (selectNumber > ConstantUtil.SCORECOOL) {
									bondNumber+=BONDNUMBERMAX; 
									guanka.setGold(guanka.getGlod()+(selectNumber-BROCK_NUMBER));
									sounds.playSound(2, 0);
								}
							}
							
							if(!isDoubleClic){
								sounds.playSound(6, 0);
								guanka.setBlockNumber(selectNumber);
								if(selectNumber >= ConstantUtil.SCORECOOL && selectNumber < ConstantUtil.SCOREGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,1);
								}
								if(selectNumber >= ConstantUtil.SCOREGOOD && selectNumber < ConstantUtil.VERYGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,2);
									sounds.playSound(11, 0);
									bombNumber+=1;
								}
								if(selectNumber >= ConstantUtil.VERYGOOD){
									lunchGoodScore(screen_width/2,screen_height/7*2,3);
									sounds.playSound(11, 0);
									bombNumber+=3;
								}
								if (selectNumber > ConstantUtil.SCORECOOL) {
									bondNumber+=BONDNUMBERMAX; 
									guanka.setGold(guanka.getGlod()+(selectNumber-BROCK_NUMBER));
									sounds.playSound(2, 0);
								}
							}
							int i = 0;
							for (Xing obj2 : Mainxinglist) {
								if (obj2.isAlive()) {
									if (obj2.isIsxuanzhong()) {
										boolean brock = true;
										if(isDoubleClic && obj2.getClickTime() <2 ){
											brock = false;
										}
										if(brock){
											i++;
											defen = 0;
											defen+=(2*i-1)*5;
											guanka.setDefen(guanka.getDefen()+defen);
											obj2.setAlive(false);
											// 爆炸该视图
											int tx = (int) obj2.getObject_x();
											int ty = (int) obj2.getObject_y();
											//moveScoreText scoretext=new moveScoreText(resources);
											//scoretext.init(defen,tx,ty,guanka.getTargetX(),guanka.getTargetY());
											//moveScoreTexts.add(scoretext);
											bombFireworks(tx,ty,obj.getCurrentColor());
										}
									}
								}
							}
							preClickColor = obj.getColor();
							//guanka.setDefen(guanka.getDefen() + defen);
						} else {
							//if(Utils.getKey(context, ConstantUtil.SINGLEDOUBLEKEY) == 0)
							obj.setIsxuanzhong(false);
						}
						preSelectlist.clear();
						for(Xing tmp:currentSelectlist){
							preSelectlist.add(tmp);
						}
					}else{
						//if(!obj.isIsxuanzhong())
						//	obj.setClickTime(0);
					}
					preClickColor = obj.getColor();
				}
			}
			iscaozuo = false;
			
		}

	}

	/*public void addScoreElement(int score,float x,float y){
		moveScoreText scoretext=new moveScoreText(resources);
		scoretext.init(score,x,y,guanka.getTargetX(),guanka.getTargetY());
		moveScoreTexts.add(scoretext);
	}*/
	// 判断点击星星是否符合两个以上消除条件
	public boolean isSelectTwo() {
		int xzshuliang = 0;
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (obj.isIsxuanzhong()) {
					xzshuliang++;
				}
				if (xzshuliang >= 2) {
					return true;
				}

			}
		}
		xzshuliang = 0;
		return false;
	}

	public void setXingFlashList(Xing xing) {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (xing.getId() != obj.getId()) {
					// 左边同颜色星星
					if (obj.getColor() == xing.getColor()
							&& !obj.IsFlash()) {
						if ((xing.getObject_x() == obj.getObject_x()
								+ obj.getObject_width())
								&& xing.getObject_y() == obj.getObject_y()) {
							currentFlashlist.add(obj);
							obj.setIsFlash(true);
							setXingFlashList(obj);
						}
						// 右边同颜色星星
						if ((xing.getObject_x() == obj.getObject_x()
								- xing.getObject_width())
								&& xing.getObject_y() == obj.getObject_y()) {
							currentFlashlist.add(obj);
							obj.setIsFlash(true);
							setXingFlashList(obj);
						}
						// 上边同颜色星星
						if ((xing.getObject_y() == obj.getObject_y()
								+ obj.getObject_height())
								&& xing.getObject_x() == obj.getObject_x()) {
							currentFlashlist.add(obj);
							obj.setIsFlash(true);
							setXingFlashList(obj);

						}
						// 下边同颜色星星
						if ((xing.getObject_y() == obj.getObject_y()
								- xing.getObject_height())
								&& xing.getObject_x() == obj.getObject_x()) {
							currentFlashlist.add(obj);
							obj.setIsFlash(true);
							setXingFlashList(obj);
						}
					}
				}
			}

		}

	}
	
	// 递归查找符合消除条件颜色一样的星星
	public void setXingList(Xing xing) {
		for (Xing obj : Mainxinglist) {
			if (obj.isAlive()) {
				if (xing.getId() != obj.getId()) {
					// 左边同颜色星星
					if (obj.getColor() == xing.getColor()
							&& !obj.isIsxuanzhong()) {
						if ((xing.getObject_x() == obj.getObject_x()
								+ obj.getObject_width())
								&& xing.getObject_y() == obj.getObject_y()) {
							obj.setIsxuanzhong(true);
							currentSelectlist.add(obj);
							setXingList(obj);
						}
						// 右边同颜色星星
						if ((xing.getObject_x() == obj.getObject_x()
								- xing.getObject_width())
								&& xing.getObject_y() == obj.getObject_y()) {
							obj.setIsxuanzhong(true);
							currentSelectlist.add(obj);
							setXingList(obj);
						}
						// 上边同颜色星星
						if ((xing.getObject_y() == obj.getObject_y()
								+ obj.getObject_height())
								&& xing.getObject_x() == obj.getObject_x()) {
							obj.setIsxuanzhong(true);
							currentSelectlist.add(obj);
							setXingList(obj);

						}
						// 下边同颜色星星
						if ((xing.getObject_y() == obj.getObject_y()
								- xing.getObject_height())
								&& xing.getObject_x() == obj.getObject_x()) {
							obj.setIsxuanzhong(true);
							currentSelectlist.add(obj);
							setXingList(obj);
						}
					}
				}
			}

		}

	}

	public Guanka getGuanka() {
		return guanka;
	}

	public void setGuanka(Guanka guanka) {
		this.guanka = guanka;
	}

	public boolean isShengliorshibai() {
		return shengliorshibai;
	}

	public void setShengliorshibai(boolean shengliorshibai) {
		this.shengliorshibai = shengliorshibai;
	}

	public void release() {
		for (GameObject obj : Mainxinglist) {
			obj.release();
		}
		for(GoodSocre obj:goodArray){
			obj.release();
		}
		goodArray.clear();
		for(Xing obj:Mainxinglist){
			obj.release();
		}
		currentFlashlist.clear();
		currentSelectlist.clear();
		this.Mainxinglist.clear();
		preSelectlist.clear();
		preSelectlist.clear();
		for(int r = 0;r<ConstantUtil.MAXROW;r++){
			for(int j = 0;j<ConstantUtil.MAXCLOUM;j++){
				stars[r][j] = null;
			}
		}
		for(GoodSocre obj:goodArray){
			obj.release();
		}
		/*for(moveScoreText text:moveScoreTexts){
			text.release();
		}*/
	}
}

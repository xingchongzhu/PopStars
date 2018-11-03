package com.develop.ui;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.PopStars.Util.Utils;
import com.develop.constant.ConstantUtil;
import com.develop.object.Button;
import com.develop.object.GameObject;
import com.develop.object.TitleButton;
import com.develop.view.MainView;
import com.develop.PopStars.MainActivity;
import com.develop.PopStars.R;
import com.umeng.analytics.game.UMGameAgent;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

public class Guanka extends GameObject{
	private int zuigaofen;
	private int mubiaofen;
	private int guanka;
	private int defen;
	private int currentScore;
	private int jiangli,syxing;
	private boolean jianglishow;	
	private boolean Startshow;	
	//private Button Restart,menu;//,start; 
	public int targetx = getBl(30);
	public int targety = getBl(170);
	MainView mMainView;
	public GameSoundPool sounds;
	public boolean dieIsShow = false;
	public static int bar_height = 5;
	public static int final_bar_height = 5;
	//public final static int bar_dis = 20;
	boolean isShow = false;
	boolean isDie = false;
	boolean needFlash = false;
	private int preScore = 0;
	public boolean isStartshow() {
		return Startshow;
	}
	public void setStartshow(boolean startshow) {
		Startshow = startshow;
	}
	public boolean isJianglishow() {
		return jianglishow;
	}
	public void setJianglishow(boolean jianglishow) {
		this.jianglishow = jianglishow;
	}
	private int jianglitime;
	private boolean shibaishow=false;
	public boolean isShibaishow() {
		return shibaishow;
	}
	public void setShibaishow(boolean shibaishow) {
		this.shibaishow = shibaishow;
	}
	
	public float getTargetX(){
		if(ScoreButton!= null){
			return ScoreButton.getContentX();
		}
		return 0;
	}
	
	public float getTargetY(){
		if(ScoreButton!= null){
			return ScoreButton.getContentY();
		}
		return 0;
	}
	
	public int getDefen() {
		return defen;
	}
	
	boolean sussed_sound = false;
	public void setDefen(int defen) {		
		this.defen = defen;
	}
	
	public void setRecoverLiveScore(int defen) {		
		this.defen = defen;
		preScore = defen;
	}
	
	int countScore;
	int blockNumber;
	public void setBlockNumber(int blockNumber){
		this.blockNumber = blockNumber;
		countScore = 0;
		Log.d("zxc15","setBlockNumber blockNumber = "+blockNumber);
		for(int i = 1;i<=blockNumber;i++){
			countScore += (2 * i - 1) * 5;
		}
	}
	
	int dis = 5;
	final int final_dis = 5;
	TitleButton bestScoreButton;
	TitleButton ScoreButton;
	TitleButton targetScoreButton;
	TitleButton guankaButton;
	TitleButton pauseButton;
	TitleButton sucessButton;
	TitleButton suplseButton;
	TitleButton dijiButton;

	Bitmap diji_bitmap;
	Bitmap best_bitmap;
	Bitmap pause_bitmap;
	Bitmap score_bitmap;
	Bitmap guanka_bitmap;
	Bitmap target_bitmap;
	Bitmap sucess_bitmap;
	Bitmap luck_bitmap;
	Bitmap suplse_stars;
	float block_x;
	float block_y;
	int glod;
    int sourceBitmapHeight = 55;
    final int finalSourceBitmapHeight = 55;
	
	final float screenWidthMeasure = 720;
	final int textSize = 40;
	int paintSize ;
	
	public void initbutton(){
		Log.d("zxc225", "Guanka initbutton");
		bar_height = (int) (screen_width/screenWidthMeasure*final_bar_height);
		dis = (int) (screen_width/screenWidthMeasure*final_dis);
		sourceBitmapHeight = (int) (screen_width/screenWidthMeasure*finalSourceBitmapHeight);
				//第一行
		best_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.announcement_bg);
		best_bitmap = Utils.getBitmap(best_bitmap,(int) (screen_width*0.5f),sourceBitmapHeight);
		
		paintSize = (int) (screen_width/screenWidthMeasure*textSize);
		
		bestScoreButton = new TitleButton(mMainView.getMainContext().getResources());
		bestScoreButton.init(mMainView.getContext().getString(R.string.best_score),""+zuigaofen,screen_width/2,
				bar_height+best_bitmap.getHeight()/2,best_bitmap.getWidth(),best_bitmap.getHeight(),best_bitmap,paintSize);
		
		//第二行
		paint.setTextSize(paintSize);

		guanka_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.announcement_bg);
		guanka_bitmap = Utils.getBitmap(guanka_bitmap,(int) (screen_width*0.2f),sourceBitmapHeight);

		guankaButton =new TitleButton(mMainView.getMainContext().getResources());
		guankaButton.init(mMainView.getContext().getString(R.string.guanka),""+guanka,(guanka_bitmap.getWidth()+paint.measureText(mMainView.getContext().getString(R.string.guanka)))/2+dis*2,
				bestScoreButton.getObject_y()+bestScoreButton.getObject_height()/2+guanka_bitmap.getHeight(),guanka_bitmap.getWidth(),guanka_bitmap.getHeight(),guanka_bitmap,paintSize);

		target_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.announcement_bg);
		target_bitmap = Utils.getBitmap(target_bitmap,(int) (screen_width*0.4f),sourceBitmapHeight);

		targetScoreButton =new TitleButton(mMainView.getMainContext().getResources());
		targetScoreButton.init(mMainView.getContext().getString(R.string.target_score),""+mubiaofen,screen_width-target_bitmap.getWidth()/2-dis-paint.measureText(mMainView.getContext().getString(R.string.target_score))/2,
				guankaButton.getObject_y(),target_bitmap.getWidth(),target_bitmap.getHeight(),target_bitmap,paintSize);
		targetScoreButton.setFlashSpeed(70);
		
		//菜单
		pause_bitmap =  BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.pause);
		pause_bitmap = Utils.getBitmap(pause_bitmap,(int) (screen_width*0.1f),(int) (screen_width*0.1f));
		pauseButton = new TitleButton(mMainView.getMainContext().getResources());
		pauseButton.init(""," ",screen_width-pause_bitmap.getWidth(),targetScoreButton.getObject_y()-pause_bitmap.getHeight()-target_bitmap.getHeight()/2,pause_bitmap.getWidth(),pause_bitmap.getHeight(),pause_bitmap,paintSize);

		//第三行
		score_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.announcement_bg);
		score_bitmap = Utils.getBitmap(score_bitmap,(int) (screen_width*0.5f),sourceBitmapHeight);

		ScoreButton =new TitleButton(mMainView.getMainContext().getResources());
		ScoreButton.init(" ",""+defen,screen_width/2,
				targetScoreButton.getObject_y()+score_bitmap.getHeight()/2+targetScoreButton.getObject_height(),score_bitmap.getWidth(),score_bitmap.getHeight(),score_bitmap,paintSize);

		
		final int lunckWideth = 80;
		//金币显示
		luck_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.lucky_star);
		float currentWidth = screen_width/720*lunckWideth;
		luck_bitmap = Utils.getBitmap(luck_bitmap,(int)currentWidth,(int)currentWidth);


		//第四行
		block_x = screen_width/2;
		block_y = ScoreButton.getObject_y()+ScoreButton.getObject_height()*2+2*dis;

	
		//第五行过关标识
		sucess_bitmap =  BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.stage_clear);
		float scal = screen_width*0.3f/sucess_bitmap.getWidth();
		sucess_bitmap = Utils.getBitmap(sucess_bitmap,(int) (sucess_bitmap.getWidth()*scal),(int) (sucess_bitmap.getHeight()*scal));
		
		sucessButton = new TitleButton(mMainView.getMainContext().getResources());
		sucessButton.init("","",screen_width/2, block_y-sucess_bitmap.getHeight()/2,sucess_bitmap.getWidth(),sucess_bitmap.getHeight(),sucess_bitmap,paintSize);
		sucessButton.setButtonFlash(true);
		sucessButton.setFlashTime(10);
		
		suplse_stars =  BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.un_thxgiftbagbackpic_2);
		float suplse_scale = screen_width/suplse_stars.getWidth();
		suplse_stars = Utils.getBitmap(suplse_stars,(int) (suplse_stars.getWidth()*suplse_scale),(int) (suplse_stars.getHeight()*suplse_scale));
		suplseButton = new TitleButton(mMainView.getMainContext().getResources());
		suplseButton.init("","",screen_width/2-suplse_stars.getWidth()/2, screen_height/2,suplse_stars.getWidth(),suplse_stars.getHeight(),suplse_stars,paintSize);

		//关卡提示
		diji_bitmap = BitmapFactory.decodeResource(mMainView.getContext().getResources(), R.drawable.guanka);
		dijiButton = new TitleButton(mMainView.getMainContext().getResources());
		dijiButton.init("",""+guanka,screen_width/2, screen_height/2,diji_bitmap.getWidth(),diji_bitmap.getHeight(),diji_bitmap,paintSize);
	}
	public Guanka(Resources resources, int zuigaofen, int mubiaofen, int guanka,MainView mMainView,GameSoundPool sounds) {
		super(resources);
		this.sounds = sounds;
		this.zuigaofen = zuigaofen;
		this.mubiaofen = mubiaofen;
		this.guanka = guanka;
		this.mMainView = mMainView;
	}

	public Guanka(Resources resources,MainView mMainView,GameSoundPool sounds) {
		super(resources);
		this.sounds = sounds;
		guanka = -1;
		mubiaofen = 1000;
		this.mMainView = mMainView;
	}

	public void init(){
		jianglishow = false;
		isShow = false;
		isDie = false;
		guanka=1;
		sussed_sound = false;
		mubiaofen=1000;
		defen=0;
		currentScore = defen;
		glod = Utils.getKey(mMainView.getContext(), ConstantUtil.GOLDKEY);
		needFlash = true;
	}

	public int getmbf(int guanka){
		int zengzhangfen=0;
		int mubiaofen=0;
		for(int i=1;i<=guanka;i++){	
			if(i==1){
				zengzhangfen=1000;
			}else if(i%3==0){
				zengzhangfen=3000;
			}else if(i>10){
				zengzhangfen=4000;
			}else{
				zengzhangfen=2000;
			}
			mubiaofen=mubiaofen+zengzhangfen;
			
		}
		return mubiaofen;
	}
	public void guankanext(){		
		jianglishow = false;
		isShow = false;
		shibaishow = false;
		dijiguan = false;
		isDie = false;
		this.jiangli = 0;
		this.guanka++;		
		int jibie=guanka/3;				
		sussed_sound = false;
		needFlash = true;
		preScore = defen;
		this.mubiaofen=getmbf(guanka);
		UMGameAgent.startLevel(""+guanka);
	}
	
	public int getPreScore(){
		return preScore;
	}
	public int getZuigaofen() {
		return zuigaofen;
	}
	public void setZuigaofen(int zuigaofen) {
		this.zuigaofen = zuigaofen;
	}
	public int getMubiaofen() {
		return mubiaofen;
	}
	public void setMubiaofen(int mubiaofen) {
		this.mubiaofen = mubiaofen;
	}
	public int getGuanka() {
		return guanka;
	}
	public void setGuanka(int guanka) {
		this.guanka = guanka;
	}
	public int getBl(int Num){
		return (int) (Num*screen_width/480);
	}
	
	public int getGlod(){
		return glod;
	}

	public void setGold(int glod){
		this.glod = glod;
	}

	public int setJiangli(int score){
		int count = 0;
		//2000-剩余方块数*剩余方块数*20
		if(score >=10)
			return count;
		count = 2000 - score*score*20;
		jiangli = count;
		return count;
	}
	
	public boolean isSucess(){
		return defen >= mubiaofen;
	}
	
	public void setCurrentScore(int currentScore){
		this.currentScore = currentScore;
	}
	
	public int getCurrentScore(){
		return currentScore;
	}
	boolean dijiguan;
	public void setShowGuanka(boolean dijiguan){
		this.dijiguan = dijiguan;
	}
	public void draw(Canvas canvas) {	
		canvas.save();
		if(needFlash && !shibaishow){
			targetScoreButton.setFlash(true);
			targetScoreButton.setFlashTime(5);
			needFlash = false;
		}
		if(dijiguan){
			//dijiButton.setContent(""+(guanka+1));
			//dijiButton.draw(canvas);
			
			paint.setTextSize( (int) (screen_width/screenWidthMeasure*50));
			paint.setColor(resources.getColor(R.color.text_glod_color));
			Typeface font = Typeface.create("宋体", Typeface.ITALIC);
			paint.setTypeface(font);
			String target_guanka = String.format(resources.getString(R.string.target_guanka),(guanka+1));
			String target_socre = String.format(resources.getString(R.string.target_socre),getmbf(guanka+1));
			canvas.drawText(target_guanka, screen_width/2-paint.measureText(target_guanka)/2, screen_height/2, paint); //绘制文字
			canvas.drawText(target_socre,screen_width/2-paint.measureText(target_socre)/2, screen_height/2+screen_width/screenWidthMeasure*80, paint); //绘制文字	
		}
		if(mMainView.getAllBlockInitFinash()){
			paint.setColor(Color.WHITE);
			Typeface font = Typeface.create("宋体", Typeface.NORMAL);
			paint.setTypeface(font);
			paint.setTextSize(paintSize);
			pauseButton.draw(canvas);
			bestScoreButton.setContent(""+zuigaofen);
			guankaButton.setContent(""+guanka);
			targetScoreButton.setContent(""+mubiaofen);
			if(currentScore < defen){
				currentScore++;
			}
			ScoreButton.setContent(""+currentScore);
			bestScoreButton.draw(canvas);
			guankaButton.draw(canvas);
			targetScoreButton.draw(canvas);
			ScoreButton.draw(canvas);
			paint.setColor(Color.WHITE);
			String text = String.format(resources.getString(R.string.block_score) ,blockNumber, countScore);
			canvas.drawText(text,screen_width/2-paint.measureText(text)/2,block_y, paint); //绘制文字
			canvas.drawBitmap(luck_bitmap, luck_bitmap.getWidth()/2-dis, guankaButton.getObject_y()+guankaButton.getObject_height()+luck_bitmap.getHeight(), paint);
			Rect rect1 = new Rect();
			paint.getTextBounds(""+glod, 0, 1, rect1);
			canvas.drawText(""+glod, (luck_bitmap.getWidth()/2-dis+luck_bitmap.getWidth()), guankaButton.getObject_y()+guankaButton.getObject_height()+luck_bitmap.getHeight()+luck_bitmap.getHeight()+dis, paint);
						
		}
		if(defen >= mubiaofen && !sussed_sound){
			sussed_sound = true;
			sounds.playSound(10,0);
		}
		
		if(sussed_sound && sucess_bitmap != null){
			sucessButton.draw(canvas);
		//	canvas.drawBitmap(sucess_bitmap, screen_width/2-sucess_bitmap.getWidth()/2, block_y+10, paint);
		}
		if((jianglishow || shibaishow) && !dijiguan){			
			int count = setJiangli(syxing);
			paint.setTextSize( (int) (screen_width/screenWidthMeasure*50));
			paint.setColor(resources.getColor(R.color.text_glod_color));
			Typeface font = Typeface.create("宋体", Typeface.ITALIC);
			paint.setTypeface(font);
			String reward = resources.getString(R.string.reward)+" "+count;
			String surplus = String.format(resources.getString(R.string.surplus),syxing);
			suplseButton.draw(canvas);
			canvas.drawText(reward, screen_width/2-paint.measureText(reward)/2, screen_height/2, paint); //绘制文字
			canvas.drawText(surplus,screen_width/2-paint.measureText(surplus)/2, screen_height/2+screen_width/screenWidthMeasure*80, paint); //绘制文字	
		}
		if(jianglishow && !isShow){
			isShow = true;
			int count = setJiangli(syxing);
			defen+=count;
			Log.d("zxc22", "draw defen = "+defen+" mubiaofen = "+mubiaofen+" syxing = "+syxing);
			if(defen >= mubiaofen){
	        	glod+= guanka*2;
	        	shibaishow = false; 
	        	sounds.playSound(11, 0);
	        }else{
	        	shibaishow = true; 
	        }
			mMainView.delayUpdateBlockBomb();
			UMGameAgent.finishLevel(""+guanka);
		}
		if(shibaishow && !isDie){
			isDie = true;
			Utils.saveKey(mMainView.getContext(), ConstantUtil.BESTSCOREKEY, zuigaofen);
	        Utils.saveKey(mMainView.getContext(), ConstantUtil.GOLDKEY, glod);
	        mMainView.delayUpdateBlockBomb();
	        UMGameAgent.failLevel(""+guanka);
		}
				
		canvas.restore();
	}
	
	public boolean isDie(){
		return isDie;
	}
	public boolean getIsDie(){
		return shibaishow;
	}

	public String onDown(float x,float y){	
		Log.d("zxc119", "onDown");
		if(pauseButton.isClick(x, y)){
			Log.d("zxc119", "onDown is click");
			mMainView.showMenuDialog();
		}
		return "";
	}
	
	public void showGuanka(){
		
	}

	public boolean isdianji(float x,float y,Button obj){
		if(x > obj.getObject_x() && x < obj.getObject_x() + obj.getObject_width() && y < obj.getObject_y() && y > obj.getObject_y() - obj.getObject_height()){
			return true;
		}
		return false;
	}

	//获得剩余星星
	public int getSyxing() {
		return syxing;
	}

	//设置剩余星星
	public void setSyxing(int syxing) {
		this.syxing = syxing;
	}

	public void setjiangli(){
		jianglishow=true;
		/*if(jiangli-200>=0){
			jiangli=jiangli-200;
		}*/
	}
	
	@Override
	protected void initBitmap() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(best_bitmap != null && best_bitmap.isRecycled()){
			best_bitmap.recycle();
		}
		if(score_bitmap != null && score_bitmap.isRecycled()){
			score_bitmap.recycle();
		}
		if(guanka_bitmap != null && guanka_bitmap.isRecycled()){
			guanka_bitmap.recycle();
		}
		if(target_bitmap != null && target_bitmap.isRecycled()){
			target_bitmap.recycle();
		}
		if(sucess_bitmap != null && sucess_bitmap.isRecycled()){
			sucess_bitmap.recycle();
		}
	}
}

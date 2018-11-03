package com.develop.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

public class moveScoreText extends GameObject{
	private int btText;
	Bitmap bitmap = null;
	float targetx;
	float targety;
	float mvx ;
	float mvy;
	float mvz;
	float dismv = 1;
	float cos;
	float tan;
	public moveScoreText(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}
	
	public int getBtText() {
		return btText;
	}
	
	public void setBitmap(Bitmap mBitmap) {
		bitmap = mBitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}

	public void setBtText(int btText) {
		this.btText = btText;
	}
	
    float speed = 8;
   // float time = 100f;
	public void init(int bttext,float obj_x,float obj_y,float targetx,float targety){
		this.btText=bttext;
		currentx=obj_x;
		currenty=obj_y;
		this.object_x=obj_x;
		this.object_y=obj_y;
		this.targetx = targetx;
		this.targety = targety;
		mvx = (targetx-object_x);
		mvy = (targety-object_y);
		this.setAlive(true);
		mvz = (float) Math.sqrt(Math.abs(mvx)*Math.abs(mvx)+Math.abs(mvy)*Math.abs(mvy));
		//speed = mvz/time;
		cos = Math.abs(mvx)/mvz;
		paint.setTextSize(35);
		paint.setColor(Color.WHITE);//rgb(235, 161, 1)
		Typeface font = Typeface.create("宋体", Typeface.BOLD);
		paint.setTypeface( font );
	}
	@Override
	protected void initBitmap() {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void release() {
		// TODO Auto-generated method stub
		if(bitmap != null){
			bitmap.recycle();
		}
		
	}


	public void draw(Canvas canvas,int paintsize) {
		//paint.setTextSize(paintsize);
		
		
	}

	float currentx;
	float currenty;
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mvx > 0){//左边
			currentx = currentx+speed;
		}else{//右边
			currentx= currentx-speed;
		}
		currenty= object_y+(currentx-object_x)*(targety-object_y)/(targetx-object_x);
		canvas.drawText("+"+btText, currentx,currenty, paint); //绘制文字
		if(targety >= currenty){
			this.setAlive(false);
		}
	}	
	
}

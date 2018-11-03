package com.develop.object;

import com.develop.PopStars.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class Button extends GameObject{
	private String btText;
	Bitmap bitmap = null;
	private String content;
	public Button(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getBtText() {
		return btText;
	}
	
	public void setBitmap(Bitmap mBitmap) {
		bitmap = mBitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}

	public void setBtText(String btText) {
		this.btText = btText;
	}
	public void init(String bttext,float obj_x,float obj_y,float obj_w,float obj_h){
		this.btText=bttext;
		this.object_x=obj_x;
		this.object_y=obj_y;
		this.object_width=obj_w;
		this.object_height=obj_h;
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
		paint.setTextSize(paintsize);
		paint.setColor(Color.rgb(235, 161, 1));
		Typeface font = Typeface.create("宋体", Typeface.BOLD);
		paint.setTypeface(font);
		Rect rect = new Rect();
		paint.getTextBounds(this.getBtText(), 0, 1, rect);
		if(bitmap != null){
			canvas.drawBitmap(bitmap, this.getObject_x(), this.getObject_y(), paint);
			canvas.drawText(this.getBtText(), this.getObject_x()+bitmap.getWidth()/2-paint.measureText(getBtText())/2,
					this.getObject_y()+bitmap.getHeight()/2+rect.height()/2, paint); //绘制文字
		}else{
			canvas.drawText(this.getBtText(), this.getObject_x(),this.getObject_y(), paint); //绘制文字
		}
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}	
	
}

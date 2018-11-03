package com.develop.object;

import com.develop.PopStars.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class TitleButton extends GameObject {
	private String btText;
	Bitmap bitmap = null;
	private String content;
	private boolean needFlash = false;
	private boolean buttonFlash = false;

	public TitleButton(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}

	public void setFlash(boolean flash) {
		needFlash = flash;
	}
	
	public void setButtonFlash(boolean flash) {
		buttonFlash = flash;
	}

	public String getBtText() {
		return btText;
	}

	public String getContent() {
		return content;
	}

	public void setBitmap(Bitmap mBitmap) {
		bitmap = mBitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public float getWidth() {
		return bitmap.getWidth() + paint.measureText(btText);
	}

	public void setBtText(String btText) {
		this.btText = btText;
	}

	float content_x;
	float content_y;
	float title_x;
	float title_y;
	float bitmap_x;
	float bitmap_y;
	int color = Color.rgb(235, 161, 1);
	float prex;
	float prey;
	
	public void setPreX(float x){
		this.prex = x;
	}
	public void setPrey(float y){
		this.prey = y;
	}
	
	public void setPainColor(int color){
		this.color = color;
	}
	
	public void init(String bttext, String content, float obj_x, float obj_y,
			float obj_w, float obj_h, Bitmap mBitmap, int paintsize) {
		paint.setTextSize(paintsize);
		paint.setColor(color);
		Typeface font = Typeface.create("����", Typeface.BOLD);
		paint.setTypeface(font);

		this.btText = bttext;
		this.object_x = obj_x;
		this.object_y = obj_y;
		this.object_width = obj_w;
		this.object_height = obj_h;
		this.content = content;
		this.bitmap = createFramedPhoto(mBitmap.getWidth(),mBitmap.getHeight(),mBitmap, 50);
		if (this.content == null || this.content.isEmpty()) {
			this.content = " ";
		}
		if (this.btText == null || this.btText.isEmpty()) {
			this.btText = " ";
		}
		Rect rect1 = new Rect();
		paint.getTextBounds(this.content, 0, 1, rect1);
		Rect rect = new Rect();
		paint.getTextBounds(btText, 0, 1, rect);
		bitmap_x = object_x - bitmap.getWidth() / 2 + paint.measureText(btText)
				/ 2;
		bitmap_y = object_y + bitmap.getHeight() / 2;
		prex = bitmap_x;
		prey = bitmap_y;
		title_x = bitmap_x - paint.measureText(getBtText())-5;
		title_y = bitmap_y + object_height / 2 + rect.height() / 7*2;
		content_x = bitmap_x + object_width / 2 - paint.measureText(content)/ 2;
		content_y = bitmap_y + object_height / 2 + rect1.height();
	}

	public boolean isClick(float x,float y){
		if(bitmap != null && x >= bitmap_x && x <= bitmap_x+bitmap.getWidth() && y >= bitmap_y && y<= bitmap_y+bitmap.getHeight()){
			return true;
		}
		return false;
	}
	@Override
	protected void initBitmap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	int dis = 5;

	public void draw(Canvas canvas, int paintsize) {

	}
	
	public void setFlashTime(int n){
		maxFlashtime = n;
	}
	
	public void setFlashSpeed(int n){
		flashSpeed =n;
	}

	int flashSpeed = 50;
	int alpha = 255;
	boolean isIncrease = false;
	int maxFlashtime = 5;
	boolean enable = true;
	public void setAlpha(int alp){
		alpha = alp;
	}
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	boolean breath = false;
	public void setBreath(boolean breath){
		this.breath = breath;
	}
	int minAlpha = 80;
	boolean flag = false;
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Rect rect1 = new Rect();
		paint.setColor(color);
		if(!enable){
			paint.setAlpha(alpha);
		}
		if(breath){
			if(alpha >= 255){
				flag = true;
			}
			if(alpha <= minAlpha){
				flag = false;
			}
			if(flag)
				alpha-=10;
			else
				alpha+=10;
			if(alpha >255)
				alpha = 255;
			if(alpha < minAlpha)
				alpha =minAlpha;
			paint.setAlpha(alpha);
		}
		if (bitmap != null) {

			paint.getTextBounds(content, 0, 1, rect1);
			content_x = bitmap_x + object_width / 2
					- paint.measureText(content) / 2;
			content_y = bitmap_y + object_height / 2 + rect1.height() / 2;
			if(buttonFlash){
				startFlash(buttonFlash);
				canvas.drawBitmap(bitmap, prex, prey, paint);
				canvas.drawText(this.getBtText(), title_x, title_y, paint); // ��������
				canvas.drawText(content, content_x, content_y, paint); // ��������
			}else if(needFlash){
				canvas.drawBitmap(bitmap,  prex, prey, paint);
				canvas.drawText(this.getBtText(), title_x, title_y, paint); // ��������
				startFlash(needFlash);
				canvas.drawText(content, content_x, content_y, paint); // ��������
			}else{
				canvas.drawBitmap(bitmap, prex, prey, paint);
				canvas.drawText(this.getBtText(), title_x, title_y, paint); // ��������
				canvas.drawText(content, content_x, content_y, paint); // ��������
			}
			paint.setAlpha(alpha);
		} else {
			canvas.drawText(this.getBtText(), this.getObject_x(),
					this.getObject_y(), paint); // ��������
		}
	}
	
	/**
    *
    * @param x ͼ��Ŀ��
    * @param y ͼ��ĸ߶�
    * @param image ԴͼƬ
    * @param outerRadiusRat Բ�ǵĴ�С
    * @return Բ��ͼƬ
    */
   Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
       //����Դ�ļ��½�һ��darwable����
       Drawable imageDrawable = new BitmapDrawable(image);

       // �½�һ���µ����ͼƬ
       Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
       Canvas canvas = new Canvas(output);

       // �½�һ������
       RectF outerRect = new RectF(0, 0, x, y);

       // ����һ����ɫ��Բ�Ǿ���
       Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
       paint.setColor(Color.RED);
       canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

       // ��ԴͼƬ���Ƶ����Բ�Ǿ�����
       paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
       imageDrawable.setBounds(0, 0, x, y);
       canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
       imageDrawable.draw(canvas);
       canvas.restore();

       return output;
   }
	
	public float getContentX(){
		return content_x;
	}
	
	public float getContentY(){
		return content_y;
	}
	
	public void startFlash(boolean flash){
		if (flash) {
			if (alpha <= 0) {
				maxFlashtime--;
				isIncrease = true;
			} else if (alpha >= 255) {
				isIncrease = false;
			}
			if (isIncrease) {
				alpha += flashSpeed;;
			} else {
				alpha -= flashSpeed;
			}
			if (alpha <= 0) {
				alpha = 0;
			}
			if (alpha > 255) {
				alpha = 255;
			}
			paint.setAlpha(alpha);
			if(maxFlashtime < 0){
				buttonFlash = false;
				needFlash = false;
			}
		} else {
			paint.setAlpha(255);
		}
	}

}

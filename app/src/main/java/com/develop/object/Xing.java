package com.develop.object;

import java.util.List;
import java.util.Random;

import com.develop.GameSoundPool.GameSoundPool;
import com.develop.constant.ConstantUtil;
import com.develop.view.MainView;
import com.develop.PopStars.R;
import com.develop.PopStars.Util.Utils;

import android.R.integer;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Xing extends GameObject {
	private int id;
	private int color;
	private Bitmap xingbitmap;
	private List<Xing> xinglist;
	private boolean isyundongdown;
	private boolean isyundongleft;
	private boolean isxuanzhong;
	private boolean isstop;
	public int starColorId;
	private int clickTime = 0;

	public void setClickTime(int clickTime){
		this.clickTime = clickTime;
	}

	public int getClickTime(){
		return clickTime;
	}

	public Xing(Resources resources) {
		super(resources);
		this.isAlive = true;
		isyundongdown = true;
		isstop = false;
		// TODO Auto-generated constructor stub
		Random random = new Random();
		this.color = random.nextInt(5);

	}

	public Xing(Resources resources, int color, boolean live) {
		super(resources);
		this.isAlive = live;
		isyundongdown = true;
		isstop = false;
		// TODO Auto-generated constructor stub
		this.color = color;
	}

	public void setWH(int Width, int Height) {
		this.object_width = Width;
		this.object_height = Height;
		initBitmap();
	}

	private static Bitmap big(Bitmap bitmap, float bl) {
		Matrix matrix = new Matrix();
		matrix.postScale(bl, bl); // ���Ϳ�Ŵ���С�ı���
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	public void initBitmap() {
		switch (color) {
		case ConstantUtil.RED: {
			xingbitmap = BitmapFactory.decodeResource(resources,
					R.drawable.block_red);
			starColorId = R.drawable.su_pink_block;

			break;
		}
		case ConstantUtil.GREEN: {
			xingbitmap = BitmapFactory.decodeResource(resources,
					R.drawable.block_green);
			starColorId = R.drawable.su_lightgreen_block;

			break;
		}
		case ConstantUtil.BLUE: {
			xingbitmap = BitmapFactory.decodeResource(resources,
					R.drawable.block_blue);
			starColorId = R.drawable.su_blue_block;

			break;
		}
		case ConstantUtil.YELLOW: {
			xingbitmap = BitmapFactory.decodeResource(resources,
					R.drawable.block_yellow);
			starColorId = R.drawable.su_yellow_block;

			break;
		}
		case ConstantUtil.PINK: {
			xingbitmap = BitmapFactory.decodeResource(resources,
					R.drawable.block_purple);
			starColorId = R.drawable.su_purple_block;

			break;
		}

		}
		xingbitmap = Utils.getBitmap(xingbitmap, (int) (object_width),
				(int) (object_height));

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void init(int id, float x, float y) {
		this.id = id;
		this.object_x = x;
		this.object_y = y;
	}

	// �����������ƶ��߼��ж�
	public void logic(List<Xing> xinglist, GameSoundPool sounds) {
		this.xinglist = xinglist;
		if (this.isAlive) {
			if (!isCollision()) {
				isyundongdown = true;
				this.object_y = this.object_y + this.speed;
			} else {
				if (!isstop) {
					// sounds.playSound(5, 0);
				}
				isyundongdown = false;
			}
		}
	}

	// ���ڵײ����������ƶ��߼�
	public List<Xing> logicLeft(List<Xing> xinglist) {
		this.xinglist = xinglist;
		if (!isleftCollision()) {
			isyundongleft = true;
		} else {
			isyundongleft = false;
		}
		return this.xinglist;
	}

	// ���ڵײ������ж��Ƿ���������Ƿ�����ײ
	public boolean isleftCollision() {
		if (this.isAlive) {
			if (this.object_y + this.object_height == screen_height) {
				Xing leftxing = getLeftXing();
				if (leftxing != null) {
					if (this.object_x - this.speed > leftxing.getObject_x()
							+ leftxing.getObject_width()) {
						setObj_x(this.speed);
						return false;
					} else {
						setObj_x(this.object_x
								- (leftxing.object_x + leftxing.object_width));
						return true;
					}
				} else if (this.object_x != 0) {
					if (this.object_x - this.speed > 0) {
						setObj_x(this.speed);
						return false;
					} else {
						setObj_x(0);
						return true;
					}
				}
			}
		}
		return true;
	}

	// �����Լ��ұ����Ǽ��������ƶ�
	public void setObj_x(float speed) {
		for (Xing obj : xinglist) {
			if (this.isAlive) {
				if (obj.getObject_x() >= this.object_x) {
					obj.setObject_x(obj.getObject_x() - speed);
				}
			}
		}
	}

	// ���ڵײ����Ƿ����Լ���ߵ�����
	public Xing getLeftXing() {
		Xing myxing = null;
		for (Xing obj : xinglist) {
			if (obj.isAlive()) {
				if (obj.object_y + obj.object_height == screen_height
						&& obj.object_x < this.object_x) {
					if (myxing != null) {
						if (obj.object_x > myxing.object_x) {
							myxing = obj;
						}
					} else {
						myxing = obj;
					}
				}
			}

		}
		return myxing;
	}

	// ���������ƶ����Լ�����������ײ���
	public boolean isCollision() {
		if (this.object_y + this.object_height >= screen_height) {
			isstop = true;
			return true;
		}
		for (Xing obj : xinglist) {
			if (obj.isAlive()) {
				if (this.id != obj.getId()) {
					if (this.object_x == obj.object_x) {
						if (((this.object_y + this.object_height + this.speed) >= obj.object_y)
								&& ((this.object_y + this.object_height) <= obj.object_y)) {
							if (this.object_y == obj.object_y
									- this.object_height) {
								isstop = true;
							}
							this.object_y = obj.object_y - this.object_height;
							return true;
						}
					}
				}
			}
		}

		if (this.object_y + this.object_height + this.speed >= screen_height) {
			if (this.object_y == screen_height - this.object_height) {
				isstop = true;
			}
			this.object_y = screen_height - this.object_height;
			return true;
		}
		isstop = false;
		return false;
	}

	// ���ڵײ����������ƶ����Լ�����������ײ���
	public float isCollisionLeft() {
		if (this.object_x == 0) {
			return 0;
		}
		for (Xing obj : xinglist) {
			if (obj.isAlive()) {
				if (this.id != obj.getId()) {
					if (this.object_y == obj.object_y) {
						if (((this.object_x - this.speed) <= (obj.object_x + obj.object_width))
								&& ((this.object_x) >= (obj.object_x + obj.object_width))) {
							return this.object_x
									- (obj.getObject_x() + obj
											.getObject_width());
						}
					}
				}
			}
		}
		if (this.object_x - this.speed < 0) {
			return this.object_x;
		}

		return this.speed;
	}

	public void setFlash(boolean flash) {
		needFlash = flash;
	}
	
	public void setHasFlashTime(int time){
		flashTime = time;
	}

	public boolean needFlash = false;
	private int alpha = 255;
	boolean isIncrease = false;
	final int flshSpeed = 255;
	int timecount = 0;
	int distime = 5;
	int flashTime = 0;
	
	boolean flag = false;
	int increase = 0;
	int increaseRange = 10;
	float top = object_x;
	float left = object_y;
	float bottom = object_x + object_width;
	float right = object_y + object_height;
	int minAlpha = 100;
	@Override
	public void draw(Canvas canvas) {
		// Log.d("zxc","Xing draw this.isAlive = " + this.isAlive);
		if (this.isAlive) {
			canvas.save();
			paint.setColor(Color.WHITE);
			if (needFlash) {
				if(timecount >distime){
					if (alpha <= 0) {
						isIncrease = true;
					} else if (alpha >= 255) {
						isIncrease = false;
					}
					if (isIncrease) {
						alpha += flshSpeed;
					} else {
						alpha -= flshSpeed;
					}
					if (alpha <= 0) {
						alpha = 0;
					}
					if (alpha > 255) {
						alpha = 255;
					}
					timecount = 0;
					flashTime++;
				}else{
					timecount++;
				}
				paint.setAlpha(alpha);
			} else{
				paint.setAlpha(255);
			}
			if(flashTime >13){
				needFlash = false;
			}
			left = object_x;
			top = object_y;
			right = object_x + object_width;
			bottom = object_y + object_height;
			if(isFlash){
				/*if(increase >increaseRange)
					flag = true;
				if(increase < 0)
					flag = false;
				if(flag){
					increase-=0.1f;
				}else{
					increase+=0.1f;
				}
				if(flag){//��С
					alpha-=1;
					left+= increase;
					top+= increase;
					right-=increase;
					bottom-=increase;
				}else{
					alpha+=1;
					left-= increase;
					top-= increase;
					right+=increase;
					bottom+=increase;
				}*/
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
			}
			
			canvas.clipRect(left, top, right,bottom);
			if(clickTime >=1){
				float dis = 2f;//object_width*0.05f;
				Rect rect = canvas.getClipBounds();
				paint.setAntiAlias(true);// ���û��ʵľ��Ч��  
				float radio = 23;
			    RectF oval3 = new RectF(rect.left-dis, rect.top-dis, rect.right+dis, rect.bottom+dis);// ���ø��µĳ�����  
			    //paint.setAlpha(230);
			    canvas.drawRoundRect(oval3, radio, radio, paint);//�ڶ���������x�뾶��������������y�뾶  
			}
			paint.setAlpha(alpha);
			canvas.drawBitmap(xingbitmap, object_x, object_y, paint);
			canvas.restore();
		}
	}
	
	/**
     * ��bitmap���ñ߿�
     * @param canvas
     */
	int dis = 2;
    private void setBitmapBorder(Canvas canvas){
        Rect rect = canvas.getClipBounds();
        rect.left = rect.left+dis;
        rect.top = rect.top+dis;
        rect.right = rect.right+dis;
        rect.bottom = rect.bottom+dis;
        
        RectF r2 = new RectF();
        r2.left =  rect.left;
        r2.right = rect.top;
        r2.top = rect.right ;
        r2.bottom = rect.bottom;
        //���ñ߿���ɫ
        paint.setColor(Color.WHITE);
        //paint.setStyle(Paint.Style.STROKE);
        //���ñ߿���
        paint.setStrokeWidth(1);
        canvas.drawRoundRect(r2,20,20, paint);
    }

	public boolean isIsxuanzhong() {
		return isxuanzhong;
	}

	public void setIsxuanzhong(boolean isxuanzhong) {
		this.isxuanzhong = isxuanzhong;
	}
	
	boolean isFlash;
	public void setIsFlash(boolean isFlash) {
		this.isFlash = isFlash;
		alpha = 255;
	}
	
	public boolean IsFlash(){
		return isFlash;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public int getCurrentColor(){
		return starColorId;
	}

	public boolean isIsyundongdown() {
		return isyundongdown;
	}

	public void setIsyundongdown(boolean isyundongdown) {
		this.isyundongdown = isyundongdown;
	}

	public boolean isIsyundongleft() {
		return isyundongleft;
	}

	public void setIsyundongleft(boolean isyundongleft) {
		this.isyundongleft = isyundongleft;
	}

	@Override
	public void release() {
		if (!xingbitmap.isRecycled()) {
			xingbitmap.recycle();
		}

	}

}

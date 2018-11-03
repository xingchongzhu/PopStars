package com.develop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import com.develop.GameSoundPool.GameSoundPool;


/**
 * Created by wayww on 2016/9/8.
 */
public class FlashScal extends View {

    private final String TAG = this.getClass().getSimpleName();
   
    Context context;
    GameSoundPool sounds;
    public FlashScal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public FlashScal(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FlashScal(Context context,GameSoundPool sounds) {
        super(context);
        this.context = context;
        this.sounds =sounds;
    }
    
    //~~~~~~~~~~~~~private method~~~~~~~~~~~~~~~~~~~


    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void lunchFireWork(float x, float y, int direction,int mode,int color){
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	//canvas.drawBitmap(bitmap, matrix, paint);
    }


}

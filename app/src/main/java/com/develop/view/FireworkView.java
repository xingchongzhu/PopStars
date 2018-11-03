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
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import com.develop.GameSoundPool.GameSoundPool;


/**
 * Created by wayww on 2016/9/8.
 */
public class FireworkView extends View {

    private final String TAG = this.getClass().getSimpleName();
    private LinkedList<Firework> fireworks = new LinkedList<Firework>();
    private int windSpeed;
    private TextWatcher mTextWatcher;
    Context context;
    GameSoundPool sounds;
    private float srceenWidth;
	private float screenHeight;
	
    public FireworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public FireworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FireworkView(Context context,GameSoundPool sounds) {
        super(context);
        this.context = context;
        this.sounds =sounds;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        srceenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }
    
    //~~~~~~~~~~~~~private method~~~~~~~~~~~~~~~~~~~


    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void lunchFireWork(float x, float y, int direction,int mode,int color){
        final Firework firework = new Firework(new Firework.Location(x, y), direction,mode,context,sounds,color,srceenWidth,screenHeight);
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
	
	public void removeAllFireWork(){
		if(fireworks != null){
			for (Firework obj:fireworks){
	            fireworks.remove(obj);
	            obj.release();
	        }
		}
	}

    @Override
    protected void onDraw(Canvas canvas) {
    	Log.d("zxc118","onDraw fireworks.size() = "+fireworks.size());
        for (int i =0 ; i<fireworks.size(); i++){
            fireworks.get(i).draw(canvas);
        }
        if (fireworks.size()>0)
            invalidate();
    }


}

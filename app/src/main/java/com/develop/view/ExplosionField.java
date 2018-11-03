/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.develop.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.develop.PopStars.Util.Utils;


/**
 * 爆炸区域
 */
public class ExplosionField extends View {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //爆炸的动画
    private List<ExplosionAnimator> mExplosions = new ArrayList<>();
    private int[] mExpandInset = new int[2];
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //ctor
    public ExplosionField(Context context) {
        super(context);
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Arrays.fill(mExpandInset, Utils.dp2Px(32));
    }

    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("zxc12","ExplosionField draw");
        //这里配合Explosion Animator的draw互相调用 知道用完动画的播放时间
        for (ExplosionAnimator explosion : mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void expandExplosionBound(int dx, int dy) {
        mExpandInset[0] = dx;
        mExpandInset[1] = dy;
    }


    public void explode(Bitmap bitmap, Rect bound, long startDelay, long duration,
    		Context context,int mode,int color) {

        //产生爆炸的动画 并且启动它
        final ExplosionAnimator explosion = new ExplosionAnimator(this, bitmap, bound,context,mode,color);
        explosion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExplosions.remove(animation);
            }
        });
        explosion.setStartDelay(startDelay);
        explosion.setDuration(duration);
        mExplosions.add(explosion);
        explosion.start();
    }

    /**
     * 引爆view
     * @param view 即将被引爆的view
     */

    private int i = 0;
    public void explode(final View view,Context context,Rect r,int mode,int starColor) {

        //获得它被可见的区域
        //Rect r = new Rect(400,500,600,800);
        //view.getGlobalVisibleRect(r);

        //获得当前视图在屏幕中的位置
        int[] location = new int[2];
        getLocationOnScreen(location);

        //偏移rect 但是我没能理解这个意思
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);

        int startDelay = 100;

        //这个动画使得view “振动”
       /* ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });
        animator.start();

        //让其逐渐变小 然后消失
        view.animate().setDuration(150)
                .setStartDelay(startDelay)
                .scaleX(0f).scaleY(0f)
                .alpha(0f).start();*/

        //爆炸相关的视图
        if(ExplosionAnimator.MODEN_SMALL == mode){
        	explode(null,
                    r,
                    50,
                    ExplosionAnimator.SMALL_DEFAULT_DURATION
            ,context,mode,starColor);
        }else{
        	explode(null,
                    r,
                    startDelay,
                    ExplosionAnimator.DEFAULT_DURATION
            ,context,mode,starColor);
        }
        
    }

    public void clear() {
        mExplosions.clear();
        invalidate();
    }

    //获得爆炸区域
    public static ExplosionField attach2Window(Activity activity,ViewGroup rootView) {

        //获得MainActivity layout的 根布局的父布局
        //在activity中 setContentView 会在当前布局文件外再套一个父布局
        //ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        ExplosionField explosionField = new ExplosionField(activity);

        //将爆炸区域添加到其中
        //ExplosionField extents View
        rootView.addView(explosionField, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //返回爆炸区域
        return explosionField;
    }
}

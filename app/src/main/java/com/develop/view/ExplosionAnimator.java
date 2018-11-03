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

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Random;

import com.develop.PopStars.Util.Utils;
import com.develop.PopStars.R;


public class ExplosionAnimator extends ValueAnimator {

    static long DEFAULT_DURATION = 0xfff;
    static long SMALL_DEFAULT_DURATION = 0x600;
    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.7f);
    private static final Interpolator SMALL_DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.3f);
    private static final float END_VALUE = 1.4f;
    private static final float SMALL_END_VALUE = 1.5f;
    private static final float X = Utils.dp2Px(5);
    private static final float Y = Utils.dp2Px(20);
    private static final float V = Utils.dp2Px(2);
    private static final float W = Utils.dp2Px(1);

    int randColor[] = {R.drawable.redstar,R.drawable.bluestar,R.drawable.greenstar,R.drawable.pinkstar,R.drawable.yellowstar};
    private Paint mPaint;
    private Particle[] mParticles;
    private Rect mBound;
    private View mContainer;
    
    Context context;

    int bigStars = 15;
    int smallStars = 2;
    public static int MODEN_SMALL = 1;
    public static int MODEN_BIG = 2;
    public ExplosionAnimator(View container, Bitmap bitmap, Rect bound,Context context,int mode,int color) {
    	this.context = context;
        mPaint = new Paint();
        mBound = new Rect(bound);
        mContainer = container;
        
        int partLen = smallStars;
        if(MODEN_SMALL == mode){
        	partLen = smallStars;
        	setFloatValues(0f, SMALL_END_VALUE);
            setInterpolator(SMALL_DEFAULT_INTERPOLATOR);
            setDuration(SMALL_DEFAULT_DURATION);
        }else{
        	partLen = bigStars;
        	setFloatValues(0f, END_VALUE);
            setInterpolator(DEFAULT_INTERPOLATOR);
            setDuration(DEFAULT_DURATION);
        }
        mParticles = new Particle[partLen * partLen];
        Random random = new Random(System.currentTimeMillis());
        if(MODEN_SMALL == mode){
	        for (int i = 0; i < partLen; i++) {
	            for (int j = 0; j < partLen; j++) {
	                mParticles[(i * partLen) + j] = generateParticle(0, random,color);
	            }
	        }
        }else{
        	for (int i = 0; i < partLen; i++) {
	            for (int j = 0; j < partLen; j++) {
	            	int color1 = randColor[(int) (Math.random()* 5)];
	            	if(color1 >= 5 && color1 < 0)
	            		color1 =1;
	                mParticles[(i * partLen) + j] = generateParticleBig(0, random,color1);
	            }
	        }
        }
    }
    
    private Particle generateParticleBig(int color, Random random,int starcColor) {

        Particle particle = new Particle();
        particle.color = color;
        particle.radius = V;

        if (random.nextFloat() < 0.2f) {
            particle.baseRadius = V + ((X - V) * random.nextFloat());
        } else {
            particle.baseRadius = W + ((V - W) * random.nextFloat());
        }

        InputStream is = context.getResources().openRawResource(starcColor);  
        particle.mBitmap = BitmapFactory.decodeStream(is);
        float nextFloat = random.nextFloat();
        particle.top = mBound.height() * ((0.18f * random.nextFloat()) + 0.2f);
        particle.top = nextFloat < 0.2f ? particle.top : particle.top + ((particle.top * 0.2f) * random.nextFloat());
        particle.bottom = (mBound.height() * (random.nextFloat() - 0.5f)) * 1.8f;
        float f = nextFloat < 0.2f ? particle.bottom : nextFloat < 0.8f ? particle.bottom * 0.6f : particle.bottom * 0.3f;
        particle.bottom = f;
        particle.mag = 4.0f * particle.top / particle.bottom;
        particle.neg = (-particle.mag) / particle.bottom;
        f = mBound.centerX() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCx = f;
        particle.cx = f;
        f = mBound.centerY() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCy = f;
        particle.cy = f;
        particle.life = END_VALUE / 10 * random.nextFloat();
        particle.overflow = 0.3f * random.nextFloat();
        particle.alpha = 1f;
        return particle;
    }

    private Particle generateParticle(int color, Random random,int starcColor) {

        Particle particle = new Particle();
        particle.color = color;
        particle.radius = V;

        if (random.nextFloat() < 0.2f) {
            particle.baseRadius = V + ((X - V) * random.nextFloat());
        } else {
            particle.baseRadius = W + ((V - W) * random.nextFloat());
        }

        InputStream is = context.getResources().openRawResource(starcColor);  
        particle.mBitmap = BitmapFactory.decodeStream(is);
        float nextFloat = random.nextFloat();
        particle.top = mBound.height() * ((0.18f * random.nextFloat()) + 0.2f);
        particle.top = nextFloat < 0.2f ? particle.top : particle.top + ((particle.top * 0.2f) * random.nextFloat());
        particle.bottom = (mBound.height() * (random.nextFloat() - 0.5f)) * 1.8f;
        float f = nextFloat < 0.2f ? particle.bottom : nextFloat < 0.8f ? particle.bottom * 0.6f : particle.bottom * 0.3f;
        particle.bottom = f;
        particle.mag = 4.0f * particle.top / particle.bottom;
        particle.neg = (-particle.mag) / particle.bottom;
        f = mBound.centerX() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCx = f;
        particle.cx = f;
        f = mBound.centerY() + (Y * (random.nextFloat() - 0.5f));
        particle.baseCy = f;
        particle.cy = f;
        particle.life = END_VALUE / 10 * random.nextFloat();
        particle.overflow = 0.3f * random.nextFloat();
        particle.alpha = 1f;
        return particle;
    }

    public boolean draw(Canvas canvas) {
    	Log.d("zxc12","ExplosionAnmi draw");
        if (!isStarted()) {
            return false;
        }

        for (Particle particle : mParticles) {
            particle.advance((float) getAnimatedValue());
            if (particle.alpha > 0f) {
               
                canvas.drawBitmap(particle.mBitmap, particle.cx, particle.cy, mPaint);
            }
        }
        mContainer.invalidate();
        return true;
    }

    @Override
    public void start() {
        super.start();
        //鏉╋拷锟斤拷锟斤拷锟斤拷view 锟斤拷 draw 娴滐拷锟芥瓕锟斤拷锟�?
        mContainer.invalidate(mBound);
    }

    private class Particle {
        float alpha;
        int color;
        float cx;
        float cy;
        float radius;
        float baseCx;
        Bitmap mBitmap;
        float baseCy;
        float baseRadius;
        float top;
        float bottom;
        float mag;
        float neg;
        float life;
        float overflow;

        public void advance(float factor) {
            float f = 0f;
            Log.d("hello11","advance factor = "+factor);
            float normalization = factor / END_VALUE;
            if (normalization < life || normalization > 1f - overflow) {
                alpha = 0f;
                return;
            }

            normalization = (normalization - life) / (1f - life - overflow);
            float f2 = normalization * END_VALUE;
            if (normalization >= 0.7f) {
                f = (normalization - 0.7f) / 0.3f;
            }
            alpha = 1f - f;
            f = bottom * f2;
            cx = baseCx + f;
            cy = (float) (baseCy - this.neg * Math.pow(f, 2.0)) - f * mag;
            radius = V + (baseRadius - V) * f2;
        }
    }
}

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
import com.develop.object.moveScoreText;
import com.develop.PopStars.R;


public class ScoreMoveAnimator extends ValueAnimator {

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

    private Paint mPaint;
    private Rect mBound;
    private View mContainer;
    moveScoreText mmoveScoreText;
    Context context;

    int bigStars = 15;
    int smallStars = 2;
    public static int MODEN_SMALL = 1;
    public static int MODEN_BIG = 2;
    public ScoreMoveAnimator(View mContainer ,moveScoreText mmoveScoreText) {
    	this.mContainer = mContainer;
        mPaint = new Paint();
        this.mmoveScoreText = mmoveScoreText;
        mBound = new Rect(0,0,800,1200);
    }
    
  
    public boolean draw(Canvas canvas) {
    	//Log.d("zxc12","ExplosionAnmi draw");
        if (!isStarted()) {
            return false;
        }
        if(mmoveScoreText != null){
        	mmoveScoreText.draw(canvas);
        }
        mContainer.invalidate();
        return true;
    }

    @Override
    public void start() {
        super.start();
        mContainer.invalidate(mBound);
    }

}

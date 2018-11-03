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
package com.develop.PopStars.Util;

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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.develop.constant.ConstantUtil;
import com.develop.object.Xing;
import com.develop.view.MainView;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Utils {

	private Utils() {
	}

	/**
	 * 
	 */
	private static final float DENSITY = Resources.getSystem()
			.getDisplayMetrics().density;
	/**
	 *
	 */
	private static final Canvas sCanvas = new Canvas();

	private static final String dbNmae = "clearStar";

	/* @author sichard
    * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
    * @return
    */ 
   public static final boolean ping() { 
    
       String result = null; 
       try { 
               String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网 
               Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次 
               // 读取ping的内容，可以不加 
               InputStream input = p.getInputStream(); 
               BufferedReader in = new BufferedReader(new InputStreamReader(input)); 
               StringBuffer stringBuffer = new StringBuffer(); 
               String content = ""; 
               while ((content = in.readLine()) != null) { 
                       stringBuffer.append(content); 
               } 
               Log.d("ping", "result content : " + stringBuffer.toString()); 
               // ping的状态 
               int status = p.waitFor(); 
               if (status == 0) { 
                       result = "success"; 
                       return true; 
               } else { 
                       result = "failed"; 
               } 
       } catch (IOException e) { 
               result = "IOException"; 
       } catch (InterruptedException e) { 
               result = "InterruptedException"; 
       } finally { 
               Log.d("----result---", "result = " + result); 
       } 
       return false;
	}
   
   public static boolean isNetworkAvailable(Context context){
       // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
       ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       
       if (connectivityManager == null)
       {
           return false;
       }
       else
       {
           // 获取NetworkInfo对象
           NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
           
           if (networkInfo != null && networkInfo.length > 0)
           {
               for (int i = 0; i < networkInfo.length; i++)
               {
                   System.out.println(i + "===状态===" + networkInfo[i].getState());
                   System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                   // 判断当前网络状态是否为连接状态
                   if (networkInfo[i].getState() == State.CONNECTED)
                   {
                       return true;
                   }
               }
           }
       }
       return false;
   }
	/**
	 * 
	 * @param dp
	 * @return
	 */
	public static int dp2Px(int dp) {
		return Math.round(dp * DENSITY);
	}

	public static void saveKey(Context ct, String key, int value) {
		if (ct != null) {
			SharedPreferences mSharedPreferences = ct.getSharedPreferences(
					dbNmae, Context.MODE_PRIVATE);
			Editor editor = mSharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}
	}

	public static void clearShare(Context context) {
		SharedPreferences share = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = share.edit();
		editor.clear().commit();
		/*
		 * int n = 0; for(int i=0;i<ConstantUtil.MAXROW;i++){ for(int
		 * j=0;j<ConstantUtil.MAXCLOUM;j++){
		 * editor.remove(ConstantUtil.STARKEY+(n++)); editor.commit(); } }
		 */
	}

	public static boolean saveObjectToShare(Context context, StarObject object,
			String key) {
		// TODO Auto-generated method stub
		SharedPreferences share = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (object == null) {
			Editor editor = share.edit().remove(key);
			return editor.commit();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		String objectStr = new String(Base64.encode(baos.toByteArray(),
				Base64.DEFAULT));
		try {
			baos.close();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Editor editor = share.edit();
		editor.putString(key, objectStr);

		return editor.commit();
	}

	public static Xing getObjectFromShare(Context context, String key,
			float screen_width, float screen_height, int xwidth, int xheight,
			int id) {
		SharedPreferences sharePre = PreferenceManager
				.getDefaultSharedPreferences(context);
		try {
			String wordBase64 = sharePre.getString(key, "");
			if (wordBase64 == null || wordBase64.equals("")) {
				return null;
			}
			byte[] objBytes = Base64.decode(wordBase64.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			StarObject obj = (StarObject) ois.readObject();
			bais.close();
			ois.close();
			Xing xing = new Xing(context.getResources(), obj.getColor(),
					obj.getLive());
			xing.setScreenWH(screen_width, screen_height);
			xing.init(id, obj.getX(), (obj.getY()));
			xing.setWH(xwidth, xheight);
			return xing;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getKey(Context ct, String key) {
		int n = 0;
		if (ct != null) {
			SharedPreferences mSharedPreferences = ct.getSharedPreferences(
					dbNmae, Context.MODE_PRIVATE);
			n = mSharedPreferences.getInt(key, 0);
		}
		return n;
	}

	public static int getKeyDefault(Context ct, String key) {
		int n = 0;
		if (ct != null) {
			SharedPreferences mSharedPreferences = ct.getSharedPreferences(
					dbNmae, Context.MODE_PRIVATE);
			n = mSharedPreferences.getInt(key, 1);
		}
		return n;
	}

	/**
	 * 
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
			int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (screenHight <= 0) {
			screenHight = bitmap.getHeight();
		}
		if (screenWidth <= 0) {
			screenWidth = bitmap.getWidth();
		}
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;
		// scale = scale < scale2 ? scale : scale2;
		matrix.postScale(scale, scale2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	public static Bitmap createBitmapFromView(View view) {

		if (view instanceof ImageView) {
			Drawable drawable = ((ImageView) view).getDrawable();
			if (drawable != null && drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}
		}

		view.clearFocus();

		Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(),
				Config.ARGB_8888, 1);

		if (bitmap != null) {
			synchronized (sCanvas) {

				Canvas canvas = sCanvas;
				canvas.setBitmap(bitmap);

				view.draw(canvas);

				canvas.setBitmap(null);
			}
		}

		return bitmap;
	}

	public static Bitmap createBitmapSafely(int width, int height,
			Config config, int retryCount) {
		try {

			return Bitmap.createBitmap(width, height, config);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();

			System.gc();
			return createBitmapSafely(width, height, config, retryCount - 1);
		}
	}

	/**
	 * 画出指定形状的图片
	 * 
	 * @param cpBitmap
	 * @param radius
	 * @return
	 */
	public static Bitmap drawShapeBitmap(Bitmap bmp, int radius,
			String shape_type) {
		// TODO Auto-generated method stub
		Bitmap squareBitmap;// 根据传入的位图截取合适的正方形位图
		Bitmap scaledBitmap;// 根据diameter对截取的正方形位图进行缩放
		int diameter = radius * 2;
		// 传入位图的宽高
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int squarewidth = 0, squareheight = 0;// 矩形的宽高
		int x = 0, y = 0;
		if (h > w) {// 如果高>宽
			squarewidth = squareheight = w;
			x = 0;
			y = (h - w) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squarewidth,
					squareheight);
		} else if (h < w) {// 如果宽>高
			squarewidth = squareheight = h;
			x = (w - h) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squarewidth,
					squareheight);
		} else {
			squareBitmap = bmp;
		}
		// 对squareBitmap进行缩放为diameter边长的正方形位图
		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);
		} else {
			scaledBitmap = squareBitmap;
		}
		Bitmap outputbmp = Bitmap.createBitmap(scaledBitmap.getWidth(),
				scaledBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outputbmp);// 创建一个相同大小的画布
		Paint paint = new Paint();// 定义画笔
		paint.setAntiAlias(true);// 设置抗锯齿
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		if ("star".equals(shape_type)) {// 如果绘制的形状为五角星形
			Path path = new Path();
			float radian = degree2Radian(45);// 36为五角星的角度
			float radius_in = (float) (radius * Math.sin(radian / 2) / Math
					.cos(radian)); // 中间五边形的半径
			path.moveTo((float) (radius * Math.cos(radian / 2)), 0);// 此点为多边形的起点
			path.lineTo((float) (radius * Math.cos(radian / 2) + radius_in
					* Math.sin(radian)),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) * 2),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) + radius_in
					* Math.cos(radian / 2)),
					(float) (radius + radius_in * Math.sin(radian / 2)));
			path.lineTo(
					(float) (radius * Math.cos(radian / 2) + radius
							* Math.sin(radian)), (float) (radius + radius
							* Math.cos(radian)));
			path.lineTo((float) (radius * Math.cos(radian / 2)),
					(float) (radius + radius_in));
			path.lineTo(
					(float) (radius * Math.cos(radian / 2) - radius
							* Math.sin(radian)), (float) (radius + radius
							* Math.cos(radian)));
			path.lineTo((float) (radius * Math.cos(radian / 2) - radius_in
					* Math.cos(radian / 2)),
					(float) (radius + radius_in * Math.sin(radian / 2)));
			path.lineTo(0, (float) (radius - radius * Math.sin(radian / 2)));
			path.lineTo((float) (radius * Math.cos(radian / 2) - radius_in
					* Math.sin(radian)),
					(float) (radius - radius * Math.sin(radian / 2)));
			path.close();// 使这些点构成封闭的多边形
			canvas.drawPath(path, paint);
		} else if ("triangle".equals(shape_type)) {// 如果绘制的形状为三角形
			Path path = new Path();
			path.moveTo(0, 0);
			path.lineTo(diameter / 2, diameter);
			path.lineTo(diameter, 0);
			path.close();
			canvas.drawPath(path, paint);
		} else if ("heart".equals(shape_type)) {// 如果绘制的形状为心形
			Path path = new Path();
			path.moveTo(diameter / 2, diameter / 5);
			path.quadTo(diameter, 0, diameter / 2, diameter / 1.0f);
			path.quadTo(0, 0, diameter / 2, diameter / 5);
			path.close();
			canvas.drawPath(path, paint);
		} else {// 这是默认形状，圆形
			// 绘制圆形
			canvas.drawCircle(scaledBitmap.getWidth() / 2,
					scaledBitmap.getHeight() / 2, scaledBitmap.getWidth() / 2,
					paint);
		}
		// 设置Xfermode的Mode
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(scaledBitmap, 0, 0, paint);

		bmp = null;
		squareBitmap = null;
		scaledBitmap = null;

		return outputbmp;

	}

	/**
	 * 
	 * 角度转弧度公式
	 * 
	 * 
	 * 
	 * @param degree
	 * 
	 * @return
	 */

	private static float degree2Radian(int degree) {

		// TODO Auto-generated method stub

		return (float) (Math.PI * degree / 180);

	}

}

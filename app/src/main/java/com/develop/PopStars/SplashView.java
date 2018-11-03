package com.develop.PopStars;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;
import cn.waps.extend.AppDetail;

/**
 * Created by jkyeo on 16/7/7.
 */
public class SplashView extends FrameLayout {

    ImageView splashImageView;
    TextView skipButton;
    private static AppDetail adDetail;
	private final Handler mHandler = new Handler();
    private static final String IMG_URL = "splash_img_url";
    private static final String ACT_URL = "splash_act_url";
    private static String IMG_PATH = null;
    private static final String SP_NAME = "splash";
    private static final int skipButtonSizeInDip = 36;
    private static final int skipButtonMarginInDip = 16;
    private Integer duration = 6;
    private static final int delayTime = 1000;   // 每隔1000 毫秒执行一次

    private String imgUrl = null;
    private String actUrl = null;

    private boolean isActionBarShowing = true;

    private Activity mActivity = null;

    private OnSplashViewActionListener mOnSplashViewActionListener = null;

    private Handler handler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (0 == duration) {
                dismissSplashView(false);
                return;
            } else {
                setDuration(--duration);
            }
            handler.postDelayed(timerRunnable, delayTime);
        }
    };

    private void setImage(Bitmap image) {
        splashImageView.setImageBitmap(image);
    }

    public SplashView(Activity context) {
        super(context);
        mActivity = context;
        initComponents();
    }

    public SplashView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = context;
        initComponents();
    }

    public SplashView(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = context;
        initComponents();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SplashView(Activity context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mActivity = context;
        initComponents();
    }

    private GradientDrawable splashSkipButtonBg = new GradientDrawable();
    SplashView splashView ;
    void initComponents() {
    	splashView = this;
        splashSkipButtonBg.setShape(GradientDrawable.OVAL);
        splashSkipButtonBg.setColor(Color.parseColor("#66333333"));

        splashImageView = new ImageView(mActivity);
        splashImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        splashImageView.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
        LayoutParams imageViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(splashImageView, imageViewLayoutParams);
        splashImageView.setClickable(true);
        handler.postDelayed(timerRunnable, delayTime);
    }

    private void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    private void setDuration(Integer duration) {
        this.duration = duration;
        if(skipButton != null)
        	skipButton.setText(String.format(mActivity.getString(R.string.skip), duration));
    }

    private void setOnSplashImageClickListener(@Nullable final OnSplashViewActionListener listener) {
        if (null == listener) return;
        mOnSplashViewActionListener = listener;
        splashImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSplashImageClick(actUrl);
            }
        });
    }

    /**
     * static method, show splashView on above of the activity
     * you should called after setContentView()
     * @param activity  activity instance
     * @param durationTime  time to countDown
     * @param defaultBitmapRes  if there's no cached bitmap, show this default bitmap;
     *                          if null == defaultBitmapRes, then will not show the splashView
     * @param listener  splash view listener contains onImageClick and onDismiss
     */
    
    public void showSplashView(@NonNull Activity activity,
                                      @Nullable Integer durationTime,
                                      @Nullable Integer defaultBitmapRes,
                                      @Nullable OnSplashViewActionListener listener,AdInfo adInfo) {

        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (null == contentView || 0 == contentView.getChildCount()) {
            throw new IllegalStateException("You should call showSplashView() after setContentView() in Activity instance");
        }
        IMG_PATH = activity.getFilesDir().getAbsolutePath().toString() + "/splash_img.jpg";
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashView.setOnSplashImageClickListener(listener);
        if (null != durationTime) splashView.setDuration(durationTime);
        Bitmap bitmapToShow = null;

        if (isExistsLocalSplashData(activity)) {
            bitmapToShow = BitmapFactory.decodeFile(IMG_PATH);
            SharedPreferences sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            splashView.setImgUrl(sp.getString(IMG_URL, null));
            splashView.setActUrl(sp.getString(ACT_URL, null));
        } else if (null != defaultBitmapRes) {
            bitmapToShow = BitmapFactory.decodeResource(activity.getResources(), defaultBitmapRes);
        }

        if (null == bitmapToShow) return;
        splashView.setImage(bitmapToShow);
       /* activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (activity instanceof Activity) {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (null != actionBar) {
                splashView.isActionBarShowing = actionBar.isShowing();
                actionBar.hide();
            }
        }*/
        contentView.addView(splashView, param);
        showAdDetail(activity,adInfo);
    }
    
    public void showAdDetail(final Context context, final AdInfo adInfo) {
    	/*ViewGroup contentView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        if (null == contentView || 0 == contentView.getChildCount()) {
            throw new IllegalStateException("You should call showSplashView() after setContentView() in Activity instance");
        }*/
		try {
			//final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
			if (adInfo != null) {
				View view = View.inflate(context,
						context.getResources().getIdentifier("detail", "layout", context.getPackageName()), null);
				ImageView icon = (ImageView) view.findViewById(
						context.getResources().getIdentifier("detail_icon", "id", context.getPackageName()));
				TextView title = (TextView) view.findViewById(
						context.getResources().getIdentifier("detail_title", "id", context.getPackageName()));
				TextView version = (TextView) view.findViewById(
						context.getResources().getIdentifier("detail_version", "id", context.getPackageName()));
				TextView filesize = (TextView) view.findViewById(
						context.getResources().getIdentifier("detail_filesize", "id", context.getPackageName()));
				Button downButton1 = (Button) view.findViewById(
						context.getResources().getIdentifier("detail_downButton1", "id", context.getPackageName()));
				TextView content = (TextView) view.findViewById(
						context.getResources().getIdentifier("detail_content", "id", context.getPackageName()));
				TextView description = (TextView) view.findViewById(
						context.getResources().getIdentifier("detail_description", "id", context.getPackageName()));
				ImageView image1 = (ImageView) view.findViewById(
						context.getResources().getIdentifier("detail_image1", "id", context.getPackageName()));
				ImageView image2 = (ImageView) view.findViewById(
						context.getResources().getIdentifier("detail_image2", "id", context.getPackageName()));
				Button downButton2 = (Button) view.findViewById(
						context.getResources().getIdentifier("detail_downButton2", "id", context.getPackageName()));

				icon.setImageBitmap(adInfo.getAdIcon());
				icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
				title.setText(adInfo.getAdName());
				title.setTextSize(17);
				version.setText("  " + adInfo.getVersion());
				filesize.setText("  " + adInfo.getFilesize() + "M");
				content.setText(adInfo.getAdText());
				description.setText(adInfo.getDescription());

				new GetImagesTask(context, adInfo, image1, image2).execute();

				downButton1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AppConnect.getInstance(context).downloadAd(context, adInfo.getAdId());
						/*if (dialog != null) {
							dialog.cancel();
						}*/
					}
				});
				downButton2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AppConnect.getInstance(context).downloadAd(context, adInfo.getAdId());
						/*if (dialog != null) {
							dialog.cancel();
						}*/
					}
				});

				int bg_img = context.getResources().getIdentifier("detail_bg", "drawable", context.getPackageName());

				if (bg_img != 0) {
					view.setBackgroundResource(bg_img);
				} else {
					view.setBackgroundResource(android.R.drawable.editbox_background);
				}

				LinearLayout layout = new LinearLayout(context);
				layout.setGravity(Gravity.CENTER);
				layout.setId(1);

				// 对小屏手机进行屏幕判断
				int displaySize = SDKUtils.getDisplaySize(context);
				if (displaySize == 320) {
					layout.setPadding(15, 15, 15, 15);
				} else if (displaySize == 240) {
					layout.setPadding(10, 10, 10, 10);
				} else {
					layout.setPadding(20, 20, 20, 20);
				}

				//layout.setBackgroundColor(Color.argb(200, 10, 10, 10));

				layout.addView(view);
				
				splashView.addView(layout);
				
				skipButton = new TextView(mActivity);
		        int skipButtonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonSizeInDip, mActivity.getResources().getDisplayMetrics());
		        LayoutParams skipButtonLayoutParams = new LayoutParams(skipButtonSize, skipButtonSize);
		        skipButtonLayoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
		        int skipButtonMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonMarginInDip, mActivity.getResources().getDisplayMetrics());
		        skipButtonLayoutParams.setMargins(0, skipButtonMargin, skipButtonMargin, 0);
		        skipButton.setGravity(Gravity.CENTER);
		        skipButton.setTextColor(mActivity.getResources().getColor(android.R.color.white));
		        skipButton.setBackgroundDrawable(splashSkipButtonBg);
		        skipButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		        this.addView(skipButton, skipButtonLayoutParams);

		        skipButton.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                dismissSplashView(true);
		            }
		        });
				//contentView.addView(layout);
				//contentView.setContentView(layout);
				//dialog.show();
			}else{
				skipButton = new TextView(mActivity);
		        int skipButtonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonSizeInDip, mActivity.getResources().getDisplayMetrics());
		        LayoutParams skipButtonLayoutParams = new LayoutParams(skipButtonSize, skipButtonSize);
		        skipButtonLayoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
		        int skipButtonMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipButtonMarginInDip, mActivity.getResources().getDisplayMetrics());
		        skipButtonLayoutParams.setMargins(0, skipButtonMargin, skipButtonMargin, 0);
		        skipButton.setGravity(Gravity.CENTER);
		        skipButton.setTextColor(mActivity.getResources().getColor(android.R.color.white));
		        skipButton.setBackgroundDrawable(splashSkipButtonBg);
		        skipButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		        this.addView(skipButton, skipButtonLayoutParams);

		        skipButton.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                dismissSplashView(true);
		            }
		        });
			}

		} catch (Exception e) {
			e.printStackTrace();
			AppConnect.getInstance(context).clickAd(context, adInfo.getAdId());
		}
		setDuration(duration);
	}
    /**
     * simple way to show splash view, set all non-able param as non
     * @param activity
     */
    public  void simpleShowSplashView(@NonNull Activity activity,AdInfo adInfo) {
        showSplashView(activity, null, null, null,adInfo);
    }

    private class GetImagesTask extends AsyncTask<Void, Void, Boolean> {
		Bitmap bitmap1;
		Bitmap bitmap2;
		AdInfo adInfo;
		ImageView image1;
		ImageView image2;
		Context context;

		public GetImagesTask(Context context, AdInfo adInfo, ImageView image1, ImageView image2) {
			this.adInfo = adInfo;
			this.image1 = image1;
			this.image2 = image2;
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean returnValue = false;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			HttpURLConnection conn1 = null;
			HttpURLConnection conn2 = null;
			try {
				conn1 = (HttpURLConnection) new URL(adInfo.getImageUrls()[0].replaceAll(" ", "%20")).openConnection();
				conn1.connect();
				bitmap1 = BitmapFactory.decodeStream(conn1.getInputStream(), null, opts);
				conn2 = (HttpURLConnection) new URL(adInfo.getImageUrls()[1].replaceAll(" ", "%20")).openConnection();
				conn2.connect();
				bitmap2 = BitmapFactory.decodeStream(conn2.getInputStream(), null, opts);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return returnValue;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				if (bitmap1 != null && bitmap2 != null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							try {
								int displaySize = SDKUtils.getDisplaySize(context);
								if (((Activity) context).getResources()
										.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
									if (displaySize == 320) {
										image1.setLayoutParams(new LinearLayout.LayoutParams(130, 210));
										image2.setLayoutParams(new LinearLayout.LayoutParams(130, 210));
									} else if (displaySize == 240) {
										image1.setLayoutParams(new LinearLayout.LayoutParams(100, 150));
										image2.setLayoutParams(new LinearLayout.LayoutParams(100, 150));

									}
								} else {
									if (displaySize == 320) {
										image1.setLayoutParams(new LinearLayout.LayoutParams(210, 350));
										image2.setLayoutParams(new LinearLayout.LayoutParams(210, 350));
									} else if (displaySize == 240) {
										image1.setLayoutParams(new LinearLayout.LayoutParams(140, 210));
										image2.setLayoutParams(new LinearLayout.LayoutParams(140, 210));
									}
								}
								image1.setImageBitmap(bitmap1);
								image2.setImageBitmap(bitmap2);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    private void dismissSplashView(boolean initiativeDismiss) {
        if (null != mOnSplashViewActionListener) mOnSplashViewActionListener.onSplashViewDismiss(initiativeDismiss);


        handler.removeCallbacks(timerRunnable);
        final ViewGroup parent = (ViewGroup) this.getParent();
        if (null != parent) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(SplashView.this, "scale", 0.0f, 0.5f).setDuration(600);
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (Float) animation.getAnimatedValue();
                    SplashView.this.setAlpha(1.0f - 2.0f * cVal);
                    SplashView.this.setScaleX(1.0f + cVal);
                    SplashView.this.setScaleY(1.0f + cVal);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    parent.removeView(SplashView.this);
                    showSystemUi();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    parent.removeView(SplashView.this);
                    showSystemUi();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private void showSystemUi() {
       /* mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (mActivity instanceof Activity) {
            android.app.ActionBar actionBar = mActivity.getActionBar();
            if (null != actionBar) {
                if (isActionBarShowing) actionBar.show();
            }
        }*/
    }

    private static boolean isExistsLocalSplashData(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String imgUrl = sp.getString(IMG_URL, null);
        return !TextUtils.isEmpty(imgUrl) && isFileExist(IMG_PATH);
    }

    /**
     * static method, update splash view data
     * @param imgUrl - url of image which you want to set as splash image
     * @param actionUrl - related action url, such as webView etc.
     */
    public static void updateSplashData(@NonNull Activity activity, @NonNull String imgUrl, @Nullable String actionUrl) {
        IMG_PATH = activity.getFilesDir().getAbsolutePath().toString() + "/splash_img.jpg";

        SharedPreferences.Editor editor = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(IMG_URL, imgUrl);
        editor.putString(ACT_URL, actionUrl);
        editor.apply();

        getAndSaveNetWorkBitmap(imgUrl);
    }

    public interface OnSplashViewActionListener {
        void onSplashImageClick(String actionUrl);
        void onSplashViewDismiss(boolean initiativeDismiss);
    }

    private static void getAndSaveNetWorkBitmap(final String urlString) {
        Runnable getAndSaveImageRunnable = new Runnable() {
            @Override
            public void run() {
                URL imgUrl = null;
                Bitmap bitmap = null;
                try {
                    imgUrl = new URL(urlString);
                    HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.connect();
                    InputStream is = urlConn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    saveBitmapFile(bitmap, IMG_PATH);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(getAndSaveImageRunnable).start();
    }

    private static void saveBitmapFile(Bitmap bm, String filePath) throws IOException {
        File myCaptureFile = new File(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static boolean isFileExist(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }
}

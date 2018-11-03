package com.develop.view;

import com.develop.PopStars.R;
import com.develop.constant.ConstantUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CustomDialog extends Dialog {

	private static boolean isDie = false;
	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        
        switch(event.getKeyCode()){
        case KeyEvent.KEYCODE_BACK:           
        	this.dismiss();
            break;
        default:
            break;
        }
        return super.dispatchKeyEvent(event);
    }

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private TextView messageTextView;
		
		private int glod;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private TextView glodTextView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private OnClickListener makeScoreButtonClickListener;
		private OnClickListener feedBackClickListener;
		private OnClickListener checkEditionClickListener;
		
		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			if(messageTextView != null)
				messageTextView.setText(message);
			return this;
		}
		
		public void setGlod(int glod) {
			this.glod = glod;
			if(glodTextView != null){
				glodTextView.setText(""+glod);
			}
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setFeedBack(OnClickListener listener) {
			this.feedBackClickListener = listener;
			return this;
		}
		
		public Builder setcheckEdition(OnClickListener listener) {
			this.checkEditionClickListener = listener;
			return this;
		}
		
		
		

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public Builder setMakeScoreButton(OnClickListener listener) {
			this.makeScoreButtonClickListener = listener;
			return this;
		}
	
		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			isDie = false;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			if(layout.findViewById(R.id.title) != null)
				((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			Button button = ((Button) layout.findViewById(R.id.positiveButton));
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			messageTextView =(TextView) layout.findViewById(R.id.message);
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(false); 
			return dialog;
		}
		
		public CustomDialog create(int layoutid,int t) {
			isDie = true;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,R.style.Dialog);
			View layout = inflater.inflate(layoutid, null);
			RelativeLayout mRelativeLayout = (RelativeLayout)layout.findViewById(R.id.background);
			if(mRelativeLayout != null)
				mRelativeLayout.getBackground().setAlpha(200);
			
			
			if (feedBackClickListener != null) {
				((TextView) layout.findViewById(R.id.feedbackButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Log.d("zxc224", "setFeedBackbbbb");
								feedBackClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							}
						});
			}
			
			if (checkEditionClickListener != null) {
				((TextView) layout.findViewById(R.id.checkout_new_edition))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Log.d("zxc224", "setcheckEditionbbbb");
								checkEditionClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							}
						});
			}
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setContentView(layout);
			return dialog;
		}

		public CustomDialog create(int layoutid) {
			isDie = true;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,R.style.Dialog);
			View layout = inflater.inflate(layoutid, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			if(layout.findViewById(R.id.title) != null)
				((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			
			if (makeScoreButtonClickListener != null) {
				((ImageView) layout.findViewById(R.id.make_score))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								makeScoreButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);
							}
						});
			}
			
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			messageTextView =(TextView) layout.findViewById(R.id.message);
			glodTextView = (TextView)layout.findViewById(R.id.glodnumber);
			if(glodTextView != null)
				glodTextView.setText(""+glod);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			Button button = ((Button) layout.findViewById(R.id.positiveButton));
			if(glod < ConstantUtil.RECOVERLIVE && button != null){
				button.setEnabled(false);
				button.setBackgroundResource(R.drawable.btn_not_selector);
				button.setTextColor(context.getColor(R.color.text_gray_color));
			}
			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(false); 
			return dialog;
		}

	}
}

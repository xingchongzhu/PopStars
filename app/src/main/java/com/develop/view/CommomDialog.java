package com.develop.view;

import com.develop.PopStars.R;
import com.develop.PopStars.Util.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommomDialog extends Dialog {

	public CommomDialog(Context context) {
		super(context);
	}

	public CommomDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        
        switch(event.getKeyCode()){
        case KeyEvent.KEYCODE_BACK:
        	//Toast.makeText(getContext(), "dadf", 1).show();
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
		private String resumeButtonText;
		private String doubleButtonText;
		private String voiceButtonText;
		private Button voiceButton;
		private Button singleButton;
		private TextView voiceText;
		private TextView doubleText;
		private View contentView;
		int voiceDrableresid = R.drawable.open_voice;
		int singleDrableresid = R.drawable.double_click1;
		private OnClickListener resumeButtonClickListener;
		private OnClickListener doubleButtonClickListener;
		private OnClickListener voiceButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public void setVoiceDrable(int resid){
			voiceDrableresid = resid;
			if(voiceButton != null){
				voiceButton.setBackgroundResource(resid);
			}
		}
		
		public void setDoubleDrable(int resid){
			singleDrableresid = resid;
			if(singleButton != null){
				singleButton.setBackgroundResource(resid);
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

		public void setDoubleButtonTitle(String str){
			doubleButtonText = str;
			if(doubleText != null){
				doubleText.setText(doubleButtonText);
			}
		}

		public void setResumeButton(String positiveButtonText,
				OnClickListener listener) {
			this.resumeButtonText = positiveButtonText;
			this.resumeButtonClickListener = listener;
		}
		
		public void setVoiceButton(String positiveButtonText,
				OnClickListener listener,int voiceDrableresid) {
			this.voiceButtonText = positiveButtonText;
			this.voiceButtonClickListener = listener;
			this.voiceDrableresid = voiceDrableresid;
		}

		public void setDoubleButton(String negativeButtonText,
				OnClickListener listener,int doubleDrableresid) {
			this.doubleButtonText = negativeButtonText;
			this.doubleButtonClickListener = listener;
			this.singleDrableresid = doubleDrableresid;
		}

		public CustomDialog create(int layoutid) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(layoutid, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			if (layout.findViewById(R.id.title) != null)
				((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			RelativeLayout mRelativeLayout = (RelativeLayout)layout.findViewById(R.id.background);

			if(mRelativeLayout != null)
				mRelativeLayout.getBackground().setAlpha(200);
			
			if (resumeButtonText != null) {
				((TextView) layout.findViewById(R.id.resume_text))
						.setText(resumeButtonText);
				if (resumeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.resume_click))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									resumeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			singleButton= (Button) layout.findViewById(R.id.double_click);
			if(singleButton != null){
				singleButton.setBackgroundResource(singleDrableresid);
			}
			doubleText = (TextView) layout.findViewById(R.id.double_text);
			// set the cancel button
			if (doubleButtonText != null) {
				((TextView) layout.findViewById(R.id.double_text))
						.setText(doubleButtonText);
				if (doubleButtonClickListener != null) {
					((Button) layout.findViewById(R.id.double_click))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									doubleButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}

			
			voiceButton = (Button) layout.findViewById(R.id.voice_click);
			if(voiceButton != null){
				voiceButton.setBackgroundResource(voiceDrableresid);
			}
			if (voiceButtonText != null) {
				((TextView) layout.findViewById(R.id.voicd_text))
						.setText(voiceButtonText);
				if (voiceButtonClickListener != null) {
					((Button) layout.findViewById(R.id.voice_click))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									voiceButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			//dialog.setCanceledOnTouchOutside(false); 
			return dialog;
		}

	}
}

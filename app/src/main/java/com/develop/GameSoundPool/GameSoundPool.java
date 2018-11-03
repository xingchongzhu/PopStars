package com.develop.GameSoundPool;

import java.util.HashMap;
import com.develop.PopStars.MainActivity;
import com.develop.PopStars.R;
import com.develop.PopStars.Util.Utils;
import com.develop.constant.ConstantUtil;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameSoundPool {
	private MainActivity mainActivity;
	private SoundPool soundPool;
	private HashMap<Integer,Integer> map;
	public GameSoundPool(MainActivity mainActivity){
		this.mainActivity = mainActivity;	
		map = new HashMap<Integer,Integer>();
		soundPool = new SoundPool(8,AudioManager.STREAM_MUSIC,0);
	}
	public void initGameSound(){
		map.put(1, soundPool.load(mainActivity,R.raw.select, 1));//选中砖块
		map.put(2, soundPool.load(mainActivity,R.raw.applause, 1));//游戏鼓励
		map.put(3, soundPool.load(mainActivity,R.raw.su_gameover, 1));//游戏结束
		map.put(4, soundPool.load(mainActivity,R.raw.button_start, 1));//点击按钮
		//map.put(5, soundPool.load(mainActivity,R.raw.landing, 1));//游戏开始放砖块
		map.put(6, soundPool.load(mainActivity,R.raw.broken, 1));//砖块破裂
		map.put(7, soundPool.load(mainActivity,R.raw.round_clear, 1));//游戏胜利
		map.put(8, soundPool.load(mainActivity,R.raw.welcome, 1));//首次进入
		map.put(9, soundPool.load(mainActivity,R.raw.fireworks_03, 1));//主菜单烟火爆炸
		map.put(10, soundPool.load(mainActivity,R.raw.discovery_02, 1));//达到目标分
		map.put(11, soundPool.load(mainActivity,R.raw.get_star_prop, 1));//金币
		map.put(12, soundPool.load(mainActivity,R.raw.fireworks_02, 1));//顺利烟花
		map.put(13, soundPool.load(mainActivity,R.raw.landing, 1));//砖块落地
	}

	public void playSound(int sound,int loop){
		if(Utils.getKeyDefault(mainActivity, ConstantUtil.VOICEKEY) == 1){
			AudioManager am = (AudioManager)mainActivity.getSystemService(Context.AUDIO_SERVICE);
			float stramVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			float stramMaxVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			float volume = stramVolumeCurrent/stramMaxVolumeCurrent;
			soundPool.play(map.get(sound), volume, volume, 1, loop, 1.0f);
		}
	}
}

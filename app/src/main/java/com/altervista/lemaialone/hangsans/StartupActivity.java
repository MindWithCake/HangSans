package com.altervista.lemaialone.hangsans;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

import java.util.Random;

public class StartupActivity extends Activity {
	public final static Random RAND = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		getFragmentManager().beginTransaction().add(R.id.fragment, new StartupFragment()).commit();
	}
}

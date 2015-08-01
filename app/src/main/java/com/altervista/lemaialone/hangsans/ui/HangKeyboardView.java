package com.altervista.lemaialone.hangsans.ui;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.altervista.lemaialone.hangsans.R;

import java.util.ArrayList;

/**
 * Keyboard displaying only capital english letters.
 * Created by Ilario Sanseverino on 28/07/15.
 */
public class HangKeyboardView extends KeyboardView {
	private ArrayList<Key> keys;
	private OnLetterListener letterListener;

	public HangKeyboardView(Context ctx){
		this(ctx, null);
	}

	public HangKeyboardView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}

	public HangKeyboardView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context){
		resetKeyboard();
		setKeyboard(new Keyboard(context, R.xml.letter_keyboard));
		setOnKeyboardActionListener(new LetterKeyboardListener());
		setPreviewEnabled(false);
	}

	public void resetKeyboard(){
		keys = new ArrayList<>('Z'-'A');
		for(char c = 'A'; c <= 'Z'; ++c)
			keys.add(new Key(c));
	}

	public void setLetterListener(OnLetterListener letterListener){
		this.letterListener = letterListener;
	}

	public void setEnabled(char key, boolean enabled){
		keys.get(key - 'A').isEnabled = enabled;
	}

	private static class Key{
		public final char key;
		public boolean isEnabled;

		public Key(char c, boolean enabled){
			key = c;
			isEnabled = enabled;
		}

		public Key(char c){
			this(c, true);
		}
	}

	private class LetterKeyboardListener implements OnKeyboardActionListener{
		@Override
		public void onKey(int i, int[] ints){
			if(letterListener != null && keys.get(i).isEnabled)
				letterListener.onLetter(keys.get(i).key);
		}

		@Override
		public void onPress(int i){}

		@Override
		public void onRelease(int i){}

		@Override
		public void onText(CharSequence charSequence){}

		@Override
		public void swipeLeft(){}

		@Override
		public void swipeRight(){}

		@Override
		public void swipeDown(){}

		@Override
		public void swipeUp(){}
	}

	public interface OnLetterListener{
		void onLetter(char letter);
	}
}

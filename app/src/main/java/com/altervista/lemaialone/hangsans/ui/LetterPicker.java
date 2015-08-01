package com.altervista.lemaialone.hangsans.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.altervista.lemaialone.hangsans.R;

/**
 * Picker to chose alphabet letters.
 * Created by Ilario Sanseverino on 26/07/15.
 */
public class LetterPicker extends NumberPicker implements NumberPicker.OnValueChangeListener {
	private String[] letters;
	private OnLetterListener letterListener;

	public LetterPicker(Context context){
		this(context, null);
	}

	public LetterPicker(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public LetterPicker(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		initPicker();
		setOnValueChangedListener(this);
		setWrapSelectorWheel(true);
	}

	public void initPicker(){
		initPicker('A', 'Z');
	}

	public void initPicker(char firstLetter, char lastLetter){
		letters = new String[lastLetter-firstLetter+1];
		for(char i = firstLetter; i <= lastLetter; ++i)
			letters[i - firstLetter] = "" + i;
		setDisplayedValues(letters);
	}

	public void disableLetter(char letter){
		String letterString = ""+letter;
		for(int i = 0; i < letters.length; ++i){
			if(letters[i].equals(letterString)){
				remove(i);
				return;
			}
		}
	}

	private void remove(int index){
		String[] newLetters = new String[letters.length-1];
		System.arraycopy(letters, 0, newLetters, 0, index);
		System.arraycopy(letters, index+1, newLetters, index, letters.length-index-1);
		letters = newLetters;
		setDisplayedValues(letters);
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal){
		if(letterListener != null)
			letterListener.onLetter(letters[newVal].charAt(0));
	}

	public void setLetterListener(OnLetterListener listener){
		this.letterListener = listener;
	}

	public interface OnLetterListener{
		void onLetter(char letter);
	}
}

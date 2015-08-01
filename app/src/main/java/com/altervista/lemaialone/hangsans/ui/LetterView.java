package com.altervista.lemaialone.hangsans.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.altervista.lemaialone.hangsans.R;

/**
 * Letter for the Hangman Game. Letter can be visible or hidden, in which case '_' is displayed.
 * Created by Ilario Sanseverino on 26/07/15.
 */
public class LetterView extends TextView {
	private char letter;

	public LetterView(Context context){
		this(context, null);
	}

	public LetterView(Context context, AttributeSet attrs){
		this(context, attrs, R.style.LetterView);
	}

	public LetterView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterView);

		try{
			String s = a.getString(R.styleable.LetterView_letter);
			if(s == null || s.isEmpty())
				s = "*";
			letter = s.toUpperCase().charAt(0);
		}
		finally{
			a.recycle();
		}

		setTextSize(TypedValue.COMPLEX_UNIT_SP, 33);
		setPadding(10, 0, 10, 0);
	}

	public char getLetter(){
		return letter;
	}

	public void setLetter(char letter){
		this.letter = Character.toUpperCase(letter);
	}

	public void showLetter(boolean isShown){
		setText(isShown? ""+letter : "_");
	}
}

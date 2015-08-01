package com.altervista.lemaialone.hangsans.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.support.annotation.NonNull;

import com.altervista.lemaialone.hangsans.R;

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import static android.util.TypedValue.*;

//(12,241) - (272, 286)
// 286x299 -> 572x598

/**
 * Hangman image with word included.
 * Created by Ilario Sanseverino on 30/07/15.
 */
public class HangView extends ImageView {
	private final static String PLACEHOLDER = "_";

	private RectF textRect = new RectF();
	private RectF originalRect;
	private String word = "";
	private String toDraw = "";
	private Paint paint;
	private float textBottom;
	private List<Character> guessed = new ArrayList<>();
	private int imageIndex = 0;

	public HangView(Context context){
		this(context, null);
	}

	public HangView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public HangView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		paint.setFakeBoldText(true);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		originalRect = new RectF(
				applyDimension(COMPLEX_UNIT_DIP, 12, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 241, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 272, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 286, metrics));
	}

	public void setWord(String word) throws CharacterCodingException{
		this.word = word.toUpperCase();
		guessed = new ArrayList<>();
		imageIndex = 0;
		paint.setColor(0xff000000);
		computeStringToDraw();
		refreshImage();
	}

	public boolean guessLetter(char letter){
		Character c = Character.toUpperCase(letter);
		guessed.add(c);
		try{
			if(!word.contains(c.toString()) && !refreshImage()){
				paint.setColor(0xffff0000);
				for(char ch : word.toCharArray())
					guessed.add(ch);
				return false;
			}
			return true;
		}
		finally{
			computeStringToDraw();
			invalidate((int)textRect.left, (int)textRect.top,
					(int)textRect.right, (int)textRect.bottom);
		}
	}

	public boolean checkVictory(){
		return !toDraw.contains(PLACEHOLDER);
	}

	private void computeStringToDraw(){
		toDraw = word.replaceAll(makeHideRegex(), PLACEHOLDER);
	}

	private boolean refreshImage(){
		TypedArray array = getResources().obtainTypedArray(R.array.hangmen);
		try{
			setImageDrawable(array.getDrawable(imageIndex++));
			return imageIndex < array.length();
		}
		finally{
			array.recycle();
		}
	}

	private String makeHideRegex(){
		if(guessed.isEmpty())
			return ".";

		String regex = "[^";
		for(char c: guessed)
			regex += c;
		return regex+"]";
	}

	@Override
	public void setImageDrawable(Drawable drawable){
		super.setImageDrawable(drawable);

		Matrix matrix = getImageMatrix();
		matrix.mapRect(textRect, originalRect);

		float textSize = 8f, textHeight;
		Rect bounds = new Rect();
		do{
			paint.setTextSize(++textSize);
			Paint.FontMetrics metrics = paint.getFontMetrics();
			paint.getTextBounds(word, 0, word.length(), bounds);
			textBottom = metrics.bottom;
			textHeight = metrics.bottom-metrics.top;
		} while(textRect.height() > textHeight && textRect.width() > bounds.width());

		paint.setTextSize(--textSize);
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas){
		super.onDraw(canvas);
		Matrix m = getImageMatrix();
		canvas.drawText(toDraw, textRect.centerX(), textRect.bottom - textBottom, paint);
	}
}

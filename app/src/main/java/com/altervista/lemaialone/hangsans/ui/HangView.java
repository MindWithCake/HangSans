package com.altervista.lemaialone.hangsans.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.altervista.lemaialone.hangsans.R;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

/**
 * Hangman image with word included.
 * Created by Ilario Sanseverino on 30/07/15.
 */
public class HangView extends ImageView {
	private final static String PLACEHOLDER = "_";

	private final RectF textRect;
	private String word = "";
	private String toDraw = "";
	private final Paint paint;
	private float textBottom;
	private List<Character> guessed = new ArrayList<>();
	private int imageIndex = 0;

	private MediaPlayer player;
	private int loadedClip = -1;

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
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		textRect = new RectF(
				applyDimension(COMPLEX_UNIT_DIP, 12, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 241, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 272, metrics),
				applyDimension(COMPLEX_UNIT_DIP, 286, metrics));
		player = new MediaPlayer();
	}

	public void setWord(String word) throws CharacterCodingException{
		this.word = word.toUpperCase();
		guessed = new ArrayList<>();
		imageIndex = 0;
		paint.setColor(getResources().getColor(R.color.hang_text));

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
		computeStringToDraw();
		refreshImage();
	}

	public boolean guessLetter(char letter){
		Character c = Character.toUpperCase(letter);

		if(guessed.contains(c))
			return true;

		guessed.add(c);
		computeStringToDraw();

		if(word.indexOf(c) >= 0)
			playResource(checkVictory()? R.raw.you_win : R.raw.ok_letter);
		else if(!refreshImage()){
			playResource(R.raw.you_lose);
			paint.setColor(getResources().getColor(R.color.error_feed));
			for(char ch : word.toCharArray())
				guessed.add(ch);
			computeStringToDraw();
			return false;
		}
		else
			playResource(R.raw.no_letter);

		invalidate();
		return true;
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

	private void playResource(int res){
		if(res != loadedClip){
			player.release();
			loadedClip = res;
			player = MediaPlayer.create(getContext(), loadedClip);
		}
		else {
			player.stop();
			try{ player.prepare(); }
			catch(IOException e){
				Log.e("Audio", "Exception while preparing: "+e);
				return;
			}
		}

		player.start();
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas){
		super.onDraw(canvas);
		canvas.concat(getImageMatrix());
		canvas.drawText(toDraw, textRect.centerX(), textRect.bottom - textBottom, paint);
	}
}

package com.altervista.lemaialone.hangsans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.altervista.lemaialone.hangsans.ui.HangKeyboardView;
import com.altervista.lemaialone.hangsans.ui.HangKeyboardView.OnLetterListener;
import com.altervista.lemaialone.hangsans.ui.HangView;

import java.nio.charset.CharacterCodingException;

import static com.altervista.lemaialone.hangsans.StartupActivity.RAND;

public class HangFragment extends Fragment implements OnLetterListener, OnClickListener {
	private final static String ARG_WORDS_RES = "HangFragment.ARG_WORDS_RES";

	private TextView button;
	private HangView hangman;
	private HangKeyboardView keyView;

	private String[] words;
	private String currentWord = "";
	private boolean inGame;

	public static HangFragment newInstance(int wordsArrayResource){
		HangFragment fragment = new HangFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_WORDS_RES, wordsArrayResource);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null)
			savedInstanceState = getArguments();

		words = getResources().getStringArray(savedInstanceState.getInt(ARG_WORDS_RES));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
		View rootView = inflater.inflate(R.layout.fragment_hang, container, false);

		button = (TextView)rootView.findViewById(R.id.continue_button);
		hangman = (HangView)rootView.findViewById(R.id.hangman);
		keyView = (HangKeyboardView)rootView.findViewById(R.id.keyboard);

		button.setOnClickListener(this);
		keyView.setLetterListener(this);

//		initQuestion();
		return rootView;
	}

	@Override
	public void onLetter(char letter){
		if(!inGame)
			return;

		keyView.setEnabled(letter, false);
		if(!hangman.guessLetter(letter))
			endGame(false);
		else if(hangman.checkVictory())
			endGame(true);
	}

	@Override
	public void onClick(View view){
		button.setVisibility(View.INVISIBLE);
		initQuestion();
	}

	@Override
	public void onStart(){
		super.onStart();
		initQuestion();
	}

	private void initQuestion() {
		int nextIndex;
		do
			nextIndex = RAND.nextInt(words.length);
		while(currentWord.equals(words[nextIndex]));

		currentWord = words[nextIndex];
		try{
			hangman.setWord(currentWord);
		}
		catch(CharacterCodingException ignored){}

		keyView.resetKeyboard();
		inGame = true;
	}

	private void endGame(boolean win){
		inGame = false;
		button.setText(win? "You Win ^^" : "You Lose...");
		button.setVisibility(View.VISIBLE);
	}
}

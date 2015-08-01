package com.altervista.lemaialone.hangsans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartupFragment extends Fragment implements View.OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View root = inflater.inflate(R.layout.fragment_startup, container, false);

		LinearLayout layout = (LinearLayout)root.findViewById(R.id.container);

		CategoryItem[] items = CategoryItem.values();
		for(CategoryItem item: items){
			TextView view = (TextView)inflater.inflate(R.layout.view_button, layout, false);
			view.setTag(item);
			view.setOnClickListener(this);
			view.setText(item.labelRes);
			view.setTextColor(getResources().getColor(R.color.orange));
			layout.addView(view);
		}

		return root;
	}

	@Override
	public void onClick(View view){
		CategoryItem item = (CategoryItem)view.getTag();
		Fragment fragment = HangFragment.newInstance(item.arrayRes);
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment, fragment)
				.addToBackStack(null)
				.commit();
	}

	private enum CategoryItem{
		FRUIT(R.string.category_fruit, R.array.fruits),
		CITY(R.string.category_city, R.array.cities),
		ANIMAL(R.string.category_animals, R.array.animals);

		public final int labelRes;
		public final int arrayRes;

		CategoryItem(int label, int arrayRes){
			this.labelRes = label;
			this.arrayRes = arrayRes;
		}
	}
}

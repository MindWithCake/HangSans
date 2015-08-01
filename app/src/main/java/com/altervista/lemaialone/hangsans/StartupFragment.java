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
			view.setText(item.label);
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
		FRUIT("Name of Fruits", R.array.fruits),
		CITY("Name of Cities", R.array.cities);

		public final String label;
		public final int arrayRes;

		CategoryItem(String label, int arrayRes){
			this.label = label;
			this.arrayRes = arrayRes;
		}
	}
}

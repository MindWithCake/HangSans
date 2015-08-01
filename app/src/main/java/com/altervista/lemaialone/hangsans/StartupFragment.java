package com.altervista.lemaialone.hangsans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartupFragment extends Fragment {
	private final static CategoryItem[] ITEMS = CategoryItem.values();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View root = inflater.inflate(R.layout.fragment_startup, container, false);

		ListView list = (ListView)root.findViewById(R.id.category_list);

		CategoryItem[] items = CategoryItem.values();

		list.setAdapter(new ArrayAdapter<CategoryItem>(root.getContext(), 0, items){
			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				if(convertView == null)
					convertView = LayoutInflater.from(getContext())
							.inflate(R.layout.view_category_button, parent, false);

				((TextView)convertView).setText(ITEMS[position].labelRes);
				return convertView;
			}
		});

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Fragment fragment = HangFragment.newInstance(ITEMS[i].arrayRes);
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment, fragment)
						.addToBackStack(null)
						.commit();
			}
		});

		return root;
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

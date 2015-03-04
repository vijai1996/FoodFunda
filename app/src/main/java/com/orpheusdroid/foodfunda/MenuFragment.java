/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 % Copyright (c) 2015. Vijai Chandra Prasad R.                                                    %
 %                                                                                                %
 % This program is free software: you can redistribute it and/or modify                           %
 % it under the terms of the GNU General Public License as published by                           %
 % the Free Software Foundation, either version 3 of the License, or                              %
 % (at your option) any later version.                                                            %
 %                                                                                                %
 % This program is distributed in the hope that it will be useful,                                %
 % but WITHOUT ANY WARRANTY; without even the implied warranty of                                 %
 % MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                  %
 % GNU General Public License for more details.                                                   %
 %                                                                                                %
 % You should have received a copy of the GNU General Public License                              %
 % along with this program.  If not, see http://www.gnu.org/licenses                              %
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

package com.orpheusdroid.foodfunda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orpheusdroid.foodfunda.utility.Debug;
import com.orpheusdroid.foodfunda.utility.MenuAdapter;
import com.orpheusdroid.foodfunda.utility.MenuItems;

import java.util.ArrayList;

/**
 * Created by vijai on 24-02-2015.
 */
public class MenuFragment extends Fragment {
    private ListView lv;
    public static String title[] = {"Burger", "Tortilla", "Noodle", "Fries", "Hot dog",
            "Pizza", "Salad", "Poori", "Idli", "Naan"};

    public static String desc[] = {"Desc 1", "Desc 2", "Desc 3", "Desc 4", "Desc 5", "Desc 6"
            , "Desc 7", "Desc 8", "Desc 9", "Desc 10"};

    public static int drawables[] = {R.drawable.burger, R.drawable.tortilla, R.drawable.noodles,
            R.drawable.fries, R.drawable.hotdog, R.drawable.pizza, R.drawable.salad,
            R.drawable.poori, R.drawable.idli, R.drawable.naan};

    public static int price[] = {100, 55, 220, 70, 110, 350, 80, 40, 30, 30};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        lv = (ListView) rootView.findViewById(R.id.menu_items);
        ArrayList<MenuItems> MenuItems = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            MenuItems.add(new MenuItems(getActivity(), title[i], desc[i], drawables[i]));

        MenuAdapter adapter = new MenuAdapter(getActivity(), MenuItems);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Debug.Toast(getActivity(), "Test");
                ItemDetailFragment frag = new ItemDetailFragment();
                Bundle args = new Bundle();
                        args.putInt("position", position);
                        args.putInt("image", drawables[position]);
                        args.putInt("price", price[position]);
                        args.putString("title", title[position]);
                frag.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, frag)
                        .addToBackStack("MenuFrag")
                        .commit();
                /*Intent i = new Intent(getActivity(), ItemDetailActivity.class)
                        .putExtra("image", drawables[position])
                        .putExtra("title", title[position])
                        .putExtra("price", price[position]);
                startActivity(i);*/
            }
        });
        return rootView;
    }
}

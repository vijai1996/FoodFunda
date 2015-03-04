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


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orpheusdroid.foodfunda.utility.InputFilterQty;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemDetailFragment extends Fragment implements View.OnClickListener{
    int id;
    private EditText qty;

    private String nutrition[] = {"Fat:17\nCalories:354\nCarbohydrates:29\nProteins:20",
            "Fat:1.9\nCalories:467\nCarbohydrates:98\nProteins:14",
            "Fat:0.4\nCalories:192\nCarbohydrates:44\nProteins:1.6",
            "Fat:10\nCalories:222\nCarbohydrates:29\nProteins:2.4",
            "Fat:13\nCalories:151\nCarbohydrates:2.2\nProteins:5",
            "Fat:10\nCalories:266\nCarbohydrates:33\nProteins:11",
            "Fat:0.11\nCalories:11\nCarbohydrates:0.32\nProteins:0.55",
            "Fat:3.39\nCalories:107\nCarbohydrates:16.82\nProteins:2.71",
            "Fat:0.19\nCalories:40\nCarbohydrates:7.89\nProteins:1.91",
            "Fat:5.1\nCalories:137\nCarbohydrates:18.8\nProteins:3.74"};

    public ItemDetailFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_detail_fragment, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Details");

        id = getArguments().getInt("position", 0);
        /*title = getArguments().getString("title");
        price = getArguments().getInt("price");
        imageID = getArguments().getInt("image");*/

        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        TextView detailView = (TextView) rootView.findViewById(R.id.detail_text);
        TextView priceView = (TextView) rootView.findViewById(R.id.price);
        ImageView item = (ImageView) rootView.findViewById(R.id.item_image);
        qty = (EditText) rootView.findViewById(R.id.qty);
        Button inc = (Button) rootView.findViewById(R.id.btnInc);
        Button dec = (Button) rootView.findViewById(R.id.btnDec);
        ImageButton addCart = (ImageButton) rootView.findViewById(R.id.add_to_cart);

        qty.setFilters(new InputFilter[]{new InputFilterQty("1", "10")});

        inc.setOnClickListener(this);
        dec.setOnClickListener(this);
        addCart.setOnClickListener(this);

        //titleView.setText(MenuFragment.title[id]);
        detailView.setText(nutrition[id]);
        priceView.append(String.valueOf(MenuFragment.price[id]));
        item.setImageResource(MenuFragment.drawables[id]);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        int num = 0;
        switch (v.getId()){
            case R.id.btnDec:
                num = Integer.parseInt(qty.getText().toString());
                if(num>1)
                    qty.setText(""+(num-1));
                break;
            case R.id.btnInc:
                num = Integer.parseInt(qty.getText().toString());
                if(num<10)
                    qty.setText(""+(num+1));
                else
                    Toast.makeText(getActivity(), "Enter a quantity below 10", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_to_cart:
                Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), CartActivity.class));
                break;
        }
    }
}

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orpheusdroid.foodfunda.ContentProviders.CartContract;


public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private String ADDRESS_TAG = "PostalAddr";
    private String save = "SaveCB";
    private String NAME_TAG = "name";
    private String Prefs = "Address";

    private TableLayout cart_itemsView;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        if (!getActivity().getSharedPreferences(Prefs, Context.MODE_PRIVATE).getBoolean(save, false))
            Alert("Enter your postal address");
        cart_itemsView = (TableLayout) rootView.findViewById(R.id.cart_items);
        cart_itemsView.setShrinkAllColumns(true);
        cart_itemsView.setStretchAllColumns(true);

        buildTable();

        TextView address = (TextView) rootView.findViewById(R.id.address_tv);
        address.setText(getActivity().getSharedPreferences(Prefs, Context.MODE_PRIVATE)
                .getString(NAME_TAG, "")+
                "\n"
                + getActivity().getSharedPreferences(Prefs, Context.MODE_PRIVATE)
                .getString(ADDRESS_TAG, ""));
        // Inflate the layout for this fragment
        return rootView;
    }

    public void buildTable(){
        Cursor cursor = getActivity().getContentResolver().query(CartContract.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();
        int total=0;

        TableRow rowProdLabels = new TableRow(this.getActivity());

        TextView slno_label = new TextView(this.getActivity());
        slno_label.setText("Sl");
        slno_label.setGravity(Gravity.CENTER);
        slno_label.setTypeface(Typeface.SERIF, Typeface.BOLD);
        slno_label.setBackgroundResource(R.drawable.cell_shape);
        rowProdLabels.addView(slno_label);

        // Price column
        TextView Item_label = new TextView(this.getActivity());
        Item_label.setText("Item");
        Item_label.setGravity(Gravity.CENTER);
        Item_label.setBackgroundResource(R.drawable.cell_shape);
        Item_label.setTypeface(Typeface.SERIF, Typeface.BOLD);
        rowProdLabels.addView(Item_label);

        // Quantity column
        TextView Qty_label = new TextView(this.getActivity());
        Qty_label.setText("Qty");
        Qty_label.setGravity(Gravity.CENTER);
        Qty_label.setBackgroundResource(R.drawable.cell_shape);
        Qty_label.setTypeface(Typeface.SERIF, Typeface.BOLD);
        rowProdLabels.addView(Qty_label);

        TextView Price_label = new TextView(this.getActivity());
        Price_label.setText("price");
        Price_label.setGravity(Gravity.CENTER);
        Price_label.setBackgroundResource(R.drawable.cell_shape);
        Price_label.setTypeface(Typeface.SERIF, Typeface.BOLD);
        rowProdLabels.addView(Price_label);

        cart_itemsView.addView(rowProdLabels);
        if (!cursor.moveToFirst())
            return;
        for (int i = 0; i < count; i++) {

            TableRow row = new TableRow(getActivity());
            // row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
            // LayoutParams.WRAP_CONTENT));
            String item = cursor.getString(cursor
                    .getColumnIndex(CartContract.COLUMN_ITEM));
            String qty = cursor
                    .getString(cursor.getColumnIndex(CartContract.COLUMN_ITEM_QUANTITY));
            int price = cursor
                    .getInt(cursor.getColumnIndex(CartContract.COLUMN_ITEM_PRICE));

            TextView slno = new TextView(getActivity());
            slno.setTextSize(18);
            slno.setGravity(Gravity.CENTER);
            slno.setBackgroundResource(R.drawable.cell_shape);
            slno.setText(String.valueOf(i + 1));
            row.addView(slno);

            TextView item_view = new TextView(getActivity());
            item_view.setTextSize(18);
            item_view.setGravity(Gravity.CENTER);
            item_view.setBackgroundResource(R.drawable.cell_shape);
            item_view.setText(item);
            row.addView(item_view);

            TextView qty_view = new TextView(getActivity());
            qty_view.setTextSize(18);
            qty_view.setGravity(Gravity.CENTER);
            qty_view.setBackgroundResource(R.drawable.cell_shape);
            qty_view.setText(qty);
            row.addView(qty_view);

            TextView price_view = new TextView(getActivity());
            price_view.setTextSize(18);
            price_view.setGravity(Gravity.CENTER);
            price_view.setBackgroundResource(R.drawable.cell_shape);
            price_view.setText(Integer.toString(price));
            total += price;
            row.addView(price_view);

            cursor.moveToNext();
            cart_itemsView.addView(row);
        }

    }

    private void Alert(String title) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.cart, null);
        final EditText name = (EditText) dialoglayout.findViewById(R.id.name);
        final EditText addr = (EditText) dialoglayout.findViewById(R.id.addr);
        final CheckBox cb = (CheckBox) dialoglayout.findViewById(R.id.cb_save);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialoglayout)
                .setTitle(title)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getActivity().getSharedPreferences(Prefs, Context.MODE_PRIVATE);
                        if (!name.getText().toString().equals("") && !addr.getText().toString().equals("")) {
                            sp.edit()
                                    .putString(NAME_TAG, name.getText().toString())
                                    .putString(ADDRESS_TAG, addr.getText().toString()).commit();
                            if (cb.isChecked())
                                sp.edit()
                                    .putBoolean(save, true)
                                .commit();
                        }
                    }
                })
                .show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
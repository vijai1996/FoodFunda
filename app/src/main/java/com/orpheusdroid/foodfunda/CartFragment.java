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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orpheusdroid.foodfunda.ContentProviders.CartContract;
import com.orpheusdroid.foodfunda.utility.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CartFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private int total;

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
        rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        ImageButton checkout = (ImageButton) rootView.findViewById(R.id.cart_checkout);
        checkout.setOnClickListener(this);

        setHasOptionsMenu(true);

        cart_itemsView = (TableLayout) rootView.findViewById(R.id.cart_items);
        cart_itemsView.setShrinkAllColumns(true);
        cart_itemsView.setStretchAllColumns(true);

        if (!getActivity().getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE).getBoolean(CartActivity.save, false))
            ((CartActivity)getActivity()).Alert("Enter your postal address");

        buildTable();

        TextView address = (TextView) rootView.findViewById(R.id.address_tv);
        address.setText(getActivity().getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE)
                .getString(CartActivity.NAME_TAG, "")+
                "\n"
                + getActivity().getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE)
                .getString(CartActivity.ADDRESS_TAG, ""));
        // Inflate the layout for this fragment
        return rootView;
    }

    public void buildTable(){
        Cursor cursor = getActivity().getContentResolver().query(CartContract.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.cart_clear:
                getActivity().getContentResolver().delete(CartContract.CONTENT_URI, null, null);
                ((MainActivity) getActivity()).updateBadge();
                break;
            case R.id.address:
                Alert("Enter your postal address");
                break;
        }

        return true;
    }

    public void Alert(String title) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.cart, null);
        final EditText name = (EditText) dialoglayout.findViewById(R.id.name);
        final EditText addr = (EditText) dialoglayout.findViewById(R.id.addr);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialoglayout)
                .setTitle(title)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getActivity().getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE);
                        if (!name.getText().toString().equals("") && !addr.getText().toString().equals("")) {
                            sp.edit()
                                    .putString(CartActivity.NAME_TAG, name.getText().toString())
                                    .putString(CartActivity.ADDRESS_TAG, addr.getText().toString()).apply();

                            sp.edit()
                                    .putBoolean(CartActivity.save, true)
                                    .apply();
                        }
                        //load();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cart_checkout) {
            AlertDialog.Builder checkout = new AlertDialog.Builder(getActivity());
            checkout.setTitle("Are you sure to place the order?")
                    .setMessage("Are you sure you want to place the order? It cannot be cancel untill" +
                                    "you call the restaurant. Order total: ₹"+ total)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Checkout();
                        }
                    })
                    .show();
            Debug.Toast(getActivity(), "Checkout pressed");
        }
    }

    private void Checkout() {
        Cursor cursor = getActivity().getContentResolver().query(CartContract.CONTENT_URI, null, null, null, null);
        JSONArray itemJson = new JSONArray();
        cursor.moveToFirst();
        do{
            JSONObject orderOBJ = new JSONObject();
            try{
                orderOBJ.put("item", cursor.getString(cursor.getColumnIndex(CartContract.COLUMN_ITEM)));
                orderOBJ.put("qty", cursor.getInt(cursor.getColumnIndex(CartContract.COLUMN_ITEM_QUANTITY)));
                orderOBJ.put("price", cursor.getInt(cursor.getColumnIndex(CartContract.COLUMN_ITEM_PRICE)));
                itemJson.put(orderOBJ);
            }catch (JSONException e) {
                Log.e("JSON ENCODE ERROR: ", e.toString());
            }
        }while(cursor.moveToNext());
        JSONObject orderJSON = new JSONObject();
        try {
            orderJSON.put("order", itemJson);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Debug.v("JSON ARRAY: ", orderJSON.toString());
        String data[] = {orderJSON.toString(), Integer.toString(total)};
        if(isNetworkAvailable() && cursor.getCount() != 0) {
            new PostMenu(getActivity()).execute(data);
        }
        else if (!isNetworkAvailable()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Internet connection not available")
                    .setMessage("Not able to connect to internet. Please check your connection and try again")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
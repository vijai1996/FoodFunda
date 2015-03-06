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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.orpheusdroid.foodfunda.ContentProviders.CartContract;

/**
 * Created by vijai on 24-02-2015.
 */
public class CartActivity extends ActionBarActivity {
    public static String ADDRESS_TAG = "PostalAddr";
    public static String save = "SaveCB";
    public static String NAME_TAG = "name";
    public static String Prefs = "Address";

    Toolbar toolbar;
    public String title;
    public static int imageID, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);

        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Cart");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CartFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.cart_clear:
                getContentResolver().delete(CartContract.CONTENT_URI, null, null);
                break;
            case R.id.address:
                Alert("Enter your postal address");
                break;
        }

        return true;
    }

    public void Alert(String title) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.cart, null);
        final EditText name = (EditText) dialoglayout.findViewById(R.id.name);
        final EditText addr = (EditText) dialoglayout.findViewById(R.id.addr);
        final CheckBox cb = (CheckBox) dialoglayout.findViewById(R.id.cb_save);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout)
                .setTitle(title)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE);
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
}

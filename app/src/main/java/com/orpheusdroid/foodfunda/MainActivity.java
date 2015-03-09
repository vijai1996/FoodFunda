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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.orpheusdroid.foodfunda.ContentProviders.CartContract;
import com.orpheusdroid.foodfunda.utility.Debug;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    public static boolean mTwoPane;
    int cart_count;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.appBar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        frame = (FrameLayout) findViewById(R.id.container);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MenuFragment())
                    .commit();
        }*/
        updateBadge();
        if (findViewById(R.id.menu_item_detail) != null){
            mTwoPane = true;
            Debug.v("View status:", "Not null");
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.menu_item_detail, new ItemDetailFragment())
                        .commit();
            }
        }else{

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        View count = menu.findItem(R.id.ab_cart).getActionView();
        TextView notifCount = (TextView) count.findViewById(R.id.count_badge);
        notifCount.setText(String.valueOf(cart_count));
        count.findViewById(R.id.cart_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart_count != 0) {
                    //startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    frame.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new CartFragment())
                            .addToBackStack(null)
                            .commit();
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Cart Empty")
                            .setMessage("Seems like you are not hungry?")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });
        ActionBar ab = getSupportActionBar();
        if (ab!=null)
            ab.setCustomView(count);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.ab_cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.map:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:12.9846915,80.2129798(Mirchi)")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateBadge(){
        Cursor cursor = getContentResolver().query(CartContract.CONTENT_URI, null, null, null, null);
        cart_count =  cursor.getCount();
        invalidateOptionsMenu();
    }
}

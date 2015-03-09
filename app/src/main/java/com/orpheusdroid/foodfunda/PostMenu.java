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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.orpheusdroid.foodfunda.ContentProviders.CartContract;
import com.orpheusdroid.foodfunda.utility.Debug;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijai on 06-03-2015.
 */
public class PostMenu extends AsyncTask<String[], Void, String> {
    private Context mContext;
    private ProgressDialog progress;

    public PostMenu(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute(){
        progress = new ProgressDialog(mContext);
        progress.setTitle("Placing order");
        progress.setMessage("Please wait until the order is placed");
        progress.show();
    }

    @Override
    protected String doInBackground(String[]... params) {
        if (params.length == 0)
            return null;
        String[] order = params[0];
        String name = mContext.getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE)
                .getString(CartActivity.NAME_TAG, "");
        String Address = mContext.getSharedPreferences(CartActivity.Prefs, Context.MODE_PRIVATE)
                .getString(CartActivity.ADDRESS_TAG, "");

        HttpClient httpClient = new DefaultHttpClient();


        HttpPost httpPost = new HttpPost("http://192.168.0.108/food/insert.php");
        HttpResponse response = null;

        List<NameValuePair> data = new ArrayList<NameValuePair>(4);
        data.add(new BasicNameValuePair("Name",name));
        data.add(new BasicNameValuePair("Address",Address));
        data.add(new BasicNameValuePair("Order",order[0]));
        data.add(new BasicNameValuePair("Amount",order[1]));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // making POST request.
        try {
            response = httpClient.execute(httpPost);
            // write response to log
            Debug.d("Http Post Response:", EntityUtils.toString(response.getEntity()));
            Debug.d("HTTP Header", ""+response.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.toString(response.getStatusLine().getStatusCode());
    }

    @Override
    protected void onPostExecute(String response){
        progress.cancel();
        if (Integer.parseInt(response) == 200) {
            mContext.getContentResolver().delete(CartContract.CONTENT_URI, null, null);
            ((MainActivity) mContext).updateBadge();
            ((MainActivity) mContext).getFragmentManager().popBackStackImmediate()
            ;
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Error while checkout")
                    .setMessage("An error has occured while checkingout with response code: "+ Integer.parseInt(response))
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
}

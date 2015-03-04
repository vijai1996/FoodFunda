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

package com.orpheusdroid.foodfunda.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orpheusdroid.foodfunda.R;

import java.util.ArrayList;

/**
 * Created by vijai on 24-02-2015.
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MenuItems> MenuItems;
    private LayoutInflater inflater;

    public MenuAdapter(Context context, ArrayList<MenuItems> MenuItems) {
        this.context = context;
        this.MenuItems = MenuItems;
    }

    @Override
    public int getCount() {
        return MenuItems.size();//movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return MenuItems.get(location);//movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.menuitems, null);



        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                MenuItems.get(position).getDrawable());

        ImageView iconView = (ImageView) convertView.findViewById(R.id.item);

        title.setText(MenuItems.get(position).getTitle());
        desc.setText(MenuItems.get(position).getShortDesc());
        //iconView.setImageResource(MenuItems.get(position).getDrawable());
        iconView.setImageBitmap(icon);
        return convertView;
    }

    class vh{

    }

}

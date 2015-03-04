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

/**
 * Created by vijai on 24-02-2015.
 */
public class MenuItems {
    String Title, shortDesc;
    int drawable;
    Context context;

    public MenuItems(Context context, String title, String desc, int image)
    {
        this.context = context;
        this.Title = title;
        this.shortDesc = desc;
        this.drawable = image;
    }

    public String getTitle(){
        return this.Title;
    }

    public void setTitle(String title){
        this.Title = title;
    }

    public String getShortDesc(){
        return this.shortDesc;
    }

    public void setShortDesc(String desc){
        this.shortDesc = desc;
    }

    public int getDrawable(){
        return this.drawable;
    }

    public void setDrawable(int image){
        this.drawable = image;
    }

}

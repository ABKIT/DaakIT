package com.daakit.sitting;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;

public class opendrawer {

    public opendrawer()
    {

    }

    public void opendw(ImageView im, DrawerLayout mDrawerLayout)
    {
        final DrawerLayout dw=mDrawerLayout;
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dw.openDrawer(GravityCompat.START);
            }
        });
    }
}
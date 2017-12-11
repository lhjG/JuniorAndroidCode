package com.example.ainge.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

/**
 * Created by zz on 2017/11/14.
 */

public class WidgetDynamicReceiver extends BroadcastReceiver {
    private int WDR_Images [] ={R.drawable.enchatedforest,R.drawable.arla,R.drawable.devondale,
            R.drawable.kindle,R.drawable.waitrose, R.drawable.mcvitie,R.drawable.ferrero,R.drawable.maltesers,
            R.drawable.lindt,R.drawable.borggreve};
    private String [] WDR_Products = {"购物车","Enchated Forest","Arla Milk","Devondale Milk",
            "Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferreo Rocher",
            "Maltesers","Lindt","Borggreve"};
    @Override
    public void onReceive(Context context, Intent intent){
        final int index = intent.getIntExtra("SelectedNumber",0);

    }
}

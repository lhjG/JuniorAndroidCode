package com.example.ainge.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by zz on 2017/10/31.
 */

public class NotifyAdded extends BroadcastReceiver {
    private int NA_Images [] ={R.drawable.enchatedforest,R.drawable.arla,R.drawable.devondale,
            R.drawable.kindle,R.drawable.waitrose, R.drawable.mcvitie,R.drawable.ferrero,R.drawable.maltesers,
            R.drawable.lindt,R.drawable.borggreve};
    private String [] NA_Products = {"购物车","Enchated Forest","Arla Milk","Devondale Milk",
            "Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferreo Rocher",
            "Maltesers","Lindt","Borggreve"};


    @Override
    public void onReceive(Context context, Intent intent)
    {
        final int index = intent.getIntExtra("SelectedNumber",0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("马上下单")
                .setContentText(NA_Products[index] + "已添加到购物车")
                .setSmallIcon(NA_Images[index-1])
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),NA_Images[index-1]))
                .setAutoCancel(true);
        Intent SplashIntent = new Intent(context,MainActivity.class);
        SplashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,  SplashIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(index,builder.build());
    }
}

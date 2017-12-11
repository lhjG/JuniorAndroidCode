package com.example.ainge.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by zz on 2017/10/30.
 */
public class StartAppReceiver extends BroadcastReceiver {
	private int SAR_Images [] ={R.drawable.enchatedforest,R.drawable.arla,R.drawable.devondale,
			R.drawable.kindle,R.drawable.waitrose, R.drawable.mcvitie,R.drawable.ferrero,R.drawable.maltesers,
			R.drawable.lindt,R.drawable.borggreve};
	private String SAR_Prices [] = {"¥5.00","¥59.00","¥79.00","¥2399.00","¥179.00", "¥14.90",
			"¥132.59","¥141.43","¥139.43","¥28.90"};
    private String [] SAR_Products = {"购物车","Enchated Forest","Arla Milk","Devondale Milk",
            "Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferreo Rocher",
            "Maltesers","Lindt","Borggreve"};
    @Override
    public void onReceive(Context context, Intent intent)
    {
		int index = intent.getIntExtra("StartApp",0);
    	Notification.Builder builder = new Notification.Builder(context);
    	builder.setContentTitle("新商品热卖")
    			.setContentText(SAR_Products[index+1] + SAR_Prices[index])
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),SAR_Images[index]))
				.setSmallIcon(R.mipmap.ic_launcher)
				.setAutoCancel(true);
		Intent SplashIntent = new Intent(context,ShoppingActivity.class);
		SplashIntent.putExtra("name",SAR_Products[index+1]);
		//        SplashIntent.setAction(Intent.ACTION_MAIN);
//SplashIntent.addCategory(Intent.GATEGORY_LAUNCHER);
        //SplashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,  SplashIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0,builder.build());
    }
}

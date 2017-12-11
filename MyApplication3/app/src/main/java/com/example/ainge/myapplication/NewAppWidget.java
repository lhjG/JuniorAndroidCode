package com.example.ainge.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
//        //CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews updateviews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//        //updateviews.setTextViewText(R.id.appwidget_text, "当前没有任何信息");
//        Intent intent = new Intent(context,MainActivity.class);
//        PendingIntent jump = PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        updateviews.setOnClickPendingIntent(R.id.appwidget_text,jump);
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, updateviews);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
        RemoteViews updateviews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //updateviews.setTextViewText(R.id.appwidget_text, "当前没有任何信息");
        Intent intent_first_time = new Intent(context,MainActivity.class);
        PendingIntent jump_to_main = PendingIntent.getActivity(context, 0,intent_first_time,PendingIntent.FLAG_UPDATE_CURRENT);
        updateviews.setOnClickPendingIntent(R.id.widget,jump_to_main);
        ComponentName me = new ComponentName(context,NewAppWidget.class);
        appWidgetManager.updateAppWidget(me,updateviews);
        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, updateviews);
        }
    }
    @Override
    public void onReceive(Context context,Intent intent)
    {
        super.onReceive(context,intent);
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
        if (intent.getAction().equals("WidgetBroadcast")){
//            Toast.makeText(context,"ajifeaf",Toast.LENGTH_SHORT).show();
            Bundle bundle = intent.getExtras();
            int widget_product_image = bundle.getInt("widget_image");
            String widget_product_name = bundle.getString("widget_name");
            String widget_product_price = bundle.getString("widget_price");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            Intent intent_after = new Intent(context,ShoppingActivity.class);
            intent_after.putExtra("name",widget_product_name);
            PendingIntent jump_to_shopping = PendingIntent.getActivity(context, 0,intent_after,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget,jump_to_shopping);
            remoteViews.setImageViewResource(R.id.widget_product_image,widget_product_image);
            remoteViews.setTextViewText(R.id.appwidget_text,widget_product_name+"仅售"+widget_product_price);
            ComponentName me = new ComponentName(context,NewAppWidget.class);
            appWidgetManager.updateAppWidget(me,remoteViews);
        }
        else if(intent.getAction().equals("addshoppinglist_forwidget"))
        {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            Bundle bundle = intent.getExtras();
            int widget_product_image = bundle.getInt("widget_image");
            String widget_product_name = bundle.getString("widget_name");
            remoteViews.setImageViewResource(R.id.widget_product_image,widget_product_image);
            remoteViews.setTextViewText(R.id.appwidget_text,widget_product_name+"已添加至购物车");
            Intent SplashIntent = new Intent(context,MainActivity.class);
            SplashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent jumppendingintent = PendingIntent.getActivity(context, 0,  SplashIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget,jumppendingintent);
            ComponentName me = new ComponentName(context,NewAppWidget.class);
            appWidgetManager.updateAppWidget(me,remoteViews);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


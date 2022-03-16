package com.playxboxtion233.screenshotpc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class DowloadWidgetStart extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_dowloadquick);
        views.setImageViewResource(R.id.imageView5,R.drawable.widgetshow);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        SharedPreferences userInfo = context.getSharedPreferences("user", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.putBoolean("canbewidget",true);
        editor.commit();
            final int N = appWidgetIds.length;
            for (int i=0; i<N; i++) {
                int appWidgetId = appWidgetIds[i];
                //获取远程的控件，第一个参数设置程序名称，第二个参数是设置AppWidget的布局文件
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_dowloadquick);
                Intent openApp = new Intent(context, TrueDowloadWidget.class);
                openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                openApp.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, openApp, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.dowloadwidget, pendingIntent);
                //更新AppWidget的绑定事件
                //第一个参数指定绑定到哪一个AppWidget
                //第二个参数是指定要更新哪一个远程控件
                System.out.println("更新了");
                appWidgetManager.updateAppWidget(appWidgetId, views);
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
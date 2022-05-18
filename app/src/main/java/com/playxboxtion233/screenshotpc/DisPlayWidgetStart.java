package com.playxboxtion233.screenshotpc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class DisPlayWidgetStart extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            //获取远程的控件，第一个参数设置程序名称，第二个参数是设置AppWidget的布局文件
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.displayphoto);
            Intent openApp = new Intent(context, TrueDisplayWidget.class);
            openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            openApp.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, openApp, PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.buwigre, pendingIntent);

            Intent randomintent=new Intent(context, RandomPhotoChoose.class);
            randomintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            randomintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, appWidgetId, randomintent, PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.randompicture, pendingIntent2);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



}
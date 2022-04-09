package com.playxboxtion233.screenshotpc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.RemoteViews;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Implementation of App Widget functionality.
 */
public class Volumecontrol extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.volumecontrol);

        Intent VOLUMEUP=new Intent();
        VOLUMEUP.setClass(context,Volumecontrol.class);
        VOLUMEUP.setAction("com.screencp.VOLUMEUP");
        PendingIntent VOLUMEUP_x = PendingIntent.getBroadcast(context, 0, VOLUMEUP, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_volumeup,VOLUMEUP_x);

        Intent VOLUMEDOWN=new Intent();
        VOLUMEDOWN.setClass(context,Volumecontrol.class);
        VOLUMEDOWN.setAction("com.screencp.VOLUMEDOWN");
        PendingIntent VOLUMEDOWN_x = PendingIntent.getBroadcast(context, 0, VOLUMEDOWN, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_volumedown,VOLUMEDOWN_x);

        Intent VOLUMEMUTE=new Intent();
        VOLUMEMUTE.setClass(context,Volumecontrol.class);
        VOLUMEMUTE.setAction("com.screencp.VOLUMEMUTE");
        PendingIntent VOLUMEMUTE_x = PendingIntent.getBroadcast(context, 0, VOLUMEMUTE, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_mute,VOLUMEMUTE_x);

        Intent lastmusic=new Intent();
        lastmusic.setClass(context,Volumecontrol.class);
        lastmusic.setAction("com.screencp.lastmusic");
        PendingIntent lastmusicx=PendingIntent.getBroadcast(context,0,lastmusic,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_lastmusic,lastmusicx);

        Intent nextmusic=new Intent();
        nextmusic.setClass(context,Volumecontrol.class);
        nextmusic.setAction("com.screencp.nextmusic");
        PendingIntent nextmusicx=PendingIntent.getBroadcast(context,0,nextmusic,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_nextmusic,nextmusicx);

        Intent pause=new Intent();
        pause.setClass(context,Volumecontrol.class);
        pause.setAction("com.screencp.pausemusic");
        PendingIntent pausex=PendingIntent.getBroadcast(context,0,pause,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_pause,pausex);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        String action=intent.getAction();
        System.out.println(action);

        switch (action){

            case "com.screencp.VOLUMEUP":volumecontrol("volumeup",context);break;
            case "com.screencp.VOLUMEDOWN":volumecontrol("volumedown",context);break;
            case "com.screencp.VOLUMEMUTE":volumecontrol("mute",context);break;
            case "com.screencp.lastmusic":volumecontrol("premusic",context);break;
            case "com.screencp.nextmusic":volumecontrol("nextmusic",context);break;
            case "com.screencp.pausemusic":volumecontrol("pause",context);break;

        }

    }
    public void volumecontrol(String choose,Context context)
    {
        DatagramSocket socket = null;
        InetAddress serverAddress = null;
        SharedPreferences userInfo = context.getSharedPreferences("user",  context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        String ip=userInfo.getString("userip","000");
        String passwordnn=userInfo.getString("userpassword","000");
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            String sendData = choose+passwordnn;
            byte[] data = sendData.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }

    }
}
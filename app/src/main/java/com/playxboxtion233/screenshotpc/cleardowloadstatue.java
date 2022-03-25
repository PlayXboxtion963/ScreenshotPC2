package com.playxboxtion233.screenshotpc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;

public class cleardowloadstatue extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleardowloadstatue);
        moveTaskToBack(true);
        Toast.makeText(this, "已取消", Toast.LENGTH_SHORT).show();
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        cancletoastttt(2);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.putBoolean("canbetile",true);
        editor.putBoolean("canbewidget",true);
        editor.commit();
        android.os.Process.killProcess(getIntent().getIntExtra("DowloadPid",android.os.Process.myPid()));
        CacheUtil mcache=new CacheUtil();
        mcache.clearAllCache(this);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cancletoastttt(int a)
    {
        final String CHANNEL_ID = "channel_id_1";
        final String CHANNEL_NAME = "channel_name_1";
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANNEL_ID);
        mNotificationManager.cancel(1);
        mNotificationManager.cancel(3);
    }
}
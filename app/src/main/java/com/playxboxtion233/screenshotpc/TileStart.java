package com.playxboxtion233.screenshotpc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class TileStart extends TileService {
    public static final String TAG = TileStart.class.getSimpleName();

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.putBoolean("canbetile",true);
        editor.commit();
        // 当用户添加Tile到快速设置区域时调用，可以在这里进行一次性的初始化操作。
        Log.d(TAG, "onTileAdded()============");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        // 当Tile变为可见时调用，这里可以进行更新Tile，注册监听或回调等操作。
        Log.d(TAG, "onStartListening()============");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        // 当Tile变为不可见时调用，这里可以进行注销监听或回调等操作。
        Log.d(TAG, "onStopListening()============");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        // 当用户从快速设置区域移除一个Tile时调用，这里不要做有关于此Tile的任何操作。
        Log.d(TAG, "onTileRemoved()============");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick() {
        super.onClick();
        collapseStatusBar(this);
        if (Build.VERSION.SDK_INT <=29) {
            Toast.makeText(this, "快捷截图暂不支持安卓10以下", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        if(userInfo.getBoolean("canbetile",true)==false){
            Toast.makeText(this,"上次还没下载完(如果卡住了请重新添加快捷方式来刷新)",Toast.LENGTH_LONG).show();
            return;
        }
        editor.putBoolean("canbetile",false);
        editor.commit();
        int state = getQsTile().getState();
        if (state == Tile.STATE_INACTIVE) {getQsTile().setState(Tile.STATE_ACTIVE);}
        else{getQsTile().setState(Tile.STATE_INACTIVE);}
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, TrueTile.class);
        startActivity(intent);
    }

    public static void collapseStatusBar(Context context) {
        try {
            @SuppressLint("WrongConstant") Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}

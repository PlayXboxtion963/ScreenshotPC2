package com.playxboxtion233.screenshotpc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.FtpOption;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrueTile extends AppCompatActivity {
    private String URIx;
    private String TASKNAME;
    private static Uri muri=null;
    private DatagramSocket socket = null;
    private InetAddress serverAddress = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toastttt(0);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        moveTaskToBack(true);
        Aria.download(this).register();
        shenqingtupian();
        AfterRequest();
    }

    public void savephotobyaria() {
        //??????????????????????????????
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//??????Editor
        String ipad=userInfo.getString("userip","000");
        String password=userInfo.getString("userpassword","000");
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("9=n@Yb(thyZ5", password);
        String murl = "ftp://" + ipad + ":23235/tempcap.bmp";//debug???????????????????????????????????????
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.bmp";//??????????????????"Screenshot"+???????????????
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        System.out.println(mImageFileName);
        String Path = getExternalCacheDir() + "/"+mImageFileName;
        System.out.println("?????????"+Path);
        TASKNAME=mImageFileName;
        System.out.println(TASKNAME+"DIYIBU");
        long taskId = Aria.download(this)
                .loadFtp(murl) // ????????????
                .option(ftpOption)
                .setFilePath(Path) // ????????????????????????
                .create();
        URIx=Path;
    }
    private  void putBitmapToMedia(Context context, String Path) {
        String fileName;
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "t_%s.png";//??????????????????"Screenshot"+???????????????
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        fileName = mImageFileName;
        Bitmap mbitmap = null;
        if(BitmapFactory.decodeFile(Path)!=null){
            mbitmap=BitmapFactory.decodeFile(Path);
        }else{
            return;
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                + File.separator + "PC");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
       System.out.println(uri+"??????");
       muri=uri;
        System.out.println(muri+"???????????????");

        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            File file = new File(URIx);
            file.delete();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);

            context.sendBroadcast(intent);//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????file???
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void shenqingtupian() {
        System.out.println("??????");
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//??????Editor
        String ip=userInfo.getString("userip","000");

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //???

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }

        try {
            String sendData = "shot";
            byte data[] = sendData.getBytes();
            //?????????8888????????????????????????
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //???
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public void AfterRequest() {
        final Boolean[] jieshoudao = {true};
        final Thread[] mthread = {null};
        final ServerSocket[] socketx1 = {null};

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                jieshoudao[0] =false;
                try {
                    socketx1[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("????????????");
                System.out.println("TILE?????????"+TASKNAME);
                cancletoastttt(1);
                SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();//??????Editor
                editor.putBoolean("canbetile",true);
                editor.commit();
                finish();
            }
        };
        timer.schedule(task, 5000);//3????????????TimeTask???run??????
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    socketx1[0] = new ServerSocket(61123);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Socket s = socketx1[0].accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //????????????
                if (jieshoudao[0] ==true) {
                    System.out.println("?????????UDP???????????????");
                    timer.cancel();
                    savephotobyaria();
                    try {
                        socketx1[0].close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {

        System.out.println(task.getTaskName());
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT > 29) {
            putBitmapToMedia(this, URIx);//???????????????BITMAP??????
        }else{
            try {
                Toast.makeText(this,"?????????????????????????????????10??????",Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        cancletoastttt(0);
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//??????Editor
        editor.putBoolean("canbetile",true);
        editor.commit();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Download.onTaskFail void taskFail(DownloadTask task, Exception e) {
        System.out.println("TILE?????????"+TASKNAME);
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        cancletoastttt(1);
        Toast.makeText(this,"???????????????????????????????????????????????????",Toast.LENGTH_LONG).show();
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//??????Editor
        editor.putBoolean("canbetile",true);
        editor.commit();
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
        Intent intent=new Intent(Intent.ACTION_VIEW);
        System.out.println(muri);
        intent.setDataAndType(muri,"image/*");
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);

                if(a==1){
                    builder
                            .setContentTitle("????????????")
                            .setContentText("???????????????")
                            .setSmallIcon(R.drawable.newlogo)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                }else{
                    try {
                        builder
                                .setContentTitle("????????????")
                                .setContentText("???????????????")
                                .setSmallIcon(R.drawable.newlogo)
                                .setLargeIcon(MediaStore.Images.Media.getBitmap(this.getContentResolver(), muri))
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(MediaStore.Images.Media.getBitmap(this.getContentResolver(), muri))
                                        .bigLargeIcon(null))
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        mNotificationManager.cancel(3);
        mNotificationManager.notify(4, builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void toastttt(int progress)
    {
        Intent intent=new Intent(this,cleardowloadstatue.class);
        System.out.println(android.os.Process.myPid()+"??????pid");
        intent.putExtra("DowloadPid",android.os.Process.myPid());
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        final String CHANNEL_ID = "channel_id_1";
        final String CHANNEL_NAME = "channel_name_1";
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANNEL_ID);
        builder
                .setContentTitle("?????????")
                .setContentText("?????????????????????")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setProgress(100,progress,false)
                .setSmallIcon(R.drawable.newlogo);
        mNotificationManager.notify(3, builder.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }

       toastttt(task.getPercent());

    }

}
package com.playxboxtion233.screenshotpc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.FtpOption;
import com.arialyy.aria.core.task.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class widgetDowloadPhoto extends AppCompatActivity {
    private String URIx;
    private String TASKNAME;
    private static Uri muri=null;
    private DatagramSocket socket = null;
    private InetAddress serverAddress = null;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    final Context context = widgetDowloadPhoto.this;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toastttt();
        super.onCreate(savedInstanceState);
        moveTaskToBack(true);
        Aria.download(this).register();
        setContentView(R.layout.activity_tilewidget_dowload_photo);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        shenqingtupian();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                savephotobyaria();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 990);//3秒后执行TimeTask的run方法



    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void toastttt()
    {
        final String CHANNEL_ID = "channel_id_1";
        final String CHANNEL_NAME = "channel_name_1";
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANNEL_ID);
        builder
                .setContentTitle("下载中")
                .setContentText("尝试下载")
                .setOngoing(true)
                .setSmallIcon(R.drawable.newlogo);
        mNotificationManager.notify(1, builder.build());
    }
    public void savephotobyaria() {
        //以上为获取两个文本框
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        String ipad=userInfo.getString("userip","000");
        String password=userInfo.getString("userpassword","000");
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("9=n@Yb(thyZ5", password);
        String murl = "ftp://" + ipad + ":23235/tempcap.bmp";//debug阶段，以后可以加上混乱端口
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.bmp";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        System.out.println(mImageFileName);
        String Path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + mImageFileName;
        TASKNAME="Pictures"+mImageFileName;
        System.out.println(TASKNAME+"DIYIBU");
        long taskId = Aria.download(this)
                .loadFtp(murl) // 下载地址
                .option(ftpOption)
                .setFilePath(Path) // 设置文件保存路径
                .create();
        URIx=Path;
    }
    private  void putBitmapToMedia(Context context, String Path) {
        String fileName;
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "t_%s.png";//图片名称，以"Screenshot"+时间戳命名
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
        muri=uri;
        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            File file = new File(URIx);
            file.delete();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);

            context.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void shenqingtupian() {
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        String ip=userInfo.getString("userip","000");

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }

        try {
            String sendData = "shot";
            byte data[] = sendData.getBytes();
            //这里的8888是接收方的端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {

        System.out.println(task.getTaskName());
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }

        Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
        //不同版本拷贝语句
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_dowloadquick);

        if (Build.VERSION.SDK_INT > 29) {
            putBitmapToMedia(this, URIx);//下载完成后BITMAP拷贝
        }else{
            try {
                Toast.makeText(this,"小组件截图暂不支持安卓10以下",Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        grantUriAccessToWidget(this,muri);
        views.setImageViewResource(R.id.imageView5,R.drawable.widgetshow);
        views.setImageViewUri(R.id.imageView5,muri);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        System.out.println(muri);
        cancletoastttt();
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }
    protected void grantUriAccessToWidget(Context context, Uri uri) {
        Intent intent= new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cancletoastttt()
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
        builder
                .setContentTitle("下载完成")
                .setContentText("下载完成了")
                .setSmallIcon(R.drawable.newlogo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        mNotificationManager.cancel(1);
        mNotificationManager.notify(2, builder.build());
    }
}
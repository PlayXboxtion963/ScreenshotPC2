package com.playxboxtion233.screenshotpc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.FtpOption;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PIPcapture extends AppCompatActivity {
    private static Uri muri=null;
    private String URIx;
    private String TASKNAME;
    private DatagramSocket socket = null;
    private InetAddress serverAddress = null;
    private boolean canbeclick=true;
    private BroadcastReceiver mbrocast=null;
    final Context context = PIPcapture.this;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipcapture);
        enterpicture();
        Aria.download(this).register();
        mbrocast=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(canbeclick==true){
                    canbeclick=false;
                    System.out.println("??????????????????");
                    screenshotx();}
            }
        };
        IntentFilter filter = new IntentFilter("com.screenshotpc.pipshot");
       this.getApplication().registerReceiver(mbrocast, filter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterpicture(){
        PictureInPictureParams.Builder builder=null;
        builder=new PictureInPictureParams.Builder();
        Intent openApp = new Intent("com.screenshotpc.pipshot");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,openApp,0);
        Icon icon=Icon.createWithResource(this,R.drawable.pipcap);
        ArrayList<RemoteAction> mactions=new ArrayList<>();
        mactions.add(new RemoteAction(icon,"title","title",pendingIntent));
        builder.setActions(mactions);
        Rational mac=new Rational(16,9);
        builder.setAspectRatio(mac);
        enterPictureInPictureMode(builder.build());
    }
    private  void screenshotx(){

        ProgressBar mbar=findViewById(R.id.pipprogressBar);
        mbar.setVisibility(View.VISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.pipSpeed);
        speed.setText("?????????");
        speed.setVisibility(View.VISIBLE);
        shenqingtupian();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                savephotobyaria();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 990);//3????????????TimeTask???run??????
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

            context.sendBroadcast(intent);//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????file???
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void shenqingtupian() {
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
            byte[] data = sendData.getBytes();
            //?????????8888????????????????????????
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //???
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        canbeclick=true;
        System.out.println("?????????"+TASKNAME);
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        ImageView imageView = findViewById(R.id.pipcap);
        Glide.with(this)
                .load(URIx)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
                .skipMemoryCache(true)//??????????????????
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
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
        ProgressBar mbar=findViewById(R.id.pipprogressBar);
        mbar.setVisibility(View.INVISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.pipSpeed);
        speed.setVisibility(View.INVISIBLE);
    }
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        ProgressBar mbar=findViewById(R.id.pipprogressBar);
        mbar.setProgress(task.getPercent()); // ??????????????????
        TextView speed=findViewById(R.id.pipSpeed);
        speed.setText(task.getConvertSpeed()+"  |  "+task.getPercent()+"%");
    }
    @Download.onTaskFail void taskFail(DownloadTask task, Exception e) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        canbeclick=true;
        Toast.makeText(this,"???????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
        ProgressBar mbar=findViewById(R.id.pipprogressBar);
        mbar.setVisibility(View.INVISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.pipSpeed);
        speed.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onDestroy() {
        //????????????????????????????????????

        super.onDestroy();

        Log.i("1", "?????????des??????");
        //???????????????????????????????????????
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("1", "?????????onPause??????");
        Log.i("1", "activity?????????????????????");
    }

    @Override
    protected void onStop() {
        try{
        this.getApplication().unregisterReceiver(mbrocast);
        Aria.download(this).unRegister();}
        catch(IllegalArgumentException e){}

        super.onStop();
        Log.i("1", "?????????onStop??????");
        Log.i("1", "activity??????????????????");
    }
    @Override
    protected void onResume() {

        super.onResume();
    }
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {

        } else {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
        }
    }

}
package com.playxboxtion233.screenshotpc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.FtpOption;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {
    private ImageButton btn;
    private static int buttoncounter=1;
    private static String URIx = null;
    private ImageButton btn_connect;
    private ImageButton btn_shotwindow;
    private ImageButton btn_history;
    private ImageButton btn_gif;
    private Button btn_debug;
    private ImageButton btn_passwordstatue;
    private DatagramSocket socket = null;
    private InetAddress serverAddress = null;
    private ProgressDialog progressDialog;
    private boolean displayx=false;
    public static final int DELAY = 1000;
    private static long lastClickTime = 0;
    private static long lastClickTime1 = 0;
    long lasttime=0;
    private static int jiance=0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String PREFS_NAME = "user";
    private boolean zhendong;
    Timer  timerx=new Timer();
    public  static Uri Uritoshare=null;
    private File sharefile=null;
    private boolean historyipstatue;
private  boolean yincanginput;
private boolean yincangbiaozhi;
private  int isoled=0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        super.onCreate(savedInstanceState);

        Aria.download(this).register();
        verifyStoragePermissions(this);

        setContentView(R.layout.activity_main);

        startup();
//状态栏透明并且文字自适应
        Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option;
            option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);//将导航栏设置为透明色


        //toolbar初始化

        Toolbar mtoolbar=(Toolbar)findViewById(R.id.my_toolbar);
        mtoolbar.setTitle(R.string.app_name);
        mtoolbar.inflateMenu(R.menu.mymenu);
        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();//获取Editor
                switch (menuItem.getItemId()) {
                    case R.id.menu_login:
                        Toast.makeText(MainActivity.this," v4.8 感谢使用,如果你想捐赠的话就去PC端下载链接捐赠吧,祝你开心愉快", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_file:
                        String path = "%2fPictures%2fPC%2f";
                        Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:" + path);
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");//想要展示的文件类型
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.menu_pc:
                        Uri urix = Uri.parse("https://gitee.com/dwaedwe12/ScreenshotPC2/releases");
                        Intent intentx = new Intent(Intent.ACTION_VIEW, urix);
                        startActivity(intentx);
                        break;
                    case R.id.zhendong:

                        //得到Editor后，写入需要保存的数据
                        if(zhendong==false){
                            zhendong=true;
                            Toast.makeText(MainActivity.this,"开", Toast.LENGTH_LONG).show();
                            editor.putBoolean("zhendong",true);
                            editor.commit();}//提交修改
                        else if(zhendong=true){
                            zhendong=false;
                            Toast.makeText(MainActivity.this,"关", Toast.LENGTH_LONG).show();
                            editor.putBoolean("zhendong",false);
                            editor.commit();//提交修改
                        }
                        break;
                    case R.id.autupatch:
                        if(historyipstatue==false){
                            historyipstatue=true;
                            Toast.makeText(MainActivity.this,"开", Toast.LENGTH_LONG).show();
                            editor.putBoolean("historyipstatue",true);
                            editor.commit();}//提交修改
                        else if(historyipstatue=true){
                            historyipstatue=false;
                            Toast.makeText(MainActivity.this,"关", Toast.LENGTH_LONG).show();
                            editor.putBoolean("historyipstatue",false);
                            editor.commit();//提交修改
                        }
                        break;
                    case R.id.inputstatue:
                        if(isoled==1){Toast.makeText(MainActivity.this,"OLED下设置无效", Toast.LENGTH_LONG).show();break;}
                        if(yincanginput==true){
                            inputstatue(false);
                            yincanginput=false;
                            Toast.makeText(MainActivity.this,"隐藏", Toast.LENGTH_LONG).show();
                            editor.putBoolean("yincanginput",false);

                        }else{//程序一开始隐藏input等于false，就是不显示，然后你点按钮就让他显示
                            inputstatue(true);
                            yincanginput=true;
                            Toast.makeText(MainActivity.this,"显示", Toast.LENGTH_LONG).show();
                            editor.putBoolean("yincanginput",true);
                        }editor.commit();
                        break;
                    default:
                        break;
                }
                if (!msg.equals("")) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //状态栏透明
//       Window window = this.getWindow();
//////        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//////        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//////        int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//////        window.getDecorView().setSystemUiVisibility(option);
//////        window.setStatusBarColor(Color.TRANSPARENT);
//      if (this.getApplicationContext().getResources().getConfiguration().uiMode == 0x11) {
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
//          window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
        //密码隐藏
        EditText et_password = (EditText)findViewById(R.id.password);
        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        if(userInfo.contains("zhendong")==false){
                editor.putBoolean("zhendong",true);
                editor.commit();
        }//初次启动，如果震动不存在，给个true
        zhendong = userInfo.getBoolean("zhendong", true);
////////////////////////////

        if(userInfo.contains("historyipstatue")==false){
            editor.putBoolean("zhendong",true);
            editor.commit();
        }//初次启动，如果不存在，给个true
        historyipstatue = userInfo.getBoolean("historyipstatue", true);
        if(historyipstatue==true){
            gethisrotyip();
        }
        //////////////////////////////
        if(userInfo.contains("yincanginput")==false){
            editor.putBoolean("yincanginput",true);
            editor.commit();
        }
        yincanginput=userInfo.getBoolean("yincanginput",true);//true才是显示
        if(yincanginput==true){
            inputstatue(true);
            yincanginput=true;
            editor.putBoolean("yincanginput",true);
            editor.commit();
        }else{
            inputstatue(false);
            yincanginput=false;
            editor.putBoolean("yincanginput",false);
            editor.commit();
        }
    }
    public void startup(){
    btn = (ImageButton) findViewById(R.id.Fullscreen);
    btn_connect = findViewById(R.id.storebtn);
    btn_shotwindow = (ImageButton) findViewById(R.id.Topcap);
    btn_gif=(ImageButton)findViewById(R.id.search);
    btn_history=(ImageButton)findViewById(R.id.history);
    btn_debug=(Button)findViewById(R.id.debug);
    ImageButton btn_screenon=(ImageButton)findViewById(R.id.screenon);
    btn_passwordstatue=(ImageButton)findViewById(R.id.passwordstatue);
    ImageButton btn_share=(ImageButton)findViewById(R.id.share);
    ImageButton btn_deleteph=(ImageButton)findViewById(R.id.deleteph);
    ImageButton btn_wallpaper=(ImageButton)findViewById(R.id.wallpaper);
    PhotoView photoView = (PhotoView) findViewById(R.id.img);
    photoView.setVisibility(View.INVISIBLE);
    ImageView ImageViewxxx=findViewById(R.id.imgbackgrdx);
    ImageViewxxx.setVisibility(View.INVISIBLE);
    ImageViewxxx.setOnClickListener(this);
    photoView.setOnClickListener(this);
    ImageView suolue=(ImageView)findViewById(R.id.imageView);
    findViewById(R.id.timeText).setVisibility(View.INVISIBLE);
    suolue.setOnClickListener(this);
    btn_wallpaper.setOnClickListener(this);
    btn_wallpaper.setOnTouchListener(this);
    btn_deleteph.setOnClickListener(this);
    btn_deleteph.setOnTouchListener(this);
    btn_share.setOnClickListener(this);
    btn_share.setOnTouchListener(this);
    btn_screenon.setOnClickListener(this);
    btn_screenon.setOnTouchListener(this);
    btn_debug.setOnClickListener(this);
    btn_passwordstatue.setOnTouchListener(this);
    btn.setOnClickListener(this);
    btn.setOnTouchListener(this);
    btn_connect.setOnClickListener(this);
    btn_connect.setOnTouchListener(this);
    btn_shotwindow.setOnClickListener(this);
    btn_shotwindow.setOnTouchListener(this);
    btn_gif.setOnClickListener(this);
    btn_gif.setOnTouchListener(this);
    btn_history.setOnClickListener(this);
    btn_history.setOnTouchListener(this);
}//按钮初始化



    //分享
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            share(uri);

        }
    }
    public void share(Uri uri) {

        //三方库分享调用
        new Share2.Builder(this)
                // 指定分享的文件类型
                .setContentType(ShareContentType.IMAGE)
                // 设置要分享的文件 Uri
                .setShareFileUri(uri)
                // 设置分享选择器的标题
                .setTitle("Share")
                .build()
                // 发起分享
                .shareBySystem();


    }



    //按钮事件

    @Override
    public void onClick(View view) {
        PhotoView photoView = (PhotoView) findViewById(R.id.img);
        ImageView imagebackg=findViewById(R.id.imgbackgrdx);
        ImageView ImageViewxx=(ImageView)findViewById(R.id.imageView);
        Toolbar mtoolbarx=(Toolbar)findViewById(R.id.my_toolbar);
        switch (view.getId()) {

            case R.id.Fullscreen://全屏按钮
                if (check() == 1)
                    break;
                long currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime1>2000){
                    lastClickTime1 = currentTime;
                }else{
                    return;
                }
                shenqingtupian();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        savephotobyaria();
                    }
                }, 1000);
                break;

            case R.id.storebtn://保存按钮
                if (check() == 1)
                    break;
                historyipstore();
                break;


            case R.id.Topcap://局部按钮
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>2000){
                    lastClickTime = currentTime;
                }else{
                    return;
                }
                if (check() == 1)
                    break;
                shenqingtupian2();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        savephotobyaria();
                    }
                }, 1500);
                break;
            case R.id.history:
                gethisrotyip();
                break;
            case R.id.search:
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>1000){
                    lastClickTime = currentTime;
                }else{
                    return;
                }
                progressDialog = ProgressDialog.show(this, "", "搜索中");
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         *要执行的操作
                         */
                        progressDialog.dismiss();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 990);//3秒后执行TimeTask的run方法
                EditText text1x = (EditText) findViewById(R.id.PCIP);
                udpjieshou(text1x);
                break;
            case R.id.debug:
                Intent intent = new Intent();
                //setClass函数的第一个参数是一个Context对象
                //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                intent.setClass(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                break;
            case R.id.screenon:

                buttoncounter++;
                ImageButton lbtitle=(ImageButton)findViewById(R.id.screenon);
                ImageButton btn_wallpaper=(ImageButton)findViewById(R.id.wallpaper);
                ImageView imgx=(ImageView) findViewById(R.id.imageView2);
                View decorView = getWindow().getDecorView();
                WindowManager.LayoutParams windowLP = getWindow().getAttributes();
                Paint paint = new Paint();
                ImageButton btn_share=(ImageButton)findViewById(R.id.share);
                ImageButton btn_deleteph=(ImageButton)findViewById(R.id.deleteph);
                ImageButton btn_passwordstatue=(ImageButton)findViewById(R.id.passwordstatue);
                ColorMatrix cm = new ColorMatrix();
                EditText text1 = (EditText) findViewById(R.id.PCIP);
                EditText text2 = (EditText) findViewById(R.id.password);
                Toolbar mtoolbar=findViewById(R.id.my_toolbar);
                if(buttoncounter==2){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(this,"屏幕常亮已打开(OLED模式)", Toast.LENGTH_SHORT).show();
                    lbtitle.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledon));
                    btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oled));
                    btn_history.setVisibility(View.INVISIBLE);
                    btn_shotwindow.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oled));
                    btn_gif.setVisibility(View.INVISIBLE);
                    btn_connect.setVisibility(View.INVISIBLE);
                    imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledsave));
                    this.getWindow().setBackgroundDrawable(getDrawable(android.R.color.black));
                    btn_share.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledshare));
                    btn_deleteph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.deletepholed));
                    btn_wallpaper.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledwallpaper));


                    isoled=1;
                    ConstraintSet set = new ConstraintSet();
                    ConstraintLayout mlayout = (ConstraintLayout) findViewById(R.id.mainview);
                    set.clone(mlayout);

                    set.connect(R.id.imageviewarea, ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 192, getResources()
                                    .getDisplayMetrics()));
                    TransitionManager.beginDelayedTransition(mlayout);
                    set.applyTo(mlayout);



                    btn_passwordstatue.setVisibility(View.INVISIBLE);
                    findViewById(R.id.timeText).setVisibility(View.VISIBLE);
                    mtoolbar.setTitle("");
                    text1.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageView3).setVisibility(View.INVISIBLE);
                    timerx=new Timer();
                    TimerTask timertask = new TimerTask() {
                        @Override
                        public void run() {

                            Message message = new Message();
                            long currentTime = System.currentTimeMillis();
                            if (currentTime-lasttime>20000){
                                lasttime = currentTime;
                                message.what = 4;
                            }else if(currentTime-lasttime>15000){
                                message.what = 3;
                            }else if(currentTime-lasttime>10000){
                                message.what = 2;
                            }
                            else if(currentTime-lasttime>5000){
                                message.what = 1;

                            }
                            handler.sendMessage(message);
                        }
                    };
                    timerx.schedule(timertask,1000,5000);
                }if(buttoncounter==3){
                    buttoncounter=1;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(this,"屏幕常亮已关闭", Toast.LENGTH_SHORT).show();
                    lbtitle.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.offf));
                    btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dayquanpin));
                    btn_history.setVisibility(View.VISIBLE);
                    btn_shotwindow.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.daydingbu));
                    btn_gif.setVisibility(View.VISIBLE);
                    btn_connect.setVisibility(View.VISIBLE);
                    imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.backgrd));
                    btn_deleteph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.deleteph));
                    btn_share.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
                    btn_wallpaper.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.wallpapaer));
                if(this.getApplicationContext().getResources().getConfiguration().uiMode == 0x21){
                        this.getWindow().setBackgroundDrawable(getDrawable(R.color.luse));
                 }else{this.getWindow().setBackgroundDrawable(getDrawable(R.color.luse));}
                findViewById(R.id.timeText).setVisibility(View.INVISIBLE);

                isoled=0;
                if(yincangbiaozhi==true){ConstraintSet set = new ConstraintSet();
                       ConstraintLayout mlayout = (ConstraintLayout) findViewById(R.id.mainview);
                       set.clone(mlayout);
                       set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM,300);
                       TransitionManager.beginDelayedTransition(mlayout);
                       set.applyTo(mlayout);}


                    mtoolbar.setTitle("懒得截图");
                    timerx.cancel();
                    ConstraintLayout mview=(ConstraintLayout)findViewById(R.id.mainview);
                    mview.scrollTo(0,0);
                    btn_passwordstatue.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.share:
                if(Uritoshare!=null){
                    share(Uritoshare);}
                else{Toast.makeText(this,"先截图,谢谢", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.deleteph:
                if(Uritoshare!=null){
                    showNormalMoreButtonDialog(view);
                }else{Toast.makeText(this,"先截图,谢谢", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.wallpaper:

                if(Uritoshare!=null){
                    Intent intenta = new Intent(Intent.ACTION_ATTACH_DATA);
                    intenta.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intenta.putExtra("mimeType", "image/*");
                    if (Uritoshare.toString().startsWith("content://")) {
                        intenta.setData(Uritoshare);
                    } else {
                        try {
                            intenta.setData(getImageContentUri(this, sharefile));
                        } catch (Exception e)
                        {}
                    }
                if(intenta.resolveActivity(getPackageManager()) != null)
                {startActivity(intenta);}
                else{Toast.makeText(this,"没有图片设置程序", Toast.LENGTH_SHORT).show();}
//                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
//                    try {
//                        wallpaperManager.setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uritoshare));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
}

                else{Toast.makeText(this,"先截图,谢谢", Toast.LENGTH_SHORT).show();}
                break;

            case R.id.imageView:
                if(Uritoshare==null)
                    break;

                ConstraintLayout mview=(ConstraintLayout)findViewById(R.id.mainview);
                photoView.setVisibility(View.VISIBLE);
                imagebackg.setVisibility(View.VISIBLE);
                photoView.enable();
                Glide.with(this)
                        .load(R.drawable.photoback)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(imagebackg);
                mtoolbarx.setVisibility(View.INVISIBLE);
                // 从普通的ImageView中获取Info
                Info info = PhotoView.getImageViewInfo(ImageViewxx);
                // 从一张图片信息变化到现在的图片，用于图片点击后放大浏览，具体使用可以参照demo的使用
                photoView.animaFrom(info);
                photoView.setAnimaDuring(300);
                photoView.setMaxScale(5);
                Glide.with(this)
                        .load(Uritoshare)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(photoView);

                photoView.setInterpolator(new AccelerateDecelerateInterpolator());

                break;

            case R.id.img:
            case R.id.imgbackgrdx:
                if(Uritoshare==null)
                    break;

                info = PhotoView.getImageViewInfo(ImageViewxx);
                                photoView.animaTo(info,new Runnable() {
                    @Override
                    public void run() {
                        //动画完成监听
                        imagebackg.setVisibility(View.INVISIBLE);
                        photoView.setVisibility(View.INVISIBLE);
                        mtoolbarx.setVisibility(View.VISIBLE);
                    }
                });

                break;
            default:
                break;

        }
    }

    private void showNormalMoreButtonDialog(View view){
        AlertDialog.Builder normalMoreButtonDialog = new AlertDialog.Builder(this);
        normalMoreButtonDialog.setTitle("确信要删除？");
        //normalMoreButtonDialog.setIcon();
        //normalMoreButtonDialog.setMessage(getString(R.string.dialog_normal_more_button_content));

        //设置按钮
        normalMoreButtonDialog.setPositiveButton("确定"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletephotowith(view);
                        dialog.dismiss();
                    }
                });

        normalMoreButtonDialog.setNeutralButton("否"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        normalMoreButtonDialog.create().show();
    }//按下删除后弹对话框
    private void deletephotowith(View view){
        deleteUri(this,Uritoshare);
        Toast.makeText(this,"删除成功", Toast.LENGTH_SHORT).show();
        ImageView imageViewx=(ImageView)findViewById(R.id.imageView3);
        imageViewx.setImageResource(android.R.color.transparent);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this)
                .load(R.drawable.daymoren)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(imageView);

        view.performHapticFeedback(HapticFeedbackConstants.REJECT);
        Intent intentx = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentx.setData(Uritoshare);
        this.sendBroadcast(intentx);
        Uritoshare=null;
    }//被对话框调用的删除第一步，它再调用下一步
    public void deleteUri(Context context, Uri uri) {

        if (uri.toString().startsWith("content://")) {
            // content://开头的Uri
            context.getContentResolver().delete(uri, null, null);
        } else {
            File file = sharefile;
            if (file.exists()&& file.isFile()){
                file.delete();
            }
        }
    }//真正执行删除图片的位置
    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }//用来做设置壁纸的URIcontent


    private Bitmap createBitmap(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }//获取当前view的bitmap用于高斯模糊





    //oled防烧代码
    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler(){
        public void handleMessage(Message message){
            Random r = new Random();
            ConstraintLayout mview=(ConstraintLayout)findViewById(R.id.mainview);
            switch (message.what){
                case 1:
                    mview.scrollTo( r.nextInt(18)+3,0);
                    break;
                case 2:
                    mview.scrollTo(0,r.nextInt(18)+3);
                    break;
                case 3:
                    mview.scrollTo( r.nextInt(21) - 20,0);
                    break;
                case 4:
                    mview.scrollTo(0,r.nextInt(21) - 20);
                    break;
            }
        }
    };

    //马达
    public boolean onTouch(View v, MotionEvent event) {
    if(zhendong) {
        switch (v.getId()) {
            case R.id.search:
            case R.id.history:
            case R.id.Fullscreen:
            case R.id.Topcap:
            case R.id.deleteph:
            case R.id.share:
            case R.id.screenon:
            case R.id.storebtn:
            case R.id.wallpaper:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                break;
            case R.id.passwordstatue:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    EditText et_password = (EditText)findViewById(R.id.password);
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    EditText et_password = (EditText)findViewById(R.id.password);
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

        return false;
    }


    //自动搜索电脑
    public void udpjieshou(EditText text) {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        String a = new String("abc");
                        DatagramSocket dgSocket = null;
                        int port = 9832;
                        dgSocket = new DatagramSocket(null);
                        dgSocket.setReuseAddress(true);
                        dgSocket.bind(new InetSocketAddress(port));
                        byte[] by = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(by, by.length);
                        dgSocket.receive(packet);
                        String str = new String(packet.getData(), 0, packet.getLength());
                        text.post(new Runnable() {
                            public void run() {
                                text.setText(str, TextView.BufferType.NORMAL);
                            }
                        });
                        Looper.prepare();
                            Toast.makeText(MainActivity.this, "搜索到"+str, Toast.LENGTH_SHORT).show();
                            Looper.loop();


                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    //链接测试
    public void connects() {

        EditText text1 = (EditText) findViewById(R.id.PCIP);
        String ip = text1.getText().toString();//murl为文本框内容
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "尝试链接", Toast.LENGTH_LONG).show();
        progressDialog = ProgressDialog.show(this, "", "尝试连接中");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                progressDialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);//3秒后执行TimeTask的run方法
        try {
            String sendData = "plstes";
            byte data[] = sendData.getBytes();
            //这里的8888是接收方的端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkudp();
        text1=null;
        ip=null;

    }

    //申请图片
    public void shenqingtupian() {

        EditText text1 = (EditText) findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl为文本框内容

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }
        Toast.makeText(MainActivity.this, "申请让电脑截图中", Toast.LENGTH_SHORT).show();

        progressDialog = ProgressDialog.show(this, "", "下载中");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                progressDialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 990);//3秒后执行TimeTask的run方法

        try {
            String sendData = "shot";
            byte data[] = sendData.getBytes();
            //这里的8888是接收方的端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }
        text1=null;

    }

    //申请局部截图
    public void shenqingtupian2() {

        EditText text1 = (EditText) findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl为文本框内容

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }
        Toast.makeText(MainActivity.this, "申请局部截图中", Toast.LENGTH_SHORT).show();
        progressDialog = ProgressDialog.show(this, "", "下载中");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                progressDialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1490);//3秒后执行TimeTask的run方法

        try {
            String sendData = "shotwindows";
            byte data[] = sendData.getBytes();
            //这里的8888是接收方的端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }
        text1=null;
    }




    //ip检测
    public int check() {
        EditText text1 = (EditText) findViewById(R.id.PCIP);
        String string = text1.getText().toString();//murl为文本框内容
        /*正则表达式*/
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//限定输入格式
        Pattern p = Pattern.compile(ip);
        Matcher m = p.matcher(string);
        boolean b = m.matches();
        if (b == false) {
            Toast.makeText(this, "IP格式输入错误", Toast.LENGTH_LONG).show();
            return 1;
        }
        return 0;
    }

    //UDP接收
    public void checkudp() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        String a = new String("abc");
                        DatagramSocket dgSocket = null;
                        int port = 21222;
                        dgSocket = new DatagramSocket(null);
                        dgSocket.setReuseAddress(true);
                        dgSocket.bind(new InetSocketAddress(port));
                        byte[] by = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(by, by.length);
                        dgSocket.receive(packet);
                        String str = new String(packet.getData(), 0, packet.getLength());
                        if (a.equals(str)) {
                            //jiance=1;
                            Looper.prepare();
                            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                     }.start();
            }

    //权限申请
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //历史IP与密码
    public void historyipstore(){
        EditText text1 = (EditText) findViewById(R.id.PCIP);
        String ipnn = text1.getText().toString();//murl为文本框内容
        EditText text2 = (EditText) findViewById(R.id.password);
        String passwordnn = text2.getText().toString();//murl为文本框内容

        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor

        //得到Editor后，写入需要保存的数据
        editor.putString("userip", ipnn);
        editor.putString("userpassword",passwordnn);
        editor.commit();//提交修改
        Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
    }
    private void gethisrotyip(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String ipmm = userInfo.getString("userip", null);//读取username
        String password=userInfo.getString("userpassword", null);
        EditText text1 = (EditText) findViewById(R.id.PCIP);
        text1.setText(ipmm, TextView.BufferType.NORMAL);

        EditText text2 = (EditText) findViewById(R.id.password);
        text2.setText(password, TextView.BufferType.NORMAL);

    }

    //用aira下载ftp到缓存中
    public void savephotobyaria() {
        EditText textBOX = (EditText) findViewById(R.id.password);
        String password = textBOX.getText().toString();//murl为文本框
        EditText textBOXip = (EditText) findViewById(R.id.PCIP);
        String ipad = textBOXip.getText().toString();//murl为文本框
        //以上为获取两个文本框
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("9=n@Yb(thyZ5", password);
        String murl = "ftp://" + ipad + ":23235/tempcap.bmp";//debug阶段，以后可以加上混乱端口
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.bmp";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String Path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + mImageFileName;

        long taskId = Aria.download(this)
                .loadFtp(murl) // 下载地址
                .option(ftpOption)
                .setFilePath(Path) // 设置文件保存路径
                .create();

        URIx = Path;//把局部路径转换为全局路径，即下载完到缓存文件夹的图片，这样回调中才能使用
    }

    //安卓10缓存路径转mediastore真正进图库，其实是拷贝私有沙盒路径到公共相册过程
    public static void putBitmapToMedia(Context context, String Path) {
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
        Uritoshare=uri;
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

    //安卓9及以下拷贝缓存到文件夹
    public void putcachetomeida(String URI)throws IOException{
        Bitmap mbitmap;
        mbitmap = BitmapFactory.decodeFile(URI);
        String dir = Environment.getExternalStorageDirectory() + File.separator + "Pictures"
                + File.separator + "PC" + File.separator;
        String fileName = System.currentTimeMillis() + ".png";
        String subForder = dir;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        mbitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(foder);
        intent.setData(uri);
        uri = Uri.fromFile(myCaptureFile);
        Uritoshare=uri;
        sharefile=myCaptureFile;
        this.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        File file = new File(URIx);
        file.delete();
    }


    //下载成功后才开始拷贝缓存和一些任务
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this)
                .load(URIx)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(30)
                        ))
                .into(imageView);

        createPaletteAsync(BitmapFactory.decodeFile(URIx));
        ImageView imagebackground=findViewById(R.id.imageView3);
        Blurry.with(this)
                .radius(3)
                .sampling(5)
                .from(BitmapFactory.decodeFile(URIx))
                .into(imagebackground);
        //不同版本拷贝语句

        if (Build.VERSION.SDK_INT > 29) {
            putBitmapToMedia(this, URIx);//下载完成后BITMAP拷贝
        }else{
            try {
                putcachetomeida(URIx);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //下载失败toast
    @Download.onTaskFail void taskFail(DownloadTask task, Exception e) {
        Toast.makeText(this,"下载失败，可能是密码错误或未连接上",Toast.LENGTH_SHORT).show();
        System.out.println("错误信息");
        System.out.println(ALog.getExceptionString(e));
    }

    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
                if(p != null){
                    int titleColor = p.getVibrantColor( -16744224);
                    System.out.println(titleColor);
                    String alphaset="FF"+String.format("%08x",titleColor).substring(2);//33为透明度十六进制
                    System.out.println(String.format("%08x",titleColor).substring(2));
                    TextView mtime=findViewById(R.id.timeText);
                    mtime.setTextColor((int) Long.parseLong(alphaset, 16));
                    // ...
                }
            }
        });
    }
    public void inputstatue(boolean inputstatue){
        if(isoled==1){
            return;
        }
        if(inputstatue==false) {
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout mlayout = (ConstraintLayout) findViewById(R.id.mainview);
            set.clone(mlayout);
            set.setVisibility(R.id.inputarea,ConstraintSet.INVISIBLE);
            set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM,300);
            TransitionManager.beginDelayedTransition(mlayout);
            set.applyTo(mlayout);
            yincangbiaozhi=true;

        }else {
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout mlayout = (ConstraintLayout) findViewById(R.id.mainview);
            set.clone(mlayout);
            set.connect(R.id.imageviewarea, ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 192, getResources()
                            .getDisplayMetrics()));

            set.setVisibility(R.id.inputarea, ConstraintSet.VISIBLE);
            TransitionManager.beginDelayedTransition(mlayout);
            set.applyTo(mlayout);
            yincangbiaozhi=false;
        }
    }

    private static boolean isExit = false;
    Handler mHandlerxxx = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandlerxxx.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


}


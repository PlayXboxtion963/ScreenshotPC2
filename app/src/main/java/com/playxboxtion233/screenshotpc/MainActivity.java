package com.playxboxtion233.screenshotpc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.print.PrintHelper;

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
    private String TASKNAME;
    private int yourheartrate=120;
    private static int buttoncounter=1;
    private static String URIx = null;
    private ImageButton btn_connect;
    private ImageButton btn_shotwindow;
    private ImageButton btn_history;
    private ImageButton btn_gif;
    private DatagramSocket socket = null;
    private InetAddress serverAddress = null;
    private ProgressDialog progressDialog;
    private static long lastClickTime = 0;
    public static long lastClickTime1 = 0;
    private static long lastClickTime2 = 0;
    long lasttime=0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String PREFS_NAME = "user";
    private boolean zhendong;
    private String Mac;
    Timer  timerx=new Timer();
    public  static Uri Uritoshare=null;
    private File sharefile=null;
    private boolean historyipstatue;
    private  boolean yincanginput;
    private boolean yincangbiaozhi;
    private  int isoled=0;
    Bluetoothheart mblue;
    public boolean canbeclick=true;
    private int ismute=0;
    private int ispause=0;
    private View Lightlayout;
    private AlertDialog signlechoose;
    @SuppressLint("NonConstantResourceId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        super.onCreate(savedInstanceState);

        Aria.download(this).register();
        verifyStoragePermissions(this);

        setContentView(R.layout.activity_main);
        startup();//按钮监听器初始化
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int option;
        option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ;
        window.getDecorView().setSystemUiVisibility(option);
        window.setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);//将导航栏设置为透明色
        TextView mtext=findViewById(R.id.heartrate);

        //清理缓存
        CacheUtil mcache=new CacheUtil();
        mcache.clearAllCache(this);

        //亮度调节初始化
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.lightseekbar, (ViewGroup)findViewById(R.id.elementseek));
        Button yourDialogButton = (Button)layout.findViewById(R.id.your_dialog_button);
        SeekBar yourDialogSeekBar =layout.findViewById(R.id.your_dialog_seekbar);
        TextView lighttext=layout.findViewById(R.id.lighttext);
        Dialog yourDialog = new Dialog(this);
        yourDialog.setContentView(layout);
        SeekBar.OnSeekBarChangeListener yourSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here

            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                //add code here
                layout.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
                lighttext.setText("亮度设置为:"+String.valueOf(progress));
            }
        };
        yourDialogButton.setOnClickListener(this);
        yourDialogSeekBar.setOnSeekBarChangeListener(yourSeekBarListener);
        Lightlayout=layout;

        //toolbar初始化
        Toolbar mtoolbar= findViewById(R.id.my_toolbar);
        mtoolbar.setTitle(R.string.app_name);
        mtoolbar.inflateMenu(R.menu.mymenu);
        mtoolbar.setOnMenuItemClickListener(menuItem -> {
            String msg = "";
            SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();//获取Editor
            switch (menuItem.getItemId()) {
                case R.id.menu_login:
                    Toast.makeText(MainActivity.this,"感谢使用,如果你想捐赠的话就去PC端下载链接捐赠吧,祝你开心愉快", Toast.LENGTH_LONG).show();
                    break;
                case R.id.menu_file:
                    openPathPhoto("/storage/emulated/0/Pictures/PC");
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
                case R.id.miband:

                    ImageView mimageview=findViewById(R.id.imageView4);


                    final EditText inputServer = new EditText(MainActivity.this);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("输入MAC地址,手环设置-关于").setIcon(R.drawable.newlogo).setView(inputServer).setMessage("打开运动心率广播，手环随便开始个运动，确保小米运动挂在后台,每隔10秒判断心率是否大于目标心率")
                            .setNegativeButton("取消", null);
                    inputServer.setText(userInfo.getString("MAC","FB:35:A2:DE:F5:49"));
                    builder1.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Mac=inputServer.getText().toString();
                            editor.putString("MAC",Mac);
                            editor.commit();
                                mblue.setContext(MainActivity.this);
                                mblue.setMac(Mac);
                                mimageview.setVisibility(View.VISIBLE);
                                mblue.setTextview(mtext);
                                mblue.startble();

                        }
                    });

                    final NumberPicker inputserver3=new NumberPicker(MainActivity.this);
                    inputserver3.setMaxValue(200);
                    inputserver3.setMinValue(20);
                    inputserver3.setValue(130);
                    inputserver3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        //当NunberPicker的值发生改变时，将会激发该方法
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
                        }
                    });
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setTitle("输入目标心率").setIcon(R.drawable.newlogo).setView(inputserver3)
                            .setNegativeButton("取消", null);
                    builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            yourheartrate=inputserver3.getValue();
                            Toast.makeText(MainActivity.this, "目标心率已设置为"+String.valueOf(yourheartrate), Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder2.show();
                    builder1.show();
                    break;
                case R.id.exitx:
                    exit();
                    System.exit(0);
                    break;
                case R.id.taskman:
                    getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                    volumecontrol("taskmanager");
                    break;
                case R.id.light:
                    getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CONFIRM);

                    yourDialog.show();
                    break;
                default:
                    break;
            }
            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        //密码隐藏
        EditText et_password = findViewById(R.id.password);
        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        if(userInfo.contains("MAC")==false){
            editor.putString("MAC","FB:35:A2:DE:F5:49");
            editor.commit();
        }
       Mac=userInfo.getString("MAC","FB:35:A2:DE:F5:49");

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
        if(userInfo.contains("profile")){
            mtoolbar.setTitle(userInfo.getString("profile","懒得截图"));
        }

    }
    public void startup(){
    btn = findViewById(R.id.Fullscreen);
    btn_connect = findViewById(R.id.storebtn);
    btn_shotwindow = findViewById(R.id.Topcap);
    btn_gif= findViewById(R.id.search);
    btn_history= findViewById(R.id.history);
    Button btn_debug = findViewById(R.id.debug);
    ImageButton btn_screenon= findViewById(R.id.screenon);
    ImageButton btn_passwordstatue = findViewById(R.id.passwordstatue);
    ImageButton btn_share= findViewById(R.id.share);
    ImageButton btn_deleteph= findViewById(R.id.deleteph);
    ImageButton btn_wallpaper= findViewById(R.id.wallpaper);
    ImageButton volumeupbtn=findViewById(R.id.volumeup);
    volumeupbtn.setOnClickListener(this);
    ImageButton volumedownbtn=findViewById(R.id.volumedown);
    volumedownbtn.setOnTouchListener(this);
    volumeupbtn.setOnTouchListener(this);
    ImageButton presound=findViewById(R.id.presound);
    ImageButton nextsound=findViewById(R.id.nextsound);
    ImageButton pause=findViewById(R.id.pause);
    presound.setOnTouchListener(this);
    presound.setOnClickListener(this);
    nextsound.setOnTouchListener(this);
    nextsound.setOnClickListener(this);
    pause.setOnTouchListener(this);
    pause.setOnClickListener(this);
    ImageButton mutebtn=findViewById(R.id.mute);
    mutebtn.setOnClickListener(this);
    mutebtn.setOnTouchListener(this);
    volumedownbtn.setOnClickListener(this);
    PhotoView photoView = findViewById(R.id.img);
    photoView.setVisibility(View.INVISIBLE);
    ImageView ImageViewxxx=findViewById(R.id.imgbackgrdx);
    ImageViewxxx.setVisibility(View.INVISIBLE);
    ImageViewxxx.setOnClickListener(this);
    photoView.setOnClickListener(this);
    ImageView suolue= findViewById(R.id.imageView);
    ImageButton editbutton=findViewById(R.id.photoedit);
    editbutton.setOnClickListener(this);
    editbutton.setOnTouchListener(this);
    ImageButton printbtn=findViewById(R.id.print);
    printbtn.setOnClickListener(this);
    printbtn.setOnTouchListener(this);
    findViewById(R.id.timeTextx).setVisibility(View.INVISIBLE);
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

        mblue=new Bluetoothheart();
        TextView et = findViewById(R.id.heartrate);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float temp=(float)Integer.valueOf(mblue.getheartrate())/yourheartrate;
                temp=temp*100;
                System.out.println(temp);
                ProgressBar mprocess=findViewById(R.id.heartrateprocess);
                mprocess.setVisibility(View.VISIBLE);
                mprocess.setProgress((int)temp);
                if(Integer.valueOf(mblue.getheartrate())>=yourheartrate){
                    if (check() == 1)
                        return;
                    long currentTime = System.currentTimeMillis();
                    if (currentTime-lastClickTime1>10000){
                        lastClickTime1 = currentTime;
                    }else{
                        return;
                    }
                    shenqingtupian();
                    Vibrator mVivrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long[] pattern = {200, 100};
                    mVivrator.vibrate(pattern,-1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            savephotobyaria();
                        }
                    }, 1000);

                }
                }
        };
        et.addTextChangedListener(watcher);

    }//心率文本框变化监测


    //这里path就是传的需要浏览指定的文件夹的路径
    public void openPathPhoto(String path) {
        File file = new File(path);
        if (file.exists() && file.listFiles().length > 0) {
            //  String path= getSDPath() + "/Pictures/PDF原图";
            new PictureScanner(this, path);
        } else {
            Toast.makeText(this,"没有相关目录",Toast.LENGTH_SHORT).show();
        }

    }



    //分享
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
        FindOutDevices mfind=new FindOutDevices();
        PhotoView photoView = findViewById(R.id.img);
        ImageView imagebackg=findViewById(R.id.imgbackgrdx);
        ImageView ImageViewxx= findViewById(R.id.imageView);
        Toolbar mtoolbarx= findViewById(R.id.my_toolbar);
        ImageButton editbutton=findViewById(R.id.photoedit);
        ImageView heartback=findViewById(R.id.imageView4);
        long currentTime;
        switch (view.getId()) {


            case R.id.Fullscreen://全屏按钮
                if(canbeclick==false){
                    return;
                }
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>1000){
                    lastClickTime = currentTime;
                }else{
                    return;
                }
                if (check() == 1)
                    break;
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
                if(canbeclick==false){
                    return;
                }
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>1000){
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
                EditText text1x = findViewById(R.id.PCIP);
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>7000){
                    lastClickTime = currentTime;
                }else{
                    return;
                }
                mfind.setcontext(MainActivity.this);
                mfind.Udpreceive();
                mfind.startsearch();

                progressDialog = ProgressDialog.show(this, "", "搜索中");
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                        String[] array=mfind.getiparray();
                        String[] namearray=mfind.getNamearray();
                        mfind.stopsearch();
                        Looper.prepare();
                        showSingleAlertDialog(view,array,namearray,text1x);
                        Looper.loop();


                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 5000);//3秒后执行TimeTask的run方法

//                udpjieshou(text1x);
                break;
            case R.id.debug:
                Intent intent = new Intent();
                //setClass函数的第一个参数是一个Context对象
                //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                intent.setClass(MainActivity.this, DebugActivity.class);
                startActivity(intent);
                break;
            case R.id.screenon:
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime2>1000){
                    lastClickTime2 = currentTime;
                }else{
                    return;
                }
                buttoncounter++;
                View Linout=findViewById(R.id.volumepart);
                ImageButton printbtn=findViewById(R.id.print);
                ImageButton lbtitle= findViewById(R.id.screenon);
                ImageButton btn_wallpaper= findViewById(R.id.wallpaper);
                ImageView imgx= findViewById(R.id.imageView2);
                View decorView = getWindow().getDecorView();
                WindowManager.LayoutParams windowLP = getWindow().getAttributes();
                Paint paint = new Paint();
                ImageButton btn_share= findViewById(R.id.share);
                ImageButton btn_deleteph= findViewById(R.id.deleteph);
                ImageButton btn_passwordstatue= findViewById(R.id.passwordstatue);
                ColorMatrix cm = new ColorMatrix();
                EditText text1 = findViewById(R.id.PCIP);
                EditText text2 = findViewById(R.id.password);
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
                    findViewById(R.id.taskman).setAlpha(0.5f);
                    findViewById(R.id.light).setAlpha(0.5f);
                    imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledsave));
                    printbtn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledprint));
                    this.getWindow().setBackgroundDrawable(getDrawable(android.R.color.black));
                    btn_share.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledshare));
                    btn_deleteph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.deletepholed));
                    btn_wallpaper.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledwallpaper));
                    heartback.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledheart));
                    editbutton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.olededit));
                    Linout.setBackgroundColor(Color.TRANSPARENT);
                    Linout.setAlpha(0.6F);
                    isoled=1;
                    ConstraintSet set = new ConstraintSet();
                    ConstraintLayout mlayout = findViewById(R.id.mainview);
                    set.clone(mlayout);

                    set.connect(R.id.imageviewarea, ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 192, getResources()
                                    .getDisplayMetrics()));
                    TransitionManager.beginDelayedTransition(mlayout);


                    set.applyTo(mlayout);



                    btn_passwordstatue.setVisibility(View.INVISIBLE);
                    findViewById(R.id.timeTextx).setVisibility(View.VISIBLE);
                    mtoolbar.setTitleTextColor(Color.TRANSPARENT);
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
                    Linout.setBackgroundColor(Color.argb(50, 102, 102, 102));
                    Linout.setAlpha(1F);
                findViewById(R.id.taskman).setAlpha(1);
                findViewById(R.id.light).setAlpha(1);
                    imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.backgrd));
                    printbtn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.print));
                    btn_deleteph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.deleteph));
                    btn_share.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
                    btn_wallpaper.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.wallpapaer));
                heartback.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.heartrate));
                editbutton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.photoedit));
                if(this.getApplicationContext().getResources().getConfiguration().uiMode == 0x21){
                        this.getWindow().setBackgroundDrawable(getDrawable(R.color.luse));
                 }else{this.getWindow().setBackgroundDrawable(getDrawable(R.color.luse));}
                findViewById(R.id.timeTextx).setVisibility(View.INVISIBLE);

                isoled=0;
                if(yincangbiaozhi==true){ConstraintSet set = new ConstraintSet();
                       ConstraintLayout mlayout = findViewById(R.id.mainview);
                       set.clone(mlayout);
                       set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM,300);
                       TransitionManager.beginDelayedTransition(mlayout);
                       set.applyTo(mlayout);}

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mtoolbar.setTitleTextColor(getColor(R.color.colortext));
                }else{
                    mtoolbar.setTitleTextColor(00000000);
                }
                timerx.cancel();
                    ConstraintLayout mview= findViewById(R.id.mainview);
                    mview.scrollTo(0,0);
                    btn_passwordstatue.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.photoedit:
                if(Uritoshare!=null){
                    Intent editIntent = new Intent(Intent.ACTION_EDIT);
                    editIntent.setDataAndType(Uritoshare, "image/*");
                    editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(editIntent, null));
                }
                else{Toast.makeText(this,"先截图,谢谢", Toast.LENGTH_SHORT).show();}
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

                ConstraintLayout mview= findViewById(R.id.mainview);
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
            case R.id.volumeup:
                volumecontrol("volumeup");
                break;
            case R.id.volumedown:
                volumecontrol("volumedown");
                break;
            case R.id.mute:
                if(ismute==0){ ImageButton mute=findViewById(R.id.mute);
                mute.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.unmute));
                ismute=1;
                }
                else if(ismute==1){
                    ImageButton mute=findViewById(R.id.mute);
                    mute.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mute));
                    ismute=0;
                }
                volumecontrol("mute");
                break;
            case R.id.nextsound:
                volumecontrol("nextmusic");
                break;
            case R.id.presound:
                volumecontrol("premusic");
                break;
            case R.id.pause:
                if(ispause==0){ ImageButton mute=findViewById(R.id.pause);
                    mute.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.play));
                    volumecontrol("pause");
                    ispause=1;
                }
                else if(ispause==1){
                    ImageButton mute=findViewById(R.id.pause);
                    volumecontrol("pause");
                    mute.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pause));
                    ispause=0;
                }

                break;
            case R.id.print:
                if(Uritoshare!=null){
                    try {
                        doPhotoPrint(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uritoshare));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{Toast.makeText(this,"先截图,谢谢", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.your_dialog_button:
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                SeekBar yourDialogSeekBar =Lightlayout.findViewById(R.id.your_dialog_seekbar);
                volumecontrol("亮度设置为|"+String.valueOf(yourDialogSeekBar.getProgress())+"|");
                System.out.println(String.valueOf(yourDialogSeekBar.getProgress())+"亮度为");
                break;
            default:
                break;

        }
    }
    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("SCREENSHOTPRINT", bitmap);
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

        AlertDialog dialog = normalMoreButtonDialog.create();
        dialog.show();
    }//按下删除后弹对话框
    private void deletephotowith(View view){
        deleteUri(this,Uritoshare);

        ImageView imageViewx= findViewById(R.id.imageView3);
        imageViewx.setImageResource(android.R.color.transparent);
        ImageView imageView = findViewById(R.id.imageView);
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
            try{
            context.getContentResolver().delete(uri, null, null);}
            catch(Exception e)
            {
                Toast.makeText(this,"图片未知",Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(this,"删除成功", Toast.LENGTH_SHORT).show();
        } else {
            File file = sharefile;
            if (file.exists()&& file.isFile()){
                file.delete();
            }
            Toast.makeText(this,"删除成功", Toast.LENGTH_SHORT).show();
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

    public void showSingleAlertDialog(View view,String[] array,String[] namearray,EditText text){
        String[] items = array;
        Toolbar mtoolbar=findViewById(R.id.my_toolbar);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        alertBuilder.setTitle("选择计算机,记得改密码框");
        alertBuilder.setSingleChoiceItems(namearray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putString("profile",namearray[i]);
                editor.commit();//提交修改
                mtoolbar.post(new Runnable() {
                    public void run() {
                        mtoolbar.setTitle(namearray[i]);
                    }
                });

                text.post(new Runnable() {
                            public void run() {
                                text.setText(items[i], TextView.BufferType.NORMAL);
                            }
                        });
                Toast.makeText(MainActivity.this, items[i], Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signlechoose.dismiss();
            }
        });

        signlechoose = alertBuilder.create();
        signlechoose.show();
    }





    //oled防烧代码
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        public void handleMessage(Message message){
            Random r = new Random();
            ConstraintLayout mview= findViewById(R.id.mainview);
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
        ImageButton mimagebutton=findViewById(R.id.Fullscreen);
        ImageButton mimagebutton2=findViewById(R.id.Topcap);
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
            case R.id.photoedit:
            case R.id.volumedown:
            case R.id.volumeup:
            case R.id.mute:
            case R.id.nextsound:
            case R.id.presound:
            case R.id.pause:
            case R.id.print:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);
                    findViewById(v.getId()).animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    //findViewById(v.getId()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    findViewById(v.getId()).animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).start();
                }
                break;
            case R.id.passwordstatue:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    EditText et_password = findViewById(R.id.password);
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    EditText et_password = findViewById(R.id.password);
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
//                while (true) {
//                    try {
//                        String a = new String("abc");
//                        DatagramSocket dgSocket = null;
//                        int port = 9832;
//                        dgSocket = new DatagramSocket(null);
//                        dgSocket.setReuseAddress(true);
//                        dgSocket.bind(new InetSocketAddress(port));
//                        byte[] by = new byte[1024];
//                        DatagramPacket packet = new DatagramPacket(by, by.length);
//                        dgSocket.receive(packet);
//                        String str = new String(packet.getData(), 0, packet.getLength());
//                        text.post(new Runnable() {
//                            public void run() {
//                                text.setText(str, TextView.BufferType.NORMAL);
//                            }
//                        });
//                        Looper.prepare();
//                            Toast.makeText(MainActivity.this, "搜索到"+str, Toast.LENGTH_SHORT).show();
//                            Looper.loop();
//
//
//                    } catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
            }
        }.start();

    }

    //链接测试
    public void connects() {

        EditText text1 = findViewById(R.id.PCIP);
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
            byte[] data = sendData.getBytes();
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

        EditText text1 = findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl为文本框内容

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }


        progressDialog = ProgressDialog.show(this, "", "申请中");
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
            byte[] data = sendData.getBytes();
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

        EditText text1 = findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl为文本框内容

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //②

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }

        progressDialog = ProgressDialog.show(this, "", "申请中");
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
            byte[] data = sendData.getBytes();
            //这里的8888是接收方的端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //③
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }
        text1=null;
    }


    public void volumecontrol(String choose)
    {
        EditText text2 = findViewById(R.id.password);
        String passwordnn = text2.getText().toString();//murl为文本框内容
        EditText text1 = findViewById(R.id.PCIP);
        String ip = text1.getText().toString();//murl为文本框内容
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
        }

    }


    //ip检测
    public int check() {
        EditText text1 = findViewById(R.id.PCIP);
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
            Toast.makeText(MainActivity.this, "IP格式输入错误", Toast.LENGTH_LONG).show();
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
                        String a = "abc";
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
        EditText text1 = findViewById(R.id.PCIP);
        String ipnn = text1.getText().toString();//murl为文本框内容
        EditText text2 = findViewById(R.id.password);
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
        EditText text1 = findViewById(R.id.PCIP);
        text1.setText(ipmm, TextView.BufferType.NORMAL);

        EditText text2 = findViewById(R.id.password);
        text2.setText(password, TextView.BufferType.NORMAL);

    }

    //用aira下载ftp到缓存中
    public void savephotobyaria() {
        canbeclick=false;
        EditText textBOX = findViewById(R.id.password);
        String password = textBOX.getText().toString();//murl为文本框
        EditText textBOXip = findViewById(R.id.PCIP);
        String ipad = textBOXip.getText().toString();//murl为文本框
        //以上为获取两个文本框
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("9=n@Yb(thyZ5", password);
        String murl = "ftp://" + ipad + ":23235/tempcap.bmp";//debug阶段，以后可以加上混乱端口

        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.bmp";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String Path = getExternalCacheDir() + "/"+mImageFileName;
        System.out.println("新路径"+Path);
        TASKNAME=mImageFileName;
        long taskId = Aria.download(this)
                .loadFtp(murl) // 下载地址
                .option(ftpOption)
                .setFilePath(Path) // 设置文件保存路径
                .create();
        ProgressBar mbar=findViewById(R.id.progressBar);
        mbar.setVisibility(View.VISIBLE);
        TextView speed=findViewById(R.id.Speed);
        speed.setVisibility(View.VISIBLE);
        speed.setText("下载速度");
        URIx = Path;//把局部路径转换为全局路径，即下载完到缓存文件夹的图片，这样回调中才能使用
    }

    //安卓10缓存路径转mediastore真正进图库，其实是拷贝私有沙盒路径到公共相册过程
    public static void putBitmapToMedia(Context context, String Path) {
        System.out.println("执行了进缓存");
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
        System.out.println("主活动1"+task.getTaskName());
        System.out.println("主活动"+TASKNAME);
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(URIx)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(30)
                        ))
                .into(imageView);


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
        canbeclick=true;
        ProgressBar mbar=findViewById(R.id.progressBar);
        mbar.setVisibility(View.INVISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.Speed);
        speed.setVisibility(View.INVISIBLE);
    }
    //下载失败toast
    @Download.onTaskFail void taskFail(DownloadTask task, Exception e) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        canbeclick=true;
        Toast.makeText(this,"下载失败，可能是密码错误或未连接上",Toast.LENGTH_SHORT).show();
        ProgressBar mbar=findViewById(R.id.progressBar);
        mbar.setVisibility(View.INVISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.Speed);
        speed.setVisibility(View.INVISIBLE);
        System.out.println("错误信息");
        System.out.println(ALog.getExceptionString(e));
    }
//文字颜色
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        ProgressBar mbar=findViewById(R.id.progressBar);
            mbar.setProgress(task.getPercent()); // 获取百分比进
        TextView speed=findViewById(R.id.Speed);
        speed.setText(task.getConvertSpeed()+"  |  "+task.getPercent()+"%");
    }

    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
                if(p != null){
                    int titleColor = p.getVibrantColor( -16744224);
                    System.out.println(titleColor);
                    String alphaset="FF"+String.format("%08x",titleColor).substring(2);//33为透明度十六进制
                    String alphaset1="50"+String.format("%08x",titleColor).substring(2);//33为透明度十六进制
                    System.out.println(String.format("%08x",titleColor).substring(2));
                    TextView mtime=findViewById(R.id.timeTextx);
                    mtime.setTextColor((int) Long.parseLong(alphaset, 16));
//                    ImageButton mbutton=findViewById(R.id.Fullscreen);
//                    ImageButton mbutton2=findViewById(R.id.Topcap);
//
//                    if(isoled==1){mbutton.setColorFilter((int) Long.parseLong(alphaset1, 16),PorterDuff.Mode.SRC_ATOP);
//                    mbutton2.setColorFilter((int) Long.parseLong(alphaset1, 16),PorterDuff.Mode.SRC_ATOP);}
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
            ConstraintLayout mlayout = findViewById(R.id.mainview);
            set.clone(mlayout);
            set.setVisibility(R.id.inputarea,ConstraintSet.INVISIBLE);
            set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM,300);
            TransitionManager.beginDelayedTransition(mlayout);
            set.applyTo(mlayout);
            yincangbiaozhi=true;

        }else {
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout mlayout = findViewById(R.id.mainview);
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
        else if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK||keyCode ==KeyEvent.KEYCODE_ENTER||keyCode ==KeyEvent.KEYCODE_SPACE) {
            if (check() == 1)
                return false;
            long currentTime = System.currentTimeMillis();
            if (currentTime-lastClickTime1>2000){
                lastClickTime1 = currentTime;
            }else{
                return false;
            }
            shenqingtupian();
            Vibrator mVivrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {200, 100};
            mVivrator.vibrate(pattern,-1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    savephotobyaria();
                }
            }, 1000);

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


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
import android.graphics.Paint;
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
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.print.PrintHelper;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private int yourheartrate = 120;
    private static int buttoncounter = 1;
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
    long lasttime = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String PREFS_NAME = "user";
    private boolean zhendong;
    private String Mac;
    Timer timerx = new Timer();
    public static Uri Uritoshare = null;
    private File sharefile = null;
    private boolean historyipstatue;
    private boolean yincanginput;
    private boolean yincangbiaozhi;
    private int isoled = 0;
    Bluetoothheart mblue;
    public boolean canbeclick = true;
    private int ismute = 0;
    private int ispause = 0;
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


        startup();//????????????????????????
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int option;
        option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(option);
        window.setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);//??????????????????????????????
        TextView mtext = findViewById(R.id.heartrate);

        //????????????
        CacheUtil mcache = new CacheUtil();
        mcache.clearAllCache(this);


        //?????????????????????
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.lightseekbar, (ViewGroup) findViewById(R.id.elementseek));
        Button yourDialogButton = (Button) layout.findViewById(R.id.your_dialog_button);
        SeekBar yourDialogSeekBar = layout.findViewById(R.id.your_dialog_seekbar);
        TextView lighttext = layout.findViewById(R.id.lighttext);
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
                lighttext.setText("???????????????:" + String.valueOf(progress));
            }
        };
        yourDialogButton.setOnClickListener(this);
        yourDialogSeekBar.setOnSeekBarChangeListener(yourSeekBarListener);
        Lightlayout = layout;

        //toolbar?????????
        Toolbar mtoolbar = findViewById(R.id.my_toolbar);
        mtoolbar.setTitle(R.string.app_name);
        mtoolbar.setSubtitle("?????????????????????");
        mtoolbar.inflateMenu(R.menu.mymenu);
        mtoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();//??????Editor
                if (isoled == 1) {
                    Toast.makeText(MainActivity.this, "OLED???????????????", Toast.LENGTH_LONG).show();
                    return;
                }
                if (yincanginput == true) {
                    inputstatue(false);
                    yincanginput = false;

                    editor.putBoolean("yincanginput", false);

                } else {//?????????????????????input??????false??????????????????????????????????????????????????????
                    inputstatue(true);
                    yincanginput = true;

                    editor.putBoolean("yincanginput", true);
                }
                editor.commit();
            }
        });

        mtoolbar.setOnMenuItemClickListener(menuItem -> {
            String msg = "";
            SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();//??????Editor
            FindOutDevices mfind = new FindOutDevices();
            long currentTime;
            switch (menuItem.getItemId()) {
                case R.id.menu_login:
                    Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();
                    break;
                case R.id.menu_file:
                    if (Uritoshare != null) {
                        Intent intenta = new Intent(Intent.ACTION_ATTACH_DATA);
                        intenta.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intenta.putExtra("mimeType", "image/*");
                        if (Uritoshare.toString().startsWith("content://")) {
                            intenta.setData(Uritoshare);
                        } else {
                            try {
                                intenta.setData(getImageContentUri(this, sharefile));
                            } catch (Exception e) {
                            }
                        }
                        if (intenta.resolveActivity(getPackageManager()) != null) {
                            startActivity(intenta);
                        } else {
                            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                        }
//                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
//                    try {
//                        wallpaperManager.setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uritoshare));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    } else {
                        Toast.makeText(this, "?????????,??????", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.menu_pc:
                    Uri urix = Uri.parse("https://github.com/PlayXboxtion963/ScreenshotPC2");
                    Intent intentx = new Intent(Intent.ACTION_VIEW, urix);
                    startActivity(intentx);
                    break;
                case R.id.zhendong:

                    //??????Editor?????????????????????????????????
                    if (zhendong == false) {
                        zhendong = true;
                        Toast.makeText(MainActivity.this, "???", Toast.LENGTH_LONG).show();
                        editor.putBoolean("zhendong", true);
                        editor.commit();
                    }//????????????
                    else if (zhendong = true) {
                        zhendong = false;
                        Toast.makeText(MainActivity.this, "???", Toast.LENGTH_LONG).show();
                        editor.putBoolean("zhendong", false);
                        editor.commit();//????????????
                    }
                    break;
                case R.id.autupatch:
                    if (historyipstatue == false) {
                        historyipstatue = true;
                        Toast.makeText(MainActivity.this, "???", Toast.LENGTH_LONG).show();
                        editor.putBoolean("historyipstatue", true);
                        editor.commit();
                    }//????????????
                    else if (historyipstatue = true) {
                        historyipstatue = false;
                        Toast.makeText(MainActivity.this, "???", Toast.LENGTH_LONG).show();
                        editor.putBoolean("historyipstatue", false);
                        editor.commit();//????????????
                    }
                    break;
                case R.id.miband:

                    ImageView mimageview = findViewById(R.id.imageView4);


                    final EditText inputServer = new EditText(MainActivity.this);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("??????MAC??????,????????????-??????").setIcon(R.drawable.newlogo).setView(inputServer).setMessage("???????????????????????????????????????????????????????????????????????????????????????,??????10???????????????????????????????????????")
                            .setNegativeButton("??????", null);
                    inputServer.setText(userInfo.getString("MAC", "FB:35:A2:DE:F5:49"));
                    builder1.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Mac = inputServer.getText().toString();
                            editor.putString("MAC", Mac);
                            editor.commit();
                            mblue.setContext(MainActivity.this);
                            mblue.setMac(Mac);
                            mimageview.setVisibility(View.VISIBLE);
                            mblue.setTextview(mtext);
                            mblue.startble();

                        }
                    });

                    final NumberPicker inputserver3 = new NumberPicker(MainActivity.this);
                    inputserver3.setMaxValue(200);
                    inputserver3.setMinValue(20);
                    inputserver3.setValue(130);
                    inputserver3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        //???NunberPicker?????????????????????????????????????????????
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
                        }
                    });
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setTitle("??????????????????").setIcon(R.drawable.newlogo).setView(inputserver3)
                            .setNegativeButton("??????", null);
                    builder2.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            yourheartrate = inputserver3.getValue();
                            Toast.makeText(MainActivity.this, "????????????????????????" + String.valueOf(yourheartrate), Toast.LENGTH_SHORT).show();
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
                case R.id.SearchPc:
                    getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                    EditText text1x = findViewById(R.id.PCIP);
                    currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > 2000) {
                        lastClickTime = currentTime;
                    } else {
                        break;
                    }
                    mfind.setcontext(MainActivity.this);
                    mfind.Udpreceive();
                    mfind.startsearch();

                    progressDialog = ProgressDialog.show(this, "", "?????????");
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            String[] array = mfind.getiparray();
                            String[] namearray = mfind.getNamearray();
                            mfind.stopsearch();
                            Looper.prepare();
                            showSingleAlertDialog(getWindow().getDecorView(), array, namearray, text1x);
                            Looper.loop();


                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1900);//3????????????TimeTask???run??????
                    break;
                default:
                    break;
            }
            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        //????????????
        EditText et_password = findViewById(R.id.password);
        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        if (userInfo.contains("MAC") == false) {
            editor.putString("MAC", "FB:35:A2:DE:F5:49");
            editor.commit();
        }
        Mac = userInfo.getString("MAC", "FB:35:A2:DE:F5:49");

        if (userInfo.contains("zhendong") == false) {
            editor.putBoolean("zhendong", true);
            editor.commit();
        }//?????????????????????????????????????????????true
        zhendong = userInfo.getBoolean("zhendong", true);
////////////////////////////

        if (userInfo.contains("historyipstatue") == false) {
            editor.putBoolean("zhendong", true);
            editor.commit();
        }//???????????????????????????????????????true
        historyipstatue = userInfo.getBoolean("historyipstatue", true);
        if (historyipstatue == true) {
            gethisrotyip();
        }
        //////////////////////////////
        if (userInfo.contains("yincanginput") == false) {
            editor.putBoolean("yincanginput", true);
            editor.commit();
        }
        yincanginput = userInfo.getBoolean("yincanginput", true);//true????????????
        if (yincanginput == true) {
            inputstatue(true);
            yincanginput = true;
            editor.putBoolean("yincanginput", true);
            editor.commit();
        } else {
            inputstatue(false);
            yincanginput = false;
            editor.putBoolean("yincanginput", false);
            editor.commit();
        }
        if (userInfo.contains("userip")) {
            mtoolbar.setSubtitle(userInfo.getString("userip", "?????????????????????"));
            mtoolbar.setTitle(userInfo.getString("profile", "????????????"));
        }

    }

    public void startup() {
        btn = findViewById(R.id.Fullscreen);
        btn_connect = findViewById(R.id.storebtn);
        btn_shotwindow = findViewById(R.id.Topcap);
        btn_gif = findViewById(R.id.search);
        btn_history = findViewById(R.id.history);
        Button btn_debug = findViewById(R.id.debug);
        ImageButton btn_screenon = findViewById(R.id.screenon);
        ImageButton btn_passwordstatue = findViewById(R.id.passwordstatue);
        ImageButton btn_share = findViewById(R.id.share);
        ImageButton btn_deleteph = findViewById(R.id.deleteph);
        ImageButton btn_wallpaper = findViewById(R.id.wallpaper);
        ImageButton volumeupbtn = findViewById(R.id.volumeup);
        ImageButton btn_recording = findViewById(R.id.recoring10s);
        btn_recording.setOnClickListener(this);
        btn_recording.setOnTouchListener(this);
        volumeupbtn.setOnClickListener(this);
        ImageButton volumedownbtn = findViewById(R.id.volumedown);
        volumedownbtn.setOnTouchListener(this);
        volumeupbtn.setOnTouchListener(this);
        ImageButton presound = findViewById(R.id.presound);
        ImageButton nextsound = findViewById(R.id.nextsound);
        ImageButton pause = findViewById(R.id.pause);
        presound.setOnTouchListener(this);
        presound.setOnClickListener(this);
        nextsound.setOnTouchListener(this);
        nextsound.setOnClickListener(this);
        pause.setOnTouchListener(this);
        pause.setOnClickListener(this);
        ImageButton mutebtn = findViewById(R.id.mute);
        mutebtn.setOnClickListener(this);
        mutebtn.setOnTouchListener(this);
        volumedownbtn.setOnClickListener(this);
        PhotoView photoView = findViewById(R.id.img);
        photoView.setVisibility(View.INVISIBLE);
        ImageView ImageViewxxx = findViewById(R.id.imgbackgrdx);
        ImageViewxxx.setVisibility(View.INVISIBLE);
        ImageViewxxx.setOnClickListener(this);
        photoView.setOnClickListener(this);
        ImageView suolue = findViewById(R.id.imageView);
        ImageButton editbutton = findViewById(R.id.photoedit);
        editbutton.setOnClickListener(this);
        editbutton.setOnTouchListener(this);
        ImageButton printbtn = findViewById(R.id.print);
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

        mblue = new Bluetoothheart();
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
                float temp = (float) Integer.valueOf(mblue.getheartrate()) / yourheartrate;
                temp = temp * 100;
                System.out.println(temp);
                ProgressBar mprocess = findViewById(R.id.heartrateprocess);
                mprocess.setVisibility(View.VISIBLE);
                mprocess.setProgress((int) temp);
                if (Integer.valueOf(mblue.getheartrate()) >= yourheartrate) {
                    if (check() == 1)
                        return;
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime1 > 10000) {
                        lastClickTime1 = currentTime;
                    } else {
                        return;
                    }
                    shenqingtupian();
                    Vibrator mVivrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long[] pattern = {200, 100};
                    mVivrator.vibrate(pattern, -1);

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

    }//???????????????????????????








    //??????path???????????????????????????????????????????????????
    public void openPathPhoto(String path) {
        File file = new File(path);
        if (file.exists() && file.listFiles().length > 0) {
            //  String path= getSDPath() + "/Pictures/PDF??????";
            new PictureScanner(this, path);
        } else {
            Toast.makeText(this,"??????????????????",Toast.LENGTH_SHORT).show();
        }

    }



    //??????
    public void share(Uri uri) {

        //?????????????????????
        new Share2.Builder(this)
                // ???????????????????????????
                .setContentType(ShareContentType.IMAGE)
                // ???????????????????????? Uri
                .setShareFileUri(uri)
                // ??????????????????????????????
                .setTitle("Share")
                .build()
                // ????????????
                .shareBySystem();
    }



    //????????????

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


            case R.id.Fullscreen://????????????
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
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        savephotobyaria();
                    }
                }, 1000);*/
                AfterRequest();
                break;

            case R.id.storebtn://????????????
                if (check() == 1)
                    break;
                historyipstore();
                break;


            case R.id.Topcap://????????????
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
                AfterRequest();
                break;
            case R.id.history:
                gethisrotyip();
                break;
            case R.id.search:
                EditText text1x = findViewById(R.id.PCIP);
                currentTime = System.currentTimeMillis();
                if (currentTime-lastClickTime>1800){
                    lastClickTime = currentTime;
                }else{
                    return;
                }
                mfind.setcontext(MainActivity.this);
                mfind.Udpreceive();
                mfind.startsearch();

                progressDialog = ProgressDialog.show(this, "", "?????????");
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
                timer.schedule(task, 1500);//3????????????TimeTask???run??????

//                udpjieshou(text1x);
                break;
            case R.id.debug:
                Intent intent = new Intent();
                //setClass?????????????????????????????????Context??????
                //Context???????????????Activity???Context???????????????????????????????????????Activity?????????????????????????????????Context??????
                //setClass?????????????????????????????????Class????????????????????????????????????????????????????????????Activity??????class??????
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
                View buttonquyu=findViewById(R.id.inputlla);
                View inputare=findViewById(R.id.inputarea);
                ImageButton printbtn=findViewById(R.id.print);
                ImageButton lbtitle= findViewById(R.id.screenon);
                ImageButton btn_wallpaper= findViewById(R.id.wallpaper);
                //ImageView imgx= findViewById(R.id.imageView2);
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
                    Toast.makeText(this,"?????????????????????(OLED??????)", Toast.LENGTH_SHORT).show();
                    lbtitle.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledon));
                    //btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oled));
                    btn_history.setVisibility(View.INVISIBLE);
                    //btn_shotwindow.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oled));
                    btn_gif.setVisibility(View.INVISIBLE);
                    btn_connect.setVisibility(View.INVISIBLE);
                    findViewById(R.id.SearchPc).setAlpha(0.5f);
                    findViewById(R.id.light).setAlpha(0.5f);
                    //inputare.setVisibility(View.INVISIBLE);
                    //imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledsave));
                    //imgx.setVisibility(View.INVISIBLE);
                    printbtn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledprint));
                    this.getWindow().setBackgroundDrawable(getDrawable(android.R.color.black));
                    btn_share.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledshare));
                    btn_deleteph.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.deletepholed));
                    btn_wallpaper.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledwallpaper));
                    heartback.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.oledheart));
                    editbutton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.olededit));
                    buttonquyu.setBackgroundColor(Color.TRANSPARENT);
                    Linout.setBackgroundColor(Color.TRANSPARENT);
                    Linout.setAlpha(0.6F);
                    inputare.setBackgroundColor(Color.TRANSPARENT);
                    isoled=1;
                    ConstraintSet set = new ConstraintSet();
                    ConstraintLayout mlayout = findViewById(R.id.mainview);
                    set.clone(mlayout);

                    set.connect(R.id.imageviewarea, ConstraintSet.TOP, R.id.inputarea, ConstraintSet.BOTTOM);
                    TransitionManager.beginDelayedTransition(mlayout);


                    set.applyTo(mlayout);



                    btn_passwordstatue.setVisibility(View.INVISIBLE);
                    findViewById(R.id.timeTextx).setVisibility(View.VISIBLE);
                    mtoolbar.setTitleTextColor(Color.TRANSPARENT);
                    mtoolbar.setSubtitleTextColor(Color.TRANSPARENT);
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
                    Toast.makeText(this,"?????????????????????", Toast.LENGTH_SHORT).show();
                    lbtitle.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.offf));
                    //btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dayquanpin));
                    btn_history.setVisibility(View.VISIBLE);
                    //btn_shotwindow.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.daydingbu));
                    btn_gif.setVisibility(View.VISIBLE);
                    btn_connect.setVisibility(View.VISIBLE);
                    Linout.setBackgroundColor(Color.argb(50, 102, 102, 102));
                buttonquyu.setBackgroundColor(Color.argb(50, 102, 102, 102));
                    Linout.setAlpha(1F);
                findViewById(R.id.SearchPc).setAlpha(1);
                findViewById(R.id.light).setAlpha(1);
                inputare.setBackgroundColor(Color.argb(50, 102, 102, 102));
                //imgx.setVisibility(View.VISIBLE);
                    //imgx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inputshape));
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
                       set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM);
                       TransitionManager.beginDelayedTransition(mlayout);
                       set.applyTo(mlayout);}

                    mtoolbar.setTitleTextColor(getColor(R.color.colortext));
                    mtoolbar.setSubtitleTextColor(getColor(R.color.colortext));

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
                else{Toast.makeText(this,"?????????,??????", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.share:
                if(Uritoshare!=null){
                    share(Uritoshare);}
                else{Toast.makeText(this,"?????????,??????", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.deleteph:
                if(Uritoshare!=null){
                    showNormalMoreButtonDialog(view);
                }else{Toast.makeText(this,"?????????,??????", Toast.LENGTH_SHORT).show();}
                break;
            case R.id.wallpaper:
                openPathPhoto("/storage/emulated/0/Pictures/PC");
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
                // ????????????ImageView?????????Info
                Info info = PhotoView.getImageViewInfo(ImageViewxx);
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????demo?????????
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
                        //??????????????????
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
                volumecontrol("mute");
                break;
            case R.id.nextsound:
                volumecontrol("nextmusic");
                break;
            case R.id.presound:
                volumecontrol("premusic");
                break;
            case R.id.pause:
                    volumecontrol("pause");

                break;
            case R.id.print:

                Intent mintent=new Intent();
                mintent.setClass(this,PIPcapture.class);
                startActivity(mintent);
                    break;
            case R.id.your_dialog_button:
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                SeekBar yourDialogSeekBar =Lightlayout.findViewById(R.id.your_dialog_seekbar);
                volumecontrol("???????????????|"+String.valueOf(yourDialogSeekBar.getProgress())+"|");
                System.out.println(String.valueOf(yourDialogSeekBar.getProgress())+"?????????");
                break;
            case R.id.recoring10s:
                //10s
                break;
            default:
                break;

        }
    }

    public void AfterRequest() {
        final Boolean[] jieshoudao = {true};
        final Thread[] mthread = {null};
        final ServerSocket[] socketx1 = {null};
        ProgressDialog mprogressDialog = ProgressDialog.show(this, "", "?????????");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                jieshoudao[0] =false;
                try {
                    socketx1[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("????????????");
                mprogressDialog.dismiss();
                Looper.prepare();

                Toast.makeText(MainActivity.this, "????????????????????????????????????5??????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
                Looper.loop();
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
                    mprogressDialog.dismiss();
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


    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("SCREENSHOTPRINT", bitmap);
    }

    private void showNormalMoreButtonDialog(View view){
        AlertDialog.Builder normalMoreButtonDialog = new AlertDialog.Builder(this);
        normalMoreButtonDialog.setTitle("??????????????????");
        //normalMoreButtonDialog.setIcon();
        //normalMoreButtonDialog.setMessage(getString(R.string.dialog_normal_more_button_content));

        //????????????
        normalMoreButtonDialog.setPositiveButton("??????"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletephotowith(view);
                        dialog.dismiss();
                    }
                });

        normalMoreButtonDialog.setNeutralButton("???"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = normalMoreButtonDialog.create();
        dialog.show();
    }//???????????????????????????
    private void deletephotowith(View view){
        deleteUri(this,Uritoshare);

        ImageView imageViewx= findViewById(R.id.imageView3);
        imageViewx.setImageResource(R.drawable.morenbackground);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(R.drawable.inputeditshape)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
                .skipMemoryCache(true)//??????????????????
                .into(imageView);

        view.performHapticFeedback(HapticFeedbackConstants.REJECT);
        Intent intentx = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentx.setData(Uritoshare);
        this.sendBroadcast(intentx);
        Uritoshare=null;
    }//????????????????????????????????????????????????????????????
    public void deleteUri(Context context, Uri uri) {

        if (uri.toString().startsWith("content://")) {
            // content://?????????Uri
            try{
            context.getContentResolver().delete(uri, null, null);}
            catch(Exception e)
            {
                Toast.makeText(this,"????????????",Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(this,"????????????", Toast.LENGTH_SHORT).show();
        } else {
            File file = sharefile;
            if (file.exists()&& file.isFile()){
                file.delete();
            }
            Toast.makeText(this,"????????????", Toast.LENGTH_SHORT).show();
        }
    }//?????????????????????????????????
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
    }//????????????????????????URIcontent

    public void showSingleAlertDialog(View view,String[] array,String[] namearray,EditText text){
        String[] items = array;
        Toolbar mtoolbar=findViewById(R.id.my_toolbar);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        alertBuilder.setTitle("???????????????,??????????????????");
        alertBuilder.setSingleChoiceItems(namearray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putString("profile",namearray[i]);
                editor.commit();//????????????
                mtoolbar.post(new Runnable() {
                    public void run() {
                        mtoolbar.setTitle(namearray[i]);
                        mtoolbar.setSubtitle(items[i]);
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

        alertBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signlechoose.dismiss();
            }
        });

        signlechoose = alertBuilder.create();
        signlechoose.show();
    }





    //oled????????????
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
    boolean isPress;//?????????????????????
    long lastTimeStamp=0;//?????????????????????
    boolean isPress2;//?????????????????????
    long lastTimeStamp2=0;//?????????????????????
    //??????
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
            case R.id.recoring10s:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);
                    findViewById(v.getId()).animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                        isPress=false;
                        isPress2=false;
                    //findViewById(v.getId()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    findViewById(v.getId()).animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).start();
                    if(v.getId()==R.id.volumeup){
                        isPress=true;
                        lastTimeStamp=System.currentTimeMillis();
                        //????????????
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (isPress){
                                    //????????????????????????300??????????????????
                                    if((System.currentTimeMillis()-lastTimeStamp)>500){
                                        volumecontrol("volumeup");
                                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                                        try {
                                            Thread.sleep(100);//??????50??????????????????????????? ???????????????????????????
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                    if(v.getId()==R.id.volumedown){
                        isPress2=true;
                        lastTimeStamp2=System.currentTimeMillis();
                        //????????????
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (isPress2){
                                    //????????????????????????300??????????????????
                                    if((System.currentTimeMillis()-lastTimeStamp2)>500){
                                        volumecontrol("volumedown");
                                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                                        try {
                                            Thread.sleep(100);//??????50??????????????????????????? ???????????????????????????
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
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

    //??????????????????
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
//                            Toast.makeText(MainActivity.this, "?????????"+str, Toast.LENGTH_SHORT).show();
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

    //????????????
    public void connects() {

        EditText text1 = findViewById(R.id.PCIP);
        String ip = text1.getText().toString();//murl??????????????????
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //???
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();
        progressDialog = ProgressDialog.show(this, "", "???????????????");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /**
                 *??????????????????
                 */
                progressDialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);//3????????????TimeTask???run??????
        try {
            String sendData = "plstes";
            byte[] data = sendData.getBytes();
            //?????????8888????????????????????????
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //???
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkudp();
        text1=null;
        ip=null;

    }

    //????????????
    public void shenqingtupian() {

        EditText text1 = findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl??????????????????

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
        text1=null;

    }

    //??????????????????
    public void shenqingtupian2() {

        EditText text1 = findViewById(R.id.PCIP);

        String ip = text1.getText().toString();//murl??????????????????

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //???

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();

        }



        try {
            String sendData = "shotwindows";
            byte[] data = sendData.getBytes();
            //?????????8888????????????????????????
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //???
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();

        }
        text1=null;
    }


    public void volumecontrol(String choose)
    {
        EditText text2 = findViewById(R.id.password);
        String passwordnn = text2.getText().toString();//murl??????????????????
        EditText text1 = findViewById(R.id.PCIP);
        String ip = text1.getText().toString();//murl??????????????????
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);  //???
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            String sendData = choose+passwordnn;
            byte[] data = sendData.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 21211);   //???
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //ip??????
    public int check() {
        EditText text1 = findViewById(R.id.PCIP);
        String string = text1.getText().toString();//murl??????????????????
        /*???????????????*/
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//??????????????????
        Pattern p = Pattern.compile(ip);
        Matcher m = p.matcher(string);
        boolean b = m.matches();
        if (b == false) {
            Toast.makeText(MainActivity.this, "IP??????????????????", Toast.LENGTH_LONG).show();
            return 1;
        }
        return 0;
    }

    //UDP??????
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
                            Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                     }.start();
            }

    //????????????
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //???????????????????????????
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // ???????????????????????????????????????????????????????????????
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //??????IP?????????
    public void historyipstore(){
        EditText text1 = findViewById(R.id.PCIP);
        String ipnn = text1.getText().toString();//murl??????????????????
        EditText text2 = findViewById(R.id.password);
        String passwordnn = text2.getText().toString();//murl??????????????????

        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//??????Editor
        Toolbar mtoolbar=findViewById(R.id.my_toolbar);
        //??????Editor?????????????????????????????????
        editor.putString("userip", ipnn);
        editor.putString("userpassword",passwordnn);
        mtoolbar.setSubtitle(ipnn);
        editor.commit();//????????????
        Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
    }
    private void gethisrotyip(){
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String ipmm = userInfo.getString("userip", null);//??????username
        String password=userInfo.getString("userpassword", null);
        EditText text1 = findViewById(R.id.PCIP);
        text1.setText(ipmm, TextView.BufferType.NORMAL);

        EditText text2 = findViewById(R.id.password);
        text2.setText(password, TextView.BufferType.NORMAL);

    }

    //???aira??????ftp????????????
    public void savephotobyaria() {
        canbeclick=false;
        System.out.println("??????");
        EditText textBOX = findViewById(R.id.password);
        String password = textBOX.getText().toString();//murl????????????
        EditText textBOXip = findViewById(R.id.PCIP);
        String ipad = textBOXip.getText().toString();//murl????????????
        //??????????????????????????????
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("9=n@Yb(thyZ5", password);
        String murl = "ftp://" + ipad + ":23235/tempcap.bmp";//debug???????????????????????????????????????

        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.bmp";//??????????????????"Screenshot"+???????????????
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String Path = getExternalCacheDir() + "/"+mImageFileName;
        System.out.println("?????????"+Path);
        TASKNAME=mImageFileName;
        long taskId = Aria.download(this)
                .loadFtp(murl) // ????????????
                .option(ftpOption)
                .setFilePath(Path) // ????????????????????????
                .create();
        ProgressBar mbar=findViewById(R.id.progressBar);
        mbar.setVisibility(View.VISIBLE);
        TextView speed=findViewById(R.id.Speed);
        speed.setVisibility(View.VISIBLE);
        speed.setText("????????????");
        URIx = Path;//????????????????????????????????????????????????????????????????????????????????????????????????????????????
    }

    //??????10???????????????mediastore????????????????????????????????????????????????????????????????????????
    public static void putBitmapToMedia(Context context, String Path) {
        System.out.println("??????????????????");
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
            context.sendBroadcast(intent);//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????file???
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    //??????9?????????????????????????????????
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
        this.sendBroadcast(intent);//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????file???
        File file = new File(URIx);
        file.delete();
    }


    //???????????????????????????????????????????????????
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        System.out.println("?????????1"+task.getTaskName());
        System.out.println("?????????"+TASKNAME);
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(URIx)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
                .skipMemoryCache(true)//??????????????????
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
        //????????????????????????

        if (Build.VERSION.SDK_INT > 29) {
            putBitmapToMedia(this, URIx);//???????????????BITMAP??????
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
    //????????????toast
    @Download.onTaskFail void taskFail(DownloadTask task, Exception e) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        canbeclick=true;
        Toast.makeText(this,"???????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
        ProgressBar mbar=findViewById(R.id.progressBar);
        mbar.setVisibility(View.INVISIBLE);
        mbar.setProgress(0);
        TextView speed=findViewById(R.id.Speed);
        speed.setVisibility(View.INVISIBLE);
        System.out.println("????????????");
        System.out.println(ALog.getExceptionString(e));
    }
//????????????
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if(task.getTaskName().equals(TASKNAME)==false){
            return;
        }
        ProgressBar mbar=findViewById(R.id.progressBar);
            mbar.setProgress(task.getPercent()); // ??????????????????
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
                    String alphaset="FF"+String.format("%08x",titleColor).substring(2);//33????????????????????????
                    String alphaset1="50"+String.format("%08x",titleColor).substring(2);//33????????????????????????
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
            set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.my_toolbar,ConstraintSet.BOTTOM);
            //set.connect(R.id.imageviewarea,ConstraintSet.BOTTOM,R.id.buttonarea,ConstraintSet.TOP);
            TransitionManager.beginDelayedTransition(mlayout);
            set.applyTo(mlayout);
            yincangbiaozhi=true;

        }else {
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout mlayout = findViewById(R.id.mainview);
            set.clone(mlayout);
            //set.connect(R.id.imageviewarea, ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(
                   // TypedValue.COMPLEX_UNIT_DIP, 192, getResources()
                         //   .getDisplayMetrics()));
            set.connect(R.id.imageviewarea,ConstraintSet.TOP,R.id.inputarea,ConstraintSet.BOTTOM);
            //set.connect(R.id.imageviewarea,ConstraintSet.BOTTOM,R.id.buttonarea,ConstraintSet.TOP);
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
            Toast.makeText(getApplicationContext(), "????????????????????????",
                    Toast.LENGTH_SHORT).show();
            // ??????handler??????????????????????????????
            mHandlerxxx.sendEmptyMessageDelayed(0, 2000);
        } else {

            System.exit(0);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPress=false;
        isPress2=false;
    }
}


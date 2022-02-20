package com.playxboxtion233.screenshotpc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.FtpOption;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.playxboxtion233.screenshotpc.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {
    private static String URIx = null;
    private Button btn;
    private int zhendong=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Aria.download(this).register();
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
        btn.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                break;
            default:
                break;
        }
    }
    public boolean onTouch(View v, MotionEvent event) {

        if(v.getId() == R.id.button){
            if(event.getAction() == MotionEvent.ACTION_UP){
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);

            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN){
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }
        return false;
    }

    //用aira下载ftp到缓存中
    public void savephotobyaria() {
        EditText textBOX = (EditText) findViewById(R.id.passworktext);
        String password = textBOX.getText().toString();//murl为文本框
        EditText textBOXip = (EditText) findViewById(R.id.IPtext);
        String ipad = textBOXip.getText().toString();//murl为文本框
        //以上为获取两个文本框
        FtpOption ftpOption = new FtpOption();
        ftpOption.login("admin", password);
        String murl = "ftp://" + ipad + "/tempcap.png";//debug阶段，以后可以加上混乱端口
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String Path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + mImageFileName;

        long taskId = Aria.download(this)
                .loadFtp(murl) // 下载地址
                .option(ftpOption)
                .setFilePath(Path) // 设置文件保存路径
                .create();

        URIx = Path;//把局部路径转换为全局路径，即下载完到缓存文件夹的图片
    }

    //安卓10缓存路径转mediastore真正进图库
    public static void putBitmapToMedia(Context context, String URI) {
        String fileName;
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "t_%s.png";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        fileName = mImageFileName;
        Bitmap mbitmap;
        mbitmap = BitmapFactory.decodeFile(URI);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                + File.separator + "PC");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            File file = new File(URIx);
            file.delete();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    //安卓9拷贝缓存到文件夹
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
        this.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }


    //下载成功后才开始拷贝缓存和一些任务
    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
        ImageView imageView = (ImageView) findViewById(R.id.yulan);
        Glide.with(this)
                .load(URIx)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(imageView);
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
}
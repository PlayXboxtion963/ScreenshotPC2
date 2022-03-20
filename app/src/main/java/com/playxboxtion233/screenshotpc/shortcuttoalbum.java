package com.playxboxtion233.screenshotpc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class shortcuttoalbum extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveTaskToBack(true);
        openPathPhoto("/storage/emulated/0/Pictures/PC");

    }
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
}

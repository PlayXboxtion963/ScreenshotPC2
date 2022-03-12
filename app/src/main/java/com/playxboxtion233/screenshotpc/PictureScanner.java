package com.playxboxtion233.screenshotpc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

public class PictureScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;
    private Context context;

    File[] allFiles;

    public PictureScanner(Context context, String pictureFolderPath) {
        File folder = new File(pictureFolderPath);
        allFiles = folder.listFiles();
        if (allFiles.length > 0) {
            swap(allFiles);
        } else {
            return;
        }
        this.context = context;
        mFile = allFiles[0];
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    public void onScanCompleted(String path, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        context.startActivity(intent);
        mMs.disconnect();
    }

    private void swap(File a[]) {
        int len = a.length;
        for (int i = 0; i < len / 2; i++) {
            File tmp = a[i];
            a[i] = a[len - 1 - i];
            a[len - 1 - i] = tmp;
        }
    }
}

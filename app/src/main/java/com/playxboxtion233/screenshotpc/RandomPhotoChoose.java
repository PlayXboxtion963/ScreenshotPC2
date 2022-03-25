package com.playxboxtion233.screenshotpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class RandomPhotoChoose extends Activity {
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    final Context context = RandomPhotoChoose.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_random_photo_choose);
        moveTaskToBack(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //判断app widget ID是否为空.(App Widget 是否添加到Launched Activity)
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        File[] allFiles;
        Uri mFile;
        Random r = new Random();
        File file = new File("/storage/emulated/0/Pictures/PC");
        allFiles = file.listFiles();
        if (allFiles.length <= 0) {
            Toast.makeText(this, "无图片", Toast.LENGTH_SHORT).show();
            finish();
        }
        int i1 = r.nextInt(allFiles.length-1);
        mFile=getImageContentUri(context,allFiles[i1]);
        System.out.println(mFile);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.displayphoto);

        grantUriAccessToWidget(context,mFile);
        Bitmap bitmap=null;
        try {
            bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), mFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap=SetRoundCornerBitmap(bitmap,60);
        gettextcolor(bitmap,views);
        //grantUriAccessToWidget(this,uri);
        views.setImageViewBitmap(R.id.imagewidget,bitmap);
        Intent mintent=new Intent(Intent.ACTION_VIEW);
        mintent.setDataAndType(mFile,"image/*");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mintent, 0);
        views.setOnClickPendingIntent(R.id.imagewidget,pendingIntent);
        //views.setImageViewUri(R.id.imagewidget,Uri.parse(""));
        //views.setImageViewUri(R.id.imagewidget,uri);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
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

    }
    public static Bitmap SetRoundCornerBitmap(Bitmap bitmap, float roundPx) {
        int width = bitmap.getWidth();
        int heigh = bitmap.getHeight();
        if(width*heigh*4>15143200)
        {
            float widthx=width;
            while (widthx>1920){
                widthx=widthx/2;
            }
            float higthx=heigh;
            while (higthx>1080){
                higthx=higthx/2;
            }
            System.out.println("图片过大");
            float scaleWidth = ((float) widthx) / width;
            float scaleHeight = ((float)higthx ) / heigh;
            // 取得想要缩放的matrix参数.
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片.
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,heigh, matrix, true);
            width=(int) widthx;
            heigh=(int) higthx;
        }
        // 创建输出bitmap对象
        Bitmap outmap = Bitmap.createBitmap(width, heigh,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, heigh);
        final RectF rectf = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectf, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return outmap;
    }


    public void gettextcolor(Bitmap bitmap,RemoteViews views){
        Palette palette = Palette.from(bitmap).generate();
        int titleColor = palette.getLightVibrantColor( 0x7f040000);
        System.out.println(titleColor);
        String alphaset="FF"+String.format("%08x",titleColor).substring(2);//33为透明度十六进制
        System.out.println(String.format("%08x",titleColor).substring(2));
        views.setTextColor(R.id.timeTextx,(int) Long.parseLong(alphaset, 16));
        views.setTextColor(R.id.buwigre,(int) Long.parseLong(alphaset, 16));
        views.setTextColor(R.id.randompicture,(int) Long.parseLong(alphaset, 16));
    }

}
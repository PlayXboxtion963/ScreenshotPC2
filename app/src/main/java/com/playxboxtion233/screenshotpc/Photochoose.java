package com.playxboxtion233.screenshotpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class Photochoose extends AppCompatActivity {
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    final Context context = Photochoose.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_photochoose);
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
        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode==1)
        {
            if (intent!=null)
            {
                Uri uri=intent.getData();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews views = new RemoteViews(context.getPackageName(),
                        R.layout.displayphoto);

                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap=SetRoundCornerBitmap(bitmap,120);

                System.out.println(uri);
                gettextcolor(bitmap,views);
                //grantUriAccessToWidget(this,uri);
                views.setImageViewBitmap(R.id.imagewidget,bitmap);
                //views.setImageViewUri(R.id.imagewidget,Uri.parse(""));
                //views.setImageViewUri(R.id.imagewidget,uri);

                appWidgetManager.updateAppWidget(mAppWidgetId, views);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();

            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
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
    // 6.圆角图片
    public static Bitmap SetRoundCornerBitmap(Bitmap bitmap, float roundPx) {
        int width = bitmap.getWidth();
        int heigh = bitmap.getHeight();
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
        int titleColor = palette.getLightMutedColor( 0x7f040000);
        System.out.println(titleColor);
        String alphaset="FF"+String.format("%08x",titleColor).substring(2);//33为透明度十六进制
        System.out.println(String.format("%08x",titleColor).substring(2));
        views.setTextColor(R.id.timeTextx,(int) Long.parseLong(alphaset, 16));
    }
}
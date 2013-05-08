package com.nostra13.example.universalimageloader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class Utils {
    public static Bitmap getReflectedImage(Bitmap bitmap) {
        final int reflectionGap = 0;
        Bitmap originalImage = bitmap;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 7), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        Paint deafaultPaint = new Paint();

        canvas.drawBitmap(originalImage, 0, 0, null);
        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();

        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x40ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        if (originalImage != bitmapWithReflection) {
            originalImage.recycle();
        }
        return bitmapWithReflection;
    }
}

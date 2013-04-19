package com.nostra13.example.universalimageloader;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.support.mdroid.widget.CoverFlow;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.example.universalimageloader.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class ImageCoverFlowActivity extends BaseActivity {
    String[] imageUrls;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        imageUrls = bundle.getStringArray(Extra.IMAGES);
        setContentView(R.layout.ac_image_cover_flow);
        CoverFlow coverFlow = (CoverFlow) findViewById(R.id.flow);
        coverFlow.setAdapter(new FlowAdapter());
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        return getReflectedImage(bitmap);
                    }
                }, "-reflected")
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    private class FlowAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = (ImageView) convertView;
            if (imageView == null) {
                imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_gallery_image, parent, false);
            }
            imageLoader.displayImage(imageUrls[position], imageView, options);
            return imageView;
        }

    }

    public Bitmap getReflectedImage(Bitmap bitmap) {
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

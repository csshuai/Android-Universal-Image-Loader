package com.nostra13.example.universalimageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.example.universalimageloader.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class ImageScrollViewActivity extends BaseActivity {
    String[] imageUrls;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scroll_view);
        final ImageView image1 = (ImageView) findViewById(R.id.image1);
        final TextView progress1 = (TextView) findViewById(R.id.progress1);
        final ImageView image2 = (ImageView) findViewById(R.id.image2);
        final TextView progress2 = (TextView) findViewById(R.id.progress2);
        final ImageView image3 = (ImageView) findViewById(R.id.image3);
        final TextView progress3 = (TextView) findViewById(R.id.progress3);
        final ImageView image4 = (ImageView) findViewById(R.id.image4);
        final TextView progress4 = (TextView) findViewById(R.id.progress4);
        final ImageView image5 = (ImageView) findViewById(R.id.image5);
        final TextView progress5 = (TextView) findViewById(R.id.progress5);
        
        Bundle bundle = getIntent().getExtras();
        imageUrls = bundle.getStringArray(Extra.IMAGES);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        return Utils.getReflectedImage(bitmap);
                    }
                }, "-reflected")
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        
        imageLoader.displayImage(imageUrls[1], image1, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingProgress(String imageUri, View view, float progress) {
                progress1.setText(progress + "");
            }
        });
        imageLoader.displayImage(imageUrls[1], image2, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingProgress(String imageUri, View view, float progress) {
                progress2.setText(progress + "");
            }
        });
        imageLoader.displayImage(imageUrls[1], image3, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingProgress(String imageUri, View view, float progress) {
                progress3.setText(progress + "");
            }
        });
        imageLoader.displayImage(imageUrls[1], image4, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingProgress(String imageUri, View view, float progress) {
                progress4.setText(progress + "");
            }
        });
        imageLoader.displayImage(imageUrls[1], image5, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingProgress(String imageUri, View view, float progress) {
                progress5.setText(progress + "");
            }
        });
    }

}

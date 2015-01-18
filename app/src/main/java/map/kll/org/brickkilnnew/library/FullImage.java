package map.kll.org.brickkilnnew.library;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import map.kll.org.brickkilnnew.BuildConfig;
import map.kll.org.brickkilnnew.R;


public class FullImage extends Activity implements OnDownloadImageComplete{

    ArrayList<String> image = new ArrayList<String>();
    public static  ArrayList<Bitmap> imageList = null;
    ProgressDialog progressDialog;
    int curIndex=0;
    int downX,upX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_full);
        Intent intent = getIntent();
        image = intent.getStringArrayListExtra("images");
        Button button2 = (Button) findViewById(R.id.close_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        DownloadImage downloadImage = new DownloadImage(this,image);
        downloadImage.execute();

    }
    @Override
    public void onImageDownload(ArrayList<Bitmap> imgList){
        this.imageList = imgList;
        showImage(imgList);
    }
    public void showImage(ArrayList<Bitmap> imgList){
       imageList = imgList;
        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(FullImage.this);
                imageView.isFocusable();
                return imageView;

            }
        });

        imageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        imageSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if(imageList!=null){
            Drawable d = new BitmapDrawable(imageList.get(curIndex));
            imageSwitcher.setImageDrawable(d);
            imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            downX = (int) event.getX();

                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            upX = (int) event.getX();
                            if (downX<upX) {

                                //curIndex  current image index in array viewed by user
                                curIndex--;
                                if (curIndex < 0) {
                                    curIndex = imageList.size()-1;
                                }


                                imageSwitcher.setInAnimation(getBaseContext(), android.R.anim.fade_in);
                                imageSwitcher.setOutAnimation(getBaseContext(), android.R.anim.fade_out);
                                Drawable d = new BitmapDrawable(imageList.get(curIndex));
                                imageSwitcher.setImageDrawable(d);
                            }
                            else if (downX>upX) {

                                curIndex++;
                                if (curIndex == imageList.size()) {
                                    curIndex = 0;
                                }


                                imageSwitcher.setInAnimation(getBaseContext(), android.R.anim.fade_in);
                                imageSwitcher.setOutAnimation(getBaseContext(), android.R.anim.fade_out);
                                Drawable d = new BitmapDrawable(imageList.get(curIndex));
                                imageSwitcher.setImageDrawable(d);
                            }
                            break;
                        }


                    }
                    return true ;
                }                       });

            curIndex = (curIndex + 1) % imageList.size();

        }else {
            Toast.makeText(this, "No Internet Connection \n Check Your Internet Connection", Toast.LENGTH_LONG).show();
            imageList=null;


        }
    }



}

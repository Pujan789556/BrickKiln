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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import map.kll.org.brickkilnnew.BuildConfig;
import map.kll.org.brickkilnnew.R;


public class FullImage extends Activity{

    ArrayList<String> image = new ArrayList<String>();
    ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    int curIndex=0;
    int downX,upX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_full);
        Intent intent = getIntent();
        image = intent.getStringArrayListExtra("images");
        imageList.clear();
if (haveNetworkConnection()==true){
        for (int i = 0; i < image.size(); i++) {
                       imageList.add(getBitmapFromURL("https://ona.io/attachment/large?media_file=ktmlabs/attachments/" + image.get(i)));
        }
}else {
    Bitmap b = BitmapFactory.decodeResource(getBaseContext().getResources(),
            R.drawable.no_connection);
    imageList.add(b);
}


    }
    @Override
    protected void onStart() {
        super.onStart();
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

    }



      public Bitmap getBitmapFromURL(String src) {

        try {

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            Log.i("Exception",e.getMessage());

            return null;
        }
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}

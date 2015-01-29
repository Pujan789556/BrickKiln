package map.kll.org.brickkilnnew.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DownloadImage extends AsyncTask<ArrayList<Bitmap>,ArrayList<Bitmap>,ArrayList<Bitmap>> {
    private Activity activity;
    private Context context;
    private ArrayList<String> image;
    private OnDownloadImageComplete callback;
    private ProgressDialog pDialog;

    public ArrayList<Bitmap> imageList;

    public DownloadImage(Context c, ArrayList<String> img) {
        this.context = c;
        this.image = img;
        Activity a = (Activity) c;
        this.activity = a;
        this.callback = (OnDownloadImageComplete) a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Downloading Images ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(ArrayList<Bitmap>... args) {

        if (haveNetworkConnection() == true && isOnline() == true) {
            imageList = new ArrayList<Bitmap>();
            for (int i = 0; i < image.size(); i++) {
                Bitmap bm = (Bitmap) getBitmapFromURL("https://ona.io/attachment/medium?media_file=ktmlabs/attachments/" + image.get(i));
                imageList.add(bm);
            }
        }
        return imageList;
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> imgList) {
        pDialog.dismiss();

        callback.onImageDownload(imgList);


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
            Log.e("Error", e.getMessage());
            Toast.makeText(context, "Download Failed \n Check Your Internet Connection", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
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
    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

}

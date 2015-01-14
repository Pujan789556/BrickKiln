package map.kll.org.brickkilnnew.library;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import map.kll.org.brickkilnnew.R;

public class MarkerActivity extends FragmentActivity{
    TabHost tHost;
    static ArrayList<String> generalInfo=new ArrayList<String>();
    static ArrayList<String> techDetail = new ArrayList<String>();
    static ArrayList<String> socioEconomic = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kiln_dialog);
        this.generalInfo.clear();
        this.techDetail.clear();
        this.socioEconomic.clear();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String name = bundle.getString("name");
        String city = bundle.getString("city");
        final ArrayList<String> image = bundle.getStringArrayList("image");
        Log.i("Image on Tap",image.get(0));
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(name + ",\n" + city);
        Button button = (Button) findViewById(R.id.btnImg1);
        button.setBackgroundResource(R.drawable.gallery);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarkerActivity.this, FullImage.class);
                Log.i("chkpt",Integer.toString(image.size()));
                intent.putStringArrayListExtra("images",image);
                startActivity(intent);

            }
        });
        String ownership = bundle.getString("ownership");
        this.generalInfo.add("Ownership:\n"+ownership);
        String market = bundle.getString("market");
        this.generalInfo.add("Market:\n"+market);
        String operating_seasons = bundle.getString("operating_seasons");
        this.generalInfo.add("Operating Seasons:\n"+operating_seasons);
        String days_open = bundle.getString("days_open");
        this.generalInfo.add("Days Open:\n"+days_open);


        String raw_material = bundle.getString("raw_material");
        this.techDetail.add("Raw Material:\n"+raw_material);
        String fuel = bundle.getString("fuel");
        this.techDetail.add("Fuel:\n"+fuel);
        String fuel_quantity = bundle.getString("fuel_quantity");
        this.techDetail.add("Fuel Quantity:\n"+fuel_quantity);
        String brick_kind = bundle.getString("brick_kind");
        this.techDetail.add("Kind Of Brick Produced:\n"+brick_kind);
        String chimney_cat = bundle.getString("chimney_cat");
        this.techDetail.add("Chimney Category:\n"+chimney_cat);
        String chimney_height= bundle.getString("chimney_height");
        this.techDetail.add("Chimney Height:\n"+chimney_height);
        String chimney_number = bundle.getString("chimney_number");
        this.techDetail.add("Chimney Number:\n"+chimney_number);
        String moulding_process = bundle.getString("moulding_process");
        this.techDetail.add("Moulding Process:\n"+moulding_process);
        String firing = bundle.getString("firing");
        this.techDetail.add("Firing:\n"+firing);
        String capacity = bundle.getString("capacity");
        this.techDetail.add("Capacity:\n"+capacity);
        String brick_per_batch = bundle.getString("brick_per_batch");
        this.techDetail.add("Bricks Per Batch:\n"+brick_per_batch);
        String quality = bundle.getString("quality");
        this.techDetail.add("Quality:\n"+quality);


        String labor_children = bundle.getString("labor_children");
        this.socioEconomic.add("Children as Labourers:\n"+labor_children);
        String labor_male = bundle.getString("labor_male");
        this.socioEconomic.add("Male Workers:\n"+labor_male);
        String labor_female = bundle.getString("labor_female");
        this.socioEconomic.add("Female Workers:\n"+labor_female);
        String labor_total = bundle.getString("labor_total");
        this.socioEconomic.add("Total Numbers of Workers:\n"+labor_total);
        String labor_young = bundle.getString("labor_young");
        this.socioEconomic.add("Young Labourers:\n"+labor_young);
        String labor_old= bundle.getString("labor_old");
        this.socioEconomic.add("Elderly Labourers:\n"+labor_old);
        String labor_currently_studying = bundle.getString("labor_currently_studying");
        this.socioEconomic.add("Labourers Currently Studying:\n"+labor_currently_studying);
        String labor_slc = bundle.getString("labor_slc");
        this.socioEconomic.add("Workers With SLC:\n"+labor_slc);
        String labor_informal_edu = bundle.getString("labor_informal_edu");
        this.socioEconomic.add("Workers With Informal Education:\n"+labor_informal_edu);
        String labor_illiterate = bundle.getString("labor_illiterate");
        this.socioEconomic.add("Workers Not Literate:\n"+labor_illiterate);
        String food_allowance = bundle.getString("food_allowance");
        this.socioEconomic.add("Food Allowance:\n"+food_allowance);



        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();

        /** Defining Tab Change Listener event. This is invoked when tab is changed */
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                InfoFragment infoFragment = (InfoFragment) fm.findFragmentByTag("info");
                TechFragment techFragment = (TechFragment) fm.findFragmentByTag("tech");
                SocioFragment socioFragment = (SocioFragment) fm.findFragmentByTag("socio");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();


                if (infoFragment != null)
                    ft.detach(infoFragment);


                if (techFragment != null)
                    ft.detach(techFragment);
                if (socioFragment != null)
                    ft.detach(socioFragment);


                if (tabId.equalsIgnoreCase("info")) {

                    if (infoFragment == null) {

                        ft.add(R.id.realtabcontent, new InfoFragment(), "info");
                    } else {

                        ft.attach(infoFragment);
                    }

                } else if (tabId.equalsIgnoreCase("tech")) {
                    if (techFragment == null) {

                        ft.add(R.id.realtabcontent, new TechFragment(), "tech");
                    } else {

                        ft.attach(techFragment);
                    }
                } else {
                    if (socioFragment == null) {

                        ft.add(R.id.realtabcontent, new SocioFragment(), "socio");
                    } else {

                        ft.attach(socioFragment);
                    }
                }
                ft.commit();
            }
        };
        /** Setting tabchangelistener for the tab */
        tHost.setOnTabChangedListener(tabChangeListener);

        /** Defining tab builder for Andriod tab */

        TabHost.TabSpec tSpecInfo = tHost.newTabSpec("info");
        tSpecInfo.setIndicator("GENERAL INFORMATION", getResources().getDrawable(R.drawable.info));
        tSpecInfo.setContent(new DummyTabContent(getBaseContext()));


        tHost.addTab(tSpecInfo);

        /** Defining tab builder for Apple tab */

        TabHost.TabSpec tSpecTech = tHost.newTabSpec("tech");
        tSpecTech.setIndicator("TECHNICAL DETAILS", getResources().getDrawable(R.drawable.ic_tech));
        tSpecTech.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecTech);

        /** Defining tab builder for Apple tab */

        TabHost.TabSpec tSpecSocio = tHost.newTabSpec("socio");
        tSpecSocio.setIndicator("SOCIO-ECONOMIC", getResources().getDrawable(R.drawable.ic_socio));
        tSpecSocio.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecSocio);
    }
        public ArrayList<String> getInfoArrayList(){
            ArrayList<String> info = this.generalInfo;

            return info;

        }
    public ArrayList<String> getTechArrayList(){
        ArrayList<String> info = this.techDetail;
        return info;

    }
    public ArrayList<String> getSocioArrayList(){
        ArrayList<String> info = this.socioEconomic;
        return info;

    }
    public  Bitmap getBitmapFromURL(String src) {
        try {

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }


}

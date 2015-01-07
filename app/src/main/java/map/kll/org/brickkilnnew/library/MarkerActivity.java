package map.kll.org.brickkilnnew.library;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

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
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String name = bundle.getString("name");
        String city = bundle.getString("city");
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(name+",\n"+city);

        /** Defining Tab Change Listener event. This is invoked when tab is changed */
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                InfoFragment infoFragment = (InfoFragment) fm.findFragmentByTag("info");
                TechFragment techFragment = (TechFragment) fm.findFragmentByTag("tech");
                SocioFragment socioFragment =  (SocioFragment)fm.findFragmentByTag("socio");
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
        tSpecInfo.setIndicator("GENERAL INFORMATION",getResources().getDrawable(R.drawable.info));
        tSpecInfo.setContent(new DummyTabContent(getBaseContext()));




        tHost.addTab(tSpecInfo);

        /** Defining tab builder for Apple tab */

        TabHost.TabSpec tSpecTech = tHost.newTabSpec("tech");
        tSpecTech.setIndicator("TECHNICAL DETAILS",getResources().getDrawable(R.drawable.ic_tech));
        tSpecTech.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecTech);

        /** Defining tab builder for Apple tab */

        TabHost.TabSpec tSpecSocio = tHost.newTabSpec("socio");
        tSpecSocio.setIndicator("SOCIO-ECONOMIC",getResources().getDrawable(R.drawable.ic_socio));
        tSpecSocio.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecSocio);

    }
}

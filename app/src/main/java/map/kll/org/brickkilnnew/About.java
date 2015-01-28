package map.kll.org.brickkilnnew;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.about_dialog);

    }
    @Override
    protected void onStart() {
        super.onStart();

        String app_name = "KLL Brick Kiln";
        TextView textView1= (TextView) findViewById(R.id.app_name_txt);
        textView1.setText(app_name);
         String version = BuildConfig.VERSION_NAME;

        TextView textView2= (TextView) findViewById(R.id.app_version_txt);
        textView2.setText(version);
        TextView textView3= (TextView) findViewById(R.id.app_detail_txt);
        textView3.setText(Html.fromHtml("This is an android app that shows Brick Kilns located in Kathmandu Valley with their Attribute data. The data is collected by field suvey from Kathmandu Living Labs and is openly accessible via OpenStreetMap. The web based map visualization of the sam data can be found at: <a href=\"http://kathmandulivinglabs.org/mapbrickkiln/\">\nhttp://kathmandulivinglabs.org/mapbrickkiln/</a>."));
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        String app_developer = "Kathmandu Living Labs";
        TextView textView4= (TextView) findViewById(R.id.app_developer_txt);
        textView4.setText(app_developer);
        Button button2 = (Button) findViewById(R.id.close_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
}

}

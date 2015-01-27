package map.kll.org.brickkilnnew.library;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import map.kll.org.brickkilnnew.R;

public class MarkerActivity extends Activity{

    static ArrayList<String> infoValue=new ArrayList<String>();
    static ArrayList<String> infoKey = new ArrayList<String>();
   
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kiln_dialog);
        this.infoKey.clear();
        this.infoValue.clear();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        String city = bundle.getString("city");
        city=city.substring(0,1).toUpperCase()+city.substring(1).toLowerCase();
        final ArrayList<String> image = bundle.getStringArrayList("image");
        Log.i("Image on Tap",image.get(0));
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(name.toUpperCase()+",");
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(city);

       Button button1 = (Button) findViewById(R.id.btnImg1);
        button1.setBackgroundResource(R.drawable.gallery);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarkerActivity.this, FullImage.class);
                Log.i("chkpt",Integer.toString(image.size()));
                intent.putStringArrayListExtra("images",image);
                startActivity(intent);

            }
        });
        Button button2 = (Button) findViewById(R.id.close_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });
        String ownership = bundle.getString("ownership");
        if (!(ownership.equals("N/A")||ownership.equals("999")||ownership.equals("999.0")||ownership.equals("0.0")||ownership.equals("0"))) {
            this.infoKey.add("Ownership:");
            ownership=autoCorrect(ownership);
            this.infoValue.add(ownership);
        }
        String market = bundle.getString("market");
        if (!(market.equals("N/A")||market.equals("999")||market.equals("999.0")||market.equals("0.0")||market.equals("0"))) {
        this.infoKey.add("Market:");
            market=autoCorrect(market);
            this.infoValue.add(market);
        }

        String operating_seasons = bundle.getString("operating_seasons");
        if (!(operating_seasons.equals("N/A")||operating_seasons.equals("999")||operating_seasons.equals("999.0")||operating_seasons.equals("0.0")||operating_seasons.equals("0"))) {
            this.infoKey.add("Operating Seasons:" );
            operating_seasons=autoCorrect(operating_seasons);
            this.infoValue.add(operating_seasons);
        }
        String days_open = bundle.getString("days_open");
        if (!(days_open.equals("N/A")||days_open.equals("999")||days_open.equals("999.0")||days_open.equals("0.0")||days_open.equals("0"))) {
            this.infoKey.add("Days Open:");
            days_open=autoCorrect(days_open);
            this.infoValue.add(days_open);
        }


        String raw_material = bundle.getString("raw_material");
        if (!(raw_material.equals("N/A")||raw_material.equals("999")||raw_material.equals("999.0")||raw_material.equals("0.0")||raw_material.equals("0"))) {
            this.infoKey.add("Raw Material:");
            raw_material=autoCorrect(raw_material);
            this.infoValue.add(raw_material);
        }
        String fuel = bundle.getString("fuel");
        if (!(fuel.equals("N/A")||fuel.equals("999")||fuel.equals("999.0")||fuel.equals("0.0")||fuel.equals("0"))) {
            this.infoKey.add("Fuel:");
            fuel=autoCorrect(fuel);
            this.infoValue.add(fuel);

        }
        String fuel_quantity = bundle.getString("fuel_quantity");
        if (!(fuel_quantity.equals("N/A")||fuel_quantity.equals("999")||fuel_quantity.equals("999.0")||fuel_quantity.equals("0.0")||fuel_quantity.equals("0"))) {
            this.infoKey.add("Fuel Quantity:");
            Log.i("Check",fuel_quantity);
            fuel_quantity=autoCorrect(fuel_quantity);
            this.infoValue.add(fuel_quantity);
        }
        String brick_kind = bundle.getString("brick_kind");
        if (!(brick_kind.equals("N/A")||brick_kind.equals("999")||brick_kind.equals("999.0")||brick_kind.equals("0.0")||brick_kind.equals("0"))) {
            this.infoKey.add("Kind Of Brick Produced:");
            brick_kind=autoCorrect(brick_kind);
            this.infoValue.add(brick_kind);
        }
        String chimney_cat = bundle.getString("chimney_cat");
        if (!(chimney_cat.equals("N/A")||chimney_cat.equals("999")||chimney_cat.equals("999.0")||chimney_cat.equals("0.0")||chimney_cat.equals("0"))) {
            this.infoKey.add("Chimney Category:");
            chimney_cat=autoCorrect(chimney_cat);
            this.infoValue.add(chimney_cat);
        }
        String chimney_height= bundle.getString("chimney_height");
        if (!(chimney_height.equals("N/A")||chimney_height.equals("999")||chimney_height.equals("999.0")||chimney_height.equals("0.0")||chimney_height.equals("0"))) {
            this.infoKey.add("Chimney Height:");
            chimney_height=autoCorrect(chimney_height);
            this.infoValue.add(chimney_height);
        }
        String chimney_number = bundle.getString("chimney_number");
        if (!(chimney_number.equals("N/A")||chimney_number.equals("999")||chimney_number.equals("999.0")||chimney_number.equals("0.0")||chimney_number.equals("0"))) {
            this.infoKey.add("Chimney Number:");
            chimney_number=autoCorrect(chimney_number);
            this.infoValue.add(chimney_number);
        }
        String moulding_process = bundle.getString("moulding_process");
        if (!(moulding_process.equals("N/A")||moulding_process.equals("999")||moulding_process.equals("999.0")||moulding_process.equals("0.0")||moulding_process.equals("0"))) {
            this.infoKey.add("Moulding Process:");
            moulding_process=autoCorrect(moulding_process);
            this.infoValue.add(moulding_process);
        }
        String firing = bundle.getString("firing");
        if (!(firing.equals("N/A")||firing.equals("999")||firing.equals("999.0")||firing.equals("0.0")||firing.equals("0"))) {
            this.infoKey.add("Firing:");
            firing=autoCorrect(firing);
            this.infoValue.add(firing);
        }
        String capacity = bundle.getString("capacity");
        if (!(capacity.equals("N/A")||capacity.equals("999")||capacity.equals("999.0")||capacity.equals("0.0")||capacity.equals("0"))) {
            this.infoKey.add("Capacity:");
            capacity=autoCorrect(capacity);
            this.infoValue.add(capacity);
        }
        String brick_per_batch = bundle.getString("brick_per_batch");
        if (!(brick_per_batch.equals("N/A")||brick_per_batch.equals("999")||brick_per_batch.equals("999.0")||brick_per_batch.equals("0.0")||brick_per_batch.equals("0"))) {
            this.infoKey.add("Bricks Per Batch:");
            brick_per_batch=autoCorrect(brick_per_batch);
            this.infoValue.add(brick_per_batch);
        }
        String quality = bundle.getString("quality");
        if (!(quality.equals("N/A")||quality.equals("999")||quality.equals("999.0")||quality.equals("0.0")||quality.equals("0"))) {
            this.infoKey.add("Quality:");
            quality=autoCorrect(quality);
            this.infoValue.add(quality);
        }


        String labor_children = bundle.getString("labor_children");
        if (!(labor_children.equals("N/A")||labor_children.equals("999")||labor_children.equals("999.0")||labor_children.equals("0.0")||labor_children.equals("0"))) {
            this.infoKey.add("Children as Labourers:");
            this.infoValue.add(labor_children);

        }
        String labor_male = bundle.getString("labor_male");
        if (!(labor_male.equals("N/A")||labor_male.equals("999")||labor_male.equals("999.0")||labor_male.equals("0.0")||labor_male.equals("0"))) {
            this.infoKey.add("Male Workers:");
            this.infoValue.add(labor_male);
        }
        String labor_female = bundle.getString("labor_female");
        if (!(labor_female.equals("N/A")||labor_female.equals("999")||labor_female.equals("999.0")||labor_female.equals("0.0")||labor_female.equals("0"))) {
            this.infoKey.add("Female Workers:");
            this.infoValue.add(labor_female);
        }
        String labor_total = bundle.getString("labor_total");
        if (!(labor_total.equals("N/A")||labor_total.equals("999")||labor_total.equals("999.0")||labor_total.equals("0.0")|labor_total.equals("0"))) {
            this.infoKey.add("Total Numbers of Workers:");
            this.infoValue.add(labor_total);
        }
        String labor_young = bundle.getString("labor_young");
        if (!(labor_young.equals("N/A")||labor_young.equals("999")||labor_young.equals("999.0")||labor_young.equals("0.0")||labor_young.equals("0"))) {
            this.infoKey.add("Young Labourers:");
            this.infoValue.add(labor_young);
        }
        String labor_old= bundle.getString("labor_old");
        if (!(labor_old.equals("N/A")||labor_old.equals("999")||labor_old.equals("999.0")||labor_old.equals("0.0")||labor_old.equals("0"))) {
            this.infoKey.add("Elderly Labourers:");
            this.infoValue.add(labor_old);
        }
        String labor_currently_studying = bundle.getString("labor_currently_studying");
        if (!(labor_currently_studying.equals("N/A")||labor_currently_studying.equals("999")||labor_currently_studying.equals("999.0")||labor_currently_studying.equals("0.0")||labor_currently_studying.equals("0"))) {
            this.infoKey.add("Labourers Currently Studying:");
            this.infoValue.add(labor_currently_studying);
        }
        String labor_slc = bundle.getString("labor_slc");
        if (!(labor_slc.equals("N/A")||labor_slc.equals("999")|labor_slc.equals("999.0")||labor_slc.equals("0.0")||labor_slc.equals("0"))) {
            this.infoKey.add("Workers With SLC:");
            this.infoValue.add(labor_slc);
        }

        String labor_informal_edu = bundle.getString("labor_informal_edu");
        if (!(labor_informal_edu.equals("N/A")||labor_informal_edu.equals("999")||labor_informal_edu.equals("999.0")||labor_informal_edu.equals("0.0")||labor_informal_edu.equals("0"))) {
            this.infoKey.add("Workers With Informal Education:");
            this.infoValue.add(labor_informal_edu);
        }
        String labor_illiterate = bundle.getString("labor_illiterate");
        if (!(labor_illiterate.equals("N/A")||labor_illiterate.equals("999")||labor_illiterate.equals("999.0")||labor_illiterate.equals("0.0")||labor_illiterate.equals("0"))) {
            this.infoKey.add("Workers Not Literate:");
            this.infoValue.add(labor_illiterate);
        }
        String food_allowance = bundle.getString("food_allowance");
        if (!(food_allowance.equals("N/A")||food_allowance.equals("999")||food_allowance.equals("999.0")||food_allowance.equals("0.0")||food_allowance.equals("0"))) {
            this.infoKey.add("Food Allowance:");
            this.infoValue.add(food_allowance);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lowerLayout);
        for(int i=0;i<this.infoKey.size();i++){
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            TextView textViewKey = new TextView(this);
            textViewKey.setText(this.infoKey.get(i));
            textViewKey.setTextSize(15);
            textViewKey.setTextColor(Color.parseColor("#FF000000"));
            TextView textViewValue = new TextView(this);
            textViewValue.setTextSize(20);
            linearLayout1.setPadding(10,0,0,0);
            textViewValue.setTypeface(Typeface.DEFAULT_BOLD);
            textViewValue.setTextColor(Color.parseColor("#FF000000"));
            textViewValue.setText(this.infoValue.get(i));
            textViewValue.setPadding(5,0,0,0);

            linearLayout1.addView(textViewKey);
            linearLayout1.addView(textViewValue);
            linearLayout.addView(linearLayout1);

        }



    }
    private String autoCorrect(String text){
        text=text.substring(0,1).toUpperCase()+text.substring(1).toLowerCase();
        String temp=text;
        for (int i = 0;i<temp.length();i++){
            if (temp.charAt(i)=='_'||temp.charAt(i)==' ') {
                if (i != temp.length()-1) {
                    temp = temp.substring(0, i) + ' ' + temp.substring(i+1,i+2).toUpperCase() + temp.substring(i+2);
                }
            }

        }
        return temp;

    }
}

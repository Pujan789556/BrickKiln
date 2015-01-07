package map.kll.org.brickkilnnew;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.Map;

import map.kll.org.brickkilnnew.library.BrickKiln;

public class Search extends Activity{
    static String nameKiln = null;
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.search_box);
       // button = (Button)findViewById(R.id.btn1);
       // editText   = (EditText)findViewById(R.id.editText1);

        button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)

                    {

                        nameKiln = editText.getText().toString();
                        MapActivity mapActivity = new MapActivity();
                        mapActivity.onSearch(nameKiln);
                        finish();
                    }
                });
    }

}

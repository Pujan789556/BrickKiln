package map.kll.org.newbrickkiln;


import android.content.Context;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class DummyTabContent implements TabHost.TabContentFactory {
    private Context mContext;


    public DummyTabContent(Context context) {
        mContext = context;
    }

        @Override
        public View createTabContent (String tag){
            View v = new View(mContext);

            return v;
        }
    }

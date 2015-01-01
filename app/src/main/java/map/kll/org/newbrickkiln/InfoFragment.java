package map.kll.org.newbrickkiln;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.R.layout;



import java.util.ArrayList;

public class InfoFragment extends ListFragment {



    public InfoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarkerActivity markerActivity = new MarkerActivity();
        ArrayList<String> gI= markerActivity.getInfoArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), layout.simple_list_item_1, gI);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);


    }
    @Override
    public void onStart() {
        super.onStart();
        getListView();


    }

}

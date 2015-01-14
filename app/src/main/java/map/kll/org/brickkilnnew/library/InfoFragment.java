package map.kll.org.brickkilnnew.library;

import android.R.layout;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import map.kll.org.brickkilnnew.MapActivity;
import map.kll.org.brickkilnnew.R;


public class InfoFragment extends ListFragment {
    static ArrayList<String> generalInfo=new ArrayList<String>();
    public InfoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarkerActivity markerActivity = new MarkerActivity();
        generalInfo = markerActivity.getInfoArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), layout.simple_list_item_1, generalInfo);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);


    }
    @Override
    public void onStart() {
        super.onStart();
        getListView().setBackgroundResource(android.R.color.darker_gray);
        getListView().setDividerHeight(15);



    }

}

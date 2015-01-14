package map.kll.org.brickkilnnew.library;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import map.kll.org.brickkilnnew.R;


public class TechFragment extends ListFragment {
    static ArrayList<String> techDetail=new ArrayList<String>();
    public TechFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarkerActivity markerActivity = new MarkerActivity();
        techDetail= markerActivity.getTechArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, techDetail);
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



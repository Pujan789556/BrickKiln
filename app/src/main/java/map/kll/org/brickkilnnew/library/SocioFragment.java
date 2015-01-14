package map.kll.org.brickkilnnew.library;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class SocioFragment extends ListFragment {
    static ArrayList<String> socioEconomic=new ArrayList<String>();
    public SocioFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarkerActivity markerActivity = new MarkerActivity();
        socioEconomic= markerActivity.getSocioArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, socioEconomic);
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

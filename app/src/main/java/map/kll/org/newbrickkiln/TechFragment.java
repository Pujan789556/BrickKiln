package map.kll.org.newbrickkiln;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class TechFragment extends ListFragment {
    public TechFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MarkerActivity markerActivity = new MarkerActivity();
        ArrayList<String> tD= markerActivity.getTechArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, tD);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);


    }
    @Override
    public void onStart() {
        super.onStart();
        getListView();


    }

}

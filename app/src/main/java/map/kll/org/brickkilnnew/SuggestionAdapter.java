package map.kll.org.brickkilnnew;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends CursorAdapter {
    private ArrayList<String> items=null;

    private AutoCompleteTextView text;


    public SuggestionAdapter(Context context, Cursor cursor, ArrayList<String> items) {

        super(context, cursor, false);

        this.items = items;

    }

        @Override

        public void bindView (View view, Context context,final Cursor cursor){



               TextView text = (TextView) view.findViewById(R.id.text);
            text.setTextColor(Color.parseColor("#FF000000"));
               text.setText(items.get(cursor.getPosition()));



        }


    @Override
        public View newView (Context context, Cursor cursor, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_suggestion, parent, false);
            return view;

        }

}


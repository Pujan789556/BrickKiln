package map.kll.org.brickkilnnew;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends CursorAdapter {
    private ArrayList<String> items;

    private TextView text;

    public SuggestionAdapter(Context context, Cursor cursor, ArrayList<String> items) {

        super(context, cursor, false);

        this.items = items;

    }
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        // Show list item data from cursor

        text.setText(items.get(cursor.getPosition()));



    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_suggestion, parent, false);

        text = (TextView) view.findViewById(R.id.text);

        return view;

    }
    public String getSelectedString(int position){
        String selectedString = (String) items.get(position);
        return selectedString;
    }

}


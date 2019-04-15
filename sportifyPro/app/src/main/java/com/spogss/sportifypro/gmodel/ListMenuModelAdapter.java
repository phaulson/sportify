package com.spogss.sportifypro.gmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spogss.sportifypro.R;

import java.util.ArrayList;

/**
 * Created by samue on 27.03.2018.
 */

public class ListMenuModelAdapter extends ArrayAdapter<ListMenuModel> {
    private final Context context;
    private final ArrayList<ListMenuModel> modelsArrayList;

    public ListMenuModelAdapter(Context context, ArrayList<ListMenuModel> modelsArrayList) {

        super(context, R.layout.target_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        ListMenuModel model = modelsArrayList.get(position);

        View rowView = null;
        if(!model.isGroupHeader()){
            rowView = inflater.inflate(R.layout.target_item, parent, false);

            // 3. Get icon,title & counter views from the rowView
            ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

            // 4. Set the text for textView
            imgView.setImageResource(model.getIcon());
            titleView.setText(model.getTitle());
            counterView.setText(model.getCounter());
        }
        else{
            rowView = inflater.inflate(R.layout.group_header_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.header);
            titleView.setText(model.getTitle());

        }

        Integer id_view = model.getViewId();
        if(id_view != null)
            rowView.setId(id_view);

        // 5. retrn rowView
        View.OnClickListener l = model.getOnClickListener();
        if(l != null)
            rowView.setOnClickListener(l);

        return rowView;
    }
}

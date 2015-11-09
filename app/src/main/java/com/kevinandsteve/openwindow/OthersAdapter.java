package com.kevinandsteve.openwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Steve on 2015-11-08.
 */
public class OthersAdapter extends ArrayAdapter<Others> {

    public OthersAdapter(Context context, ArrayList<Others> users){
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Others others = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
            }
            // Lookup view for data population
            TextView otherName = (TextView) convertView.findViewById(R.id.othersname);
            TextView otherPhone = (TextView) convertView.findViewById(R.id.otherphone);
            CheckBox otherCheck = (CheckBox) convertView.findViewById(R.id.checkothers);
            // Populate the data into the template view using the data object
            otherName.setText(others.name);
            otherPhone.setText(others.number);
            // Return the completed view to render on screen
            return convertView;
        }
}

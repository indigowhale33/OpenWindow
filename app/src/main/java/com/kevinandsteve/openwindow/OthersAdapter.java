package com.kevinandsteve.openwindow;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Steve on 2015-11-08.
 */
public class OthersAdapter extends ArrayAdapter<Others> {
    boolean[] mChecked;
    SharedPreferences prefs;
    Context con;
    CheckBox[] checkBoxArray;
    ArrayList added;
    private LayoutInflater mInflater;
    ViewHolder holder;
    SharedPreferences.Editor editor;

    static class ViewHolder {
        TextView otherName;
        TextView otherPhone;
        CheckBox otherCheck;

    }

    public int getCount() {
        return added.size();

    }

    public Others getItem(int position) {
        return (Others)added.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public OthersAdapter(Context context, ArrayList<Others> users){
        super(context, 0, users);
        mInflater = LayoutInflater.from(context);
        con = context;
        added = users;
        mChecked= new boolean[users.size()];
        for(int i=0; i<mChecked.length; i++){
            mChecked[i]=false;
            checkBoxArray = new CheckBox[mChecked.length];

            //Toast.makeText(con, checked.toString(),Toast.LENGTH_SHORT).show();
        }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Others others = getItem(position);
            final SharedPreferences sharedPrefs = con.getSharedPreferences("Owpref", Context.MODE_PRIVATE);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.contact_list_item, null);
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
                holder = new ViewHolder();
                holder.otherName = (TextView) convertView.findViewById(R.id.othersname);
                holder.otherPhone = (TextView) convertView.findViewById(R.id.otherphone);
                holder.otherCheck = (CheckBox)convertView.findViewById(R.id.checkothers);

//                TextView otherName = (TextView) convertView.findViewById(R.id.othersname);
//                TextView otherPhone = (TextView) convertView.findViewById(R.id.otherphone);
//                CheckBox otherCheck = (CheckBox) convertView.findViewById(R.id.checkothers);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            // Lookup view for data population
            editor = sharedPrefs.edit();

            holder.otherName.setText(others.getName() + ": ");
            holder.otherPhone.setText(others.getNumber());
            holder.otherCheck.setChecked(sharedPrefs.getBoolean("CheckValue"+position, false));

//            otherCheck.setTag(Integer.valueOf(position));
//
//            // Populate the data into the template view using the data object
//            otherName.setText(others.name + ": ");
//            otherPhone.setText(others.number);
            // Return the completed view to render on screen
            holder.otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.remove("CheckValue"+position);
                    editor.putBoolean("CheckValue" + position, isChecked);
                    editor.commit();
                    Set<String> NameSet = new TreeSet<String>(prefs.getStringSet("OTHERSNAME", new TreeSet<String>()));
                    Set<String> PhoneSet = new TreeSet<String>(prefs.getStringSet("OTHERSPHONE", new TreeSet<String>()));

                    if(isChecked){
                        editor.remove("OTHERSNAME");
                        editor.remove("OTHERSPHONE");
                        NameSet.add(others.getName());
                        PhoneSet.add(others.getNumber());
                        editor.apply();
                        editor.putStringSet("OTHERSNAME", NameSet);
                        editor.putStringSet("OTHERSPHONE",PhoneSet);
                    }else{
                        editor.remove("OTHERSNAME");
                        editor.remove("OTHERSPHONE");
                        NameSet.remove(others.getName());
                        PhoneSet.remove(others.getNumber());
                        editor.apply();
                        editor.putStringSet("OTHERSNAME",NameSet);
                        editor.putStringSet("OTHERSPHONE",PhoneSet);
                    }
                    editor.apply();

                }
            });
            
            return convertView;
        }

    
}

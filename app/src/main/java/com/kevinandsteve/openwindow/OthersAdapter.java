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
import java.util.Arrays;
import java.util.HashSet;
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
                holder.otherCheck = (CheckBox) convertView.findViewById(R.id.checkothers);

//                TextView otherName = (TextView) convertView.findViewById(R.id.othersname);
//                TextView otherPhone = (TextView) convertView.findViewById(R.id.otherphone);
//                CheckBox otherCheck = (CheckBox) convertView.findViewById(R.id.checkothers);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Lookup view for data population
            editor = sharedPrefs.edit();

            holder.otherName.setText(others.getName() + ": ");
            holder.otherPhone.setText(others.getNumber());

//            if(others.getCheck()){
//                holder.otherCheck.setChecked(true);
//            }else{
//                holder.otherCheck.setChecked(false);
//            }
            Boolean a = sharedPrefs.getBoolean("OTHERSCHECK" + others.getName() + ": " + others.getNumber(), false);
            String b = others.getName();
            holder.otherCheck.setChecked(sharedPrefs.getBoolean("OTHERSCHECK" + others.getName() + ": " + others.getNumber(), false));
     //       holder.otherCheck.setChecked(false);
//            holder.otherCheck.setChecked(sharedPrefs.getBoolean("CheckValue"+ others.getName(), false));

//            otherCheck.setTag(Integer.valueOf(position));
//
//            // Populate the data into the template view using the data object
//            otherName.setText(others.name + ": ");
//            otherPhone.setText(others.number);
            // Return the completed view to render on screen
            holder.otherCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Others another = getItem(position);
                    Boolean gotcheck = sharedPrefs.getBoolean("OTHERSCHECK" + another.getName() + ": " + another.getNumber(), false);
                    if(another.getCheck()){
                        another.checked = false;
                        editor.putBoolean("OTHERSCHECK" + another.getName() + ": " + another.getNumber(), false);
                    }else{
                        another.checked = true;
                        editor.putBoolean("OTHERSCHECK" + another.getName() + ": " + another.getNumber(), true);
                    }

                    editor.apply();
                    Set<String> NameSet = new TreeSet<String>(sharedPrefs.getStringSet("OTHERSNAME", new TreeSet<String>()));
                    Set<String> PhoneSet = new TreeSet<String>(sharedPrefs.getStringSet("OTHERSPHONE", new TreeSet<String>()));

                    if (gotcheck) {
                        editor.remove("OTHERSNAME");
                        editor.remove("OTHERSPHONE");
                        NameSet.add(another.getName());
                        PhoneSet.add(another.getNumber());
                        editor.apply();
                        editor.putStringSet("OTHERSNAME", NameSet);
                        editor.putStringSet("OTHERSPHONE", PhoneSet);
                    } else {
                        editor.remove("OTHERSNAME");
                        editor.remove("OTHERSPHONE");
                        NameSet.remove(another.getName());
                        PhoneSet.remove(another.getNumber());
                        editor.apply();
                        editor.putStringSet("OTHERSNAME", NameSet);
                        editor.putStringSet("OTHERSPHONE", PhoneSet);
                    }
                    editor.apply();

                }
            });
            return convertView;
        }
//
//            holder.otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Set checkset = sharedPrefs.getStringSet("OTHERSCHECK", new TreeSet<String>());
//                    Others another = getItem(position);
//
//
//                    editor.putBoolean("OTHERSCHECK" + another.getName() + ": " + another.getNumber(), isChecked);
//                    editor.apply();
////                    if(isChecked){
////                        another.checked = true;
////                        checkset.add(another.getName() + ": " + another.getNumber());
////                    }else{
////                        another.checked = false;
////                        checkset.remove(another.getName() + ": " + another.getNumber());
////
////                    }
////                    editor.remove("OTHERSCHECK");
////                    editor.apply();
////                    editor.putStringSet("OTHERSCHECK", checkset);
//                    editor.apply();
////                    editor.remove("CheckValue" + others.getName());
////                    editor.putBoolean("CheckValue" + others.getName(), isChecked);
////                    editor.apply();
//                    Set<String> NameSet = new TreeSet<String>(sharedPrefs.getStringSet("OTHERSNAME", new TreeSet<String>()));
//                    Set<String> PhoneSet = new TreeSet<String>(sharedPrefs.getStringSet("OTHERSPHONE", new TreeSet<String>()));
//
//                    if (isChecked) {
//                        editor.remove("OTHERSNAME");
//                        editor.remove("OTHERSPHONE");
//                        NameSet.add(another.getName());
//                        PhoneSet.add(another.getNumber());
//                        editor.apply();
//                        editor.putStringSet("OTHERSNAME", NameSet);
//                        editor.putStringSet("OTHERSPHONE", PhoneSet);
//                    } else {
//                        editor.remove("OTHERSNAME");
//                        editor.remove("OTHERSPHONE");
//                        NameSet.remove(another.getName());
//                        PhoneSet.remove(another.getNumber());
//                        editor.apply();
//                        editor.putStringSet("OTHERSNAME", NameSet);
//                        editor.putStringSet("OTHERSPHONE", PhoneSet);
//                    }
//                    editor.apply();
//
//                }
//            });
//
//            return convertView;
//        }

    
}

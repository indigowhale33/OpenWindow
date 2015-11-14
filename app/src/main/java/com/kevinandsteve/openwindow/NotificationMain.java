package com.kevinandsteve.openwindow;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Steve on 2015-10-26.
 */
public class NotificationMain extends AppCompatActivity {
    String OTHERSHR = "OTHERSHR";
    String OTHERSMIN = "OTHERSMIN";
    String OTHERSNOTICH = "OTHERSNOTICH";
    String MYNOTIHR = "MYNOTIHR";
    String MYNOTIMIN = "MYNOTIMIN";
    String MYNOTICH = "MYNOTICH";


    CheckBox ch1, ch2;
    EditText timetext, othertxt;
    Button addbutt;
    private PendingIntent pendingIntent;
    public static final String OWPREF = "Owpref" ;
    SharedPreferences prefs;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_main);
        ch1=(CheckBox)findViewById(R.id.checkBox1);   //checkbox for the user itself
        ch2=(CheckBox)findViewById(R.id.checkBox2);   //checkbox for the user itself
        addbutt = (Button)findViewById(R.id.addothers);
        timetext= (EditText)findViewById(R.id.textv); //textfield for user
        othertxt= (EditText)findViewById(R.id.othertext); //textfield for user


        timetext.setKeyListener(null);       // make user cannot edit the textfield
        othertxt.setKeyListener(null);       // make user cannot edit the textfield

        prefs = getSharedPreferences(OWPREF, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(OWPREF, MODE_PRIVATE).edit();
//        Iterator testit = prefs.getStringSet("OTHERSPHONE",new TreeSet<String>()).iterator();
//        String str = "";
//        while(testit.hasNext()){
//            str += testit.next();
//        }
//        othertxt.setText(str);
        // Construct the data source
        ArrayList<Others> arrayOfUsers = new ArrayList<Others>();
        // Create the adapter to convert the array to views
        OthersAdapter adapter = new OthersAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.noti_others);

        Set<String> exOtherC = new TreeSet<String>(prefs.getStringSet("OthersContacts", new TreeSet<String>()));
        Iterator exIt = exOtherC.iterator();

        add_adapter(exOtherC, exIt, adapter);
        listView.setAdapter(adapter);
        //Toast.makeText(NotificationMain.this, ((TextView)findViewById(R.id.othersname)).getText(), Toast.LENGTH_SHORT).show();
        Integer restoredhr = prefs.getInt(MYNOTIHR, -1);
        Integer restoredmin = prefs.getInt(MYNOTIMIN, -1);
        String restore_selfch = prefs.getString(MYNOTICH,"n");

        if(restoredmin != -1 && restoredhr != -1){  // if no previous time set,
            timetext.setText("Your notification will be at "+restoredhr + ":" + restoredmin);
        }else{
            timetext.setText("Set Your Daily Notification");
            ch1.setChecked(false);
        }

        if(restore_selfch != "n"){
            ch1.setChecked(true);
        }


        Integer others_hr = prefs.getInt(OTHERSHR, -1);      // other's notification hour
        Integer others_min = prefs.getInt(OTHERSMIN, -1);    // other's notification min
        String others_ch = prefs.getString(OTHERSNOTICH, "n");  // total other's checkbox

        if(others_hr != -1 && others_min != -1){  // if no previous time set,
            othertxt.setText("Contacts will be notified daily at "+others_hr + ":" + others_min);
        }else{
            othertxt.setText("Set Time for SMS Notifications");
            ch2.setChecked(false);
        }

        if(others_ch != "n"){
            ch2.setChecked(true);
        }

        /* Retrieve a PendingIntent that will perform a broadcast */



        addbutt.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

//                showDialog(ADD_DIALOG_ID);
                final ArrayList contacts = (RetContacts());
                final String[] contactsstr = (String[])contacts.toArray(new String[contacts.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationMain.this);
                builder.setTitle("Add Contacts");
                builder.setView(LayoutInflater.from(NotificationMain.this).inflate(R.layout.dialog_list, null));
                final ArrayList<String> selectedItems = new ArrayList<String>();
                builder.setMultiChoiceItems(contactsstr, null, new DialogInterface.OnMultiChoiceClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            selectedItems.add(contactsstr[which]);
                        }else{
                            selectedItems.remove(contactsstr[which]);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Set<String> newcontacts = prefs.getStringSet("OthersContacts", new TreeSet<String>());
                        Set<String> ncontacts = new TreeSet<String>();
                        String selectedd = "";

                        for(String selected: selectedItems){
                            if(!newcontacts.contains(selected)){
                                newcontacts.add(selected);
                                ncontacts.add(selected);
                            }

//                            Others addsel = new Others(parts[0], parts[1],"n");
//                            otheradapter.add(addsel);
                        }
                        editor.remove("OthersContacts");
                        editor.apply();
                        editor.putStringSet("OthersContacts", newcontacts);
                        editor.apply();
                        if(ncontacts.size() == 0){
                            //add_adapter( newcontacts,ncontacts.iterator(), (OthersAdapter) listView.getAdapter());

                        }else {
                            add_adapter(newcontacts, ncontacts.iterator(), (OthersAdapter) listView.getAdapter());

                        }
                        //((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });


        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmIntent = new Intent(getBaseContext(), AppReceiver.class);
                if (ch1.isChecked()) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");
                    editor.remove(MYNOTIHR);
                    editor.remove(MYNOTIMIN);
                    editor.remove(MYNOTICH);
                    editor.apply();
                    editor.putString(MYNOTICH, "y");
                    editor.apply();
                } else {
                    timetext.setText("Set Your Daily Notification");
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 10, alarmIntent, 0);
                    manager.cancel(pendingIntent);

                    editor.remove(MYNOTICH);
                    editor.remove(MYNOTIHR);
                    editor.remove(MYNOTIMIN);
                    editor.apply();
                }
            }
        });

        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmIntent = new Intent(getBaseContext(), AppReceiver.class);
                if (ch2.isChecked()) {
                    DialogFragment newFragment = new TimeOtherFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");
                    editor.remove(OTHERSHR);
                    editor.remove(OTHERSMIN);
                    editor.remove(OTHERSNOTICH);
                    editor.apply();
                    editor.putString(OTHERSNOTICH, "y");

                    editor.apply();
                } else {
                    othertxt.setText("Set Time for SMS Notifications");
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 21, alarmIntent, 0);
                    manager.cancel(pendingIntent);

                    editor.remove(OTHERSNOTICH);
                    editor.remove(OTHERSHR);
                    editor.remove(OTHERSMIN);
                    editor.apply();

                }
            }
        });


        timetext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (ch1.isChecked()) {
                    setAlarm("ch1");
                    editor.remove("SELFNOTICHECK");
                    editor.apply();
                    editor.putString("SELFNOTICHECK", "y");

                } else {
                    editor.remove("SELFNOTICHECK");
                }
                editor.apply();


            }
        });


        othertxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (ch2.isChecked()) {
                    setAlarm("ch2");
                    editor.remove("TOTOTHERSCH");
                    editor.apply();
                    editor.putString("TOTOTHERSCH", "y");

                } else {
                    editor.remove("TOTOTHERSCH");
                }
                editor.apply();

            }
        });

    }



    public void setAlarm(String ch){
        SharedPreferences prefs = getSharedPreferences(OWPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences(OWPREF, MODE_PRIVATE).edit();
        String beforetime ="";
        if(ch == "ch1") {
            beforetime = timetext.getText().toString();
            beforetime = beforetime.replace("You will be notified daily at ", "");
        }else if(ch == "ch2"){
            beforetime = othertxt.getText().toString();
            beforetime = beforetime.replace("Contacts will be notified daily at ", "");
        }
        int ampm = 0;
        if(beforetime.contains("pm") && beforetime.split(" : ")[0] != "12"){
            ampm = 12;
        }
        int hr = Integer.valueOf(beforetime.split(" : ")[0]);
        if(hr == 12 && ampm == 12){  //if 12pm,
            hr = 12;
        }else {
            hr = Integer.valueOf(beforetime.split(" : ")[0]) + ampm;
        }
        int min =  Integer.valueOf(beforetime.split(" : ")[1].replace("am","").replace("pm",""));
        Integer restoredhr = -1;
        Integer restoredmin = -1;
        if(ch == "ch1") {
            restoredhr = prefs.getInt(MYNOTIHR, -1);
            restoredmin = prefs.getInt(MYNOTIMIN, -1);
            if (restoredhr != -1 && restoredmin != -1) {
                editor.remove(MYNOTIHR);
                editor.remove(MYNOTIMIN);
                editor.apply();
                editor.putInt(MYNOTIHR, hr);    //storing sharedpreference
                editor.putInt(MYNOTIMIN, min);  //storing sharedpreference
                editor.apply();
            } else {
                editor.putInt(MYNOTIHR, hr);    //storing sharedpreference
                editor.putInt(MYNOTIMIN, min);  //storing sharedpreference
                editor.apply();
            }
        }else if(ch == "ch2"){
            restoredhr = prefs.getInt(OTHERSHR, -1);
            restoredmin = prefs.getInt(OTHERSMIN, -1);
            if (restoredhr != -1 && restoredmin != -1) {
                editor.remove(OTHERSHR);
                editor.remove(OTHERSMIN);
                editor.apply();
                editor.putInt(OTHERSHR, hr);    //storing sharedpreference
                editor.putInt(OTHERSMIN, min);  //storing sharedpreference
                editor.apply();
            } else {
                editor.putInt(OTHERSHR, hr);    //storing sharedpreference
                editor.putInt(OTHERSMIN, min);  //storing sharedpreference
                editor.apply();
            }
        }


         /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(getBaseContext(), AppReceiver.class);

        if(ch == "ch1") {
            alarmIntent.setAction("Selfnoti");
            alarmIntent.putExtra("requestCode",10);
            //pendingIntent = PendingIntent.getActivity(getBaseContext(), 10, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 10, alarmIntent, 0);

        }else {
            alarmIntent.setAction("Othernoti");
            alarmIntent.putExtra("requestCode",21);
            //pendingIntent = PendingIntent.getActivity(getBaseContext(), 21, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 21, alarmIntent, 0);
        }
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        calendar.set(Calendar.MINUTE, Integer.valueOf(min));

        /* Repeating on everyday interval */
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    protected ArrayList<String> RetContacts(){
        ArrayList <String> contacts = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + ": " + (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("/n","-"));
            //String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(name);
        }
        cursor.close();
        return contacts;
    }

    protected void add_adapter(Set set, Iterator it, OthersAdapter adapter){
        String contact = "";
        while(it.hasNext()) {
            contact = (String) it.next();
            String[] parts = contact.split(": ");

            Others newUser = new Others(parts[0], parts[1], prefs.getBoolean("OTHERSCHECK" + contact, false));
            adapter.add(newUser);
        }
//        while(seti.hasNext()) {
//            Toast.makeText(NotificationMain.this, (String)seti.next(), Toast.LENGTH_SHORT).show();
//        }
    }

}

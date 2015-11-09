package com.kevinandsteve.openwindow;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Steve on 2015-10-26.
 */
public class NotificationMain extends AppCompatActivity {
    CheckBox ch1, ch2;
    EditText timetext, othertxt;
    Button addbutt;
    private PendingIntent pendingIntent;
    public static final String OWPREF = "Owpref" ;
    SharedPreferences sharedpreferences;

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
        SharedPreferences prefs = getSharedPreferences(OWPREF, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(OWPREF, MODE_PRIVATE).edit();

        // Construct the data source
        ArrayList<Others> arrayOfUsers = new ArrayList<Others>();
        // Create the adapter to convert the array to views
        OthersAdapter adapter = new OthersAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.noti_others);
        listView.setAdapter(adapter);
        Others newUser = new Others("Nathan", "7575757" , null);
        Others newUser2 = new Others("Steve", "2575215757" , null);
        Others newUser3 = new Others("Kevin", "757151257" , null);
        Others newUser4 = new Others("Daniel", "757575237" , null);
        adapter.add(newUser);
        adapter.add(newUser2);
        adapter.add(newUser3);
        adapter.add(newUser4);

        Integer restoredhr = prefs.getInt("MYSELFHR", -1);
        Integer restoredmin = prefs.getInt("MYSELFMIN", -1);
        String restore_selfch = prefs.getString("SELFCHECKED","n");
        if(restoredmin != -1 && restoredhr != -1){  // if no previous time set,
            timetext.setText("Your notification will be at "+restoredhr + " : " + restoredmin);
        }else{
            timetext.setText("Set Yourself Daily Notification");
        }

        if(restore_selfch != "n"){
            ch1.setChecked(true);
        }

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(getBaseContext(), AppReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, alarmIntent, 0);

//        addbutt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//                startActivityForResult(intent, 1);
//
//            }
//        });


        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch1.isChecked()) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");
                    editor.remove("MYSELFHR");
                    editor.remove("MYSELFMIN");
                    editor.remove("SELFCHECKED");
                    editor.putString("SELFCHECKED", "y");
                    editor.apply();
                } else {
                    timetext.setText("Set Yourself Daily Notification");
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    editor.remove("SELFCHECKED");
                    editor.remove("MYSELFHR");
                    editor.remove("MYSELFMIN");
                    editor.apply();
                }
            }
        });

        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch1.isChecked()) {
                    DialogFragment newFragment = new TimeOtherFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");

                } else {
                    othertxt.setText("Set Others' Daily Notification(SMS will be sent!)");


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
                    setAlarm();
                }


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
                 //   setAlarm();
                }


            }
        });

    }



    public void setAlarm(){
        SharedPreferences prefs = getSharedPreferences(OWPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences(OWPREF, MODE_PRIVATE).edit();
        String beforetime = timetext.getText().toString();
        beforetime = beforetime.replace("You will be notified daily at ", "");
        String hr =  beforetime.split(" : ")[0];
        String min =  beforetime.split(" : ")[1];

        Integer restoredhr = prefs.getInt("MYSELFHR", -1);
        Integer restoredmin = prefs.getInt("MYSELFMIN", -1);
        if(restoredhr != -1 && restoredmin != -1){
            editor.remove("MYSELFHR");
            editor.remove("MYSELFMIN");
            editor.apply();
            editor.putInt("MYSELFHR", Integer.valueOf(hr));    //storing sharedpreference
            editor.putInt("MYSELFMIN", Integer.valueOf(min));  //storing sharedpreference
            editor.apply();
        }else{
            editor.putInt("MYSELFHR", Integer.valueOf(hr));    //storing sharedpreference
            editor.putInt("MYSELFMIN", Integer.valueOf(min));  //storing sharedpreference
            editor.apply();
        }


         /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(getBaseContext(), AppReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, alarmIntent, 0);

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


}

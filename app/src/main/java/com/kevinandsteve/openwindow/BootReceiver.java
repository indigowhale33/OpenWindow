package com.kevinandsteve.openwindow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Steve on 2015-10-30.
 */
public class BootReceiver extends BroadcastReceiver{
    public static final String OWPREF = "Owpref" ;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String OTHERSHR = "OTHERSHR";
    String OTHERSMIN = "OTHERSMIN";
    String OTHERSNOTICH = "OTHERSNOTICH";
    String OTHERNOTIAPM = "OTHERNOTIAPM";
    String MYNOTIHR = "MYNOTIHR";
    String MYNOTIMIN = "MYNOTIMIN";
    String MYNOTICH = "MYNOTICH";
    String MYNOTIAPM = "MYNOTIAPM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "BootReceiver");
        //intent = new Intent(context, SelfNotifBack.class);
        //context.startService(intent);
        prefs = context.getSharedPreferences(OWPREF, Context.MODE_PRIVATE);
        editor = context.getSharedPreferences(OWPREF, Context.MODE_PRIVATE).edit();
        int hr, min;
        String myapm, otherapm;
        PendingIntent pendingIntent;

        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
            /* Setting the alarm here */
        Intent alarmIntent = new Intent(context, AppReceiver.class);


        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        hr = prefs.getInt(MYNOTIHR, -1);
        min = prefs.getInt(MYNOTIMIN, -1);
        myapm = prefs.getString(MYNOTIAPM, "");
        if(hr == 12 && myapm == "pm"){  //if 12pm,
            hr = 12;
        }else if(myapm == "pm" && hr != 12){
            hr += 12;
        }

        if(hr != -1 && min != -1) {
            alarmIntent.setAction("SelfNotification");
            pendingIntent = PendingIntent.getBroadcast(context, 10, alarmIntent, 0);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
            calendar.set(Calendar.MINUTE, Integer.valueOf(min));
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

        }


        hr = prefs.getInt(OTHERSHR, -1);
        min = prefs.getInt(OTHERSMIN, -1);
        otherapm = prefs.getString(OTHERNOTIAPM, "");
        if(hr == 12 && otherapm == "pm"){  //if 12pm,
            hr = 12;
        }else if(otherapm == "pm" && hr != 12){
            hr += 12;
        }

        if(hr != -1 && min != -1) {
            alarmIntent.setAction("OtherNotification");
            pendingIntent = PendingIntent.getBroadcast(context, 21, alarmIntent, 0);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
            calendar.set(Calendar.MINUTE, Integer.valueOf(min));
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

        }
    }
}

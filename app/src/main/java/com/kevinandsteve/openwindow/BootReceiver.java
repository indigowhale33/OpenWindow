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

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "BootReceiver");
        intent = new Intent(context, SelfNotifBack.class);
        context.startService(intent);
        prefs = context.getSharedPreferences(OWPREF, Context.MODE_PRIVATE);
        editor = context.getSharedPreferences(OWPREF, Context.MODE_PRIVATE).edit();
        Integer hr;
        Integer min;
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
            /* Setting the alarm here */
        Intent alarmIntent = new Intent(context, AppReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        hr = prefs.getInt("MYSELFHR", -1);
        min = prefs.getInt("MYSELFMIN", -1);
        if(hr != -1 && min != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
            calendar.set(Calendar.MINUTE, Integer.valueOf(min));
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);


        }
    }
}

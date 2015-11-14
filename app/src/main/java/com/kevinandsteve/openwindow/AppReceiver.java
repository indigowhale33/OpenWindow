package com.kevinandsteve.openwindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Steve on 2015-10-26.
 */
public class AppReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        Intent newintent = new Intent(context, SelfNotifBack.class);
        int a = intent.getIntExtra("requestCode", -1);
        newintent.putExtra("requestCode", intent.getIntExtra("requestCode", -1));
       // intent.putExtra("requestCode",intent.getExtras().getSerializable("requestCode"));
        context.startService(newintent);
//        Intent startIntent = new Intent(context, SelfNotifBack.class);
//        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startIntent);
//        Intent startIntent2 = new Intent(context, ResultActivity.class);
//        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startIntent2);
    }

}

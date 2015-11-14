package com.kevinandsteve.openwindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Steve on 2015-10-26.
 */
public class AppReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {


            //Intent newintent = new Intent(context, SelfNotifBack.class);
        Intent newintent = new Intent(context, SelfNotifBack.class);
            newintent.setAction("NOTIFY");
            newintent.putExtra("requestCode", intent.getIntExtra("requestCode", -1));
            // intent.putExtra("requestCode",intent.getExtras().getSerializable("requestCode"));
            context.startService(newintent);


    }

}

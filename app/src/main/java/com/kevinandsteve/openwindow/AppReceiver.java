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

        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

            Intent newintent = new Intent(context, SelfNotifBack.class);
            newintent.putExtra("requestCode", intent.getIntExtra("requestCode", -1));
            // intent.putExtra("requestCode",intent.getExtras().getSerializable("requestCode"));
            context.startService(newintent);
        }

    }

}

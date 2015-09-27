package com.kevinandsteve.openwindow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);
        Intent extras = getIntent();
        int zipcode = -1;
        if(extras != null){
            zipcode = extras.getIntExtra("EXTRA_ZIPCODE", 0);
        }
        String strzip = Integer.toString(zipcode);
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=" + fDate + "&date=2015-09-27&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";

        Toast toast = Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Toast.LENGTH_SHORT);
        toast.show();

        InputStream input = null;
        OutputStream output = null;
        try {
            URL url = new URL(strurl);
            url.getQuery();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT);
                toast1.show();
            }
            input = connection.getInputStream();
            output = new FileOutputStream("/sdcard/file_name.extension");

            int fileLength = connection.getContentLength();

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button

                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Toast toast = Toast.makeText(getApplicationContext(), Integer.toString(zipcode), Toast.LENGTH_SHORT);
//        toast.show();
//        toast.show();
//        toast.show();


    }

    //public int xmlparse(int zipcode, XmlDom xml, )

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

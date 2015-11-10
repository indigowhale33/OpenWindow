package com.kevinandsteve.openwindow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Steve on 2015-10-30.
 */
public class SelfNotifBack extends Service{
    private PendingIntent pendingIntent;
    public static final String OWPREF = "Owpref" ;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private static String xmlresponse;

    public int onStartCommand(Intent intent, int flags, int startId) {
        //super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(OWPREF, MODE_PRIVATE);
        editor = getSharedPreferences(OWPREF, MODE_PRIVATE).edit();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());    //today's date
        String restore_selfch = prefs.getString("SELFCHECKED", "n"); // to make sure
        String zipcode = prefs.getString("USERZIP", null);   // retrieve zipcode

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(restore_selfch != "n" && zipcode != null){
            String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=" + zipcode + "&date=" + fDate + "&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";

            try {
                xmlresponse = ResultActivity.sendGet(strurl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            String resp = null;
            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlresponse)));
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("ForecastByZip");
                if (nList.getLength() != 0) {
                    for (int zip = 0; zip < nList.getLength(); zip++) {
                        Node nNode = nList.item(zip);
                        Element eElement = (Element) nNode;
                        //Toast.makeText(SelfNotifBack.this, "DateIssue : " + eElement.getElementsByTagName("DateIssue").item(0).getTextContent(), Toast.LENGTH_SHORT).show();
//                        xmlshow.append("DateForecast : " + eElement.getElementsByTagName("DateForecast").item(0).getTextContent() + "\n");
//                        xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent() + "\n");
//                        xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent() + "\n");
//                        xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent() + "\n");
//                        xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent() + "\n");
//                        xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent() + "\n");
//                        xmlshow.append("Actionday : " + eElement.getElementsByTagName("ActionDay").item(0).getTextContent() + "\n");
//                        resp = eElement.getTextContent();
//                        xmlshow.append(resp);
//                        xmlshow.append(Integer.toString(nList.getLength()));
                        Notify("OpenWindow","DateIssue : " + eElement.getElementsByTagName("DateIssue").item(0).getTextContent());
                        Iterator check = prefs.getStringSet("OthersContacts", null).iterator();

                    }
                } else {
                    resp = "\nInvalid Zipcode / Try nearby zipcode";
                    Notify("OpenWindow","Invalid Zipcode. Please Try nearby zipcode");
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        return 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void Notify(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.mipmap.ic_launcher,"New Message", System.currentTimeMillis());
        notification.setLatestEventInfo(SelfNotifBack.this, notificationTitle,notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
    }

    protected void sendSMSMessage(Iterator contacts, String message) {
        String contact="";

        while(contacts.hasNext()){
            contact = ((String)contacts.next()).split(": ")[1];

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


    }
}

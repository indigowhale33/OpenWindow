package com.kevinandsteve.openwindow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ResultActivity extends AppCompatActivity {
    //progress dialog
    private static String xmlresponse;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        EditText xmlshow = (EditText) findViewById(R.id.resulttext);
        button = (Button) findViewById(R.id.button);

        Intent extras = getIntent();
        int zipcode = -1;
        if (extras != null) {
            zipcode = extras.getIntExtra("EXTRA_ZIPCODE", 0);
        }
        String strzip = Integer.toString(zipcode);
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=" + strzip + "&date=" + fDate + "&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";
        //String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=23185&date=2015-10-12&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";
//        Toast toast = Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Toast.LENGTH_SHORT);
//        toast.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            xmlresponse = sendGet(strurl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        xmlshow.setText(xmlresponse);
        //Toast toast2 = Toast.makeText(getApplicationContext(), xmlresponse, Toast.LENGTH_SHORT);
        //toast2.show();
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
                    xmlshow.append("DateIssue : " + eElement.getElementsByTagName("DateIssue").item(0).getTextContent() + "\n");
                    xmlshow.append("DateForecast : " + eElement.getElementsByTagName("DateForecast").item(0).getTextContent() + "\n");
                    xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent() + "\n");
                    xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent() + "\n");
                    xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent() + "\n");
                    xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent() + "\n");
                    xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent() + "\n");
                    xmlshow.append("Actionday : " + eElement.getElementsByTagName("ActionDay").item(0).getTextContent() + "\n");
                    resp = eElement.getTextContent();
                    xmlshow.append(resp);
                    xmlshow.append(Integer.toString(nList.getLength()));
                }
            } else {
                resp = "\nInvalid Zipcode / Try nearby zipcode";
                xmlshow.append(resp);
                button.setVisibility(View.INVISIBLE);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(getBaseContext(),
                        NotificationMain.class);
                startActivity(myIntent);
            }
        });




    }


    public static String sendGet(String strurl) throws Exception {
        String USER_AGENT = "Mozilla/5.0";
        String url = strurl;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
//        Toast toast = Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT);
//        toast.show();

        return response.toString();
    }





}
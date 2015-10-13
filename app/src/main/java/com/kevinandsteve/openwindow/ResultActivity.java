package com.kevinandsteve.openwindow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    //progress dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static String xmlresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);
        EditText xmlshow = (EditText) findViewById(R.id.resulttext);

        Intent extras = getIntent();
        int zipcode = -1;
        if(extras != null){
            zipcode = extras.getIntExtra("EXTRA_ZIPCODE", 0);
        }
        String strzip = Integer.toString(zipcode);
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=" + strzip + "&date=" + fDate + "&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";
        String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=23185&date=2015-10-12&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";
        Toast toast = Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Toast.LENGTH_SHORT);
        toast.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new ProgressTask(strurl).execute();

//        try {
//            xmlresponse = sendGet(strurl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        xmlshow.setText(xmlresponse);
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = null;
//        try {
//            builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(xmlresponse)));
//            doc.getDocumentElement().normalize();
//            NodeList nList = doc.getElementsByTagName("ForecastByZip");
//
//            for (int zip = 0; zip < nList.getLength(); zip++) {
//                Node nNode = nList.item(zip);
//                    Element eElement = (Element) nNode;
//                    xmlshow.append("DateIssue : " + eElement.getElementsByTagName("DateIssue").item(0).getTextContent()+"\n");
//                    xmlshow.append("DateForecast : " + eElement.getElementsByTagName("DateForecast").item(0).getTextContent()+"\n");
//                    xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent()+"\n");
//                    xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent()+"\n");
//                    xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent()+"\n");
//                    xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent()+"\n");
//                    xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent()+"\n");
//                    xmlshow.append("Actionday : " + eElement.getElementsByTagName("ActionDay").item(0).getTextContent()+"\n");
//                    //String resp = eElement.getTextContent();
//                    //xmlshow.setText(resp);
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    private String sendGet(String strurl) throws Exception {
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
        Toast toast = Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT);
        toast.show();

        return response.toString();
    }



    class ProgressTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String strurl;
        String xmlresponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ResultActivity.this);
            pd.setMessage("loading");
            pd.show();
        }

        public ProgressTask(String strurl) {
            super();
            this.strurl = strurl;
            // do stuff
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                this.xmlresponse = sendGet(strurl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null)
            {
                pd.dismiss();
            }
        }
    }



}

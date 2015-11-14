package com.kevinandsteve.openwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.view.WindowManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ResultActivity extends AppCompatActivity {
    //progress dialog
    private static String xmlresponse;
    Button button;
    Handler handler = new Handler();
    TextView aqitxt;
    TextView thezip;
    TextView resultrate;
    TextView resultratetxt;
    TextView aqicircle;
    EditText newzip;
    String aqi = "";
    int aqilvl = 0;
    int aqival = 0;
    String[] rrarray = {"Good","Moderate", "Unhealthy for Sensitive", "Unhealthy", "Very Unhealthy", "Hazardous"};
    String[] rrtarray = {"Air quality is considered satisfactory, and air pollution poses little or no risk. Breathe free!",
            "Air quality is acceptable; however, for some pollutants there may be a moderate health concern for a very small number of people. For example, people who are unusually sensitive to ozone may experience respiratory symptoms.",
            "Although general public is not likely to be affected at this AQI range, people with lung disease, older adults and children are at a greater risk from exposure to ozone, whereas persons with heart and lung disease, older adults and children are at greater risk from the presence of particles in the air.",
            "Everyone may begin to experience some adverse health effects, and members of the sensitive groups may experience more serious effects.",
            "Health warnings of emergency conditions. The entire population is more likely to be affected.",
            "Health alert: everyone may experience more serious health effects"};
    private final int colGreen = 0xFF6BEA36;
    private final int colYellow = 0xFFFCF45E;
    private final int colOrange = 0xFFE29047;
    private final int colRed = 0xFFD65234;
    private final int colPurple = 0xFFB73D8B;
    private final int colMaroon = 0xFF5E212B;
    Thread thread;
    int[] colarray = {colGreen,colYellow,colOrange,colRed,colPurple,colMaroon};
    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        final EditText xmlshow = (EditText) findViewById(R.id.resulttext);
        button = (Button) findViewById(R.id.button);
        thezip = (TextView) findViewById(R.id.zipresult);
        aqitxt = (TextView) findViewById(R.id.theaqitxt);
        aqicircle = (TextView) findViewById(R.id.aqicircle);
        resultrate = (TextView) findViewById(R.id.resultrating);
        resultratetxt = (TextView) findViewById(R.id.resultratingtext);
        newzip = (EditText) findViewById(R.id.zipsearch);
        Intent extras = getIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(newzip.getWindowToken(), 0);
        int zipcode = -1;
        if (extras != null) {
            zipcode = extras.getIntExtra("EXTRA_ZIPCODE", 0);
            xmlresponse = extras.getStringExtra("XML");
        }
        String strzip = Integer.toString(zipcode);
        thezip.setText(strzip);
        //String fDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        final String strurl = "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/xml&zipCode=" + strzip + "&distance=120&API_KEY=4D57B8FF-A70B-44EE-948E-B4100173F3FA";


        //String strurl = "http://www.airnowapi.org/aq/forecast/zipCode/?format=application/xml&zipCode=23185&date=2015-10-12&distance=20&API_KEY=89073F7F-3417-4795-B2CE-D9CF7FA83279";
//        Toast toast = Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), Toast.LENGTH_SHORT);
//        toast.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        thread = new Thread(){
            @Override
            public void run(){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            xmlresponse = sendGet(strurl);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //xmlshow.setText(xmlresponse);
                        //Toast toast2 = Toast.makeText(getApplicationContext(), xmlresponse, Toast.LENGTH_SHORT);
                        //toast2.show();
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = null;
                        String resp = null;
                        try {
                            builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(new InputSource(new StringReader(xmlresponse)));
                            doc.getDocumentElement().normalize();
                            NodeList nList = doc.getElementsByTagName("ObsByZip");
                            if (nList.getLength() != 0) {
                                for (int zip = 0; zip < nList.getLength(); zip++) {
                                    Node nNode = nList.item(zip);
                                    Element eElement = (Element) nNode;
                                    //xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent() + "\n");
                                    //xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent() + "\n");
                                    //xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent() + "\n");
                                    //xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent() + "\n");
                                    //xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent() + "\n");
                                    resp = eElement.getTextContent();
                                    //xmlshow.append(resp);
                                    //xmlshow.append(Integer.toString(nList.getLength()));
                                    aqi = eElement.getElementsByTagName("AQI").item(0).getTextContent();
                                    aqival = Integer.parseInt(aqi);

                                }
                                aqicircle.setText(aqi);
                                aqitxt.setText("The current AQI for");
                                newzip.setHint("Search Zipcode");
                                aqicircle.setVisibility(View.VISIBLE);
                                resultrate.setVisibility(View.VISIBLE);
                                resultratetxt.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                                if(aqival > 300)
                                    aqilvl = 5;
                                else if(aqival > 200)
                                    aqilvl = 4;
                                else if(aqival > 150)
                                    aqilvl = 3;
                                else if(aqival > 100)
                                    aqilvl = 2;
                                else if(aqival > 50)
                                    aqilvl = 1;
                                else
                                    aqilvl = 0;

                                resultrate.setText(rrarray[aqilvl]);
                                thezip.setVisibility(View.VISIBLE);
                                aqicircle.setTextColor(colarray[aqilvl]);
                                resultrate.setTextColor(colarray[aqilvl]);
                                resultratetxt.setText(rrtarray[aqilvl]);
                            } else {
                                resp = "\nInvalid Zipcode :/ Try nearby zipcode";
                                aqitxt.setText("Invalid zipcode :/ Try a nearby zipcode");
                                newzip.setHint("New zip?");
                                aqicircle.setVisibility(View.INVISIBLE);
                                thezip.setVisibility(View.INVISIBLE);
                                resultrate.setVisibility(View.INVISIBLE);
                                resultratetxt.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                            }
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                //code to do the HTTP request
            }
        };
        thread.start();

        newzip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    newzip.clearFocus();
                    hide_keyboard(ResultActivity.this);
                    final String newurl = "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/xml&zipCode=" + newzip.getText().toString() + "&distance=120&API_KEY=4D57B8FF-A70B-44EE-948E-B4100173F3FA";
                    if(thread.isAlive()){
                        thread.interrupt();
                    }
                    thread = new Thread(){
                        @Override
                        public void run(){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        xmlresponse = sendGet(newurl);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //xmlshow.setText(xmlresponse);
                                    //Toast toast2 = Toast.makeText(getApplicationContext(), xmlresponse, Toast.LENGTH_SHORT);
                                    //toast2.show();
                                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder builder = null;
                                    String resp = null;
                                    try {
                                        builder = factory.newDocumentBuilder();
                                        Document doc = builder.parse(new InputSource(new StringReader(xmlresponse)));
                                        doc.getDocumentElement().normalize();
                                        NodeList nList = doc.getElementsByTagName("ObsByZip");
                                        if (nList.getLength() != 0) {
                                            for (int zip = 0; zip < nList.getLength(); zip++) {
                                                Node nNode = nList.item(zip);
                                                Element eElement = (Element) nNode;
                                                //xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent() + "\n");
                                                //xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent() + "\n");
                                                //xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent() + "\n");
                                                //xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent() + "\n");
                                                //xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent() + "\n");
                                                resp = eElement.getTextContent();
                                                //xmlshow.append(resp);
                                                //xmlshow.append(Integer.toString(nList.getLength()));
                                                aqi = eElement.getElementsByTagName("AQI").item(0).getTextContent();
                                                aqival = Integer.parseInt(aqi);

                                            }
                                            aqicircle.setText(aqi);
                                            aqicircle.setText(aqi);
                                            aqitxt.setText("The current AQI for");
                                            newzip.setHint("Search Zipcode");
                                            aqicircle.setVisibility(View.VISIBLE);
                                            thezip.setVisibility(View.VISIBLE);
                                            resultrate.setVisibility(View.VISIBLE);
                                            resultratetxt.setVisibility(View.VISIBLE);
                                            button.setVisibility(View.VISIBLE);
                                            if(aqival > 300)
                                                aqilvl = 5;
                                            else if(aqival > 200)
                                                aqilvl = 4;
                                            else if(aqival > 150)
                                                aqilvl = 3;
                                            else if(aqival > 100)
                                                aqilvl = 2;
                                            else if(aqival > 50)
                                                aqilvl = 1;
                                            else
                                                aqilvl = 0;
                                            thezip.setText(newzip.getText().toString());
                                            resultrate.setText(rrarray[aqilvl]);
                                            aqicircle.setTextColor(colarray[aqilvl]);
                                            resultrate.setTextColor(colarray[aqilvl]);
                                            resultratetxt.setText(rrtarray[aqilvl]);
                                        } else {
                                            resp = "\nInvalid Zipcode / Try nearby zipcode";
                                            //xmlshow.append(resp);
                                            aqitxt.setText("Invalid zipcode :/ Try a nearby zipcode");
                                            newzip.setHint("New zip?");
                                            aqicircle.setVisibility(View.INVISIBLE);
                                            thezip.setVisibility(View.INVISIBLE);
                                            resultrate.setVisibility(View.INVISIBLE);
                                            resultratetxt.setVisibility(View.INVISIBLE);
                                            button.setVisibility(View.INVISIBLE);
                                            button.setVisibility(View.INVISIBLE);
                                        }
                                    } catch (ParserConfigurationException e) {
                                        e.printStackTrace();
                                    } catch (SAXException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                            //code to do the HTTP request
                        }
                    };
                    thread.start();


                }
                return handled;
            }
        });


//        Exception e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        xmlshow.setText(xmlresponse);
//        //Toast toast2 = Toast.makeText(getApplicationContext(), xmlresponse, Toast.LENGTH_SHORT);
//        //toast2.show();
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = null;
//        String resp = null;
//        try {
//            builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(xmlresponse)));
//            doc.getDocumentElement().normalize();
//            NodeList nList = doc.getElementsByTagName("ForecastByZip");
//            if (nList.getLength() != 0) {
//                for (int zip = 0; zip < nList.getLength(); zip++) {
//                    Node nNode = nList.item(zip);
//                    Element eElement = (Element) nNode;
//                    xmlshow.append("DateIssue : " + eElement.getElementsByTagName("DateIssue").item(0).getTextContent() + "\n");
//                    xmlshow.append("DateForecast : " + eElement.getElementsByTagName("DateForecast").item(0).getTextContent() + "\n");
//                    xmlshow.append("Reporting Area : " + eElement.getElementsByTagName("ReportingArea").item(0).getTextContent() + "\n");
//                    xmlshow.append("Parameter Name : " + eElement.getElementsByTagName("ParameterName").item(0).getTextContent() + "\n");
//                    xmlshow.append("AQI : " + eElement.getElementsByTagName("AQI").item(0).getTextContent() + "\n");
//                    xmlshow.append("CategoryNumber : " + eElement.getElementsByTagName("CategoryNumber").item(0).getTextContent() + "\n");
//                    xmlshow.append("CategoryName : " + eElement.getElementsByTagName("CategoryName").item(0).getTextContent() + "\n");
//                    xmlshow.append("Actionday : " + eElement.getElementsByTagName("ActionDay").item(0).getTextContent() + "\n");
//                    resp = eElement.getTextContent();
//                    xmlshow.append(resp);
//                    xmlshow.append(Integer.toString(nList.getLength()));
//                }
//            } else {
//                resp = "\nInvalid Zipcode / Try nearby zipcode";
//                xmlshow.append(resp);
//                button.setVisibility(View.INVISIBLE);
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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
        //String USER_AGENT = "Mozilla/5.0";
        String url = strurl;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;

        try{
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer result = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }
                in.close();
                return result.toString();
            }
        }catch(Exception e){

        }


        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();


        //return response.toString();
        return "";
    }





}
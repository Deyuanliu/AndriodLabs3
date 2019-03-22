package com.example.andriodlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    private TextView uvvalue;
    private TextView min;
    private TextView max;
    private TextView ct;
    private ImageView imageview;
    private ProgressBar progressBar;
    String tvalue;
    String tmin;
    String tmax;
    String iconName;
    double uv;
    Bitmap image = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar

        uvvalue = (TextView) findViewById(R.id.UV_rating);
        min = (TextView) findViewById(R.id.min_temperature);
        max = (TextView) findViewById(R.id.max_temperature);
        ct = (TextView) findViewById(R.id.current_temperature);
        imageview = (ImageView) findViewById(R.id.imageView);


        ForecastQuery query = new ForecastQuery();
        query.execute();

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                //get the string url:
                // Strg myUrl = params[0];

                //create the network connection:
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");  //inStream comes from line 46   protected String doInBackground(String... params) {
//now loop over the XML:
                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("temperature")) {
                            tvalue = xpp.getAttributeValue(null, "value");
                            publishProgress(25); //tell android to call onProgressUpdate with 1 as parameter
                            Thread.sleep(500);
                            tmin = xpp.getAttributeValue(null, "min");
                            publishProgress(50); //tell android to call onProgressUpdate with 1 as parameter
                            Thread.sleep(500);
                            tmax = xpp.getAttributeValue(null, "max");
                            publishProgress(75); //tell android to call onProgressUpdate with 1 as parameter
                            Thread.sleep(500);

                            Log.e("Weather Forcast", "Found parameter message: " + tvalue + tmin + tmax);

                        } else if (tagName.equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                            Log.e("Weather Forcast", "Found parameter message: " + iconName);

                            publishProgress(80);
                            Thread.sleep(500);

                        }
                    }
                    xpp.next();
                }
                if (fileExistance(iconName + ".png")) {
                    FileInputStream fis = null;
                    Log.i("Weather Forcast","piture  found" + iconName);
                    try {
                        fis = openFileInput(iconName + ".png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);

                } else {
                    Log.i("Weather Forcast","piture not found" + iconName);
                    URL uvurl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) uvurl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());

                    }

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.e("Weather Forcast", "Found the image");
                    publishProgress(100);
                }
//create the network connection:
                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                uv = jObject.getDouble("value");
                Log.i("UV is:", "" + uv);


            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }


            return "finished task";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("WeatherForcast", "update:" + values[0]);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27

            // messageBox.setText("Finished all tasks");
            uvvalue.setText("UV value:" + uv);
            min.setText("Min Temperature:" + tmin);
            max.setText("Max Temperature:" + tmax);
            ct.setText("Current Temperature:" + tvalue);
            imageview.setImageBitmap(image);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}












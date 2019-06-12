package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.quakereport.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {


    public static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final String LOG_TAG =QueryUtils.class.getSimpleName() ;


    private QueryUtils() {

    }

    public static List<Earthquake> fetchEarthquakeData(String stringUrl){
        URL url=createUrl(stringUrl);
        String jsonResponse="";
        try {
            jsonResponse=makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Earthquake> earthquakes=extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl){
        URL url=null;
        try {
            url=new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try {
           httpURLConnection =(HttpURLConnection) url.openConnection();
           httpURLConnection.setReadTimeout(10000);
           httpURLConnection.setConnectTimeout(15000);
           httpURLConnection.setRequestMethod("GET");
           httpURLConnection.connect();

           if (httpURLConnection.getResponseCode()==200){
               inputStream=httpURLConnection.getInputStream();
               jsonResponse= readFromStream(inputStream);
           }else {
               Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
           }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection!=null)
                httpURLConnection.disconnect();
        }if (inputStream!=null){
            inputStream.close();
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }return output.toString();
    }


    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();
  try {

      JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                JSONObject properties = currentEarthquake.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");


                String location = properties.getString("place");


                long time = properties.getLong("time");


                String url = properties.getString("url");


                Earthquake earthquake = new Earthquake(magnitude, location, time, url);


                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }
}
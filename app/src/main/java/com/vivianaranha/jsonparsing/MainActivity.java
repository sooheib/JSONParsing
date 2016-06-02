package com.vivianaranha.jsonparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20zip%3D%2794085%27%20and%20query%3D%27pizza%27&format=json&callback=";

    ListView listView;
    ArrayList<String> pizzalocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        pizzalocations = new ArrayList<String>();

        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute();
    }

    public class DownloadJSON extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {


            try {
                URL theUrl = new URL(SERVER_URL);
                BufferedReader reader = new BufferedReader(

                        new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                Log.d("JSON", jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);

                JSONObject queryObj = jsonObject.getJSONObject("query");
                JSONObject resultsObj = queryObj.getJSONObject("results");
                JSONArray resultArray = resultsObj.getJSONArray("Result");

                for(int i=0;i<resultArray.length(); i++){

                    JSONObject theObject = resultArray.getJSONObject(i);

                    Log.d("JSON", theObject.getString("Title"));
                    pizzalocations.add(theObject.getString("Title"));
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter adapter = new ArrayAdapter(
                    MainActivity.this, android.R.layout.simple_list_item_1, pizzalocations);
            listView.setAdapter(adapter);

        }
    }
}

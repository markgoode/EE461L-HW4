package com.example.marek.homework4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyB-O_nlgZHWoMlyIyvhOA86nj0j-UgI-Xo";
    public static TextView display;
    public static EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button locationButton = (Button) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetLocation().execute();
            }
        });
    }

    private class GetLocation extends AsyncTask<Void, Void, String>{
        protected String doInBackground(Void... urls){
            try{
                input = (EditText) findViewById(R.id.locationText);
                String location = input.getText().toString();
                URL url = new URL("GoogleGeoAPIURL..." + location + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            }catch(Exception e){
                return null;
            }
        }

        protected void onPostExecute(String result){

            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                //Take the data from the JSON object and display it
            }
            catch(Exception e){
            }
        }
    }
}

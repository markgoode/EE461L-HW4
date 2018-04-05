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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String API_KEY = "AIzaSyB-O_nlgZHWoMlyIyvhOA86nj0j-UgI-Xo";

    public static EditText input;
    public GoogleMap map;

    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button locationButton = (Button) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetLocation().execute();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng location = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(location).title("Location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
    private class GetLocation extends AsyncTask<Void, Void, JSONObject>{
        protected JSONObject doInBackground(Void... urls){
            try{
                input = (EditText) findViewById(R.id.locationText);
                String location = input.getText().toString();
                String [] locs = location.split(" ");

                String googleURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

                for(int i = 0; i<locs.length-1; i++){
                    googleURL += locs[i];
                    googleURL += '+';
                }
                googleURL += locs[locs.length-1];
                googleURL += "&key=";
                googleURL += API_KEY;

                URL url = new URL(googleURL);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    bufferedReader.close();
                    String myString = stringBuilder.toString();

                    JSONObject json = new JSONObject(myString);

                    return json;
                } finally {
                    urlConnection.disconnect();
                }
            }catch(Exception e){
                return null;
            }
        }

        protected void onPostExecute(JSONObject json){

            try {
                JSONObject myJson = json.getJSONObject("geometry");
                myJson = myJson.getJSONObject("location");
                lat = myJson.getDouble("lat");
                lng = myJson.getDouble("lng");
            }
            catch(Exception e){
            }
        }
    }
}

package com.example.myfirstmapsapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private JSONArray jsonArray;

    private ArrayList<JSONObject> earthquakeObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadJSON();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        try {

            double eLat = 0.0;
            double eLong = 0.0;
            double eMag = 0.0;
            String eRegion = "";
            String eDate = "";

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject earthquake = jsonArray.getJSONObject(i);

                eLat = earthquake.getDouble("Lat");
                eLong = earthquake.getDouble("Lon");
                eRegion = earthquake.getString("Region");
                eDate = earthquake.getString("Month") +
                        "/" + earthquake.getString("Day") +
                        "/" + earthquake.getString("Year");


                LatLng location = new LatLng(eLat, eLong);
                eMag = earthquake.getDouble("Mag");

                //The hue is a value between 0 and 360,

                float markerColor;

                if(eMag < 8.0f ){
                    markerColor = 180.0f;

                } else if (eMag >=8.0f && eMag < 8.5f){
                    markerColor = 100.0f;

                }else if(eMag >= 8.5f && eMag < 9.0f){

                    markerColor = 50.0f;
                } else {

                    markerColor = 30.0f;
                }

                mMap.addMarker(new MarkerOptions().position(location)
                        .title(eRegion+ "\n" + "Lat: " + eLat + ", Lon: " + eLong).snippet(eDate +" Mag: " + eMag)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))

                );

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadJSON(){

        InputStream inputStream = getResources().openRawResource(R.raw.earthquakes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int counter;
        try {
            counter = inputStream.read();
            while (counter != -1) {
                byteArrayOutputStream.write(counter);
                counter = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            jsonArray = jObject.getJSONArray("earthquakes");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

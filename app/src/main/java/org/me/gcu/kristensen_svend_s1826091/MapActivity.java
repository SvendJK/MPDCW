package org.me.gcu.kristensen_svend_s1826091;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle extras = getIntent().getExtras();
        String georss = extras.getString("georss");
        String titletxt = extras.getString("title");
        mMap = googleMap;
        double[] MapPos = LatAndLongChopper(georss);
        LatLng mapPos = new LatLng(MapPos[0], MapPos[1]);
        mMap.addMarker(new MarkerOptions().position(mapPos).title(titletxt).visible(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapPos, 15));
    }
    public double[] LatAndLongChopper(String LatAndLong) {
        String splitString[] = LatAndLong.split(" ");
        double[] result = {Double.parseDouble(splitString[0]), Double.parseDouble(splitString[1])};
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView title = findViewById(R.id.titleTxtMap);
        TextView description = findViewById(R.id.descriptionTxtMap);
        TextView link = findViewById(R.id.linkTxtMap);
        TextView pubDate = findViewById(R.id.pubDateTxtMap);
        TextView startDate = findViewById(R.id.startDateTxtMap);
        TextView endDate = findViewById(R.id.endDateTxtMap);

        Bundle extras = getIntent().getExtras();
        String titleTxt = extras.getString("title"); //unpack and assigning the variables sent over from main activity
        String descTxt = extras.getString("description");
        String startDateTxt = extras.getString("startDate");
        String endDateTxt = extras.getString("endDate");
        String linkTxt = extras.getString("link");
        String pubDateTxt = extras.getString("pubDate");

        title.setText(titleTxt);
        description.setText(descTxt);
        startDate.setText(startDateTxt);
        endDate.setText(endDateTxt);
        link.setText(linkTxt);
        pubDate.setText(pubDateTxt);

    }
}


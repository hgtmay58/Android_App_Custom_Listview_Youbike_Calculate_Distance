package com.example.myapplicationcustlist_youbike;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    Location location,nowlocation;
    double mLatitude = 0;
    double mLongitude = 0;
    String provider;
    MainActivity mactivity;
    ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> bList;
    static final String URL = "https://data.ntpc.gov.tw/api/datasets/71CD1490-A2DF-4198-BEF1-318479775E8A/json/preview";
    // parent node
    static final String KEY_ID = "geocode";
    static final String KEY_TITLE = "locationName";
    static final String KEY_lat = "lat";
    static final String KEY_lon = "lon";
    static final String KEY_ARTIST = "value";
    static final String KEY_DURATION = "description";
    static final String KEY_THUMB_URL = "thumb_url";
    static final String KEY_date = "dataTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mactivity=this;
        list=(ListView)findViewById(R.id.list);

        //
        //
        if (ActivityCompat.checkSelfPermission(mactivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(mactivity, new String[]{ACCESS_FINE_LOCATION}, 12);

        }
        else
            initlocation();

        //

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });
    }
//
//
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
{
    if(requestCode==12)
    {
        if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            initlocation();
        }
        else
            return;
    }
}
    //
//
void initlocation() {
    try {
        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);
        // Enabling  get My Location
        // Getting Current Location From GPS
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 30000, 0, mactivity);
        onLocationChanged(location);
    }
    catch(SecurityException e)
    {
        ActivityCompat.requestPermissions(mactivity, new String[]{ACCESS_FINE_LOCATION}, 12);
    }

}
    //
//
    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            nowlocation=location;
            Log.d(" XY ", ""+mLatitude+","+mLongitude );
            bList = new ArrayList<HashMap<String, String>>();
            JsonParser_yb parser = new JsonParser_yb(this,nowlocation);
            parser.getjsonFromUrl(URL,nowlocation); // getting XML from URL
        }
        else
        {
            Log.d(" XY ", "null" );
            bList = new ArrayList<HashMap<String, String>>();
            JsonParser_yb parser = new JsonParser_yb(this,location);
            parser.getjsonFromUrl(URL,nowlocation); // getting XML from URL
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    //
    //

}

package aplication.app.josafat.com.gps;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class MainActivity extends Activity implements LocationListener {

    LocationManager lm;

    //Defining Latitude & Longitude
    double lat = 21.0476007, long1 = -89.64647788;
    //Defining Radius
    float radius = 1000;

    //Intent Action
    String ACTION_FILTER = "com.example.proximityalert";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //i'm registering my Receiver First
        registerReceiver(new ProximityReciever(), new IntentFilter(ACTION_FILTER));

        //i'm calling ther service Location Manager
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        //for debugging...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        }


        //Setting up My Broadcast Intent
        Intent i= new Intent(ACTION_FILTER);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, i, 0);

        //setting up proximituMethod
        lm.addProximityAlert(lat, long1, radius, -1, pi);

    }

    @Override
//just For debugging to See the distance between my actual position and the aproximit point
    public void onLocationChanged(Location newLocation) {

        Location old = new Location("OLD");
        old.setLatitude(lat);
        old.setLongitude(long1);

        double distance = newLocation.distanceTo(old);

        Log.i("MyTag", "Distance: " + distance);
    }

    @Override
    public void onProviderDisabled(String arg0) {}

    @Override
    public void onProviderEnabled(String arg0) {}

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}

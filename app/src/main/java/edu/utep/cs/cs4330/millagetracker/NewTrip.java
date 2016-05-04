package edu.utep.cs.cs4330.millagetracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class NewTrip extends DataBaseActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    private Button save;
    private RadioButton gpsOn;
    private RadioButton gpsOff;
    private EditText date;
    private EditText  origin;
    private EditText destination;
    private EditText begin;
    private EditText ending;
    private TextView total;
    private boolean isGPS = false;
    private String sDate;
    private String sOrigin;
    private String sDestination;
    private int iBegin;
    private int iEnd;
    private int iTotal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyFields())
                    saveToDB();
            }
        });
        gpsOn = (RadioButton) findViewById(R.id.radioOn);
        gpsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        gpsOff = (RadioButton) findViewById(R.id.radioOff);
        gpsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        date = (EditText) findViewById(R.id.date);
        origin =  (EditText) findViewById(R.id.editOrigin);
        destination = (EditText) findViewById(R.id.editDestination);
        begin = (EditText) findViewById(R.id.editBegining);
        ending = (EditText) findViewById(R.id.editEnding);
        total = (TextView) findViewById(R.id.total);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyFields()){
                    saveToDB();
                    int end = Integer.parseInt(ending.getText().toString());
                    int beg = Integer.parseInt(begin.getText().toString());
                    int tot = end - beg;
                    total.setText("" + tot);
                }
            }
        });


    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioOn:
                if (checked) {
                    isGPS=true;
                    getLocation();
                }
                break;
            case R.id.radioOff:
                if (checked) {
                    isGPS=false;
                }
                break;
        }
        getLocation();
    }

    public void saveToDB(){
        try{
            mileDatabase = this.openOrCreateDatabase("Miles", MODE_PRIVATE, null);
            mileDatabase.execSQL("INSERT INTO trip (date, origin, destination, begin, end) VALUES ( '" + sDate + "', '"+ sOrigin + "', '"+ sDestination + "', " + iBegin + ", " + iEnd + ")");
            Log.i("Query", "INSERT INTO trip (date, origin, destination, begin, end) VALUES ( '" + sDate + "', '"+ sOrigin + "', '"+ sDestination + "', " + iBegin + ", " + iEnd + ")");
            mileDatabase.close();
        }
        catch (Exception e){

        }

    }
    public boolean verifyFields(){
        int fields=0;
        if(date.getText().toString().length()>0){
            Log.i("NewTrip", date.getText().toString());
            sDate = date.getText().toString();
            fields++;
        }
        if(origin.getText().toString().length()>0){
            Log.i("NewTrip", origin.getText().toString());
            sOrigin = origin.getText().toString();
            fields++;
        }
        if(destination.getText().toString().length()>0){
            Log.i("NewTrip", destination.getText().toString());
            sDestination = destination.getText().toString();
            fields++;
        }
        if(begin.getText().toString().length()>0){
            Log.i("NewTrip", begin.getText().toString());
            iBegin = Integer.parseInt(begin.getText().toString());
            fields++;
        }
        if(ending.getText().toString().length()>0){
            Log.i("NewTrip", ending.getText().toString());
            iEnd = Integer.parseInt(ending.getText().toString());
            fields++;
        }
        Log.i("Fields", "" + fields);
        return (fields==5) ? true : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onResume(){
        super.onResume();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //provider = locationManager.NETWORK_PROVIDER;
                    //location = locationManager.getLastKnownLocation(provider);
                    Log.i("info","need to ask permission since it is note accepted in the first time");
                    // need to ask permission since it is note accepted in the first time
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    // after first installation this triggers so location should be called from onRequestPermissionsResult method
                    Log.i("info", "if statement working, permission yet not granted");
                }

                return;
            } else {
                if(isGPS)
                    locationManager.requestLocationUpdates(provider, 400, 1, this);

            }
        } catch (Exception e) {
            Log.i("Exception", "Exception on resume");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        //Calculate new mile
        Log.i("Loc: Latitude", lat.toString() + ", " + lng.toString());
        Log.i("Loc: Longitude", lng.toString());


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                //provider = locationManager.NETWORK_PROVIDER;
                //location = locationManager.getLastKnownLocation(provider);
                Log.i("info","need to ask permission since it is note accepted in the first time");
                // this part triggers to ask permission when access is not granted in the first time user runs the app (this triggers in the second run)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                // after first installation this part triggers so getLocation() should be called from onRequestPermissionsResult method
                Log.i("info", "if statement working, permission yet not granted");
            }

            return;
        } else {

            Location nlocation = locationManager.getLastKnownLocation(provider);

            if (nlocation != null) {

                Log.i("Location info", "Location achieved!");
                onLocationChanged(nlocation);

            } else {

                Log.i("Location info", "No location :(");

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // grantResults[0] = -1
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("INFO", "ACCESS GRANTED");
                    //getLocation(); this will crash due to a bug in android system 6.0.x but once this bug is solved you can call your method without restarting your app
                } else {
                    Log.i("INFO", "ACCESS DENIED");
                }
                return;
            }
        }

    }

}

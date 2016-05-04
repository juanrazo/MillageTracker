package edu.utep.cs.cs4330.millagetracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by juanrazo on 5/3/16.
 */
public class DataBaseActivity extends AppCompatActivity {

    protected static SQLiteDatabase mileDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            SQLiteDatabase mileDatabase = this.openOrCreateDatabase("Miles", MODE_PRIVATE, null);
            mileDatabase.execSQL("CREATE TABLE IF NOT EXISTS trip (id INTEGER PRIMARY KEY, date VARCHAR, origin VARCHAR, destination VARCHAR, begin INT(4), end INT(4))");

        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.trips) {
            mileDatabase.close();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Log.i("Menu", " Trips");
            return true;
        }

//        if (id == R.id.action_settings) {
//            mileDatabase.close();
//            Log.i("Menu", " Settings");
//            return true;
//        }

        if (id == R.id.newtrips){
            mileDatabase.close();
            Intent intent = new Intent(getApplicationContext(), NewTrip.class);
            startActivity(intent);
            Log.i("Menu", "New Trip");
            return true;
        }

        if (id == R.id.reports){
            mileDatabase.close();
            Intent intent = new Intent(getApplicationContext(), ExportReport.class);
            startActivity(intent);
            Log.i("Menu", "Reports");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

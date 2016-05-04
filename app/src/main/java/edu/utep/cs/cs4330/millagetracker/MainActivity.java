package edu.utep.cs.cs4330.millagetracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends DataBaseActivity {
    ArrayList<String> trips = new ArrayList<String>();
    ArrayList<String> uID = new ArrayList<String>();
    ListView tripListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tripListView = (ListView) findViewById(R.id.tripListView);
        loadList();


    }

    @Override
    public void onResume(){
        super.onResume();
        loadList();
    }

    private void loadList(){
        try{
            mileDatabase = this.openOrCreateDatabase("Miles", MODE_PRIVATE, null);


            Cursor cursor = mileDatabase.rawQuery("SELECT * FROM trip", null);

            int idIndex =  cursor.getColumnIndex("id");
            int dateIndex =  cursor.getColumnIndex("date");
            int beginIndex = cursor.getColumnIndex("begin");
            int endIndex = cursor.getColumnIndex("end");

            cursor.moveToFirst();

            while(cursor != null){
                trips.add("Date: "+ cursor.getString(dateIndex) + "            Total: "+ (cursor.getInt(endIndex)- cursor.getInt(beginIndex)) + " \nBegining Mile: " + cursor.getInt(beginIndex) + "\nEnding Mile: " + cursor.getInt(endIndex));
                uID.add(Integer.toString(cursor.getInt(idIndex)));
                cursor.moveToNext();
            }
            mileDatabase.close();

        }
        catch (Exception e){
            e.printStackTrace();

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, trips);

        tripListView.setAdapter(arrayAdapter);
        tripListView.setTextFilterEnabled(true);

        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Trip Selected", uID.get(position));
                Intent myIntent = new Intent(view.getContext(), EditTrip.class); // when a row is tapped, load SubView.class
                myIntent.putExtra("id", uID.get(position));
                startActivity(myIntent); // display SubView.class
            }
        });
    }
}

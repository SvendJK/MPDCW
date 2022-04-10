package org.me.gcu.kristensen_svend_s1826091;
//NAME SVEND KRISTENSEN
//STUDENTID : S1820691
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TrafficScotlandAdapter mAdapter;
    private ListView TrafficScotlandList;
    private EditText editTextRoad;
    private EditText editTextDate;
    private String fileInUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SvendCW", "OnCreate()");
        setContentView(R.layout.activity_main);

        //Get reference to our ListView
        TrafficScotlandList = (ListView)findViewById(R.id.TSList);

        //Set the click listener to launch the map when a row is clicked.
        TrafficScotlandList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {

                String georssData = mAdapter.getItem(pos).getGeorss();
                String titleData = mAdapter.getItem(pos).getTitle();
                String description = mAdapter.getItem(pos).getDescription();
                String link = mAdapter.getItem(pos).getLink();
                String pubDate = mAdapter.getItem(pos).getPubDate();
                String startDate = mAdapter.getItem(pos).getStartDate();
                String endDate = mAdapter.getItem(pos).getEndDate();

                Log.v("onClick", "georss is = " + georssData);
                Log.v("onClick", "title is = " + titleData);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("georss", georssData); // package and send our variables to be used on the mapActivity screen
                intent.putExtra("title", titleData);
                intent.putExtra("description", description);
                intent.putExtra("link", link);
                intent.putExtra("pubDate", pubDate);
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);


                startActivity(intent);

            }

        });

        editTextRoad = findViewById(R.id.text_input);
        editTextRoad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParserSearch.getFromFile(MainActivity.this, editTextRoad.getText().toString(), fileInUse));
                TrafficScotlandList.setAdapter(mAdapter);
                return false;
            }
        });

        editTextDate=findViewById(R.id.date_input);
        editTextDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParserSearchDate.getFromFile(MainActivity.this, editTextDate.getText().toString(), fileInUse));
                TrafficScotlandList.setAdapter(mAdapter);
                return false;
            }
        });

       // checks if network is availible, if not attempts to use local file saved.
        if(isNetworkAvailable()){
            Log.i("SvendCW", "starting download Task");
            TrafficScotlandDownloadTask download = new TrafficScotlandDownloadTask();
            download.execute();
        }else{

            mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParser.getFromFile(MainActivity.this, fileInUse));
            TrafficScotlandList.setAdapter(mAdapter);
        }

    }

    //switch views depending on radio button click
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_current:
                if (checked)
                    mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParser.getFromFile(MainActivity.this, "Current.xml"));
                    TrafficScotlandList.setAdapter(mAdapter);
                    fileInUse = "Current.xml";
                    if (!isNetworkAvailable()) {
                        Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection, you can still parse XML files locally", Toast.LENGTH_LONG);
                        toast.show();
                 }
                    break;

            case R.id.radio_planned:
                if (checked)
                    mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParser.getFromFile(MainActivity.this,  "Planned.xml"));
                    TrafficScotlandList.setAdapter(mAdapter);
                    fileInUse = "Planned.xml";
                if (!isNetworkAvailable()) {
                    Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection, you can still parse XML files locally", Toast.LENGTH_LONG);
                    toast.show();
                }
                    break;
            case R.id.radio_roadworks:
                if (checked)
                    mAdapter = new TrafficScotlandAdapter(MainActivity.this, -1, XMLPullParser.getFromFile(MainActivity.this,  "Roadworks.xml"));
                TrafficScotlandList.setAdapter(mAdapter);
                fileInUse = "Roadworks.xml";
                if (!isNetworkAvailable()) {
                    Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection, you can still parse XML files locally", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;


        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


     //uses the downloader class to download all 3 files and save them locally
    private class TrafficScotlandDownloadTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx", openFileOutput("Planned.xml", Context.MODE_PRIVATE));
                Downloader.DownloadFromUrl("https://trafficscotland.org/rss/feeds/currentincidents.aspx", openFileOutput("Current.xml", Context.MODE_PRIVATE));
                Downloader.DownloadFromUrl("https://trafficscotland.org/rss/feeds/roadworks.aspx", openFileOutput("Roadworks.xml", Context.MODE_PRIVATE));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
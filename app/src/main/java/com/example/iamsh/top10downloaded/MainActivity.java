package com.example.iamsh.top10downloaded;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ListView listApps;
 //   TextView title;
    String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    int count;
    final String FEED_URL = "FEEDURL";
    final String COUNT = "COUNT";
    String oldUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = findViewById(R.id.listApp);
    //    title = findViewById(R.id.title);
        if (savedInstanceState == null) {
            count = 10;
            downloadUrl(feedUrl);
        //    title.setText("Top 10 Free Apps");
            //    MenuItem top10 = findViewById(R.id.top10);


            //    top10.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   DownloadData data = new DownloadData();
        // data.execute("URL Goes here");
        //   data.execute("");
        Log.d(TAG, "onResume: DONE");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FEED_URL, feedUrl);
        outState.putInt(COUNT,count);
        oldUrl = "";
        Log.e(TAG, "onSaveInstanceState: " );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: " );
        feedUrl = savedInstanceState.get(FEED_URL).toString();
        count = savedInstanceState.getInt(COUNT);
        downloadUrl(feedUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.freeApps:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.paidApps:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.songs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.top10:
                count = 10;
                break;
            case R.id.top25:
                count = 25;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        downloadUrl(feedUrl);
        return true;
    }

    void downloadUrl(String Url) {
        DownloadData data = new DownloadData();


        // data.execute("URL Goes here");
        Log.e(TAG, "downloadUrl: (1)"+Url );
        String newUrl = String.format(Url, count);
        Log.e(TAG, "downloadUrl: (2)"+Url );
        Log.e(TAG, "downloadUrl: (3)"+newUrl );
        if (!newUrl.equalsIgnoreCase(oldUrl)) {
            Log.e(TAG, "downloadUrl: URL CHANGED" );
            oldUrl = newUrl;

            data.execute(newUrl);


        }else{
            Log.e(TAG, "downloadUrl: URL NOT CHANGED" );
        }

    }

    class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error");
            }
            Log.e(TAG, "doInBackground: DOWNLOADING DATA" );
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
    //        Log.d(TAG, "onPostExecute: Parameter is " + s);
            ProcessApplication p = new ProcessApplication();
            p.parse(s);
            CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.list_record, p.getApplication());
            listApps.setAdapter(adapter);
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    } else if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: MalformedURL" + e.getMessage());
                e.printStackTrace();
            } catch (IOException ie) {
                Log.e(TAG, "downloadXML: IO Exception" + ie.getMessage());
            }
            return null;
        }
    }
}

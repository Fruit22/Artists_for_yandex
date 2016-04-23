package com.fruit.foryandex;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fruit.foryandex.ErrDialog.ErrConnection;
import com.fruit.foryandex.ErrDialog.ErrServer;
import com.fruit.foryandex.ErrDialog.ErrUnknown;

public class MainActivity extends AppCompatActivity {
    private String strJSON="";
    private ArrayList<Artist> artists = new ArrayList<Artist>();
    private ListView artistsListView;
    private File root = Environment.getExternalStorageDirectory();
    private final static String TAG = MainActivity.class.getSimpleName();
    private JSONArray arrayParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

     public void start(){
        if (isOnline()){
            new downloadOrUpdateJSON().execute();
            }
        else {
            if(isAvailableJSON()){
                JSONToString();
            }
            else {
                DialogFragment errConnection = new ErrConnection();
                errConnection.show(getFragmentManager(),"errConnection");
            }
        }
    }

    protected boolean isAvailableJSON(){
        File file = new File(root.getAbsolutePath() + "/download/artists.json");
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    protected boolean isOnline() {
        String connectService = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager connectManager = (ConnectivityManager)
                getSystemService(connectService);
        if (connectManager.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    // Первая закачка файла JSON или его обновление. Вызов JSONToString()
    private class downloadOrUpdateJSON extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            File dir = new File (root.getAbsolutePath() + "/download");
            if(!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(root.getAbsolutePath() + "/download/artists.json");
            long lenOldJSON = file.length();
            try {
                URL url = new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                long lenNewJSON = connection.getContentLength();
                if (lenOldJSON != lenNewJSON) {
                    InputStream input = connection.getInputStream();
                    OutputStream output = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/Download/artists.json"));
                    int bytesRead = -1;
                    byte[] buffer = new byte[4096];
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    output.close();
                    input.close();
                }
            }
            catch (IOException e){
                if (isAvailableJSON()){
                    return null;
                }
                else {
                    DialogFragment errServer = new ErrServer();
                    errServer.show(getFragmentManager(), "errServer");
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            JSONToString();
        }
    }

    protected void JSONToString() {
        File file = new File(root.getAbsolutePath() + "/download/artists.json");
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bf = new BufferedReader(reader);
            strJSON= bf.readLine();
        } catch (IOException e) {
            DialogFragment errUnknown = new ErrUnknown();
            errUnknown.show(getFragmentManager(), "errUnknown");
        }
        parser(strJSON);
    }

    protected void parser(String strJSON) {
        JSONParser parser = new JSONParser();
        Object objParser= null;
        try {
            objParser = parser.parse(strJSON);
        } catch (ParseException e) {
            if (!isOnline()){}
            else {
                DialogFragment errServer = new ErrServer();
                errServer.show(getFragmentManager(), "errServer");
            }
        }
        try {
            arrayParser = (JSONArray) objParser;
            int i = 0;
            while (i < ((JSONArray) objParser).size()) {
                JSONObject parseArtist = (JSONObject) arrayParser.get(i);
                JSONObject parseCover = (JSONObject) parseArtist.get("cover");
                String name = String.valueOf(parseArtist.get("name"));
                String genres = parseArtist.get(("genres")).toString();
                genres = genres.replace("[\"", "");
                genres = genres.replace("\"]", "");
                genres = genres.replace("\",\"", ", ");
                String info = String.valueOf(genres + "\n" + "Альбомы: " + parseArtist.get("albums") + ",  Песни: " + parseArtist.get("tracks") + "\n");
                String cover = String.valueOf(parseCover.get("small"));
                artists.add(new Artist(name, info, cover));
                i++;
            }
            printArtist();
        }
        catch (NullPointerException e){
            if (!isOnline()){
                DialogFragment errConnection = new ErrConnection();
                errConnection.show(getFragmentManager(),"errConnection");
            }
            else {
                DialogFragment errServer = new ErrServer();
                errServer.show(getFragmentManager(), "errServer");
            }
        }
    }

    protected void printArtist(){
        ArtistAdapter artistAdapter;
        artistAdapter = new ArtistAdapter(MainActivity.this, artists);
        artistsListView = (ListView) findViewById(R.id.artistsListView);
        artistsListView.setAdapter(artistAdapter);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ArtistActivity.class);
                intent.putExtra("strJSON", String.valueOf(arrayParser.get((int) id)));
                startActivity(intent);
            }
        });
    }
}

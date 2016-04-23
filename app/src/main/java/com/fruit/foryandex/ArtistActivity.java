package com.fruit.foryandex;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import com.fruit.foryandex.ErrDialog.ErrUnknown;
import com.squareup.picasso.Picasso;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ArtistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        try {
            printAll(intent.getStringExtra("strJSON"));
        } catch (ParseException e) {
            DialogFragment errUnknown = new ErrUnknown();
            errUnknown.show(getFragmentManager(), "errUnknown");
        }
    }

    protected void printAll(String strJSON) throws ParseException {
        JSONParser parser = new JSONParser();
        Object objParser =  parser.parse(strJSON);
        JSONObject parseArtist = (JSONObject) objParser;
        JSONObject parseCover = (JSONObject) parseArtist.get("cover");
        String name = parseArtist.get("name").toString();
        String genres = parseArtist.get(("genres")).toString();
        String albums = parseArtist.get("albums").toString();
        String tracks = parseArtist.get("tracks").toString();
        String description = parseArtist.get("description").toString();
        String cover = String.valueOf(parseCover.get("big"));
        Picasso.with(this)
                .load(cover)
                .placeholder(R.drawable.bigcover)
                .into((ImageView) this.findViewById(R.id.bigcover));
        // Форматирование и вывод текста
        setTitle(name);
        genres = genres.replace("[\"", "");
        genres = genres.replace("\"]","");
        genres = genres.replace("\",\"",", ");
        TextView textView = (TextView) findViewById(R.id.informArtist);
        textView.setText(Html.fromHtml("<big>"+"<big>"+"<i>"+"<font color=#30b4c9>"+genres+"<br>"+"Альбомы:"+albums+
                "&nbsp; &nbsp;"+"Песни:"+tracks+"</font>"+"<br>"+"<br>"+"Описание:"+"</big>"+"<br>"+description+"</i>"+"</big>"));
    }
}

package com.fruit.foryandex;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ArtistAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Artist> objects;

    ArtistAdapter(Context context, ArrayList<Artist> artists) {
        ctx = context;
        objects = artists;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.itemartist, parent, false);
        }
        Artist artist = getArtist(position);
        ((TextView) view.findViewById(R.id.nameArtist)).setText(artist.getName());
        ((TextView) view.findViewById(R.id.infoArtist)).setText(artist.getInfo());// new LoadImage().execute(p.getCoverResource());
        Picasso.with(ctx)
                .load(artist.getCoverResource())
                .resize(100, 100)
                .placeholder(R.drawable.cover)
                .into((ImageView) view.findViewById(R.id.cover));
        return view;
    }

    Artist getArtist(int position) {
        return ((Artist) getItem(position));
    }
}


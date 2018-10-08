package com.example.iamsh.top10downloaded;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    private static final String TAG = "CustomAdapter";
    final int layoutResource;
    final LayoutInflater layoutInflater;
    List<DataEntry> list;

    public CustomAdapter(@NonNull Context context, int resource, List<DataEntry> list) {
        super(context, resource);

        this.list = list;
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     //   Log.e(TAG, "getView: " );
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
        }

        TextView summary = convertView.findViewById(R.id.tvSummary);
        TextView name = convertView.findViewById(R.id.tvName);
        TextView artist = convertView.findViewById(R.id.tvArtist);

        DataEntry ob = list.get(position);
        name.setText(ob.getName());
        artist.setText(ob.getArtist());
        summary.setText(ob.getSummary());


   //     Log.e(TAG, "getView_Name "+ob.getName() );
   //     Log.e(TAG, "getView_Artist "+ob.getArtist() );
    //    Log.e(TAG, "getView_Summary "+ob.getSummary() );
        return convertView;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}

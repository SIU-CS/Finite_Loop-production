package edu.siu.cs.www.parkingspotfinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jacobreed on 4/18/17.
 */

public class SpotAdapter extends ArrayAdapter<Spot>{

    Context context;
    int layoutResourceID;
    Spot data[] = null;

    public SpotAdapter(Context context, int layoutResourceID, Spot[] data){
        super(context, layoutResourceID, data);
        this.layoutResourceID = layoutResourceID;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        SpotHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new SpotHolder();
            holder.imageView = (ImageView)row.findViewById(R.id.imgIcon);
            holder.textView = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        } else {
            holder = (SpotHolder) row.getTag();
        }

        Spot spot = data[position];
        holder.textView.setText(spot.spotName);
        holder.imageView.setImageResource(spot.icon);

        return row;
    }

    static class SpotHolder{
        ImageView imageView;
        TextView textView;
    }
}

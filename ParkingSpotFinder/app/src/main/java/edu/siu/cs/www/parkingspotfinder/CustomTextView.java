package edu.siu.cs.www.parkingspotfinder;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jacobreed on 2/21/17.
 */

public class CustomTextView extends TextView {

    public CustomTextView(Context con, AttributeSet set){
        super(con, set);
        this.setTypeface(Typeface.createFromAsset(con.getAssets(), "fonts/lobstertwo.ttf"));
    }
}

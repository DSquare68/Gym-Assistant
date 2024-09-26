package pl.dsquare.gymassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pl.dsquare.gymassistant.R;

public class Serie extends LinearLayout {


    public Serie(Context context, AttributeSet set) {
        super(context,set);
        inflate(context, R.layout.serie_add_training,null);
    }
}

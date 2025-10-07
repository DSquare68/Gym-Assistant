package pl.dsquare.home.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pl.dsquare.home.R;

public class Serie extends LinearLayout {

    public Serie(Context context) {
        super(context);
        inflate(context, R.layout.serie_add_training,this);
    }

    public Serie(Context context, AttributeSet set, int defStyle) {
        super(context,set,defStyle);
        inflate(context, R.layout.serie_add_training,this);
    }
}

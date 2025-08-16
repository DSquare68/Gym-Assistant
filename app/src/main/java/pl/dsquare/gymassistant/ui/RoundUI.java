package pl.dsquare.gymassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pl.dsquare.gymassistant.R;

public class RoundUI extends LinearLayout {
    public RoundUI(Context context) {
        super(context);
        inflate(context, R.layout.round,this);
    }

    public RoundUI(Context context, AttributeSet set, int defStyle) {
        super(context,set,defStyle);
        inflate(context, R.layout.round,this);
    }
}

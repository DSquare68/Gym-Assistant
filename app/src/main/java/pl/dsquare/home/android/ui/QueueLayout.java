package pl.dsquare.home.android.ui;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import pl.dsquare.home.android.Units;
import pl.dsquare.home.android.android.R;

public class QueueLayout extends LinearLayout {

    private TextView home, guest, date;
    private boolean isHome =false, isGuest= false;
    private Context context;


    public QueueLayout(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.queue_match, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Units.dpToPx(context,125)));
        home = findViewById(R.id.queue_match_home);
        guest = findViewById(R.id.queue_match_guest);
        date = findViewById(R.id.queue_match_vs);
        date.setOnClickListener((v)->setMatchWinner(0));
        home.setOnClickListener((v)->setMatchWinner(1));
        guest.setOnClickListener((v)->setMatchWinner(-1));

    }

    private void setMatchWinner(int winner) {
        switch (winner){
            case 0 ->   setWinner(true,true);
            case -1 ->  setWinner(false,true);
            case 1 ->   setWinner(true,false);
        }
    }

    private void setWinner(boolean h, boolean g) {
        isHome = h;
        isGuest = g;
        int colorHome = isHome ? R.color.colorLightGreen : R.color.colorLightRed;
        int colorGuest = isGuest ? R.color.colorLightGreen : R.color.colorLightRed;
        new Thread(() -> {
            home.post(()->home.setBackgroundColor(ContextCompat.getColor(context,colorHome)));
            guest.post(()->guest.setBackgroundColor(ContextCompat.getColor(context,colorGuest)));
        }).start();
    }

}

package pl.dsquare.home.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import pl.dsquare.home.android.android.R;
import pl.dsquare.home.android.data.MatchRecord;
import pl.dsquare.home.android.db.AppDatabase;
import pl.dsquare.home.android.ui.QueueLayout;

public class FootballActivity extends Activity {
    private List<MatchRecord> queues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int daysToFriday = 6 - dayOfWeek;
        today.add(Calendar.DATE, daysToFriday);
        String fridayStr = sdf.format(today.getTime());
        today.add(Calendar.DATE, 3+7);
        today.set(Calendar.HOUR_OF_DAY, 23);
        String mondayStr = sdf.format(today.getTime());

        Thread t = new Thread(()->queues = db.matchDao().getQueue(fridayStr,mondayStr));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d("FootballActivity",queues.size()+"");
        int a=0;
        a++;
        initQueue();
    }

    private void initQueue() {
        LinearLayout ll = findViewById(R.id.queue_layout);
        for (MatchRecord matchRecord : queues) {
            QueueLayout match = new QueueLayout(this);
            ((TextView) match.findViewById(R.id.queue_match_home)).setText(matchRecord.getHome());
            ((TextView) match.findViewById(R.id.queue_match_date)).setText(matchRecord.getDate_of_match());
            ((TextView) match.findViewById(R.id.queue_match_guest)).setText(matchRecord.getGuest());
            new Thread(()->ll.post(()->ll.addView(match))).start();
        }
    }
}

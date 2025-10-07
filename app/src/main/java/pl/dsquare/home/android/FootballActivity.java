package pl.dsquare.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.dsquare.home.data.MatchRecord;
import pl.dsquare.home.db.AppDatabase;

public class FootballActivity extends Activity {
    private List<MatchRecord> queues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        today.add(Calendar.DATE, 3);
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
    }
}

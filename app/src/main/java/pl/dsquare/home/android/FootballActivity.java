package pl.dsquare.home.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import pl.dsquare.home.android.android.R;
import pl.dsquare.home.android.data.MatchRecord;
import pl.dsquare.home.android.db.AppDatabase;
import pl.dsquare.home.android.ui.QueueLayout;

public class FootballActivity extends AppCompatActivity {
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
        today.add(Calendar.DATE, -7);
        Thread t = new Thread(()->{
            int queueLP = db.matchDao().getQueueByDate(sdf.format(today.getTime()),MatchRecord.CODE_WEB);
            queues = db.matchDao().getQueue(queueLP,MatchRecord.CODE_WEB);
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.match_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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

    public void sendMatch(MenuItem item) {
        LinearLayout ll = findViewById(R.id.queue_layout);
        ArrayList<MatchRecord> guests = new ArrayList<>(queues);
        AppDatabase db = AppDatabase.getDatabase(this);
        for(int i=0; i< ll.getChildCount();i++){
            QueueLayout match = (QueueLayout) ll.getChildAt(i);
            String resoultMatch="";
            boolean h = match.isHome;
            boolean g = match.isGuest;
            if(h && !g)
                resoultMatch = MatchRecord.CODE_HOME;
            else if(!h && g)
                resoultMatch = MatchRecord.CODE_GUEST;
            else if(h && g)
                resoultMatch = MatchRecord.CODE_TIE;
            guests.get(i).setMode_of_data(resoultMatch);
            queues.get(i).setID(0);
        }
        new Thread(()->db.insertQueue(guests)).start();
    }
}

package pl.dsquare.home.android.db;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class ExerciseNamesAdapter extends ArrayAdapter<String> {
    AppDatabase ad;
    public ExerciseNamesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        ad = AppDatabase.getDatabase(getContext());
        Thread t1 = new Thread(this::setData);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        ad.exerciseDao().getAllNames().forEach(this::add);
    }
    public List<Exercise> getData(){
        return  ad.exerciseDao().getAll();
    }
}

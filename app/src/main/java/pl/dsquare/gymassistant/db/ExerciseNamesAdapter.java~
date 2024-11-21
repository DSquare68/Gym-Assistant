package pl.dsquare.gymassistant.db;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class ExerciseNamesAdapter extends ArrayAdapter<String> {
    public ExerciseNamesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        Thread t1 = new Thread(this::setData);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        AppDatabase.getDatabase(getContext()).exerciseDao().getAllNames().forEach(this::add);
    }
}

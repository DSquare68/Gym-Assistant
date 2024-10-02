package pl.dsquare.gymassistant.data;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

import pl.dsquare.gymassistant.db.Exercise;
import pl.dsquare.gymassistant.db.ExerciseDao;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.ExerciseNames;

public class ExerciseNamesAdapter extends ArrayAdapter<String> {
    public ExerciseNamesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        setData();
    }

    private void setData() {
        ExerciseDao exerciseDao = AppDatabase.getDatabase(getContext()).exerciseDao();
        exerciseDao.getAllNames().forEach(this::add);
    }
}

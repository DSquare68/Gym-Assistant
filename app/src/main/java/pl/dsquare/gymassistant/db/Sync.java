package pl.dsquare.gymassistant.db;

import android.content.Context;

import java.util.List;

public class Sync {

    public void sync(Context c) {
        ExerciseNamesAdapter ena = new ExerciseNamesAdapter(c, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        List<Exercise> eList = ena.getData();

    }
}

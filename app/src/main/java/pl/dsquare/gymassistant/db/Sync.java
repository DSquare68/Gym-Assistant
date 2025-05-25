package pl.dsquare.gymassistant.db;

import android.content.Context;

public class Sync {

    public void sync(Context c) {
        ExerciseNamesAdapter ena = new ExerciseNamesAdapter(c, android.R.layout.simple_spinner_item);
        //OracleSchema es = new OracleSchema(c,ena.getData());
        //es.sync();

    }
}

package pl.dsquare.gymassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.Units;
import pl.dsquare.gymassistant.data.Training;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.ui.ExerciseCreate;

public class TrainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private List<TrainingRecord> records = new ArrayList<>();
    private Training trainingSchema;
    public Training training;
    LinkedList<ExerciseCreate> ecList;
    private List<String> schemas;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        db = AppDatabase.getDatabase(this);
        Spinner s = findViewById(R.id.schemas_spinner);
        new Thread(()->{schemas = db.trainingDao().getAllSchemaNames();
         runOnUiThread(()->s.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,schemas)));
        }).start();

        s.setOnItemSelectedListener(onCLickSpinner());
    }

    private AdapterView.OnItemSelectedListener onCLickSpinner() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name =((TextView) view).getText().toString();
                ((EditText) findViewById(R.id.traing_name)).setText(name);
                new Thread(()->{
                    int ID = db.trainingDao().getIDTrainingSchamaByName(name);
                    records = db.trainingDao().getTrainigByIDTraining(ID);
                    trainingSchema = TrainingRecord.toTraining((ArrayList<TrainingRecord>) records,view.getContext());
                    runOnUiThread(()->refresh());
                }).start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void refresh() {
        ll = findViewById(R.id.ll_train_exercises);
        ll.removeAllViews();
        for(String name : trainingSchema.getExercises()){
            ExerciseCreate tc = new ExerciseCreate(this);
            tc.setOrientation(LinearLayout.VERTICAL);
            tc.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,350), ViewGroup.LayoutParams.WRAP_CONTENT));
            tc.setExerciseValues(trainingSchema.getRounds().get(name),name);
            ll.addView(tc);
        }

    }

}
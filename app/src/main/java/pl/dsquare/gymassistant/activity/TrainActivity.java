package pl.dsquare.gymassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Id;
import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.ExerciseNamesAdapter;
import pl.dsquare.gymassistant.ui.ExerciseCreate;

public class TrainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private ArrayList<TrainingRecord> records = new ArrayList<>();
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
                ((EditText) findViewById(R.id.traing_name)).setText(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

}
package pl.dsquare.gymassistant.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.Units;
import pl.dsquare.gymassistant.api.ApiClient;
import pl.dsquare.gymassistant.api.ApiEksport;
import pl.dsquare.gymassistant.data.Training;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.ui.ExerciseCreate;
import pl.dsquare.gymassistant.ui.RoundUI;

public class TrainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private List<TrainingRecord> records = new ArrayList<>();
    private Training trainingSchema;
    public Training training;
    LinkedList<ExerciseCreate> ecList;
    private List<String> schemas;
    private AppDatabase db;
    int ID_Schema = 0;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_training_menu, menu);
        return true;
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
                    ID_Schema = records.get(0).getSCHEMA();
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
            tc.setExerciseValuesSchema(trainingSchema.getRounds().get(name),name);
            ll.addView(tc);
        }

    }
    public void addTraining(MenuItem item) {
        readTraining();
        ApiEksport apiEksport = new ApiClient().getRetrofitInstance().create(ApiEksport.class);
        DatePicker dp = ((DatePicker) this.findViewById(R.id.date_training));
        int day = dp.getDayOfMonth();
        int month = dp.getMonth() + 1;
        int year = dp.getYear();
        Date date = new Date(year - 1900, month - 1, day);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        AtomicInteger code= new AtomicInteger();
        new Thread(()-> {
            try {
                code.set(apiEksport.addTraining(Training.toTrainingRecord(db, training, ((EditText) this.findViewById(R.id.traing_name)).getText().toString(), simpleDateFormat.format(date), ID_Schema)).execute().code());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Toast.makeText(this,"Code: "+code,Toast.LENGTH_LONG).show();
    }

    private void readTraining() {
        ll = findViewById(R.id.ll_train_exercises);
        for(int i=0; i < ll.getChildCount(); i++) {
            LinearLayout exerciseLayout = (LinearLayout) ll.getChildAt(i);
            String name = ((TextView) exerciseLayout.findViewById(R.id.exercise_name)).getText().toString();
            training.getExercises().set(i,name);
            LinearLayout extendedSerie= exerciseLayout.findViewById(R.id.ll_extended_series_parent);
            ArrayList<Training.Round> rounds = new ArrayList<>();
            for(int j=0;j<extendedSerie.getChildCount(); j++) {
                RoundUI serie = (RoundUI) extendedSerie.getChildAt(j);
                int repeats = Integer.parseInt(((EditText) serie.findViewById(R.id.editText2)).getText().toString());
                double weight = Double.parseDouble(((EditText) serie.findViewById(R.id.editText)).getText().toString());
                Training.Round r = training.new Round(j+1, repeats, weight);
                rounds.add(r);
            }
            training.getRounds().put(name,rounds);
        }
    }
    public void addTrainingFromFile(MenuItem item){
        this.findViewById(R.id.traing_name).setVisibility(GONE);
        this.findViewById(R.id.fileField).setVisibility(VISIBLE);
        this.findViewById(R.id.date_training).setVisibility(GONE);
        this.findViewById(R.id.scroll_exercise).setVisibility(GONE);
        this.findViewById(R.id.parser_file_button).setVisibility(VISIBLE);
        ((Button) this.findViewById(R.id.parser_file_button)).setOnClickListener((v)->{
            parseFile();
            fillData();
            this.findViewById(R.id.scroll_exercise).setVisibility(VISIBLE);
            this.findViewById(R.id.traing_name).setVisibility(VISIBLE);
            this.findViewById(R.id.fileField).setVisibility(GONE);
            this.findViewById(R.id.date_training).setVisibility(VISIBLE);
            this.findViewById(R.id.parser_file_button).setVisibility(GONE);
        } );
    }
    private void fillData() {
        ll = findViewById(R.id.ll_train_exercises);
        ll.removeAllViews();
        for(String name : trainingSchema.getExercises()){
            ExerciseCreate tc = new ExerciseCreate(this);
            tc.setOrientation(LinearLayout.VERTICAL);
            tc.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,350), ViewGroup.LayoutParams.WRAP_CONTENT));
            tc.setExerciseValues(training.getRounds().get(name),name);
            ll.addView(tc);
        }
    }

    private void parseFile() {
        String text = ((EditText) this.findViewById(R.id.fileField)).getText().toString();
        training = new Training(((EditText) this.findViewById(R.id.traing_name)).getText().toString());
        int i=0;
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
           String line = scanner.nextLine();
           if(line.isBlank())
               continue;
           String[] parts = line.split(" ");
           String name = trainingSchema.getExercises().set(i,trainingSchema.getExercises().get(Integer.valueOf(parts[0].substring(0,parts[0].length()-1))-1));
           training.getExercises().add(name);
           training.getRounds().put(name, new ArrayList<>());
           for(int j = 0; j < (parts.length-1)/2; j++) {
               if(parts[j].isBlank())
                   continue;
               Training.Round r = training.new Round((j/2+1) , Integer.valueOf(parts[j*2+2]), Double.valueOf(parts[j*2+1]));
               training.getRounds().get(name).add(r);
           }
           i++;
        }
        scanner.close();
    }
}
package pl.dsquare.gymassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.Units;
import pl.dsquare.gymassistant.ui.ExerciseCreate;

public class CreateTrainingActivity extends AppCompatActivity {

    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);
        ll = findViewById(R.id.ll_exercises_create_training);
    }

    public void addAnotherExercise(View view) {
        ExerciseCreate ec = new ExerciseCreate(this);
        ec.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,350), ViewGroup.LayoutParams.WRAP_CONTENT));
        ec.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ec,ll.getChildCount()-1);
    }
}
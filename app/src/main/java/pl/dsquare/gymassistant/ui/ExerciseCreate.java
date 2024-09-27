package pl.dsquare.gymassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pl.dsquare.gymassistant.R;

public class ExerciseCreate extends LinearLayout {
    public ExerciseCreate(Context context){
        super(context);
        inflate(context, R.layout.add_training_component_basic,this);
    }
    public ExerciseCreate(Context context, AttributeSet set) {
        super(context,set);
        inflate(context, R.layout.add_training_component_basic,this);
    }
}

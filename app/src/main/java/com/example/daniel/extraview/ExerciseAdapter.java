package com.example.daniel.extraview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.exercise.values.ExerciseValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.gymassistant.R;


import java.util.List;

/**
 * Created by Daniel on 2017-04-05.
 */

public  class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {


    public static List<ExerciseValue> exerciseList;


    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;

    Slider slider = new Slider();
    public ExerciseAdapter(List<ExerciseValue> moviesList, Context c) {
        this.exerciseList = moviesList;
        this.inflater = LayoutInflater.from(c);
    }

    public void setItem(ExerciseValue exerciseValue, int firstFree) {
        exerciseList.set(firstFree,exerciseValue);
    }

    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryIconClick(int p);
    }


    public class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AutoCompleteTextView title;
        public ImageView slider;
        public TextView roundNumberTV, weightTV, repsTV;
        public EditText roundNumberET, weightET, repsET;
        public MyCustomEditTextListener[] customEditTextListener = new MyCustomEditTextListener[4];


        public ExerciseHolder(View view) {
            super(view);
            title = view.findViewById(R.id.autocomplete_exercise);
            setHints(view,title);
            slider = view.findViewById(R.id.slider_image);
            slider.setOnClickListener(this);
            LinearLayout parent = (LinearLayout) slider.getParent();
            final LinearLayout hidden = (LinearLayout) parent.getChildAt(2);
            slider.setOnClickListener(ExerciseAdapter.this.slider.setListener(hidden));
            roundNumberTV = view.findViewById(R.id.round_number_text_view);
            repsTV = view.findViewById(R.id.reps_text_view);
            weightTV = view.findViewById(R.id.weight_text_view);
            roundNumberET = view.findViewById(R.id.round_number_edit_text);
            repsET =  view.findViewById(R.id.reps_edit_text);
            weightET = view.findViewById(R.id.weight_edit_text);

            this.customEditTextListener[0] = new MyCustomEditTextListener(1);
            this.title.addTextChangedListener(customEditTextListener[0]);

            this.customEditTextListener[1] = new MyCustomEditTextListener(2);
            this.roundNumberET.addTextChangedListener(customEditTextListener[1]);

            this.customEditTextListener[2] = new MyCustomEditTextListener(3);
            this.weightET.addTextChangedListener(customEditTextListener[2]);

            this.customEditTextListener[3] = new MyCustomEditTextListener(4);
            this.repsET.addTextChangedListener(customEditTextListener[3]);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.exercise){
                itemClickCallback.onItemClick(getAdapterPosition());
            } else {
                itemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }


    }
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        /**
         * 1-title 2-roundNumber 3- weight 4-reps
         */
        private int index;
        public MyCustomEditTextListener(int index){
            this.index = index;
        }
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            switch (index){
                case 1: exerciseList.get(position).setName(charSequence.toString()); break;
                case 2: if(!(charSequence.toString().equals(""))) exerciseList.get(position).setRoundNumber(Integer.valueOf(charSequence.toString())); else exerciseList.get(position).setRoundNumber(0);  break;
                case 3: if(!(charSequence.toString().equals(""))) exerciseList.get(position).setWeight(Double.valueOf(charSequence.toString()));else exerciseList.get(position).setWeight(0); break;
                case 4: if(!(charSequence.toString().equals(""))) exerciseList.get(position).setReps(Integer.valueOf(charSequence.toString()));else exerciseList.get(position).setReps(0); break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
    @Override
    public void onViewRecycled(ExerciseHolder holder) {
        super.onViewRecycled(holder);
    }



    private void setHints(View view, AutoCompleteTextView title){
        ExerciseDatabase cw = new  ExerciseDatabase(view.getContext());
        int numberOfExercise = cw.getCount();
        String[] exerciseS = new String[numberOfExercise];
        Exercise[] exercises = cw.getAll();
        for(int i = 0; i<cw.getCount(); i++){
            exerciseS[i] = exercises[i].getName();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.add_training_list_item,R.id.item, exerciseS);
        title.setAdapter(arrayAdapter);
        title.setThreshold(1);
        cw.close();
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.add_training_auto_complete_text_view, parent, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder holder, int position) {
        for (int i=0;i<4;i++) {
            holder.customEditTextListener[i].updatePosition(position);
        }
        holder.title.setText(exerciseList.get(holder.getPosition()).getName());
        if(exerciseList.get(holder.getPosition()).getWeight()!=0)holder.weightET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getWeight()));
        if(exerciseList.get(holder.getPosition()).getReps()!=0)holder.repsET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getReps()));
        if(exerciseList.get(holder.getPosition()).getRoundNumber()!=0)holder.roundNumberET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getRoundNumber()));

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

}
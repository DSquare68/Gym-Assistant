package com.example.daniel.extraview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.exercise.values.ExerciseValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.gymassistant.R;
import com.example.daniel.values.AddTrainingValues;


import java.util.List;

/**
 * Created by Daniel on 2017-04-05.
 */

public  class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {


    public static List<ExerciseValue> exerciseList;


    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    Context context;
    RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> adapter;
    Slider slider = new Slider();
    public ExerciseAdapter(List<ExerciseValue> moviesList, Context c) {
        this.exerciseList = moviesList;
        this.inflater = LayoutInflater.from(c);
        this.context=c;
        adapter=this;
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
        public View view;
        public CheckBox dropSetCB;
        public MyCustomEditTextListener[] customEditTextListener = new MyCustomEditTextListener[4];


        public ExerciseHolder(View view) {
            super(view);
            this.view=view;
            title = view.findViewById(R.id.autocomplete_exercise);
            setHints(view,title);
            slider = view.findViewById(R.id.slider_image);
            slider.setOnClickListener(this);
            LinearLayout parent = (LinearLayout) slider.getParent();
            final LinearLayout hidden = (LinearLayout) parent.getChildAt(2);
            hidden.setOrientation(LinearLayout.VERTICAL);
            slider.setOnClickListener(ExerciseAdapter.this.slider.setListener(hidden));
            roundNumberTV = view.findViewById(R.id.round_number_text_view);
            repsTV = view.findViewById(R.id.reps_text_view);
            weightTV = view.findViewById(R.id.weight_text_view);
            roundNumberET = view.findViewById(R.id.round_number_edit_text);
            repsET =  view.findViewById(R.id.reps_edit_text);
            weightET = view.findViewById(R.id.weight_edit_text);
            dropSetCB = view.findViewById(R.id.checkbox_drop_set);
            dropSetCB.setOnCheckedChangeListener(setCheckListener(view,roundNumberET));
            this.customEditTextListener[0] = new MyCustomEditTextListener(1);
            this.title.addTextChangedListener(customEditTextListener[0]);

            this.customEditTextListener[1] = new MyCustomEditTextListener(2);
            this.roundNumberET.addTextChangedListener(customEditTextListener[1]);

            this.customEditTextListener[2] = new MyCustomEditTextListener(3);
            this.weightET.addTextChangedListener(customEditTextListener[2]);

            this.customEditTextListener[3] = new MyCustomEditTextListener(4);
            this.repsET.addTextChangedListener(customEditTextListener[3]);
        }

        private CompoundButton.OnCheckedChangeListener setCheckListener(final View view, final EditText roundNumberET) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int oldRoundNumber = ((LinearLayout) view).getChildCount() - 3;
                    if(isChecked){
                        int newRoundsNumber=0;
                        if(!roundNumberET.getText().toString().equals("")) newRoundsNumber=Integer.valueOf(roundNumberET.getText().toString());
                        for(int j=0;j<newRoundsNumber;j++) {
                            if (newRoundsNumber > oldRoundNumber ) {
                                view.findViewById(R.id.weight_and_reps).setVisibility(View.GONE);
                                ((LinearLayout) view).addView(View.inflate(context, R.layout.add_training_round_values, null));
                                ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.round_number_edit_text)).setText(context.getResources().getString(R.string.round) + " " + (j + 1) + ": ");
                            } else {
                                break;
                            }
                        }
                    }else{
                        int newRoundsNumber = Integer.valueOf(0);
                            view.findViewById(R.id.weight_and_reps).setVisibility(View.VISIBLE);
                            while (newRoundsNumber < oldRoundNumber) {
                                ((LinearLayout) view).removeViewAt(oldRoundNumber + 2);
                                oldRoundNumber--;
                            }
                        }
                }
            };
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
        View view;
        CheckBox checkBox;
        public MyCustomEditTextListener(int index){
            this.index = index;
        }
        public void updatePosition(int position) {
            this.position = position;
        }
        public void updateView(View view) {this.view = view;}
        public void updateCheckBox(View view) {this.checkBox = checkBox;}
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            switch (index){
                case 1: exerciseList.get(position).setName(charSequence.toString()); break;
                case 2: if(!(charSequence.toString().equals(""))){
                    exerciseList.get(position).setRoundNumber(Integer.valueOf(charSequence.toString()));
                    //TODO by pojawiała sie odpowiednia ilość
                    int newRoundsNumber=Integer.valueOf(charSequence.toString());
                    int oldRoundNumber = ((LinearLayout) view).getChildCount()-3;
                    for(int j=0;j<newRoundsNumber;j++){
                        if(newRoundsNumber>oldRoundNumber&&((CheckBox) view.findViewById(R.id.checkbox_drop_set)).isChecked()){
                            view.findViewById(R.id.weight_and_reps).setVisibility(View.GONE);
                            ((LinearLayout) view).addView(View.inflate(context,R.layout.add_training_round_values,null));
                            ((TextView) ((LinearLayout) view).getChildAt(3+j).findViewById(R.id.round_number_edit_text)).setText(context.getResources().getString(R.string.round)+" "+(j+1)+": ");
                        }else{
                            break;
                        }
                    }
                } else {
                    exerciseList.get(position).setRoundNumber(0);
                    int newRoundsNumber=Integer.valueOf(0);
                    int oldRoundNumber = ((LinearLayout) view).getChildCount()-3;
                    while (newRoundsNumber<oldRoundNumber&&((CheckBox) view.findViewById(R.id.checkbox_drop_set)).isChecked()) {
                        ((LinearLayout) view).removeViewAt(oldRoundNumber + 2);
                        oldRoundNumber--;
                    }
                }  break;
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
            holder.customEditTextListener[i].updateView(holder.view);
        }
        holder.title.setText(exerciseList.get(holder.getPosition()).getName());

        if(exerciseList.get(holder.getPosition()).getWeight()!=0)holder.weightET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getWeight()));
        if(exerciseList.get(holder.getPosition()).getReps()!=0)holder.repsET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getReps()));
        if(exerciseList.get(holder.getPosition()).getRoundNumber()!=0){
            holder.roundNumberET.setText(String.valueOf(exerciseList.get(holder.getPosition()).getRoundNumber()));
            Log.d("dafsdasdas","ADFSdasddfsaadfs");
            ((LinearLayout) holder.view).addView(View.inflate(context,R.layout.add_training_round_values,null));
        }

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

}
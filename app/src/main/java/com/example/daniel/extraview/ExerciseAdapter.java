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
import com.example.daniel.gymassistant.R;
import com.example.daniel.values.AddTrainingValues;
import com.example.daniel.values.Training;


import java.util.List;

/**
 * Created by Daniel on 2017-04-05.
 */

public  class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {


    public static Training training;


    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;
    Context context;
    RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> adapter;
    Slider slider = new Slider();
    public ExerciseAdapter(List<ExerciseValue> moviesList, Context c) {
        training = new Training();
        this.training.setExercises(moviesList);
        this.inflater = LayoutInflater.from(c);
        this.context=c;
        adapter=this;
    }

    public void setItem(ExerciseValue exerciseValue, int firstFree) {
        //training.set(firstFree, exerciseValue);
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
        public DropSetEditTextListener[][] dropSetListener= new DropSetEditTextListener[2][];
        int position=-1;
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
                    AddTrainingValues.setDropSet(isChecked);
                    if(isChecked){
                        int newRoundsNumber=0;
                        if(!roundNumberET.getText().toString().equals("")) newRoundsNumber=Integer.valueOf(roundNumberET.getText().toString());
                        dropSetListener[0] = new DropSetEditTextListener[newRoundsNumber];
                        dropSetListener[1] = new DropSetEditTextListener[newRoundsNumber];
                        for(int i=0;i<newRoundsNumber;i++) {
                            dropSetListener[0][i] = new DropSetEditTextListener(1, i);
                            dropSetListener[0][i].updatePosition(position);
                            dropSetListener[1][i] = new DropSetEditTextListener(2, i);
                            dropSetListener[1][i].updatePosition(position);
                        }
                        for(int j=0;j<newRoundsNumber;j++) {
                            if (newRoundsNumber > oldRoundNumber ) {
                                view.findViewById(R.id.weight_and_reps).setVisibility(View.GONE);
                                ((LinearLayout) view).addView(View.inflate(context, R.layout.add_training_round_values, null));
                                ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.round_number_edit_text)).setText(context.getResources().getString(R.string.round) + " " + (j + 1) + ": ");
                                ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.weight_edit_text)).addTextChangedListener(dropSetListener[0][j]);
                                ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.reps_edit_text)).addTextChangedListener(dropSetListener[1][j]);
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
                        training.align(position,1);
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

        public DropSetEditTextListener[][] dropSetListener= new DropSetEditTextListener[2][];
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
                case 1: training.set(Training.NAME,charSequence.toString(),position,0); break;
                case 2: if(!(charSequence.toString().equals(""))){
                    training.set(Training.ROUND_NUMBER,Integer.valueOf(charSequence.toString()),position,0);
                    int newRoundsNumber=Integer.valueOf(charSequence.toString());
                    int oldRoundNumber = ((LinearLayout) view).getChildCount()-3;
                    dropSetListener[0] = new DropSetEditTextListener[newRoundsNumber];
                    dropSetListener[1] = new DropSetEditTextListener[newRoundsNumber];
                    for(int k=0;k<newRoundsNumber;k++) {
                        dropSetListener[0][k] = new DropSetEditTextListener(1, k);
                        dropSetListener[0][k].updatePosition(position);
                        dropSetListener[1][k] = new DropSetEditTextListener(2, k);
                        dropSetListener[1][k].updatePosition(position);
                    }
                    for(int j=0;j<newRoundsNumber;j++){
                        if(newRoundsNumber>oldRoundNumber&&((CheckBox) view.findViewById(R.id.checkbox_drop_set)).isChecked()){
                            view.findViewById(R.id.weight_and_reps).setVisibility(View.GONE);
                            ((LinearLayout) view).addView(View.inflate(context,R.layout.add_training_round_values,null));
                            ((TextView) ((LinearLayout) view).getChildAt(3+j).findViewById(R.id.round_number_edit_text)).setText(context.getResources().getString(R.string.round)+" "+(j+1)+": ");
                            ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.weight_edit_text)).addTextChangedListener(dropSetListener[0][j]);
                            ((TextView) ((LinearLayout) view).getChildAt(3 + j).findViewById(R.id.reps_edit_text)).addTextChangedListener(dropSetListener[1][j]);
                        }else{
                            break;
                        }
                    }
                } else {
                    training.set(Training.ROUND_NUMBER,Integer.valueOf(charSequence.toString().equals("") ? String.valueOf(0) : charSequence.toString()),position,0);
                    int newRoundsNumber=Integer.valueOf(0);
                    int oldRoundNumber = ((LinearLayout) view).getChildCount()-3;
                    while (newRoundsNumber<oldRoundNumber&&((CheckBox) view.findViewById(R.id.checkbox_drop_set)).isChecked()) {
                        ((LinearLayout) view).removeViewAt(oldRoundNumber + 2);
                        oldRoundNumber--;
                    }
                    training.align(position,1);
                }  break;
                case 3: if(!(charSequence.toString().equals(""))) training.set(Training.WEIGHT,Double.valueOf(charSequence.toString()),position,0);else training.set(Training.WEIGHT,Double.valueOf(0),position,0); break;
                case 4: if(!(charSequence.toString().equals(""))) training.set(Training.REPS,Integer.valueOf(charSequence.toString()),position,0);else training.set(Training.REPS,Integer.valueOf(0),position,0); break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
    private class DropSetEditTextListener implements TextWatcher {
        private int position;
        /**
         *  1- weight 2-reps
         */
        private int index;
        View view;
        CheckBox checkBox;
        private int indexRound;
        public DropSetEditTextListener(int index, int indexRound){
            this.index = index;
            this.indexRound=indexRound;
        }
        public void updatePosition(int position) {
            this.position = position;
        }
        public void updatendexRound(int indexRound) {this.indexRound=indexRound;}
        public void updateView(View view) {this.view = view;}
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(training.get(position).size()-1<indexRound) training.align(position,indexRound+1);
            training.set(Training.ROUND_NUMBER,indexRound+1,position,indexRound);
            switch (index){
                case 1: if(!(charSequence.toString().equals(""))) training.set(Training.WEIGHT,Double.valueOf(charSequence.toString()),position,indexRound);else training.set(Training.WEIGHT,0.0,position,indexRound); break;
                case 2: if(!(charSequence.toString().equals(""))) training.set(Training.REPS,Integer.valueOf(charSequence.toString()),position,indexRound);else training.set(Training.REPS,0,position,indexRound); break;
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
        for(int i=0;i<2;i++){
            holder.position=position;
            if(holder.dropSetListener[i]==null) break;
           // for(int i=0;i< ((LinearLayout) holder.view).getChildCount()-3;i++){
             //   holder.view.((TextView) ((LinearLayout) holder.view).getChildAt(3 + i).findViewById(R.id.round_number_edit_text));
            //}
            for(int j=0;j<holder.dropSetListener[i].length;j++) {
                holder.dropSetListener[i][j].updatePosition(position);
                holder.dropSetListener[i][j].updateView(holder.view);
            }
        }
        if(!String.valueOf(training.get(Training.NAME,holder.getPosition(),0)).equals("null")&&!training.get(Training.NAME,holder.getPosition(),0).equals(null))holder.title.setText(String.valueOf(training.get(Training.NAME,holder.getPosition(),0)));
        //TODO maybe something add
        if(Double.valueOf(training.get(Training.WEIGHT,holder.getPosition(),0).toString())!=0)holder.weightET.setText(String.valueOf(training.get(Training.WEIGHT,holder.getPosition(),0).toString()));
        if(Integer.valueOf(training.get(Training.REPS,holder.getPosition(),0).toString())!=0)holder.repsET.setText(String.valueOf(training.get(Training.REPS,holder.getPosition(),0).toString()));
        if(Integer.valueOf(training.get(Training.ROUND_NUMBER,holder.getPosition(),0).toString())!=0){
            holder.roundNumberET.setText(String.valueOf(training.get(Training.ROUND_NUMBER,holder.getPosition(),0).toString()));
            ((LinearLayout) holder.view).addView(View.inflate(context,R.layout.add_training_round_values,null));
        }

    }

    @Override
    public int getItemCount() {
        return training.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

}
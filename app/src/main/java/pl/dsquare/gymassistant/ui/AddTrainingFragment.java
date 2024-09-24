package pl.dsquare.gymassistant.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.db.AppDatabase;

public class AddTrainingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentLayout = inflater.inflate(R.layout.fragment_add_training,container,false);
        Button add = parentLayout.findViewById(R.id.add_training_adding_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrainingActionButton();
            }
        });
        Spinner trainings = parentLayout.findViewById(R.id.add_training_trainings_spinner);
        AppDatabase db = Room.databaseBuilder(inflater.getContext(),
                AppDatabase.class, AppDatabase.DB_NAME).build();

       // trainings.set);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrainingActionButton();
            }
        });
        return parentLayout;
    }

    private void addTrainingActionButton() {
    }

}

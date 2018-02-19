package com.example.daniel.gymassistant;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.daniel.values.SettingsValues;


public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        Preference dialogPreference1 = (Preference) getPreferenceScreen().findPreference(getResources().getString(R.string.key_start_mode_preferences));
        Preference  dialogPreference2 = (Preference) getPreferenceScreen().findPreference(getResources().getString(R.string.key_what_display_on_exercise_round));
        dialogPreference1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                utwórzDialog(1);  // 1 odnosi sie do trybu rozpocznij trening, 2 co wyświetlać przy seriach treningu jako podpowiedź
                return true;
            }
        });
        dialogPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                utwórzDialog(2);  // 1 odnosi sie do trybu rozpocznij trening, 2 co wyświetlać przy seriach treningu jako podpowiedź
                return true;
            }
        });
    }
    private void utwórzDialog(int buttonNumber){
        final Dialog dialog = new Dialog(Settings.this);
        String[] s= new String[3];
        dialog.setContentView(R.layout.dialog_radio_group_cancel_ok);
        dialog.setTitle("Wybierz Trening");
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group_start_mode);
        switch(buttonNumber){
            case 1:
                s= getResources().getStringArray(R.array.start_mode_dialog);
                break;
            case 2:
                s=getResources().getStringArray(R.array.what_display_on_exercise_round);
                break;
        }

        final RadioButton[] radioButtons = new RadioButton[s.length];
        for(int i=0;i<s.length;i++){
            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setText(s[i]);
            radioGroup.addView(radioButtons[i]);
        }
        Button okButton =(Button)  dialog.findViewById(R.id.ok);
        Button anulujButton=(Button) dialog.findViewById(R.id.cancel);
        switch(buttonNumber) {
            case 1: okButton.setOnClickListener(okButtonActionListenerStartTrainingMode(radioGroup, dialog)); break;
            case 2: okButton.setOnClickListener(okButtonActionListenerPodpowiedziDoSerii(radioGroup,dialog)); break;
        }
        anulujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private View.OnClickListener okButtonActionListenerStartTrainingMode(final RadioGroup radioGroup, final Dialog dialog) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if(radioButtonID!=-1) {
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);
                    switch(idx){
                        case 0:
                            SettingsValues.setValue(SettingsValues.TRAINING_START_MODE,getApplicationContext(),1);
                            break;
                        case 1:
                            SettingsValues.setValue(SettingsValues.TRAINING_START_MODE,getApplicationContext(),2);
                            break;
                    }
                    dialog.dismiss();
                }

            }
        };
        return listener;
    }

    private View.OnClickListener okButtonActionListenerPodpowiedziDoSerii(final RadioGroup radioGroup, final Dialog dialog) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if(radioButtonID!=-1) {
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);
                    switch(idx){
                        case 0:
                            SettingsValues.setValue(SettingsValues.DISPLAY_TIPS,getApplicationContext(),1);
                            break;
                        case 1:
                            SettingsValues.setValue(SettingsValues.DISPLAY_TIPS,getApplicationContext(),2);
                            break;
                        case 2:
                            SettingsValues.setValue(SettingsValues.DISPLAY_TIPS,getApplicationContext(),3);
                            break;
                    }
                    dialog.dismiss();
                }

            }
        };
        return listener;
    }



}

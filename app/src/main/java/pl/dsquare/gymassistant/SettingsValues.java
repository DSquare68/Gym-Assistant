package pl.dsquare.gymassistant;

import android.content.Context;
import android.content.SharedPreferences;

import pl.dsquare.gymassistant.R;
/**
 * Created by Daniel on 2018-02-05.
 */

public class SettingsValues {
    static Context context;
    public SettingsValues(Context Context){
        this.context = Context;
    }
    /**
     * default value: 1 - mode "Start nearest training"
     *  2 -  "Ask which training start"
     */
    public static int startModeTraining = 1;
    public final static String TRAINING_START_MODE ="training_start_mode";
    /**
     * default value
     *  1 mode 'names'
     *  2 mode 'values'
     *  3 mode 'names & values'
     *  4 mode 'dropSet'
     */
    public static int  whichTextDisplayOnExerciseRound=1;
    public final static String DISPLAY_TIPS ="display_tips";

    public final static String FIRST_OPEN_APP = "first_open_app";
    public final static String DROPSET="drop_set";
    public final static int firstOpenApp= 1;

    public static  void setValue(String key, Context context, int newValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.title_shared_preferences_settings), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, newValue);
        editor.commit();
    }

    public static int getValue(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.title_shared_preferences_settings), Context.MODE_PRIVATE);
        int value=0;
        switch (key){
            case TRAINING_START_MODE:value= startModeTraining; break;
            case DISPLAY_TIPS:value=whichTextDisplayOnExerciseRound; break;
            case FIRST_OPEN_APP:value=firstOpenApp; break;

        }
        return sharedPreferences.getInt(key,value);
    }

}
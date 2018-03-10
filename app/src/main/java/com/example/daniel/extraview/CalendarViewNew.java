package com.example.daniel.extraview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.gymassistant.R;
import com.example.daniel.procedures.DateTraining;

import java.util.Date;

/**
 * Created by Daniel on 2017-07-27.
 */

public class CalendarViewNew extends LinearLayout {
    public LinearLayout[] weekLayout = new LinearLayout[6];
    static String[] monthNames;
    static Date today = new Date();
    static int whichMonth =0;
    public CalendarViewNew(Context context) {
        super(context,null);
        monthNames =context.getResources().getStringArray(R.array.month);
        init(context);
        setButtonListener();
    }

    private void setButtonListener() {
        ImageView back = findViewById(R.id.back);
        ImageView forward = findViewById(R.id.forward);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                today = new Date(today.getYear(), today.getMonth()-1, today.getDate(), today.getHours(), today.getMinutes(), today.getSeconds());
                whichMonth--;
                init(getContext());
            }
        });
        forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(today.getDate()!=31)
                    today = new Date(today.getYear(), today.getMonth()+1, today.getDate(), today.getHours(), today.getMinutes(), today.getSeconds());
                else today = new Date(today.getYear(), today.getMonth()+1, today.getDate()-1, today.getHours(), today.getMinutes(), today.getSeconds());
                whichMonth++;
                init(getContext());

            }
        });
    }


    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar,this);
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(getContext());
        TrainingValue[] trainingValues = wtd.getAllTrainings();
        TextView monthName =findViewById(R.id.textView4);
        monthName.setText(monthNames[today.getMonth()]+" "+ String.valueOf(today.getYear()+1900));
        DateTraining trainingDate = new DateTraining(getContext());

        Date helpDay=null;
        helpDay = new Date(today.getYear(), today.getMonth(),0);
        int pompomDay=helpDay.getDay();
        if(pompomDay!=0) pompomDay--; else pompomDay=6;
        helpDay = new Date(helpDay.getYear(),helpDay.getMonth(),helpDay.getDate()-pompomDay);
        if(helpDay.getDate()==1){
            helpDay = new Date(helpDay.getYear(),helpDay.getMonth(),helpDay.getDate()-7);
        }
        int sum=0,max=0;
        for(int i=0;i<trainingValues.length;i++){
            int numberOfTrainingsInMonth =trainingDate.readNumberOfTrainingsInMonth(trainingValues[i]);
            sum+=numberOfTrainingsInMonth;
            if(max<numberOfTrainingsInMonth) max=numberOfTrainingsInMonth;
        }
        Date trainingDates[][] = new Date[trainingValues.length][max];
        int k,g=0;
        String[] trainingNames = new String[sum];
        for(int j=0;j<trainingValues.length;j++){
            k=0;
            for(int i = 0; i<trainingDate.readNumberOfTrainingsInMonth(trainingValues[j]); i++,k++){
                trainingDates[j][k] = trainingDate.readDateFromString(trainingDate.switchYearWithDay(trainingDate.getNearestTrainingDateInMonth(trainingValues[j],k, whichMonth)));
                if(trainingDates[j][k]!=null&&trainingDate.readDateFromString(trainingValues[j].getFirstDayTraining()).after(trainingDates[j][k])){
                    trainingDates[j][g]= new Date(trainingDates[j][g].getYear()+30,0,0);
                    trainingNames[g]="";
                }
            }
            trainingNames[j] =trainingValues[j].getTrainingName();
        }
        trainingDates = changeNull(trainingDates);
        trainingDates =  sort(trainingDates);
        trainingDates = correctDate(trainingDates, helpDay,trainingNames);
        boolean noTraining;
        int[] tabLength = new int[trainingValues.length];
        for (int i=0;i<tabLength.length;i++){
            tabLength[i]=0;
        }
        for(int i=0;i<6;i++) {
            weekLayout[i] =(LinearLayout) ((LinearLayout)getChildAt(0)).getChildAt(i+2);
            weekLayout[i].removeAllViews();
            for(int j=0;j<7;j++){
                noTraining=true;
                for(int p=0;p<tabLength.length;p++) {
                    if (i == 0 && helpDay.getDate() > 20) {
                        if (tabLength[p] < trainingDates[p].length && trainingDate.getDate(helpDay).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            noTraining=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), wtd.get(trainingNames[p]), false, helpDay));
                        }
                        else if(p==tabLength.length-1&&noTraining)
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), null, false, helpDay));
                    } else if (i == 4 && helpDay.getDate() < 6) {
                        if (tabLength[p] < trainingDates[p].length && trainingDate.getDate(helpDay).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            noTraining=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), wtd.get(trainingNames[p]), false, helpDay));
                        }
                        else if(p==tabLength.length-1&&noTraining)
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), null, false, helpDay));
                    } else if (i == 5 && helpDay.getDate() < 25) {
                        if (tabLength[p]< trainingDates[p].length &&  trainingDate.getDate(helpDay).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            noTraining=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), wtd.get(trainingNames[p]), false, helpDay));
                        }
                        else if(p==tabLength.length-1&&noTraining)
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), null, false, helpDay));
                    } else if (tabLength[p] < trainingDates[p].length) {
                        if (trainingDate.getDate(helpDay).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            noTraining=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), wtd.get(trainingNames[p]), true, helpDay));
                        }
                        else if(p==tabLength.length-1&&noTraining)
                            weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), null, true, helpDay));
                    } else if(p==tabLength.length-1&&noTraining){
                        weekLayout[i].addView(new WeekDayButton(getContext(), helpDay.getDate(), null, true, helpDay));
                    }


                }
                helpDay = new Date(helpDay.getYear(), helpDay.getMonth(), helpDay.getDate() + 1);
            }
        }

    }

    private Date[][] correctDate(Date[][] dataTrainingsDate, Date helpDay, String[] trainingsNames) {
        String trainingMode;
        TrainingNamesDatabase tnd = new TrainingNamesDatabase(getContext());
        int coIle;
        boolean first;
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(getContext());
        for(int j=0;j<dataTrainingsDate.length;j++) {
            trainingMode= wtd.getByTrainingID(tnd.getIndex(trainingsNames[j])).getSchedule();
            coIle=wtd.getByTrainingID(tnd.getIndex(trainingsNames[j])).getRepetition();
            String[] scheduleRules = getResources().getStringArray(R.array.repetition);
            for(int i=0;i<scheduleRules.length;i++){
                if(scheduleRules[i].equals(trainingMode)&&(i==0||i==1)){
                    first =true;
                    addDays(true,dataTrainingsDate,helpDay,trainingsNames,first,j,7*(i+1));
                    addDays(false,dataTrainingsDate,helpDay,trainingsNames,first,j,7*(i+1));
                } else if(scheduleRules[i].equals(trainingMode)&&(i==3)){
                    first =true;
                    while (helpDay.after(dataTrainingsDate[j][0])) {
                        if (helpDay.after(dataTrainingsDate[j][0])) {
                            do {
                                dataTrainingsDate[j][0] = new Date(dataTrainingsDate[j][0].getYear(), dataTrainingsDate[j][0].getMonth(), dataTrainingsDate[j][0].getDate() + coIle);
                            }
                            while (dataTrainingsDate[j][dataTrainingsDate[j].length - 1].after(dataTrainingsDate[j][0]) || helpDay.after(dataTrainingsDate[j][0]));
                            if(first==true&&!(dataTrainingsDate[j][dataTrainingsDate[j].length-1].equals(dataTrainingsDate[j][0]))){
                                first=false;
                            } else {

                                dataTrainingsDate[j][0] = new Date(dataTrainingsDate[j][0].getYear(), dataTrainingsDate[j][0].getMonth(), dataTrainingsDate[j][0].getDate() + coIle);
                            }
                            dataTrainingsDate = sort(dataTrainingsDate);
                        }

                    }
                    if(dataTrainingsDate[j][dataTrainingsDate[j].length-1].equals(dataTrainingsDate[j][dataTrainingsDate[j].length-2])){
                        dataTrainingsDate[j][dataTrainingsDate[j].length-1]= new Date(dataTrainingsDate[j][0].getYear(), dataTrainingsDate[j][0].getMonth(), dataTrainingsDate[j][0].getDate() + coIle);
                    }
                }
            }
        }
        return dataTrainingsDate;
    }

    private void addDays(boolean adding, Date[][] datesTraining, Date help, String[] trainingsNames, boolean first, int j, int ile){
        int compared,ended;
        boolean condition, count;
        Date helpDay= new Date(help.getYear(),help.getMonth(),help.getDate()+41);
        if(!adding) {ile=-ile; compared=datesTraining[0].length-1;ended=0; } else {compared=0;ended=datesTraining[0].length-1;}
        Date pomDni = new Date(150, 1, 1);
       for(int i=0;i<datesTraining[0].length;i++){
           if(pomDni.equals(datesTraining[j][ended])) ended--;
           if(pomDni.equals(datesTraining[j][compared])) compared--;
       }
       if(adding){ count=help.after(datesTraining[j][compared]);} else {count=helpDay.before(datesTraining[j][compared]);}
        while (count) {
            if (count) {
                do {
                    datesTraining[j][compared] = new Date(datesTraining[j][compared].getYear(), datesTraining[j][compared].getMonth(), datesTraining[j][compared].getDate() + ile);
                    if(adding){
                        condition = datesTraining[j][ended].after(datesTraining[j][compared]) || help.after(datesTraining[j][compared]);
                    } else{

                        condition = datesTraining[j][ended].before(datesTraining[j][compared]) || helpDay.before(datesTraining[j][compared]);
                    }
                }
                while (condition);
                if(first==true&&!(datesTraining[j][ended].equals(datesTraining[j][compared]))){
                    first=false;
                } else {
                    datesTraining[j][compared] = new Date(datesTraining[j][compared].getYear(), datesTraining[j][compared].getMonth(), datesTraining[j][compared].getDate() + ile);
                }
                datesTraining = sort(datesTraining);
            }
            if(adding){count=help.after(datesTraining[j][compared]);} else{count=helpDay.before(datesTraining[j][compared]);}

        }
    }

    private Date[][] sort(Date[][] dates) {
        int j=0;
        Date v;
        for(int k=0;k<dates.length;k++) {
            for (int i = 1; i < dates[k].length; i++) {
                j = i;
                v = dates[k][i];
                while ((j > 0) && (dates[k][j - 1].after(v))) {
                    dates[k][j] = dates[k][j - 1];
                    j--;
                }
                dates[k][j] = v;
            }
        }
        return dates;
    }
    private Date[][] changeNull(Date[][] dates){
        for(int k=0;k<dates.length;k++) {
            for (int i = 1; i < dates[k].length; i++) {
                if (dates[k][i] == null) dates[k][i] = new Date(150, 1, 1);
            }
        }
        return dates;
    }
}

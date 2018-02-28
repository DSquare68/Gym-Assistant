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
    //Todo: czy wyświetlać skończone treningi???
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
        TrainingValue[] trainingValues = wtd.getAll();
        TextView monthName =findViewById(R.id.textView4);
        monthName.setText(monthNames[today.getMonth()]+" "+ String.valueOf(today.getYear()+1900));
        DateTraining trainingDate = new DateTraining(getContext());

        Date pomPondziałek=null;
        pomPondziałek = new Date(today.getYear(), today.getMonth(),0);
        int pompomDzień=pomPondziałek.getDay();
        if(pompomDzień!=0) pompomDzień--; else pompomDzień=6;
        pomPondziałek = new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()-pompomDzień);
        if(pomPondziałek.getDate()==1){
            pomPondziałek = new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()-7);
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
        trainingDates = poprawDate(trainingDates, pomPondziałek,trainingNames);
        boolean braktreningu=true;
        int[] tabLength = new int[trainingValues.length];
        for (int i=0;i<tabLength.length;i++){
            tabLength[i]=0;
        }
        for(int i=0;i<6;i++) {
            weekLayout[i] =(LinearLayout) ((LinearLayout)getChildAt(0)).getChildAt(i+2);
            weekLayout[i].removeAllViews();
            for(int j=0;j<7;j++){
                braktreningu=true;
                for(int p=0;p<tabLength.length;p++) {
                    if (i == 0 && pomPondziałek.getDate() > 20) {
                        if (tabLength[p] < trainingDates[p].length && trainingDate.getDate(pomPondziałek).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingNames[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        //pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (i == 4 && pomPondziałek.getDate() < 6) {
                        if (tabLength[p] < trainingDates[p].length && trainingDate.getDate(pomPondziałek).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingNames[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (i == 5 && pomPondziałek.getDate() < 25) {
                        if (tabLength[p]< trainingDates[p].length &&  trainingDate.getDate(pomPondziałek).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingNames[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (tabLength[p] < trainingDates[p].length) {
                        if (trainingDate.getDate(pomPondziałek).equals(trainingDate.getDate(trainingDates[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingNames[p]), true, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, true, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if(p==tabLength.length-1&&braktreningu){
                        weekLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, true, pomPondziałek));
                        //pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    }


                }
                pomPondziałek = new Date(pomPondziałek.getYear(), pomPondziałek.getMonth(), pomPondziałek.getDate() + 1);
            }
        }

    }

    private Date[][] poprawDate(Date[][] dataTreninguDate, Date pomPondziałek, String[] nazwyTreningów) {// przerobic całkowicie
        String rodzajTreningu;
        TrainingNamesDatabase tnd = new TrainingNamesDatabase(getContext());
        int coIle=0;
        boolean pierwsza =true;
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(getContext());
        for(int j=0;j<dataTreninguDate.length;j++) {
            rodzajTreningu= wtd.getByTrainingID(tnd.getIndex(nazwyTreningów[j])).getSchedule();
            coIle=wtd.getByTrainingID(tnd.getIndex(nazwyTreningów[j])).getRepetition();
            String[] scheduleRules = getResources().getStringArray(R.array.repetition);
            for(int i=0;i<scheduleRules.length;i++){
                if(scheduleRules[i].equals(rodzajTreningu)&&(i==0||i==1)){
                    pierwsza =true;
                    dodajDni(true,dataTreninguDate,pomPondziałek,nazwyTreningów,pierwsza,j,7*(i+1));
                    dodajDni(false,dataTreninguDate,pomPondziałek,nazwyTreningów,pierwsza,j,7*(i+1));
                } else if(scheduleRules[i].equals(rodzajTreningu)&&(i==3)){
                    pierwsza =true;
                    while (pomPondziałek.after(dataTreninguDate[j][0])) {
                        if (pomPondziałek.after(dataTreninguDate[j][0])) { // przypadek gdy równy
                            do {
                                dataTreninguDate[j][0] = new Date(dataTreninguDate[j][0].getYear(), dataTreninguDate[j][0].getMonth(), dataTreninguDate[j][0].getDate() + coIle);
                            }
                            while (dataTreninguDate[j][dataTreninguDate[j].length - 1].after(dataTreninguDate[j][0]) || pomPondziałek.after(dataTreninguDate[j][0]));
                            if(pierwsza==true&&!(dataTreninguDate[j][dataTreninguDate[j].length-1].equals(dataTreninguDate[j][0]))){
                                pierwsza=false;
                            } else {

                                dataTreninguDate[j][0] = new Date(dataTreninguDate[j][0].getYear(), dataTreninguDate[j][0].getMonth(), dataTreninguDate[j][0].getDate() + coIle);
                            }
                            dataTreninguDate = sort(dataTreninguDate);
                        }

                    }
                    if(dataTreninguDate[j][dataTreninguDate[j].length-1].equals(dataTreninguDate[j][dataTreninguDate[j].length-2])){
                        dataTreninguDate[j][dataTreninguDate[j].length-1]= new Date(dataTreninguDate[j][0].getYear(), dataTreninguDate[j][0].getMonth(), dataTreninguDate[j][0].getDate() + coIle);
                    }
                }
            }
        }
        return dataTreninguDate;
    }

    private void dodajDni(boolean adding, Date[][] datesTraining, Date help, String[] nazwyTreningów, boolean firts, int j, int ile){
        int compared,ended;
        boolean condition, count;
        Date pomPondziałek2= new Date(help.getYear(),help.getMonth(),help.getDate()+41);
        if(!adding) {ile=-ile; compared=datesTraining[0].length-1;ended=0; } else {compared=0;ended=datesTraining[0].length-1;}
        Date pomDni = new Date(150, 1, 1);
       for(int i=0;i<datesTraining[0].length;i++){
           if(pomDni.equals(datesTraining[j][ended])) ended--;
           if(pomDni.equals(datesTraining[j][compared])) compared--;
       }
       if(adding){ count=help.after(datesTraining[j][compared]);} else {count=pomPondziałek2.before(datesTraining[j][compared]);}
        while (count) {
            if (count) {
                do {
                    datesTraining[j][compared] = new Date(datesTraining[j][compared].getYear(), datesTraining[j][compared].getMonth(), datesTraining[j][compared].getDate() + ile);
                    if(adding){
                        condition = datesTraining[j][ended].after(datesTraining[j][compared]) || help.after(datesTraining[j][compared]);
                    } else{

                        condition = datesTraining[j][ended].before(datesTraining[j][compared]) || pomPondziałek2.before(datesTraining[j][compared]);
                    }
                }
                while (condition);
                if(firts==true&&!(datesTraining[j][ended].equals(datesTraining[j][compared]))){
                    firts=false;
                } else {
                    datesTraining[j][compared] = new Date(datesTraining[j][compared].getYear(), datesTraining[j][compared].getMonth(), datesTraining[j][compared].getDate() + ile);
                }
                datesTraining = sort(datesTraining);
            }
            if(adding){count=help.after(datesTraining[j][compared]);} else{count=pomPondziałek2.before(datesTraining[j][compared]);}

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

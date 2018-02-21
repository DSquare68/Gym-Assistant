package com.example.daniel.extraview;

import android.content.Context;
import android.util.Log;
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

public class Calendar extends LinearLayout {
    public LinearLayout[] tydzieńLayout= new LinearLayout[6];
    static String[] nazwyMiesięcy;
    static Date dataAktualna = new Date();
    static int któryMiesiąc=0;
    //Todo: czy wyświetlać skończone treningi???
    public Calendar(Context context) {
        super(context,null);
        nazwyMiesięcy=context.getResources().getStringArray(R.array.month);
        init(context);
        ustawLisenerButton();
    }

    private void ustawLisenerButton() {
        ImageView lewy = findViewById(R.id.back);
        ImageView prawy = findViewById(R.id.forward);
        lewy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataAktualna= new Date(dataAktualna.getYear(),dataAktualna.getMonth()-1,dataAktualna.getDate(),dataAktualna.getHours(),dataAktualna.getMinutes(),dataAktualna.getSeconds());
                któryMiesiąc--;
                init(getContext());
            }
        });
        prawy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataAktualna.getDate()!=31)dataAktualna= new Date(dataAktualna.getYear(),dataAktualna.getMonth()+1,dataAktualna.getDate(),dataAktualna.getHours(),dataAktualna.getMinutes(),dataAktualna.getSeconds());
                else dataAktualna= new Date(dataAktualna.getYear(),dataAktualna.getMonth()+1,dataAktualna.getDate()-1,dataAktualna.getHours(),dataAktualna.getMinutes(),dataAktualna.getSeconds());
                któryMiesiąc++;
                init(getContext());

            }
        });
    }


    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar,this);
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(getContext());
        TrainingValue[] trainingValues = wtd.getAll();
        TextView nazwaMiesiąca =findViewById(R.id.textView4);
        nazwaMiesiąca.setText(nazwyMiesięcy[dataAktualna.getMonth()]+" "+ String.valueOf(dataAktualna.getYear()+1900));
        DateTraining dataTreningu = new DateTraining(getContext());
        Date pomPondziałek=null;
        pomPondziałek = new Date(dataAktualna.getYear(),dataAktualna.getMonth(),0);
        int pompomDzień=pomPondziałek.getDay();
        if(pompomDzień!=0) pompomDzień--; else pompomDzień=6;
        pomPondziałek = new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()-pompomDzień);
        if(pomPondziałek.getDate()==1){
            pomPondziałek = new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()-7);
        }
        int sum=0,max=0;
        for(int i=0;i<trainingValues.length;i++){
            sum+=dataTreningu.readNumberOfTrainingsInMonth(dataAktualna,trainingValues[i],pomPondziałek);
            if(max<dataTreningu.readNumberOfTrainingsInMonth(dataAktualna,trainingValues[i],pomPondziałek)) max=dataTreningu.readNumberOfTrainingsInMonth(dataAktualna,trainingValues[i],pomPondziałek);
        }
        Date dataTreninguDate[][] = new Date[trainingValues.length][max];
        int k=0,g=0;
        String[] trainingName = new String[sum];
        for(int j=0;j<trainingValues.length;j++){
            k=0;
            for(int i = 0; i<dataTreningu.readNumberOfTrainingsInMonth(dataAktualna,trainingValues[j],pomPondziałek); i++,k++){
                dataTreninguDate[j][k] = dataTreningu.readDateFromString(dataTreningu.switchYearWithDay(dataTreningu.getNearestTrainingDateInMonth(trainingValues[j],k,któryMiesiąc)));
                if(dataTreninguDate[j][k]!=null&&dataTreningu.readDateFromString(trainingValues[j].getFirstDayTraining()).after(dataTreninguDate[j][k])){
                    dataTreninguDate[j][g]= new Date(dataTreninguDate[j][g].getYear()+30,0,0);
                    trainingName[g]="";
                }
            }
            trainingName[j] =trainingValues[j].getTrainingName();
        }
        dataTreninguDate = changeNull(dataTreninguDate);
        dataTreninguDate=  sort(dataTreninguDate);
        dataTreninguDate = poprawDate(dataTreninguDate, pomPondziałek,trainingName);

        k=0;
        g = 0;
       // poniedziałek=dataAktualna;


        //pomPondziałek=new Date(poniedziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()-(new Date(poniedziałek.getYear(),poniedziałek.getMonth(),-1).getDay()));
        boolean braktreningu=true;
        int[] tabLength = new int[trainingValues.length];
        for (int i=0;i<tabLength.length;i++){
            tabLength[i]=0;
        }
        for(int i=0;i<6;i++) {
            tydzieńLayout[i] =(LinearLayout) ((LinearLayout)getChildAt(0)).getChildAt(i+2);
            tydzieńLayout[i].removeAllViews();
            for(int j=0;j<7;j++){
                braktreningu=true;
                for(int p=0;p<tabLength.length;p++) {
                    if (i == 0 && pomPondziałek.getDate() > 20) {
                        if (tabLength[p] < dataTreninguDate[p].length && dataTreningu.getDate(pomPondziałek).equals(dataTreningu.getDate(dataTreninguDate[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingName[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        //pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (i == 4 && pomPondziałek.getDate() < 6) {
                        if (tabLength[p] < dataTreninguDate[p].length && dataTreningu.getDate(pomPondziałek).equals(dataTreningu.getDate(dataTreninguDate[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingName[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (i == 5 && pomPondziałek.getDate() < 25) {
                        if (tabLength[p]< dataTreninguDate[p].length &&  dataTreningu.getDate(pomPondziałek).equals(dataTreningu.getDate(dataTreninguDate[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingName[p]), false, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, false, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if (tabLength[p] < dataTreninguDate[p].length) {
                        if (dataTreningu.getDate(pomPondziałek).equals(dataTreningu.getDate(dataTreninguDate[p][tabLength[p]]))) {
                            tabLength[p]++;
                            braktreningu=false;
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), wtd.get(trainingName[p]), true, pomPondziałek));
                        }
                        else if(p==tabLength.length-1&&braktreningu)
                            tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, true, pomPondziałek));
                        // pomPondziałek= new Date(pomPondziałek.getYear(),pomPondziałek.getMonth(),pomPondziałek.getDate()+1);
                    } else if(p==tabLength.length-1&&braktreningu){
                        tydzieńLayout[i].addView(new WeekDayButton(getContext(), pomPondziałek.getDate(), null, true, pomPondziałek));
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
            Log.d("adfsaa",nazwyTreningów[j]);
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

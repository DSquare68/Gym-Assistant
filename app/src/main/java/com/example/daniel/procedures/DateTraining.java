package com.example.daniel.procedures;

import android.content.Context;
import java.util.Calendar;
import android.util.Log;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.gymassistant.R;


import java.util.Date;
import java.util.Scanner;

/**
 * Created by Daniel on 2017-05-11.
 */

public class DateTraining {
    private Context context;
    static TrainingValuesDatabase trainingValuesDatabase;
    public DateTraining(Context context){

        this.context =context;
        this.trainingValuesDatabase = new TrainingValuesDatabase(context);
    }
    final static int ONE_WEEK=0;
    final static int TWO_WEEKS=1;
    final static int MONTHLY =2;
    final static int X_DAYS=3;

    public String getNearestTrainingDate(TrainingValue trainingValue){ //
        Calendar calendar = Calendar.getInstance();
        java.util.Date trainingDate=null;
        int scheduleValue =setScheduleValue(trainingValue);
        int dayValue = getDayValue(calendar.get(Calendar.DAY_OF_WEEK),trainingValue);
        Log.d(trainingValue.getTrainingName(), String.valueOf(scheduleValue)+"   "+String.valueOf(dayValue)+"   "+String.valueOf(calendar.get(Calendar.DATE)+(dayValue-calendar.get(Calendar.DAY_OF_WEEK))));
        switch(scheduleValue){
            case ONE_WEEK:
                trainingDate= new Date(calendar.get(Calendar.YEAR)-1900,calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)+(dayValue-calendar.get(Calendar.DAY_OF_WEEK))+1);
                break;
            case TWO_WEEKS:
                trainingDate= new Date(calendar.get(Calendar.YEAR)-1900,calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)+(dayValue-calendar.get(Calendar.DAY_OF_WEEK)+1)+addWeek(trainingValue));
                break;
            case MONTHLY:
                trainingDate = getNextTrainingMonthly(trainingValue,dayValue);
                break;
            case X_DAYS:
                trainingDate = getNextTrainingXDays(trainingValue,0);
                break;
        }
        updataTrainingDates(trainingValue,trainingDate);
        return  trainingDate.getDate()+"."+(trainingDate.getMonth()+1)+"."+(calendar.get(Calendar.YEAR));
    }

    public String[] getNearestDatesOfTrainings(TrainingValue trainingValue, int position) {
        String[] dates = new String[7];
        int l=0;
        Calendar calendar = Calendar.getInstance();
        java.util.Date trainingDate=null;
        int scheduleValue =setScheduleValue(trainingValue);
        int[] dayValues = getDayValues(calendar.get(Calendar.DAY_OF_WEEK),trainingValue);
        while(dayValues[l]!=100) {
            switch (scheduleValue) {
                case ONE_WEEK:
                    trainingDate = new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + (dayValues[l] - calendar.get(Calendar.DAY_OF_WEEK )+1) + 7*position);
                    updataTrainingDates(trainingValue, trainingDate);
                    break;
                case TWO_WEEKS:
                    trainingDate = new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + (dayValues[l] - calendar.get(Calendar.DAY_OF_WEEK )+1) + addWeek(trainingValue) + (position>=2 ? 14 :0));
                    break;
                case MONTHLY:
                    trainingDate = getNextTrainingMonthly(trainingValue,dayValues[l]);
                    break;
                case X_DAYS:
                    for(int i=0; i<7*4/trainingValue.getRepetition();i++) {
                        trainingDate = getNextTrainingXDays(trainingValue, i);
                        dates[l++] = trainingDate.getDate() + "." + (trainingDate.getMonth() + 1) + "." + (calendar.get(Calendar.YEAR));
                    }
                    break;
            }
            if(scheduleValue!=X_DAYS) dates[l++] = trainingDate.getDate() + "." + (trainingDate.getMonth() + 1) + "." + (calendar.get(Calendar.YEAR));
        }
        String[] result = new String[l];
        for(int i=0;i<result.length;i++){
            result[i]=dates[i];
        }
        return result;
    }
    private Date getNextTrainingXDays(TrainingValue trainingValue,int i) {
        int repetitions = trainingValue.getRepetition();
        Date firstTraining = readDateFromString(trainingValue.getFirstDayTraining());
        Date today = new Date();
        while(today.after(firstTraining)){
            firstTraining= new Date(firstTraining.getYear(),firstTraining.getMonth(),firstTraining.getDate()+repetitions);
        }
        firstTraining = new Date(firstTraining.getYear(),firstTraining.getMonth(),firstTraining.getDate()+repetitions*i);
        return  firstTraining;
    }

    private Date getNextTrainingMonthly(TrainingValue trainingValue, int dayValue) {
        if(dayValue>6) dayValue-=7;
        Date firstTraining = readDateFromString(trainingValue.getFirstDayTraining());
        Date today = new Date();
        while(today.after(firstTraining)) {
            firstTraining = new Date(firstTraining.getYear(), firstTraining.getMonth() + 1, firstTraining.getDate());
        }
        Log.d("get monthly", String.valueOf(dayValue)+"  "+String.valueOf(firstTraining.toString()));
        while(dayValue!=firstTraining.getDay()){
            if(dayValue>firstTraining.getDay()){
                firstTraining = new Date(firstTraining.getYear(),firstTraining.getMonth(),firstTraining.getDate()+1);
            } else {
                firstTraining = new Date(firstTraining.getYear(),firstTraining.getMonth(),firstTraining.getDate()-1);
            }
        }
        return firstTraining;
    }
    private int addWeek(TrainingValue trainingValue) {
        Date date = new Date();
        Date firstTrainingDate = readDateFromString(trainingValue.getFirstDayTraining());
        if(firstTrainingDate==null) return 0;
        if(date.equals(firstTrainingDate)){return 0; }
        if(date.before(firstTrainingDate)) return 0;
        if(date.after(firstTrainingDate)){
            while (true) {
                if (date.before(firstTrainingDate)){
                    firstTrainingDate = new Date(firstTrainingDate.getYear(), firstTrainingDate.getMonth(), firstTrainingDate.getDate() -7);
                    if(date.after(firstTrainingDate)){
                        return 0;
                    }else {
                        return 7;
                    }
                }
                else {
                    firstTrainingDate = new Date(firstTrainingDate.getYear(), firstTrainingDate.getMonth(), firstTrainingDate.getDate() + 14);
                }
            }
        } else{
            while (true) {
                if (date.after(firstTrainingDate)) {
                    firstTrainingDate = new Date(firstTrainingDate.getYear(), firstTrainingDate.getMonth(), firstTrainingDate.getDate() -7);
                    if(date.before(firstTrainingDate)){
                        return 0;
                    }else {
                        return 7;
                    }
                }
                else {
                    firstTrainingDate = new Date(firstTrainingDate.getYear(), firstTrainingDate.getMonth(), firstTrainingDate.getDate() - 14);
                }
            }
        }
    }

    private int setScheduleValue(TrainingValue trainingValue) {
        String schedule = trainingValue.getSchedule();
        String[] scheduleArray = context.getResources().getStringArray(R.array.repetition);
        for(int j=0;j<scheduleArray.length;j++){
            if(schedule.equals(scheduleArray[j])){
                return j;
            }
        }
        return -1;
    }

    private int getDayValue(int todayDayNumber,TrainingValue trainingValue) {
        int[] daysValue  = new int[7];
        for (int j=0;j<daysValue.length;j++){
            daysValue[j]=100;
        }
        String[] shortDays = context.getResources().getStringArray(R.array.short_week_days);
        Scanner s = new Scanner(trainingValue.getWeekDays());
        for(int j=0;s.hasNext();j++) {
            String string = s.next();
            for(int l=0;l<shortDays.length;l++){
                if (shortDays[l].equals(string)){
                    daysValue[j]=(l==6 ? 0 : l+1);
                }
            }
        }
        int temp;
        for(int i=0;i<shortDays.length;i++){
            for(int j=1;j<shortDays.length-i;j++){
                if(daysValue[j-1] > daysValue[j]){
                    temp = daysValue[j-1];
                    daysValue[j-1] = daysValue[j];
                    daysValue[j] = temp;
                }
            }
        }
        int result=-1;
        for(int i=0;i<daysValue.length;i++){
            if(daysValue[i]==100) {
                return result<todayDayNumber ? result+7  : result;
            }else if(daysValue[i]==todayDayNumber){
                return daysValue[i];
            } else if(result==-1){
                result=daysValue[i];
            } else if(result!=-1&&todayDayNumber>daysValue[i]&&result>daysValue[i]){
                result=daysValue[i];
            } else if(result!=-1&&todayDayNumber<daysValue[i]&&result<daysValue[i]){
                result=daysValue[i];
            }
        }
        return result<todayDayNumber ? result+7  : result; // ugly but works
    }
    private int[] getDayValues(int todayDayNumber,TrainingValue trainingValue) {
        int[] daysValue = new int[7];
        for (int j = 0; j < daysValue.length; j++) {
            daysValue[j] = 100;
        }
        String[] shortDays = context.getResources().getStringArray(R.array.short_week_days);
        Scanner s = new Scanner(trainingValue.getWeekDays());
        for (int j = 0; s.hasNext(); j++) {
            String string = s.next();
            for (int l = 0; l < shortDays.length; l++) {
                if (shortDays[l].equals(string)) {
                    daysValue[j] = (l == 6 ? 0 : l + 1);
                }
            }
        }
        int temp;
        for (int i = 0; i < shortDays.length; i++) {
            for (int j = 1; j < shortDays.length - i; j++) {
                if (daysValue[j - 1] > daysValue[j]) {
                    temp = daysValue[j - 1];
                    daysValue[j - 1] = daysValue[j];
                    daysValue[j] = temp;
                }
            }
        }
        return daysValue;
    }

    public String getNearestTrainingDate(int i, TrainingValue trainingValue){
        java.util.Date date= new java.util.Date();
        java.util.Date trainingDate=null;
        String data="";
        int[] daysValue = new int[7];
        String schedule = trainingValue.getSchedule();
        int scheduleValue=0, howManyWeeksElapsed= howManyWeeksElapsed(date,trainingValue), howManyDaysElapsed = howManyDaysElapsed(date,trainingValue);
        for (int j=0;j<daysValue.length;j++){
            daysValue[j]=-1;
        }
        String[] shortDays = context.getResources().getStringArray(R.array.short_week_days);
        Scanner s = new Scanner(trainingValue.getWeekDays());
        for(int j=0;s.hasNext();j++) {
            String string = s.next();
            for(int l=0;l<shortDays.length;l++){
                if (shortDays[l].equals(string)){
                    daysValue[j]=l;
                }
            }
        }
        String[] scheduleArray = context.getResources().getStringArray(R.array.repetition);
        for(int j=0;j<scheduleArray.length;j++){
            if(schedule.equals(scheduleArray[j])){
                scheduleValue=j;
            }
        }

        for(int j=0;j<daysValue.length;j++){
            if(daysValue[j]==0){
                daysValue[j]=7;
                if(daysValue[j]>=(date.getDay()+1)&&(scheduleValue==1||scheduleValue==2)){
                    trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(daysValue[j]-date.getDay()+7*(howManyWeeksElapsed)));
                    if(date.getDay()==0){
                        trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+7*(scheduleValue-1));
                    }
                    updataTrainingDates(trainingValue,trainingDate);
                    break;
                }
            }
            if(daysValue[j]>=(date.getDay()-1)&&(scheduleValue==1||scheduleValue==2)){
                trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(daysValue[j]-date.getDay()+7*(howManyWeeksElapsed)));
                updataTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(daysValue[j]<(date.getDay()-1)&&(scheduleValue==1||scheduleValue==2)){
                trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(+7*(howManyWeeksElapsed+1)-(date.getDay()-daysValue[j])));
                updataTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(scheduleValue==3){
                if(readDateFromString(trainingValue.getFirstDayTraining()).after(date)) {trainingDate= readDateFromString(trainingValue.getFirstDayTraining());
                    updataTrainingDates(trainingValue,trainingDate);String dataTreninguString= switchYearWithDay(getDate(trainingDate)); dataTreninguString = removeZeros(dataTreninguString); Log.d("DFSAADFSASDAS", dataTreninguString); return dataTreninguString;  }
                    trainingDate= new java.util.Date(date.getYear(),date.getMonth()+1, nextTrainingNextMonth(date,trainingValue.getFirstDayTraining(),howManyDaysElapsed));
                updataTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(scheduleValue==4){
                trainingDate = new java.util.Date(date.getYear(),date.getMonth(), nextTraining(date,trainingValue.getFirstDayTraining(),trainingValue.getRepetition()));
                updataTrainingDates(trainingValue,trainingDate);
                break;
            }
        }
        if (trainingDate==null){
            trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(7-daysValue[0]-1+7*(scheduleValue-1)));
        }
        data=trainingDate.getDate()+"."+(trainingDate.getMonth()+1)+"."+(trainingDate.getYear()+1900);
        return data;
    }

    public String getNearestTrainingDateInMonth(TrainingValue wt, int któryTreningLiczy, int któryMiesiąc) {
            String trainingDate = getNearestTrainingDate(wt);
            int scheduleValue=0;
            String schedule = wt.getSchedule();
            String[] scheduleArray = context.getResources().getStringArray(R.array.repetition);
            for(int j=0;j<scheduleArray.length;j++){
                if(schedule.equals(scheduleArray[j])){
                    scheduleValue=j+1;
                }
            }
            if(scheduleValue==1) {trainingDate = addHowManyDays(trainingDate, któryTreningLiczy,któryMiesiąc); return trainingDate;}
            if(scheduleValue==3) {return trainingDate;}
            if(scheduleValue==4) {trainingDate=dodajIleDni(trainingDate,któryTreningLiczy,wt.getRepetition());return trainingDate;}
            if(scheduleValue==2) {trainingDate = addHowManyDays(trainingDate, któryTreningLiczy*2,któryMiesiąc); return trainingDate;}  else {return getNearestTrainingDate(wt);}
        }


    private String addHowManyDays(String dataTreningu, int któryTydzieńDoPrzodu, int któryMieciąc) {
        dataTreningu= switchYearWithDay(dataTreningu);
        java.util.Date data = readDateFromString(dataTreningu);
        // Log.d("d1111 2",data.toString());
        data = new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+7*któryTydzieńDoPrzodu+4*7*któryMieciąc);
        //Log.d("d1111 4",data.toString());
        dataTreningu = getDate(data);
        dataTreningu= switchYearWithDay(dataTreningu);
        dataTreningu = removeZeros(dataTreningu);
        return dataTreningu;
    }
    private String dodajIleDni(String dataTreningu, int któryTydzieńDoPrzodu, int ilośćDniDodawanych) {
        dataTreningu= switchYearWithDay(dataTreningu);
        java.util.Date data = readDateFromString(dataTreningu);
        // Log.d("d1111 2",data.toString());
        data = new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+ilośćDniDodawanych*któryTydzieńDoPrzodu);
        //Log.d("d1111 4",data.toString());
        dataTreningu = getDate(data);
        dataTreningu= switchYearWithDay(dataTreningu);
        dataTreningu = removeZeros(dataTreningu);
        return dataTreningu;
    }

    private String removeZeros(String pomdzien) {
        int ilKrope=0;
        boolean popKropka=false;
        for (int i=0;i<pomdzien.length();i++){
            if(pomdzien.charAt(i)=='.') {ilKrope++;popKropka=true;}
            if(ilKrope==0&&pomdzien.charAt(i)=='0'&&i==0) {pomdzien= pomdzien.substring(0,i) + pomdzien.substring(i + 1);}
            if(ilKrope==1&&pomdzien.charAt(i)=='0'&&popKropka==true) pomdzien= pomdzien.substring(0,i) + pomdzien.substring(i + 1);
            if(pomdzien.charAt(i)!='.')popKropka=false;
        }
        return pomdzien;
    }

    public TrainingValue getNearestTraining(){
        TrainingValue[] treningi = trainingValuesDatabase.getAll();
        String[] daty = new String[trainingValuesDatabase.getCount()];
        java.util.Date[] date = new java.util.Date[trainingValuesDatabase.getCount()];
        int a=0;
        for (int i = 0; i< trainingValuesDatabase.getCount(); i++){
            daty[i]= getNearestTrainingDate(treningi[i]);
            date[i]= readDateFromString(daty[i]);
        }
        for(int i = 0; i< trainingValuesDatabase.getCount()-1; i++){
            if(date[i].after(date[i+1])){
                a=i;
                break;
            }
        }
        if(treningi.length==0){
            return null;
        }
        return treningi[a];
    }
    private int howManyWeeksElapsed(Date date, TrainingValue trainingValue){
        if (trainingValue.getFirstDayTraining().equals("")) {return 0; }
        int number=0;
        Date lastTrainingDate = readDateFromString(trainingValue.getFirstDayTraining());
        if(date.equals(lastTrainingDate)){return 0; }
        if(date.after(lastTrainingDate)){
            while (true) {
                if (date.before(lastTrainingDate)) return number-1;
                else {
                    number++;
                    lastTrainingDate = new Date(lastTrainingDate.getYear(), lastTrainingDate.getMonth(), lastTrainingDate.getDate() + 7);
                }
            }
        } else{
            while (true) {
                if (date.after(lastTrainingDate)) return number-1;
                else {
                    number++;
                    lastTrainingDate = new Date(lastTrainingDate.getYear(), lastTrainingDate.getMonth(), lastTrainingDate.getDate() - 7);
                }
            }
        }

    }
    public int howManyDaysElapsed(Date date, TrainingValue trainingValue){
        Date lastDay;
        if (String.valueOf(trainingValue.getLastTrainingDayDate()).equals("")) {
            lastDay = readDateFromString(trainingValue.getFirstDayTraining());
        } else {
            lastDay = readDateFromString(trainingValue.getLastTrainingDayDate());
        }
        int number=0;
        if(date.equals(lastDay)) return 0;
        if(date.after(lastDay)){
            while (true) {
                if (date.before(lastDay)) return number-1;
                else {
                    number++;
                    lastDay = new Date(lastDay.getYear(), lastDay.getMonth(), lastDay.getDate() + 1);
                }
            }
        } else{
            while (true) {
                if (date.after(lastDay)) return number-1;
                else {
                    number++;
                    lastDay = new Date(lastDay.getYear(), lastDay.getMonth(), lastDay.getDate() - 1);
                }
            }
        }
    }
    private void updataTrainingDates(TrainingValue trainingValue, java.util.Date dataTreningu){
        if(String.valueOf(trainingValue.getFirstDayTraining()).equals(null)|| String.valueOf(trainingValue.getFirstDayTraining()).equals("")){
            trainingValue.setFirstDayTraining((dataTreningu.getYear()+1900)+"."+(dataTreningu.getMonth()+1)+"."+dataTreningu.getDate());
            trainingValuesDatabase.update(trainingValue);
        }
        if(String.valueOf(trainingValue.getLastTrainingDayDate()).equals(null)|| String.valueOf(trainingValue.getLastTrainingDayDate()).equals("")){
            trainingValue.setLastTrainingDayDate((dataTreningu.getYear()+1900)+"."+(dataTreningu.getMonth()+1)+"."+dataTreningu.getDate());
            trainingValuesDatabase.update(trainingValue);
        }
    }
    public java.util.Date readDateFromString(String data){
        if(data.equals("")||data.equals(null)) return null;
        int numberDay=0, month=0,year=0,j=0;
        String s="";
        if(data.charAt(4)=='.') data = switchYearWithDay(data);
        for(int i=0;i<data.length();i++){
            if(data.charAt(i)!='.'&&j==2){s=s+data.charAt(i);}if(i==data.length()-1){j++; year= Integer.valueOf(s);s="";}
            if(data.charAt(i)!='.'&&j==1){s=s+data.charAt(i);}else if(data.charAt(i)=='.'&&j==1){j++;month= Integer.valueOf(s);s="";}
            if(data.charAt(i)!='.'&&j==0){s=s+data.charAt(i);} else if (data.charAt(i)=='.'&&j==0){j++;numberDay= Integer.valueOf(s);s="";}
        }
        return new java.util.Date(year-1900,month-1,numberDay);
    }
    private java.util.Date readDateFromString(String data, String timeTraining) {
        int numberDay=0, month=0,year=0,j=0;
        String s="";
        if(data.charAt(4)=='.') data = switchYearWithDay(data);
        for(int i=0;i<data.length();i++){
            if(data.charAt(i)!='.'&&j==2){s=s+data.charAt(i);}if(i==data.length()-1){j++; year= Integer.valueOf(s);s="";}
            if(data.charAt(i)!='.'&&j==1){s=s+data.charAt(i);}else if(data.charAt(i)=='.'&&j==1){j++;month= Integer.valueOf(s);s="";}
            if(data.charAt(i)!='.'&&j==0){s=s+data.charAt(i);} else if (data.charAt(i)=='.'&&j==0){j++;numberDay= Integer.valueOf(s);s="";}
        }
        int sekundy=0, minuty=0,godziny=0;
        j=0;
        s="";
        for(int i=0;i<timeTraining.length();i++){
            if(timeTraining.charAt(i)!=':'&&j==2){s=s+timeTraining.charAt(i);}if(i==timeTraining.length()-1){j++; sekundy= Integer.valueOf(s);s="";}
            if(timeTraining.charAt(i)!=':'&&j==1){s=s+timeTraining.charAt(i);}else if(timeTraining.charAt(i)==':'&&j==1){j++;minuty= Integer.valueOf(s);s="";}
            if(timeTraining.charAt(i)!=':'&&j==0){s=s+timeTraining.charAt(i);} else if (timeTraining.charAt(i)==':'&&j==0){j++;godziny= Integer.valueOf(s);s="";}
        }
        return new java.util.Date(year-1900,month-1,numberDay,godziny,minuty,sekundy);
    }

    private int nextTrainingNextMonth(java.util.Date date, String firstTrainingDate, int ileTygodniMineło){
        int dayNumber;
        if(ileTygodniMineło<0) ileTygodniMineło=0;
        java.util.Date firtsDay= readDateFromString(firstTrainingDate);
        java.util.Date helpString =firtsDay;
        date = new Date(date.getYear(),date.getMonth(),date.getDate()+30-ileTygodniMineło);
        if(date.before(helpString)){
            return helpString.getDate();
        }
        while(true){
            if(date.before(helpString)){
                break;
            } else{
                helpString= new Date(helpString.getYear(),helpString.getMonth(),helpString.getDate()+7);
            }
        }
        dayNumber = helpString.getDate();
        return dayNumber;
    }
    private int nextTraining(Date date, String data, int coIle){
        int dayNumber=0;
        java.util.Date firtsDay= readDateFromString(data);
        java.util.Date helpString =firtsDay;
        if(date.before(helpString)){
            return helpString.getDate();
        }
        while(true){
            if(date.before(helpString)){
                break;
            } else{
                helpString= new Date(helpString.getYear(),helpString.getMonth(),helpString.getDate()+coIle);
            }
        }
        dayNumber = helpString.getDate();
        return dayNumber;
    }
    public String getDate(java.util.Date date){
        String miesiąc,dzień;
        if(date.getMonth()<9){
             miesiąc = "0"+ String.valueOf(date.getMonth()+1);
        } else {
            miesiąc = String.valueOf(date.getMonth()+1);
        }
        if(date.getDate()<=9){
            dzień = "0"+ String.valueOf(date.getDate());
        } else {
            dzień = String.valueOf(date.getDate());
        }
        return String.valueOf(String.valueOf(date.getYear()+1900)+"."+miesiąc+"."+dzień);
    }
    public String switchYearWithDay(String data){
        String finalData=""; int dayNumber=0,month=0,year=0;
        int i=0;
        boolean isYearFirst;
        if(data.charAt(2)=='.') isYearFirst=false; else isYearFirst=true;
        if(isYearFirst==true){
            for(int j=0;j<data.length();j++){
                if(data.charAt(j)!='.'&&i==2){finalData=finalData+data.charAt(j);} if(j==data.length()-1){i++; dayNumber= Integer.valueOf(finalData);finalData=""; }
                if(data.charAt(j)!='.'&&i==1){finalData=finalData+data.charAt(j);}else if(data.charAt(j)=='.'&&i==1){i++;month= Integer.valueOf(finalData);finalData="";}
                if(data.charAt(j)!='.'&&i==0){finalData=finalData+data.charAt(j);} else if (data.charAt(j)=='.'&&i==0){i++;year= Integer.valueOf(finalData);finalData="";}
            }
        } else{
            for(int j=0;j<data.length();j++){
                if(data.charAt(j)!='.'&&i==2){finalData=finalData+data.charAt(j);} if(j==data.length()-1){i++;year= Integer.valueOf(finalData);finalData="";}
                if(data.charAt(j)!='.'&&i==1){finalData=finalData+data.charAt(j);}else if(data.charAt(j)=='.'&&i==1){i++;month= Integer.valueOf(finalData);finalData="";}
                if(data.charAt(j)!='.'&&i==0){finalData=finalData+data.charAt(j);}else if (data.charAt(j)=='.'&&i==0){i++; dayNumber= Integer.valueOf(finalData);finalData="";}


            }
        }
        if(isYearFirst){
            finalData = String.format("%02d",dayNumber)+"."+ String.format("%02d",month)+"."+ String.valueOf(year);
        } else{
            finalData = String.valueOf(year)+"."+ String.format("%02d",month)+"."+ String.format("%02d",dayNumber);
        }
        return finalData;
    }


    public String readDate(String shortDaysWeekData, String coIle) {
        java.util.Date data = new java.util.Date();
        int[] shortDaysValue= new int[7];
        for(int i=0;i<shortDaysValue.length;i++){
            shortDaysValue[i]=-1;
        }
        Scanner s = new Scanner(shortDaysWeekData);
        String[] shortDaysWeek = context.getResources().getStringArray(R.array.short_week_days);
        for(int j=0;s.hasNext();j++) {
            String day = s.next();
            for(int i=0;i<shortDaysWeek.length;i++){
                if(day.equals(shortDaysWeek[i])){
                    shortDaysValue[j]=i;
                }
            }
        }
        int dzieńtygodnia=data.getDay();
        if (dzieńtygodnia==0) dzieńtygodnia=6; else dzieńtygodnia--;
        java.util.Date dzień=null;
        for (int i=0;i<7;i++){
            if(dzieńtygodnia==shortDaysValue[i]){
                dzień  = data;
            } else if (dzieńtygodnia<shortDaysValue[i]){
                dzień= new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+shortDaysValue[i]-dzieńtygodnia);
            }
        }
        if(dzień==null){
            for (int i=0;i<7;i++) {
                if (dzieńtygodnia > shortDaysValue[i]) {
                    if(shortDaysValue[i]==-1) break;
                    dzień = new java.util.Date(data.getYear(), data.getMonth(), data.getDate() + shortDaysValue[i] - dzieńtygodnia);
                }
            }
        }
        return getDate(dzień);
    }

    public OldTraining[][] lastTraining(OldTraining old, Context context){
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(context,old.getTrainingName());
        String[][] dane = dstd.getDateAndTime();
        java.util.Date dataTreningu= readDateFromString(old.getTrainingDate(),old.getTrainingTime());
        java.util.Date poprzednia=null;
        String[] wynik= new String[3];
        for(int i=0;i<dane.length;i++){
            poprzednia= readDateFromString(dane[i][0],dane[i][2]);
            if (poprzednia.before(dataTreningu)){
                wynik=dane[i];
                break;
            }
        }
        return dstd.getAll(wynik[0],wynik[2]);
    }

    public OldTraining[][] lastTraining(String data, String godzina, String name, Context context){
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(context,name);
        String[][] dane = dstd.getDateAndTime();
        java.util.Date trainingDate= readDateFromString(data,godzina);
        java.util.Date last=null;
        String[] wynik= new String[3];
        for(int i=0;i<dane.length;i++){
            last= readDateFromString(dane[i][0],dane[i][2]);
            if (last.before(trainingDate)){
                wynik=dane[i];
                break;
            }
        }
        if(wynik[0]==null) return null;
        return dstd.getAll(wynik[0],wynik[2]);
    }

    public OldTraining[][] lastTraining(String name, Context context){ // jeśli by działał tylko do rozpocznij trening to czy nie wystarczy wczytywać tylko ostatni trening?????
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(context,name);
        String[][] dane = dstd.getDateAndTime();
        if (dane==null) return null;
        dane = reverseTab(dane);
        java.util.Date trainingDate= new java.util.Date();//readDateFromString(data,godzina);
        java.util.Date last=null;
        String[] result= new String[3];
        for(int i=0;i<dane.length;i++){
            last= readDateFromString(dane[i][0],dane[i][2]);
            if (last.before(trainingDate)){
                result=dane[i];
                break;
            }
        }
        return dstd.getAll(result[0],result[2]);
    }

    private String[][] reverseTab(String[][] dane) {
        String[][] wynik = new String[dane.length][dane[0].length];
        for(int i=0;i<dane.length;i++){
            wynik[i]=dane[dane.length-1];
        }
        return wynik;
    }

    public int readNumberOfTrainingsInMonth(TrainingValue wt) {
        int  numberDT=0;// ilość Dni Trenignowych gdy trening jest co tydzień lub dwa np trzy, gdy jest pon, śr, sob
        String schedule = wt.getSchedule();
        int repetition=0;
        int [] daysValues = new int[7];
        for (int j=0;j<daysValues.length;j++){
            daysValues[j]=-1;
        }
        Scanner s = new Scanner(wt.getWeekDays());
        String[] shortDays = context.getResources().getStringArray(R.array.short_week_days);
        for(int j=0;s.hasNext();j++,numberDT++) {
            String shortDay=s.next();
            for(int k=0;k<shortDays.length;k++){
                if(shortDays[k].equals(shortDay)){
                    daysValues[j]=(k+1);
                    break;
                }
            }
        }
        String[] schedules = context.getResources().getStringArray(R.array.repetition);
        for(int i=0;i<schedules.length;i++){
            if(schedules[i].equals(schedule)){
                repetition=i+1;
                break;
            }
        }

        if(repetition==1){
            return numberDT*6;
        } else if (repetition==2){
            return 3*numberDT;
        }else if(repetition==4){
            return 6*7/wt.getRepetition()+1;
        } else if (repetition == 3) {
            return 1;
        }
        return -1;
    }

    private java.util.Date policzDateKoncowDlaMiesiąca(java.util.Date pierwsza) {
        java.util.Date date = new java.util.Date(pierwsza.getYear(),pierwsza.getMonth()+2,1);
        int  weekDay=date.getDay();
        if(weekDay==0){
            return date;
        } else{
            date= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+1);
        }
        return null;
    }


    public java.util.Date wczydajDateZeStringaIGodzine(String aktualny, String Czas) {
        java.util.Date data = readDateFromString(aktualny);
        data=dodajGodzine(data,Czas);
        return data;
    }
    private java.util.Date dodajGodzine(java.util.Date data, String czasS){
        String[] czas = wczytajWartościGodziny(czasS);
        return  new java.util.Date(data.getYear(),data.getMonth(),data.getDate(), Integer.valueOf(czas[0]), Integer.valueOf(czas[1]), Integer.valueOf(czas[2]));
    }

    private String[] wczytajWartościGodziny(String czas){
        String[] dane=new String[3];
        String finalData="";
        int j=0;
        for (int i=0;i<czas.length();i++){
            if(czas.charAt(i)!=':'&&j==2){finalData=finalData+czas.charAt(i);} if(i==czas.length()-1){j++;dane[2]=finalData;finalData="";}
            if(czas.charAt(i)!=':'&&j==1){finalData=finalData+czas.charAt(i);}else if(czas.charAt(i)==':'&&j==1){j++;dane[1]=finalData;finalData="";}
            if(czas.charAt(i)!=':'&&j==0){finalData=finalData+czas.charAt(i);}else if (czas.charAt(i)==':'&&j==0){j++; dane[0]=finalData;finalData="";}
        }
        return dane;
    }
}

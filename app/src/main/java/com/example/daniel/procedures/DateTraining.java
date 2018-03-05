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
        updateTrainingDates(trainingValue,trainingDate);
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
                    updateTrainingDates(trainingValue, trainingDate);
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
                    updateTrainingDates(trainingValue,trainingDate);
                    break;
                }
            }
            if(daysValue[j]>=(date.getDay()-1)&&(scheduleValue==1||scheduleValue==2)){
                trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(daysValue[j]-date.getDay()+7*(howManyWeeksElapsed)));
                updateTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(daysValue[j]<(date.getDay()-1)&&(scheduleValue==1||scheduleValue==2)){
                trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(+7*(howManyWeeksElapsed+1)-(date.getDay()-daysValue[j])));
                updateTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(scheduleValue==3){
                if(readDateFromString(trainingValue.getFirstDayTraining()).after(date)) {trainingDate= readDateFromString(trainingValue.getFirstDayTraining());
                    updateTrainingDates(trainingValue,trainingDate);String dataTrainingString= switchYearWithDay(getDate(trainingDate)); dataTrainingString = removeZeros(dataTrainingString); return dataTrainingString;  }
                    trainingDate= new java.util.Date(date.getYear(),date.getMonth()+1, nextTrainingNextMonth(date,trainingValue.getFirstDayTraining(),howManyDaysElapsed));
                updateTrainingDates(trainingValue,trainingDate);
                break;
            }
            if(scheduleValue==4){
                trainingDate = new java.util.Date(date.getYear(),date.getMonth(), nextTraining(date,trainingValue.getFirstDayTraining(),trainingValue.getRepetition()));
                updateTrainingDates(trainingValue,trainingDate);
                break;
            }
        }
        if (trainingDate==null){
            trainingDate= new java.util.Date(date.getYear(),date.getMonth(),date.getDate()+(7-daysValue[0]-1+7*(scheduleValue-1)));
        }
        data=trainingDate.getDate()+"."+(trainingDate.getMonth()+1)+"."+(trainingDate.getYear()+1900);
        return data;
    }

    public String getNearestTrainingDateInMonth(TrainingValue wt, int whichTraining, int whichMonth) {
            String trainingDate = getNearestTrainingDate(wt);
            int scheduleValue=0;
            String schedule = wt.getSchedule();
            String[] scheduleArray = context.getResources().getStringArray(R.array.repetition);
            for(int j=0;j<scheduleArray.length;j++){
                if(schedule.equals(scheduleArray[j])){
                    scheduleValue=j+1;
                }
            }
            if(scheduleValue==1) {trainingDate = addHowManyDays(trainingDate, whichTraining,whichMonth); return trainingDate;}
            if(scheduleValue==3) {return trainingDate;}
            if(scheduleValue==4) {trainingDate= addDays(trainingDate,whichTraining,wt.getRepetition());return trainingDate;}
            if(scheduleValue==2) {trainingDate = addHowManyDays(trainingDate, whichTraining*2,whichMonth); return trainingDate;}  else {return getNearestTrainingDate(wt);}
        }


    private String addHowManyDays(String dataTraining, int weekAhead, int whichMonth) {
        dataTraining= switchYearWithDay(dataTraining);
        java.util.Date data = readDateFromString(dataTraining);
        // Log.d("d1111 2",data.toString());
        data = new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+7*weekAhead+4*7*whichMonth);
        //Log.d("d1111 4",data.toString());
        dataTraining = getDate(data);
        dataTraining= switchYearWithDay(dataTraining);
        dataTraining = removeZeros(dataTraining);
        return dataTraining;
    }
    private String addDays(String dataTraining, int weekAhead, int daysAhead) {
        dataTraining= switchYearWithDay(dataTraining);
        java.util.Date data = readDateFromString(dataTraining);
        // Log.d("d1111 2",data.toString());
        data = new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+daysAhead*weekAhead);
        //Log.d("d1111 4",data.toString());
        dataTraining = getDate(data);
        dataTraining= switchYearWithDay(dataTraining);
        dataTraining = removeZeros(dataTraining);
        return dataTraining;
    }

    private String removeZeros(String helpDay) {
        int numberOfDots=0;
        boolean helpDot=false;
        for (int i=0;i<helpDay.length();i++){
            if(helpDay.charAt(i)=='.') {numberOfDots++;helpDot=true;}
            if(numberOfDots==0&&helpDay.charAt(i)=='0'&&i==0) {helpDay= helpDay.substring(0,i) + helpDay.substring(i + 1);}
            if(numberOfDots==1&&helpDay.charAt(i)=='0'&&helpDot==true) helpDay= helpDay.substring(0,i) + helpDay.substring(i + 1);
            if(helpDay.charAt(i)!='.')helpDot=false;
        }
        return helpDay;
    }

    public TrainingValue getNearestTraining(){
        TrainingValue[] trainings = trainingValuesDatabase.getAll();
        String[] data = new String[trainingValuesDatabase.getCount()];
        java.util.Date[] date = new java.util.Date[trainingValuesDatabase.getCount()];
        int a=0;
        for (int i = 0; i< trainingValuesDatabase.getCount(); i++){
            data[i]= getNearestTrainingDate(trainings[i]);
            date[i]= readDateFromString(data[i]);
        }
        for(int i = 0; i< trainingValuesDatabase.getCount()-1; i++){
            if(date[i].after(date[i+1])){
                a=i;
                break;
            }
        }
        if(trainings.length==0){
            return null;
        }
        return trainings[a];
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
    private void updateTrainingDates(TrainingValue trainingValue, java.util.Date dataTraining){
        if(String.valueOf(trainingValue.getFirstDayTraining()).equals(null)|| String.valueOf(trainingValue.getFirstDayTraining()).equals("")){
            trainingValue.setFirstDayTraining((dataTraining.getYear()+1900)+"."+(dataTraining.getMonth()+1)+"."+dataTraining.getDate());
            trainingValuesDatabase.update(trainingValue);
        }
        if(String.valueOf(trainingValue.getLastTrainingDayDate()).equals(null)|| String.valueOf(trainingValue.getLastTrainingDayDate()).equals("")){
            trainingValue.setLastTrainingDayDate((dataTraining.getYear()+1900)+"."+(dataTraining.getMonth()+1)+"."+dataTraining.getDate());
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
        int seconds=0, minutes=0,hours=0;
        j=0;
        s="";
        for(int i=0;i<timeTraining.length();i++){
            if(timeTraining.charAt(i)!=':'&&j==2){s=s+timeTraining.charAt(i);}if(i==timeTraining.length()-1){j++; seconds= Integer.valueOf(s);s="";}
            if(timeTraining.charAt(i)!=':'&&j==1){s=s+timeTraining.charAt(i);}else if(timeTraining.charAt(i)==':'&&j==1){j++;minutes= Integer.valueOf(s);s="";}
            if(timeTraining.charAt(i)!=':'&&j==0){s=s+timeTraining.charAt(i);} else if (timeTraining.charAt(i)==':'&&j==0){j++;hours= Integer.valueOf(s);s="";}
        }
        return new java.util.Date(year-1900,month-1,numberDay,hours,minutes,seconds);
    }

    private int nextTrainingNextMonth(java.util.Date date, String firstTrainingDate, int weeksPassed){
        int dayNumber;
        if(weeksPassed<0) weeksPassed=0;
        java.util.Date firstDay= readDateFromString(firstTrainingDate);
        java.util.Date helpString =firstDay;
        date = new Date(date.getYear(),date.getMonth(),date.getDate()+30-weeksPassed);
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
        int dayNumber;
        java.util.Date firstDay= readDateFromString(data);
        java.util.Date helpString =firstDay;
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
        String month,day;
        if(date.getMonth()<9){
             month = "0"+ String.valueOf(date.getMonth()+1);
        } else {
            month = String.valueOf(date.getMonth()+1);
        }
        if(date.getDate()<=9){
            day = "0"+ String.valueOf(date.getDate());
        } else {
            day = String.valueOf(date.getDate());
        }
        return String.valueOf(String.valueOf(date.getYear()+1900)+"."+month+"."+day);
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
        int weekDay=data.getDay();
        if (weekDay==0) weekDay=6; else weekDay--;
        java.util.Date day=null;
        for (int i=0;i<7;i++){
            if(weekDay==shortDaysValue[i]){
                day  = data;
            } else if (weekDay<shortDaysValue[i]){
                day= new java.util.Date(data.getYear(),data.getMonth(),data.getDate()+shortDaysValue[i]-weekDay);
            }
        }
        if(day==null){
            for (int i=0;i<7;i++) {
                if (weekDay > shortDaysValue[i]) {
                    if(shortDaysValue[i]==-1) break;
                    day = new java.util.Date(data.getYear(), data.getMonth(), data.getDate() + shortDaysValue[i] - weekDay);
                }
            }
        }
        return getDate(day);
    }

    public OldTraining[][] lastTraining(String data, String hour, String name, Context context){
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(context,name);
        String[][] dane = dstd.getDateAndTime();
        java.util.Date trainingDate= readDateFromString(data,hour);
        java.util.Date last;
        String[] result= new String[3];
        for(int i=0;i<dane.length;i++){
            last= readDateFromString(dane[i][0],dane[i][2]);
            if (last.before(trainingDate)){
                result=dane[i];
                break;
            }
        }
        if(result[0]==null) return null;
        return dstd.getAll(result[0],result[2]);
    }

    public OldTraining[][] lastTraining(String name, Context context){
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(context,name);
        String[][] dane = dstd.getDateAndTime();
        if (dane==null) return null;
        dane = reverseTab(dane);
        java.util.Date trainingDate= new java.util.Date();
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
        String[][] result = new String[dane.length][dane[0].length];
        for(int i=0;i<dane.length;i++){
            result[i]=dane[dane.length-1];
        }
        return result;
    }

    public int readNumberOfTrainingsInMonth(TrainingValue wt) {
        int  numberDT=0;
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


    public java.util.Date getDataFromDateAndTime(String current, String time) {
        java.util.Date data = readDateFromString(current);
        data= addHour(data,time);
        return data;
    }
    private java.util.Date addHour(java.util.Date data, String timeS){
        String[] time = getTimes(timeS);
        return  new java.util.Date(data.getYear(),data.getMonth(),data.getDate(), Integer.valueOf(time[0]), Integer.valueOf(time[1]), Integer.valueOf(time[2]));
    }

    private String[] getTimes(String time){
        String[] dane=new String[3];
        String finalData="";
        int j=0;
        for (int i=0;i<time.length();i++){
            if(time.charAt(i)!=':'&&j==2){finalData=finalData+time.charAt(i);} if(i==time.length()-1){j++;dane[2]=finalData;finalData="";}
            if(time.charAt(i)!=':'&&j==1){finalData=finalData+time.charAt(i);}else if(time.charAt(i)==':'&&j==1){j++;dane[1]=finalData;finalData="";}
            if(time.charAt(i)!=':'&&j==0){finalData=finalData+time.charAt(i);}else if (time.charAt(i)==':'&&j==0){j++; dane[0]=finalData;finalData="";}
        }
        return dane;
    }
}

package com.example.daniel.extraview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.trainings.names.TrainingName;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.procedures.Units;

import java.util.Date;

/**
 * Created by Daniel on 2018-02-28.
 */

public class Diagram extends HorizontalScrollView {
    Exercise exercise;

    /**
     *  first dim data, second dim time, third dim round
     */
    OldTraining[][][] oldTrainings;
    Context context;
    LinearLayout container;
    LinearLayout table;
    public static int maxReps;
    public static  double maxWeight;
    public static int bottomMargin;
    TextPaint mTextPaint;
    StaticLayout mStaticLayout;
    public boolean isExercise =true;
    static int widthColumn;
    public Diagram(Context context, Exercise exercise) {
        super(context);
        this.context = context;
        this.exercise = exercise;
        this.oldTrainings = addOldTrainings();
        if(oldTrainings ==null) {
            isExercise =false;
            // a.finish();
            return;
        }
        widthColumn =Units.dpToPx(context,40);
        bottomMargin =Units.dpToPx(getContext(),120);
        setDane();
        init();
    }

    private void setDane() {
        for (int i = 0; i < oldTrainings.length; i++) {
            for (int j = 0; j < oldTrainings[0].length; j++) {
                for (int k = 0; k < oldTrainings[0][0].length; k++) {
                    if (oldTrainings[i][j][k]!=null&& oldTrainings[i][j][k].getReps() > maxReps) {
                        maxReps = oldTrainings[i][j][k].getReps();}
                    if (oldTrainings[i][j][k]!=null&& oldTrainings[i][j][k].getWeight() > maxWeight) {
                        maxWeight = oldTrainings[i][j][k].getWeight();
                    }

                }
            }

        }
    }

    private OldTraining[][][] addOldTrainings() {
        TrainingNamesDatabase ntd = new TrainingNamesDatabase(context);
        TrainingName[] name = ntd.getAll();
        OldTrainingsDatabase[] dstd = new OldTrainingsDatabase[name.length];
        /**
         * One dimension for trainings second fot dates
         */
        String[][] date = new String[name.length][];
        String[][] time = new String[name.length][];
        int maxNumberOfRounds = 0;
        for (int i = 0; i < name.length; i++) {
            dstd[i] = new OldTrainingsDatabase(context, name[i].getName());
            if (maxNumberOfRounds < dstd[i].maxNumberOfRounds(name[i].getID(), exercise.getID()))
                maxNumberOfRounds = dstd[i].maxNumberOfRounds(name[i].getID(), exercise.getID());
            date[i] = dstd[i].getDataTrainings(exercise.getID());
            time[i] = dstd[i].getTime(exercise.getID());
        }
        int numberOfDates = 0,numberOfTimes=0;
        boolean[] hasTrainingExercise = new boolean[name.length];
        for (int i = 0; i < date.length; i++) {
            for(int j=0;j<date[i].length;j++) {
                if (date[i][j] != "") {
                    numberOfDates++;
                    hasTrainingExercise[i] = true;
                } else {
                    hasTrainingExercise[i] = false;
                }
            }
        }
        numberOfTimes= calculateTime(date,time);
        OldTraining[][][] result;
        OldTraining[][] previousResult = new OldTraining[numberOfDates][];
        int j = 0;
        if(!isTraining(hasTrainingExercise)) return null;
        for (int i = 0; i < name.length; i++) {
            while (!hasTrainingExercise[i]) {i++; if(i==name.length) return  methodGroup(previousResult,numberOfDates,numberOfTimes,maxNumberOfRounds);}
            previousResult[j++] = dstd[i].get(name[i].getID(), exercise.getID());
        }
        result = methodGroup(previousResult,numberOfDates,numberOfTimes,maxNumberOfRounds);
        return result;
    }

    private boolean isTraining(boolean[] hasTrainingAnExercise) {
        boolean result = false;
        for (int i = 0; i < hasTrainingAnExercise.length; i++)
            if (hasTrainingAnExercise[i]==true) return true;
        return result;
    }


    private int calculateTime(String[][] data, String[][] time) {
        //TODO: finish
        return 5;
    }

    private OldTraining[][][] methodGroup(OldTraining[][] previousResult, int numberOfDates, int numberOfTime, int numberOfRounds) {
        OldTraining[][][] result=new OldTraining[numberOfDates][numberOfTime][numberOfRounds];
        OldTraining[] singleResult = new OldTraining[numberOfDates*numberOfTime*numberOfRounds];
        int q=0;
        for(int i=0;i<previousResult.length;i++){
            if((previousResult[i]!=null)) {
                for (int j = 0; j < previousResult[i].length; j++) {
                    singleResult[q++] = previousResult[i][j];
                }
            }
        }
        int x1=0,x2=0;
        String[] dateToCompare= new String[2], dateWhichIsToCompare= new String[2];
        int number,nr=0;
        String popData="",popTime="";

        for(int i=0;i<singleResult.length;){
            while(singleResult[i]==null||singleResult[i].getTrainingDate()==""||singleResult[i].getTrainingTime()=="") {
                i++;
                if (i >= singleResult.length) break;
            }
            if (i >= singleResult.length) break;
            dateToCompare[0]=singleResult[i].getTrainingDate();
            dateToCompare[1]=singleResult[i].getTrainingTime();
            number=singleResult[i].getRoundNumber();


            int j=0;boolean isNotPrevious=true;
            while(j<singleResult.length){
                while(singleResult[j]==null||singleResult[j].getTrainingDate()==""||singleResult[j].getTrainingTime()==""){
                    j++;
                    if(j>=singleResult.length) break;
                }
                if(j>=singleResult.length) break;
                dateWhichIsToCompare[0]=singleResult[j].getTrainingDate();
                dateWhichIsToCompare[1]=singleResult[j].getTrainingTime();
                if(checkISTherePreviousTraining(dateToCompare[0],dateWhichIsToCompare[0],dateToCompare[1],dateWhichIsToCompare[1])==1){
                    dateToCompare=dateWhichIsToCompare.clone();
                    number=singleResult[j].getRoundNumber();
                    nr=j;
                    isNotPrevious=false;
                } else if(checkISTherePreviousTraining(dateToCompare[0],dateWhichIsToCompare[0],dateToCompare[1],dateWhichIsToCompare[1])==-1){

                }
                else if(checkISTherePreviousTraining(dateToCompare[0],dateWhichIsToCompare[0],dateToCompare[1],dateWhichIsToCompare[1])==0){
                    if(isNotPrevious&&singleResult[j].getRoundNumber()==1){
                        dateToCompare=dateWhichIsToCompare.clone();
                        number=singleResult[j].getRoundNumber();
                        nr=j;
                    }

                }
                j++;
            }
            if(!popData.equals(dateToCompare[0])){
                popData=dateToCompare[0];
                x1++;
                x2=0;

            }
            if(!popTime.equals(dateToCompare[1])){
                popTime=dateToCompare[1];
                x2++;
            }
            if(number==1){
                result[x1-1][x2-1]= addSeries(singleResult,nr,numberOfRounds);
            }

        }
        return result;
    }

    private OldTraining[] addSeries(OldTraining[] singleResult, int nr, int maxReps) {
        OldTraining[] result = new OldTraining[maxReps];
        boolean first=false;
        for (int i=0;i<maxReps;i++){
            if(singleResult[nr]==null) break;
            if(singleResult[nr].getRoundNumber()==1&&first==false){
                first=true;
            } else if(singleResult[nr].getRoundNumber()==1&&first==true){
                break;
            }
            result[i]=singleResult[nr];
            singleResult[nr++]= setNullOldTraining();

        }
        return result;
    }

    private int checkISTherePreviousTraining(String currentDate, String checkedDate, String currentTime, String checkedTime){
        DateTraining dateTraining = new DateTraining(context);
        Date current= dateTraining.getDataFromDateAndTime(currentDate,currentTime);
        Date checked=dateTraining.getDataFromDateAndTime(checkedDate,checkedTime);
        if (current.after(checked)) return 1;
        if (current.before(checked)) return -1;
        if (current.equals(checked)) return 0;
        return 123;
    }
    private OldTraining setNullOldTraining(){
        return new OldTraining(0,0,"","","",0,0,0);
    }

    private void init() {
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        table =new LinearLayout(context);
        container = new LinearLayout(context);
        table.setLayoutParams(new LinearLayout.LayoutParams(5000, ViewGroup.LayoutParams.MATCH_PARENT)); //TODO calculate width
        table.setLayoutParams(new LinearLayout.LayoutParams(5000,getHeight()));
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        final HorizontalScrollView hsv = this;
        container.addView(table);
        addView(container);
        hsv.post(new Runnable() {
            @Override
            public void run() {
                hsv.scrollTo( table.getWidth(),0);
            }
        });
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(Units.dpToPx(getContext(),40));
        mTextPaint.setColor(0xFF000000);




    }
    static public int scaleOneColumnReps;
    static  public int scaleOneColumnWeight;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int height = getHeight();
        scaleOneColumnReps = (height- bottomMargin)/ maxReps;
        scaleOneColumnWeight =(int)((height- bottomMargin)/ maxWeight);
        int width1 = table.getWidth();

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#00d000")); // green
        paint.setStrokeWidth(0);
        int l=0;
        for(int i = 0; i< oldTrainings.length; i++){
            for(int j = 0; j< oldTrainings[i].length; j++){
                for(int k = 0; k< oldTrainings[i][j].length; k++,l++) {
                    if(oldTrainings[i][j][k]==null) {
                        break;
                    }
                    canvas.drawRect(width1 - widthColumn * (2 * l + 1), getHeight() - scaleOneColumnReps * oldTrainings[i][j][k].getReps()- bottomMargin, width1 - widthColumn * (2 * l), getHeight()- bottomMargin, paint);
                }
            }
        }
        l=0;
        paint.setColor(Color.parseColor("#0909c6"));
        for(int i = 0; i< oldTrainings.length; i++){
            for(int j = 0; j< oldTrainings[i].length; j++) {
                for (int k = 0; k < oldTrainings[i][j].length; k++,l++){
                    if(oldTrainings[i][j][k]==null) break;
                    canvas.drawRect(width1 - widthColumn * ((2 * l ) + 1 + 1), getHeight() - scaleOneColumnWeight * (float) oldTrainings[i][j][k].getWeight()- bottomMargin, width1 - widthColumn * (2 * l + 1), getHeight()- bottomMargin, paint);
                }
            }
        }
        paint.setColor(0xFFFF0000);
        paint.setStrokeWidth(Units.dpToPx(context, 1));
        l=0;
        for(int i = 0; i< oldTrainings.length; i++){
            for(int j = 0; j< oldTrainings[i].length; j++) {
                for (int k = 0; k < oldTrainings[i][j].length*2; k++, l++) {
                    if(oldTrainings[i][j][(int)k/2]==null) break;
                    if(k==0)   paint.setStrokeWidth(Units.dpToPx(context, 4)); else   paint.setStrokeWidth(Units.dpToPx(context, 1));
                    if(k==0){
                        canvas.drawLine(width1 - widthColumn * l, 0, width1 - widthColumn * l, getHeight(), paint);
                    }else if(k%2==0){
                        canvas.drawLine(width1 - widthColumn * l, 0, width1 - widthColumn * l, getHeight()- bottomMargin +Units.dpToPx(getContext(),50), paint);
                    } else{
                        canvas.drawLine(width1 - widthColumn * l, 0, width1 - widthColumn * l, getHeight()- bottomMargin, paint);
                    }
                }
            }
        }
        paint.setStrokeWidth(Units.dpToPx(context, 4));
        canvas.drawLine(width1 - widthColumn * l, 0, width1 - widthColumn * l, getHeight(), paint);

        //Todo: scaling, different number of rounds, too long names
        l=0;
        DateTraining dateTraining = new DateTraining(getContext());
        for(int i = 0; i< oldTrainings.length; i++){
            for(int j = 0; j< oldTrainings[i].length; j++) {
                for (int k = 0; k < oldTrainings[i][j].length ; k++, l++) {
                    mTextPaint.setTextSize(Units.dpToPx(getContext(),40));
                    if(oldTrainings[i][j][k]==null) break;
                    canvas.save();
                    int width = (int) mTextPaint.measureText(String.valueOf(k+1));
                    canvas.translate(width1-(widthColumn *l*2+ widthColumn +width/2), getHeight()- bottomMargin);
                    mStaticLayout = new StaticLayout(String.valueOf(k+1), mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                    mStaticLayout.draw(canvas);
                    canvas.restore();
                    if(k==0){
                        mTextPaint.setTextSize(Units.dpToPx(getContext(),30));
                        canvas.save();
                        String data =dateTraining.switchYearWithDay(oldTrainings[i][j][k].getTrainingDate());
                        width = (int) mTextPaint.measureText(data);
                        while (width> widthColumn *2*(findNumberOfRounds(oldTrainings[i][j])+1) ){
                            int x = (int) mTextPaint.getTextSize()-10;
                            mTextPaint.setTextSize(x);
                            width=(int) mTextPaint.measureText(data);
                        }
                        canvas.translate(width1-(widthColumn *l*2+(widthColumn *2*(findNumberOfRounds(oldTrainings[i][j])+1)+width)/2), getHeight()- bottomMargin +Units.dpToPx(getContext(),50));
                        mStaticLayout = new StaticLayout(data, mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                        mStaticLayout.draw(canvas);
                        canvas.restore();
                        canvas.save();
                        mTextPaint.setTextSize(Units.dpToPx(getContext(),30));
                        width=(int) mTextPaint.measureText(oldTrainings[i][j][k].getTrainingName());
                        while (width> widthColumn *2* oldTrainings[i][j].length ){
                            int x = (int) mTextPaint.getTextSize()-10;
                            mTextPaint.setTextSize(x);
                            width=(int) mTextPaint.measureText(oldTrainings[i][j][k].getTrainingName());
                        }
                        width = (int) mTextPaint.measureText(oldTrainings[i][j][k].getTrainingName());
                        canvas.translate(width1-(widthColumn *l*2+ widthColumn * oldTrainings[i][j][findNumberOfRounds(oldTrainings[i][j])].getRoundNumber()+width/2), getHeight()- bottomMargin +Units.dpToPx(getContext(),80));
                        mStaticLayout = new StaticLayout(oldTrainings[i][j][k].getTrainingName(), mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                        mStaticLayout.draw(canvas);
                        canvas.restore();
                    }

                }
            }
        }
        Rect bounds = new Rect();
        l=0;
        int width=(int) mTextPaint.measureText(String.valueOf(maxWeight));
        int fontValueWeight=80;
        while(width> widthColumn) {
            mTextPaint.setTextSize(Units.dpToPx(getContext(), --fontValueWeight));
            width = (int) mTextPaint.measureText(String.valueOf(maxWeight));
        }
        int fontValueReps=80;
        mTextPaint.setTextSize(Units.dpToPx(getContext(), fontValueReps));
        int width2=(int) mTextPaint.measureText(String.valueOf(maxReps));
        while(width2> widthColumn) {
            mTextPaint.setTextSize(Units.dpToPx(getContext(), --fontValueReps));
            width2 = (int) mTextPaint.measureText(String.valueOf(maxReps));
        }

        mTextPaint.setColor(Color.parseColor("#000000"));
        for(int i = 0; i< oldTrainings.length; i++){
            for(int j = 0; j< oldTrainings[i].length; j++) {
                for (int k = 0; k < oldTrainings[i][j].length ; k++, l++) {
                    if(oldTrainings[i][j][k]==null) break;
                    canvas.save();
                    mTextPaint.setTextSize(Units.dpToPx(getContext(),fontValueReps));
                    width = (int) mTextPaint.measureText(String.valueOf(oldTrainings[i][j][k].getReps()));
                    mTextPaint.getTextBounds(String.valueOf(oldTrainings[i][j][k].getReps()), 0,String.valueOf(oldTrainings[i][j][k].getReps()).length(), bounds);
                    canvas.translate(width1-(widthColumn *l*2+ widthColumn /2+width/2), getHeight()- bottomMargin - scaleOneColumnReps * oldTrainings[i][j][k].getReps());
                    mStaticLayout = new StaticLayout(String.valueOf(oldTrainings[i][j][k].getReps()), mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                    mStaticLayout.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    mTextPaint.setTextSize(Units.dpToPx(getContext(),fontValueWeight));
                    width = (int) mTextPaint.measureText(String.valueOf(oldTrainings[i][j][k].getWeight()));
                    mTextPaint.getTextBounds(String.valueOf(oldTrainings[i][j][k].getWeight()), 0,String.valueOf(oldTrainings[i][j][k].getWeight()).length() , bounds);
                    canvas.translate(width1-(widthColumn *l*2+ widthColumn /2+ widthColumn +width/2), (float) (getHeight()- bottomMargin - scaleOneColumnWeight * oldTrainings[i][j][k].getWeight()));
                    mStaticLayout = new StaticLayout(String.valueOf(oldTrainings[i][j][k].getWeight()), mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                    mStaticLayout.draw(canvas);
                    canvas.restore();

                }
            }
        }

    }

    private int findNumberOfRounds(OldTraining[] oldTrainings) {
        int a=0;
        for(int i=0;i<oldTrainings.length;i++){
            if(oldTrainings[i]!=null){
                a++;
            } else {
                return a-1;
            }
        }
        return a-1;
    }
}



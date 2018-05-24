package com.example.daniel.values;

import android.content.Context;
import android.util.Log;

import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SynchSocket  extends Thread{
    private Context context;
    DataOutputStream dOutClient;
    
    public SynchSocket(Context context){
        this.context=context;
    }
    @Override
    public void run() {
        super.run();
        Socket clientSock=null;
        try {
            clientSock= new Socket("192.168.1.24" ,38300);
            DataInputStream dInKlient = new DataInputStream(clientSock.getInputStream());
            dOutClient = new DataOutputStream(clientSock.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
        synchExerciseNames(clientSock);
        synchTrainingNames(clientSock);
        synchOldTrainings(clientSock);
    }
    public void synchExerciseNames(Socket socket){
        ExerciseDatabase exerciseDatabase = new ExerciseDatabase(context);
        sendFile(exerciseDatabase.getDB(),socket);
    }

    public void synchTrainingNames(Socket socket){
        TrainingValuesDatabase trainingDatabase = new TrainingValuesDatabase(context);
        sendFile(trainingDatabase.getDB(),socket);

    }

    public void synchOldTrainings(Socket socket){
        OldTrainingsDatabase oldTrainingsDatabase = new OldTrainingsDatabase(context,null);
        sendFile(oldTrainingsDatabase.getDB(),socket);

    }
    public void  sendTable(){

    }
    public void sendFile(String path,Socket socket){
        if (path.equals(null)) return;
        try {
            FileOutputStream fOutKlient = new FileOutputStream(path,true);
            if (fOutKlient==null) return;
            byte[] bytes = new byte[(int) fOutKlient.getChannel().size()];
            sendLength(fOutKlient.getChannel().size());
            BufferedInputStream bis= new BufferedInputStream(new FileInputStream(path));
                bis.read(bytes, 0, bytes.length);
                OutputStream os = socket.getOutputStream();
                os.write(bytes, 0, bytes.length);
                os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendLength(long length){
        try {
            dOutClient.writeLong(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

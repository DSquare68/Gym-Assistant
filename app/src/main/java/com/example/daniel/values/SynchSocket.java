package com.example.daniel.values;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
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
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardCopyOption.*;

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
        copyFile("kopia.db",exerciseDatabase.getDB());
        try {
            dOutClient.writeBoolean(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sendFile(exerciseDatabase.getDB(),socket);

    }

    private void copyFile(String path, String source) {
        try{
            FileInputStream fOutKlient = new FileInputStream(source);
            File file = new File("/storage/sdcard0/test/", path);
            file.createNewFile();
            FileOutputStream fInKlient = new FileOutputStream(file);

            FileChannel src = fOutKlient.getChannel();
            FileChannel dst = fInKlient.getChannel();
            dst.transferFrom(src, 0, src.size());

            src.close();
            dst.close();

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void synchTrainingNames(Socket socket){
        TrainingValuesDatabase trainingDatabase = new TrainingValuesDatabase(context);
        sendFile(trainingDatabase.getDB(),socket);

    }

    public void synchOldTrainings(Socket socket){
        OldTrainingsDatabase oldTrainingsDatabase = new OldTrainingsDatabase(context,null);
        sendFile(oldTrainingsDatabase.getDB(),socket);

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

package com.example.daniel.gymassistant;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;


public class Synch extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synch);
        USBSocket usbThread = new USBSocket();
        usbThread.start();
    }
    class USBSocket extends Thread{
        @Override
        public void run() {
            super.run();
            Socket clientSock=null;
            try {
                clientSock= new Socket("localhost" ,38300);
                DataInputStream dInKlient = new DataInputStream(clientSock.getInputStream());
                DataOutputStream dOutKlient = new DataOutputStream(clientSock.getOutputStream());
                Log.d("bool",String.valueOf(dInKlient.readBoolean()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

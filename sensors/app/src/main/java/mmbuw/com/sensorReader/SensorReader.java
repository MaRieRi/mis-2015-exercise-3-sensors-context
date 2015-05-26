package mmbuw.com.sensorReader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.support.v4.app.NotificationCompat;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;


public class SensorReader extends Activity implements SeekBar.OnSeekBarChangeListener, SensorEventListener {
    private SensorManager mySensorManager;
    private Sensor mySensor;
    private int progressChanged;
    private int progressWindow;
    private NotificationCompat.Builder builder;
    private NotificationManager notManager;


    private DrawSomething drawView; //CustomViewInstance
    private FFTView fftView;

    private SeekBar seekMe;
    private SeekBar seekMeToo;
    private float[] gravity = {0,0,0};

    private float x;
    private float y;
    private float z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        seekMe = (SeekBar) findViewById(R.id.scaleBar);
        seekMe.setOnSeekBarChangeListener(this);

        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mySensor= mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorManager.registerListener(this,mySensor,1000);

        drawView = (DrawSomething)findViewById(R.id.firstView);

        seekMeToo = (SeekBar)findViewById(R.id.scaleWindowBar);
        seekMeToo.setOnSeekBarChangeListener(this);
        fftView = (FFTView)findViewById(R.id.fftView);

        Intent myInt = new Intent(this,SensorReader.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(myInt);
        PendingIntent myPendingInt = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder= new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                .setContentText("Activity")
                .setContentText("I'm sleeping....zzzZZZZ");
        builder.setContentIntent(myPendingInt);
        notManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notManager.notify(1,builder.build());

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId()==seekMe.getId()){
            progressChanged=progress;
            mySensorManager.unregisterListener(this,mySensor);
            mySensorManager.registerListener(this,mySensor,progressChanged*1000);
        }
        else if(seekBar.getId()==seekMeToo.getId()){
           progressWindow=progress;
            RelativeLayout.LayoutParams windowSize=(RelativeLayout.LayoutParams) fftView.getLayoutParams();
            windowSize.height=progressWindow*20;
            fftView.setLayoutParams(windowSize);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate
        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        x=event.values[0] - gravity[0];
        y=event.values[1] - gravity[1];
        z=event.values[2] - gravity[2];
        drawView.setData(x,y,z);
        fftView.getMagnitude(x,y,z);


        if(fftView.getAverage()!=""){
            builder= new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                    .setContentText("Activity")
                    .setContentText(fftView.getAverage());
            notManager.notify(1,builder.build());
            fftView.setAverage(0);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
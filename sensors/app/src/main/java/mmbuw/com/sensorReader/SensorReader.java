package mmbuw.com.sensorReader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;


public class SensorReader extends Activity implements SeekBar.OnSeekBarChangeListener, SensorEventListener {
    private SensorManager mySensorManager;
    private Sensor mySensor;

    private DrawSomething drawView; //CustomViewInstance

    private SeekBar seekMe;
    private float[] gravity = {0,0,0};

    private float x;
    private float y;
    private float z;
    private LinkedList<Float> accValuesX = new LinkedList<>();
    private LinkedList<Float> accValuesY = new LinkedList<>();
    private LinkedList<Float> accValuesZ = new LinkedList<>();

    public class DrawSomething extends View{

        private Paint paint = new Paint();
        public DrawSomething(Context context){
            super(context);
        }

        public void setData(float x, float y, float z){

            if(accValuesX.size()==1024){
                accValuesX.removeFirst();
                accValuesX.addLast(x);}
            else
                accValuesX.addLast(x);

            if(accValuesY.size()==1024){
                accValuesY.removeFirst();
                accValuesY.addLast(y);}
            else
                accValuesY.addLast(y);

            if(accValuesZ.size()==1024){
                accValuesZ.removeFirst();
                accValuesZ.addLast(z);}
            else
                accValuesZ.addLast(z);

            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas){
            Paint paint = new Paint();
            //background coloring
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);

            paint.setColor(Color.BLUE);
            canvas.drawLine(0,0,50,50,paint);

            for (int i=0;i<accValuesX.size()-1;i++){
                paint.setColor(Color.RED);
                int point1X=i*getWidth()/1024;
                int point2X=(i+1)*getWidth()/1024;
                canvas.drawLine(point1X,accValuesX.get(i),point2X,accValuesX.get(i+1),paint);
            }
            for (int i=0;i<accValuesY.size()-1;i++){
                paint.setColor(Color.GREEN);
                int point1X=i*getWidth()/1024;
                int point2X=(i+1)*getWidth()/1024;
                canvas.drawLine(point1X,accValuesY.get(i),point2X,accValuesY.get(i+1),paint);
            }
            for (int i=0;i<accValuesZ.size()-1;i++){
                paint.setColor(Color.BLUE);
                int point1X=i*getWidth()/1024;
                int point2X=(i+1)*getWidth()/1024;
                canvas.drawLine(point1X,accValuesZ.get(i),point2X,accValuesZ.get(i+1),paint);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sensor);
        seekMe = (SeekBar) findViewById(R.id.scaleBar);
        seekMe.setOnSeekBarChangeListener(this);

        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mySensor= mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        drawView = new DrawSomething(this);
        //drawView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                //LinearLayout.LayoutParams.WRAP_CONTENT));

        addContentView(drawView,drawView.getLayoutParams());

        //drawView.getLayoutParams().height=drawView.getHeight()/2;
        //drawView.getLayoutParams().width=drawView.getWidth();
        //drawView.setBackgroundColor(Color.BLACK);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
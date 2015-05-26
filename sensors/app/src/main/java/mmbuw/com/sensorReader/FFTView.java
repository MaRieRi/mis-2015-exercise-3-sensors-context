package mmbuw.com.sensorReader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Marie on 26.05.2015.
 */
public class FFTView extends View {
    private Paint paint;
    private int size=256;
    private LinkedList<Float> magnitudeList= new LinkedList<>();
    private double[] xArray = new double[size];
    private double[] yArray= new double[size];
    private float average = 0;
    private FFT fftDing;
    public FFTView(Context context,AttributeSet att){
        super(context,att);
        fftDing = new FFT(size);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint = new Paint(Color.BLACK);
        canvas.drawPaint(paint);

        if(magnitudeList.size()==size){
            Arrays.fill(yArray,0);
            magnitudeList.clear();
            fftDing.fft(xArray,yArray);

            for (int i = 0; i < size/2; ++i) {
                yArray[i] = Math.sqrt(Math.pow(xArray[i], 2) + Math.pow(yArray[i], 2));
                average+=yArray[i];
            }
            average=average/(size/2);

            for (int i=0;i<size-1;i++){
                paint.setColor(Color.WHITE);
                int point1X=i*getWidth()/50;
                int point2X=(i+1)*getWidth()/50;
                double tempY1=(yArray[i]*10)+(getHeight()/2);
                double tempY2=(yArray[i+1]*10)+(getHeight()/2);
                canvas.drawLine(point1X,(float)tempY1,point2X,(float)tempY2,paint);
        }
        }else{
            for (int i=0;i<yArray.length-1;i++){
                paint.setColor(Color.WHITE);
                int point1X=i*getWidth()/50;
                int point2X=(i+1)*getWidth()/50;
                double tempY1=(yArray[i])+(getHeight()/2);
                double tempY2=(yArray[i+1])+(getHeight()/2);
                canvas.drawLine(point1X,(float)tempY1,point2X,(float)tempY2,paint);}
        }
    }

    public void getMagnitude(float x, float y, float z){
        double tempMag = Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2);
        float magnitude = (-1.0f)*(float)tempMag;
        if(magnitudeList.size()<size)
            magnitudeList.addLast(magnitude);
        else{
            for(int i=0;i<size;++i){
                xArray[i]=magnitudeList.get(i);
            }
        }
        invalidate();
    }

    public String getAverage(){
        String s ="";
        //überfordert unser handy....schläft zuviel
        //if(average<16){
        //   s="I'm sleeping on the table....zzzZZZ";
        //}
        //Werte durch ausprobieren ermittelt, sind sehr unzuverlässig.
        //Sensor eignet sich gut, weil er Stromsparend ist und die
        // beim Fortbewegen guut erkannt werden.

        if(average<100&&average>20){
            s="I might be walking....";
        }
        else if(average<300&&average>100){
            s="I'm driving with the bus...";
        }
           else if(average>300){
            s="I'm running....";
        }
        return s;
    }
    public void setAverage(int number){
        average=number;
    }

}

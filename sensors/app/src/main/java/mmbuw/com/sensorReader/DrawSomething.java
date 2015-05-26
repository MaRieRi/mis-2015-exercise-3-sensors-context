package mmbuw.com.sensorReader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.LinkedList;

public class DrawSomething extends View {

    private Paint paint = new Paint();
    private LinkedList<Float> accValuesX = new LinkedList<>();
    private LinkedList<Float> accValuesY = new LinkedList<>();
    private LinkedList<Float> accValuesZ = new LinkedList<>();
    private LinkedList<Float> magnitude = new LinkedList<>();

    public DrawSomething(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        }

    public void setData(float x, float y, float z){
    int listSize= 50;
        if(accValuesX.size()==listSize){
            accValuesX.removeFirst();
            accValuesX.addLast((-1.0f)*x);}
        else
            accValuesX.addLast((-1.0f)*x);

        if(accValuesY.size()==listSize){
        accValuesY.removeFirst();
        accValuesY.addLast((-1.0f)*y);}
        else
        accValuesY.addLast((-1.0f)*y);

        if(accValuesZ.size()==listSize){
        accValuesZ.removeFirst();
        accValuesZ.addLast((-1.0f)*z);}
        else
        accValuesZ.addLast((-1.0f)*z);

    if(magnitude.size()==listSize){
        magnitude.removeFirst();
        magnitude.addLast(getMagnitude(x,y,z));}
    else
        magnitude.addLast(getMagnitude(x,y,z));

        invalidate();

        }

@Override
protected void onDraw(Canvas canvas){
        Paint paint = new Paint();
        //background coloring
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        for (int i=0;i<accValuesX.size()-1;i++){
        paint.setColor(Color.RED);
        int point1X=i*getWidth()/50;
        int point2X=(i+1)*getWidth()/50;
        canvas.drawLine(point1X,(accValuesX.get(i)*10)+(getHeight()/2),point2X,(accValuesX.get(i+1)*10)+(getHeight()/2),paint);
        }
        for (int i=0;i<accValuesY.size()-1;i++){
        paint.setColor(Color.GREEN);
        int point1X=i*getWidth()/50;
        int point2X=(i+1)*getWidth()/50;
        canvas.drawLine(point1X,(accValuesY.get(i)*10)+(getHeight()/2),point2X,(accValuesY.get(i+1)*10)+(getHeight()/2),paint);
        }
        for (int i=0;i<accValuesZ.size()-1;i++){
        paint.setColor(Color.BLUE);
        int point1X=i*getWidth()/50;
        int point2X=(i+1)*getWidth()/50;
        canvas.drawLine(point1X,(accValuesZ.get(i)*10)+(getHeight()/2),point2X,(accValuesZ.get(i+1)*10)+(getHeight()/2),paint);
        }

    for (int i=0;i<magnitude.size()-1;i++){
        paint.setColor(Color.WHITE);
        int point1X=i*getWidth()/50;
        int point2X=(i+1)*getWidth()/50;
        canvas.drawLine(point1X,(magnitude.get(i)*10)+(getHeight()/2),point2X,(magnitude.get(i+1)*10)+(getHeight()/2),paint);
    }
        }
    public float getMagnitude(float x,float y,float z){
        double tempMag = Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2);
        float magnitude = (-1.0f)*(float)tempMag;
        return magnitude;
        }
    }

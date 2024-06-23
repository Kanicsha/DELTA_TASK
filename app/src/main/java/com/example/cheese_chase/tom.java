package com.example.cheese_chase;

import static android.graphics.BlendMode.COLOR;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class tom {
    float x_center,y_center,tom_headY;
    int radius;
    float speed; //to make tom move closer to jerry by decreasing y_center using this factor
    Paint paint =new Paint();
    Paint paint_outline=new Paint();
    int current_track;

    public tom(float x, float y, int r, float s){
        x_center=x;
        y_center=y;
        radius=r;
        speed=s;
        tom_headY=y_center-radius;
        current_track=1;
    }
    public void increase_speed(float speed){
        y_center=y_center-speed;
    }

    public void create_tom(Canvas canvas){
        paint.setColor(Color.parseColor("#424242"));
        paint.setStyle(Paint.Style.FILL);
        paint_outline.setColor(Color.BLACK);
        paint_outline.setStrokeWidth(10);
        paint_outline.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x_center,y_center,radius,paint);
        canvas.drawCircle(x_center,y_center,radius,paint_outline);
    }
    public float getX_center(){
        return x_center;
    }
    public float getY_center(){
        return y_center;
    }
    public int getRadius(){
        return radius;
    }
    public boolean checkDodge_obstacle(obstacle obstacle) {
        if (obstacle != null) {
            if (tom_headY == obstacle.bottom) {
                Log.i("HITT", "YOYO tommy hit the obstacle");
                return true;

            }
        }
        return false;
    }
    public void setSpeed(float speed){
        this.speed=speed;
    }
}

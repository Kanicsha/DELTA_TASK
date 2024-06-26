package com.example.cheese_chase;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class cheese {
    public Bitmap bm_cheese;
    float x_center,y_center;
    int size_x, size_y;
    int current_track=-1;
    int speed;

    public cheese(Bitmap cheese_bm,float x,float y,int size) {
        this.bm_cheese=cheese_bm;
        this.x_center=x;
        this.y_center=y;
        this.size_x=size;
        current_track=-1;
    }
    public void create_powerUP(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#0288D1"));;
        paint.setStyle(Paint.Style.FILL);
//        paint_outline.setColor(Color.BLACK);
//        paint_outline.setStrokeWidth(10);
//        paint_outline.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(bm_cheese,x_center,y_center,paint);
        //canvas.drawRect(left,top,right,bottom,paint_outline);


    }
    public void increase_speed(float speed){ //make it move upwards if speed is positive n down when neg
        this.speed= (int) speed;
        y_center-=speed;
    }
}

package com.example.cheese_chase;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class powerUp  {
    private Bitmap bm_powerUp;
    float x_center,y_center;
    int size_x, size_y;
    int current_track;
    int speed;

    public powerUp(Bitmap bm,float x,float y,int size) {
        this.bm_powerUp=bm;
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
        canvas.drawBitmap(bm_powerUp,x_center,y_center,paint);
        //canvas.drawRect(left,top,right,bottom,paint_outline);


    }
    public void increase_speed(float speed){ //make it move upwards if speed is positive n down when neg
        y_center-=speed;
        this.speed= (int) speed;
    }

    public Bitmap getBm_powerUp() {
        return bm_powerUp;
    }

    public void setSize_x(int size_x) {
        this.size_x = size_x;
    }

    public void setSize_y(int size_y) {
        this.size_y = size_y;
    }

    public void setX_center(float x_center) {
        this.x_center = x_center;
    }

    public void setY_center(float y_center) {
        this.y_center = y_center;
    }
}


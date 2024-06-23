package com.example.cheese_chase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;


public class obstacle_animation extends SurfaceView implements SurfaceHolder.Callback {

    private  ArrayList<obstacle> obstacles;
    Random random=new Random();
    int[] track_width={85,495,855};
    SurfaceView surfaceView=new SurfaceView(getContext());

    public obstacle_animation(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        obstacles = new ArrayList<>();
        int width=surfaceView.getWidth();
        int height= surfaceView.getHeight();
        new Thread(new obstacle_thread(holder)).start();
        int track_no=random.nextInt(3);//value between 0 and 2
        float left = track_width[track_no];
        float top= (float) (Math.random()*(2200-100+1)+100);
        for (obstacle block: obstacles){
            block = new obstacle(90, left, top);
            obstacles.add(block);

        }

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    public class obstacle_thread implements Runnable{
        SurfaceHolder holder;
        public obstacle_thread(SurfaceHolder holder){
            this.holder=holder;

        }
        @Override
        public void run() {
            while (true) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    int track_no=random.nextInt(3);//value between 0 and 2
                    float left = track_width[track_no];
                    float top= (float) (Math.random()*(2200-100+1)+100);
                    for (obstacle block: obstacles){
                        block = new obstacle(90, left, top);
                        obstacles.add(block);
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    public void create_obstacle(Canvas canvas) {
        int track_no=random.nextInt(3);//value between 0 and 2
        float left = track_width[track_no];
        float top= (float) (Math.random()*(2200-100+1)+100);
        float right=left+260;
        float bottom=2300;
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#0288D1"));;
        paint.setStyle(Paint.Style.FILL);
        Paint paint_outline = new Paint();
        paint_outline.setColor(Color.BLACK);
        paint_outline.setStrokeWidth(10);
        paint_outline.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left,top,right,bottom,paint);
        canvas.drawRect(left,top,right,bottom,paint_outline);


    }
}

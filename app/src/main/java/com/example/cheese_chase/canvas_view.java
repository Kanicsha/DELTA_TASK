package com.example.cheese_chase;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;

import android.os.Handler;

import androidx.annotation.NonNull;

public class canvas_view extends View {
    Paint paint, paint_outline;
    private MediaPlayer mediaPlayer;
    Random random = new Random();
    float speed=-1;
    boolean jerryCollided=false;
    int max_powe=2;
    int cheeseCollected =0;

    private TextChangeListener tcl;
MainActivity ma;
    jerry j;
    public tom t;
    int number_obstacles = 10;
    int obstacle_hit_jerry=0;
    private Runnable gameRunnable;
    ArrayList<obstacle> obstacles1 = new ArrayList<>();
    ArrayList<powerUp> powerUps = new ArrayList<>();
    ArrayList<cheese> cheeses = new ArrayList<>();
    ArrayList<trap> traps = new ArrayList<>();
    int number_powerUps=1;
    int number_cheese=1;
    int number_traps=1;
    //obstacle block;
   int[] track_width = {135, 495, 855};
    //int[] track_width = {180,540,900};
    boolean first_touch_jerry=true;
    boolean gameover=false;
    int previous_cheese=0;
    int track_end,track_start;

    int[] jerry_tom_center={180,540,900};
    private Timer timer;
    HashSet<obstacle> hashSet=new HashSet<>();
    HashSet<obstacle> hashSetC=new HashSet<>();
    HashSet<obstacle> hashSetT=new HashSet<>();

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.powerup);
    private int score=0;
    cheese cheese;
    trap trap;

    private boolean score_deducted=false;



    public canvas_view(Context context,AttributeSet attributeSet) throws IOException {
        super(context,attributeSet);
        init();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(getContext(), Uri.parse("android.resource://" + getContext().getPackageName() + "/raw/bg"));
        mediaPlayer.prepare();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

       // gameRunnable = new GameRunnable();

    }

    public void setMainActivity(MainActivity ma){
        this.ma=ma;
    }

    // the left and top values to place the obstacles right inside the tracks wud be track1:85 track2:495 track 3:855
    //for tom and jerry,d centers will be track1:  2:540,3:
    public void init() {
        paint = new Paint();
        paint_outline = new Paint();


        paint.setColor(Color.parseColor("#B2FF59"));
        paint_outline.setColor(Color.BLACK);
        paint_outline.setStrokeWidth(20);
        paint_outline.setStyle(Paint.Style.STROKE);
        j = new jerry((float) 540, 1700, 70, 0);
        t = new tom((float) 540, 2100, 90, 0);
       addObstacles();
       addBitmap(); //just add them into arraylist now;
        gameover=false;

    }
    public void addObstacles(){
        for (int i = 0; i < number_obstacles; i++) {
            int track_no = random.nextInt(3);//value between 0 and 2
            //  Log.i("arrays",left[i]+"  "+top[i]);
            float top = (float) (Math.random() * (1400 - 400 + 1) + 400);
            // Log.i("random tiop", String.valueOf(top));

            obstacle obstacle = new obstacle(90, track_width[track_no], top);
            obstacle.current_track = track_no;
            if (obstacle.current_track == 1) {
                if (obstacle.top > 1500) {
                    // obstacle.top-=500;
                }
            }
            obstacle.gap_down = 600;
            obstacle.speed=0;
            obstacles1.add(obstacle);
            //obstacle.increase_speed(-2);
        }
//        if (bitmap==null) {
//            Log.e("Error", "Failed to load bitmap");
//        }



        //generating obstacles with a minimum gap between them in each track
        for (int i = 1; i < obstacles1.size(); i++) {
            obstacle current_obstacle = obstacles1.get(i);
            //   Log.i("obs", obstacles1.get(i - 1).left + " and " + obstacles1.get(i - 1).top + " " + obstacles1.get(i - 1).bottom);
            //  obstacle_new.setTop(obstacle_new.bottom, obstacles.get(i-1).gap_down);
            //  if (obstacle_new.current_track==obstacles1.get(i-1).current_track) {
            //   float newTop = obstacles1.get(i - 1).bottom + obstacles1.get(i - 1).gap_down;
            for (int j = i - 1; j >= 0; j--) {
                if (current_obstacle.current_track == obstacles1.get(j).current_track) {
                    float newTop = obstacles1.get(j).bottom + obstacles1.get(j).gap_down;
                    // Log.i("gap|bottom", obstacles1.get(j).gap_down +"  "+ obstacles1.get(j).bottom);
                    float newTop1 = Math.max(newTop, current_obstacle.top);
                    current_obstacle.setTop(newTop);
                    //current_obstacle.gap_down+= current_obstacle.bottom;
                    break;
                }
            }
            // obstacle_new.setTop(newTop);
            //obstacle_new.gap_down = obstacle_new.top + obstacle_new.size;

        }

    }
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetGame();
    }

    @Override
            protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        createTracks(canvas, 260, 100);
        t.create_tom(canvas);
        j.create_jerry(canvas);
        createObstacles(canvas);
//        Log.i("imo", String.valueOf(obstacles1.get(0).bottom));
        createBitmap(canvas);
        createCheese(canvas);
        createTrap(canvas);

      //  if((int) (Math.random() * 30) ==3) {
            //createCheese(canvas);
      //  }

        invalidate();
    }


    public void createObstacles(Canvas canvas){
        for (obstacle obstacle:obstacles1){
            obstacle.create_obstacle(canvas);
           // obstacle.increase_speed(-2);
        }
    }

    private void updateObstacles() {
        float minimum = 0;
        for (int i = 0; i < obstacles1.size(); i++) {
            obstacle obstacle = obstacles1.get(i);
            obstacle.increase_speed(speed);
            if (speed > -12) {
                speed -= 0.001f;
            }
            for (int j = 0; j < obstacles1.size(); j++) {
                if (i != j && obstacles1.get(j).top<=minimum) {
                    minimum=obstacles1.get(j).top;
                    break;
                }
            }
            if (obstacle.bottom >= 2200) {
                //  Log.i("end","obstacle croseed!!");
                obstacle.setTop(minimum-500);
                int track_no = random.nextInt(3);//value between 0 and 2
                obstacle.current_track=track_no;
                obstacle.setLeft(track_width[track_no]);

            }
        }
    }


    private void createBitmap(Canvas canvas) {
        for (powerUp powerUp : powerUps) {
                powerUp.create_powerUP(canvas);
                powerUp.increase_speed(speed);
                if(powerUp.y_center==2100){
                    powerUp.y_center= (float) (Math.random()*(-10000-200)+200);
                }
    }

    }
    private void addBitmap(){
        for(int i=0;i<number_powerUps;i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.powerup);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
            if (bitmap==null) {
                Log.e("Error", "Failed to load bitmap");
            }
            int track_no = random.nextInt(3);//value between 0 and 2
            float top = (float) (Math.random() * (1600 - 500 + 1) + 500);
            powerUp powerUp=new powerUp(resizedBitmap, (float) track_width[track_no], top, 2);
            powerUps.add(powerUp);
            powerUp.current_track=track_no;
            Log.i("bm",powerUp.current_track+ "  "+powerUp.y_center);
        }
    }
    public void createCheese(Canvas canvas) {
        for (int i = 0; i < number_cheese; i++) {
            cheese.create_powerUP(canvas);
           // cheese.increase_speed(speed);
            if (cheese.y_center >= 2100) {
                cheese.y_center = (float) (Math.random() * (-5000 - 200) + 200);
            }

        }
    }
    public void addCheese() {
        for (int i = 0; i < number_cheese; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
            int track_no = random.nextInt(3);//value between 0 and 2
            float top = (float) (Math.random() * (-5000 - 500 + 1) + 500);
            cheese = new cheese(resizedBitmap, track_width[track_no], top, 9);
            cheeses.add(cheese);
        }
    }
    public void addTrap() {
        for (int i = 0; i < number_traps; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trap);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
            int track_no = random.nextInt(3);//value between 0 and 2
            float top = (float) (Math.random() * (-5000 - 500 + 1) + 500);
            trap = new trap(resizedBitmap, track_width[track_no], top, 90);
            traps.add(trap);
        }
    }
    public void createTrap(Canvas canvas) {
        for (int i = 0; i < number_traps; i++) {
            trap.createTrap(canvas);
          //  trap.increase_speed(speed);

            if (trap.y_center >= 2100) {
                trap.y_center = (float) (Math.random() * (-5000 - 200) + 200);
            }
        }
    }
    private void createNewObstacle() {
        /*
        float top = (float) (Math.random() * (1800 - 100 + 1) + 100);
        int track_no = random.nextInt(3);
       obstacle obstacle = new obstacle(90, track_width[track_no], top);
        obstacle.current_track = track_no;
        int attempts=0;
    for(int j=obstacles1.size()-1;j>=0;j--){

            obstacle existing_obstacle=obstacles1.get(j);
           // if (obstacle.current_track==existing_obstacle.current_track){
                    if(!obstacleOverlap(obstacle,existing_obstacle)){ //if they do not overlap then add into the list
                        obstacles1.add(obstacle);
                    }
                    else {                                                           //if they overlap, then generate a new top value and try again until it doesn't overlap with it
                         top = (float) (Math.random() * (1800 - 100 + 1) + 100);
                         obstacle.top=top;
                        // j++;//try again
                    }
                    attempts++;
            }
      //  }
        Log.i("newly added obstacle", obstacle.size+"  "+ obstacle.current_track);
       obstacles1.add(obstacle);*/

        float top = (float) (Math.random() * (1800 - 100 + 1) + 100);
        int track_no = random.nextInt(3);
        obstacle obstacle = new obstacle(90, track_width[track_no], top);
        obstacle.current_track = track_no;

        int maxAttempts = 100; // adjust this value based on your needs
        int attempts = 0;

        while (attempts < maxAttempts) {
            boolean overlap = false;
            for (int j = obstacles1.size() - 1; j >= 0; j--) {
                obstacle existing_obstacle = obstacles1.get(j);
                if (obstacleOverlap(obstacle, existing_obstacle)) {
                    overlap = true;
                    break;
                }
            }
            if (!overlap) {
                obstacles1.add(obstacle);
                break;
            } else {
                top = (float) (Math.random() * (1800 - 100 + 1) + 100);
                obstacle.top = top;
            }
            attempts++;
        }
        if (attempts >= maxAttempts) {
            Log.w("createNewObstacle", "Failed to create a non-overlapping obstacle after " + attempts + " attempts.");
        }
        if (obstacles1.size() >= 99) {
            obstacles1.remove(0); // remove the oldest obstacle
        }
    }
    public boolean obstacleOverlap(obstacle new_obstacle,obstacle existing_obstacle){
        if (new_obstacle.top<existing_obstacle.bottom && new_obstacle.top>existing_obstacle.top){
            return true;
        }
        else if(new_obstacle.bottom<existing_obstacle.top && new_obstacle.top<existing_obstacle.top){
            return true;
        }
        return false;
    }
boolean stop=false;
    public void runGame() {
        Handler handler1=new Handler();
        gameRunnable = new Runnable() {
            @Override
            public void run() {
               // Log.i("rin","running");
              if(obstacle_hit_jerry<max_powe)
              {updateObstacles();
                updateScore();
                updatePowerUps();
                for(obstacle obstacle:obstacles1){
                    obstacle.increase_speed(speed);
                }
                  t.current_track=j.current_track;
                  t.x_center=jerry_tom_center[t.current_track];
                if(isTomCollidingWithObstacle(t, obstacles1)){
                    TomdodgeObstacle();
                }
                if(isJerryCollidingWithObstacle(j,obstacles1)!=null){

                    hashSet.add(isJerryCollidingWithObstacle(j,obstacles1));
                    obstacle_hit_jerry=hashSet.size();
                    Log.i("hitting","jerryy  hit"+obstacle_hit_jerry+" obstacle");
                }

                checkLosses();
                  ma.onTextChange("SCORE:"+updateScore()/30);
                invalidate();
                handler1.postDelayed(this, 0);
              }

              else{
                  ma.showDialog();
                  ma.setDialogText("SCORE: "+String.valueOf(updateScore()/30));
              }
            }
        };
        handler1.post(gameRunnable);
    }
    private void checkLosses(){
                if (obstacle_hit_jerry==1){
                        t.y_center=1900;
                }

        if(obstacle_hit_jerry==max_powe){
            //t.y_center-=20;
            catchJerry();
            if (j.y_center==t.y_center){
                t.y_center=1700;
                if(stop){
                    ma.showDialog();
                    ma.setDialogText("SCORE:  "+updateScore());
                }
            }



        }
    }

    private void stopgame() {
        int final_score=updateScore();
        stop=true;
        obstacles1.clear();
       // handler.removeCallbacksAndMessages(gameRunnable);
       // handler.removeCallbacks(this::runGame);

    }

    private void updateGameLogic() {
        updateObstacles();
       updateScore();
        updatePowerUps();
        for(obstacle obstacle:obstacles1){
            obstacle.increase_speed(-1);
        }
       if(isTomCollidingWithObstacle(t, obstacles1)){
           TomdodgeObstacle();
           if(t.current_track!=j.current_track){
               t.current_track=j.current_track;
               t.x_center=jerry_tom_center[t.current_track];
           }
       }
       if(isJerryCollidingWithObstacle(j,obstacles1)!=null){
           hashSet.add(isJerryCollidingWithObstacle(j,obstacles1));
           obstacle_hit_jerry=hashSet.size();
          Log.i("hitting","jerryy  hit"+obstacle_hit_jerry+" obstacle");
                }
            checkLosses();

    }

    private int updateScore() {
               if(obstacle_hit_jerry==1 && !score_deducted && !stop){
                   score-=200;
                   score_deducted=true;
                   Log.i("score","deducting");
               } else if(!stop)
               {
                   score++;
               }
               //ma.setDialogText("SCore: "+score);
               return score;
    }

    private void updatePowerUps() {
        for(powerUp powerUp:powerUps){
            //powerUp.increase_speed(-1);
            if(powerUp.y_center>=2200){
                powerUp.y_center=(float) (Math.random()*(-5000+100)+100);
            }
        }
        JerryPU(j,powerUps);
        for(cheese cheese:cheeses) {
            cheese.increase_speed(speed);

            if (cheese.y_center >= 2200) {
                cheese.y_center = (float) (Math.random()*(-5000+100)+100);
            }
        }
        JerryCheese(j,cheeses);
        for(trap trap:traps) {
            trap.increase_speed(speed);
            if (trap.y_center >= 2200) {
                trap.y_center = (float) (Math.random()*(-5000+100)+100);
            }
        }
        JerryTrap(j,traps);

    }
              /* final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        j.increase_speed(0.4F);
                       updateObstacles();
                        if (obstacle_hit_jerry < 2) {
                            // if (isTomCollidingWithObstacle(t, obstacles)) {
                            //       TomdodgeObstacle();

                            //   }

                            //if(isJerryCollidingWithObstacle(j,obstacles)) {
                            obstacle_hit_jerry++;
                            Log.i("HIIT", "jerry hit obstacle " + obstacle_hit_jerry + " times");
                            if (obstacle_hit_jerry == 1) {
                                t.increase_speed(2);
                            } else if (obstacle_hit_jerry == 2) {
                                t.increase_speed(2);
                                j.speed -= 1;
                                gameover = true;
                                catchJerry();
                            }
                            Log.i("HIIT", "gameover:" + gameover);
                        }

                        //updateRectanglePosition();
                        handler.postDelayed(this, 30); // 30 milliseconds interval for animation speed
                        //
                        //}
                    }

                    private void updateObstacles() {
                        for (int i = 0; i < obstacles1.size(); i++) {
                            obstacle currentObstacle = obstacles1.get(i);
                            if (currentObstacle.bottom == track_end) {
                                obstacles1.remove(i); // Remove obstacle from the list
                                //generate a new obstacle in the top again at a random position
                                float top = (float) (Math.random() * (1800 - 100 + 1) + 100);
                                int track_no = random.nextInt(3);//value between 0 and 2
                                obstacle obstacle = new obstacle(90, track_width[track_no], top);
                                obstacle.current_track = track_no;
                                if (obstacle.current_track == 1) {
                                    if (obstacle.top > 1500) {
                                        // obstacle.top-=500;
                                    }
                                }
                                obstacle.gap_down = 700;
                                obstacles1.add(obstacle);
                                obstacle.increase_speed(-2);

                            }
                        }
                        invalidate();

                    }
                };
                        handler.post(runnable);


*/

            private void catchJerry() {
                t.increase_speed(20);
                t.current_track=j.current_track;
                t.x_center=jerry_tom_center[t.current_track];
                if(t.y_center==j.y_center){
                    t.setSpeed(0);
                }
//                int horizontal_gap = (int) (t.x_center - j.x_center);
//                int track_difference = t.current_track - j.current_track;
//                int current_track = t.current_track;
//                if (track_difference == 1) { //jerry is present in the next adjacent track!
//                    if ((current_track >= 0) && (current_track < jerry_tom_center.length)) {
//                        if (current_track < 2) {
//                            t.x_center = jerry_tom_center[current_track + 1]; // Move to the next track (safe access)
//                            t.current_track += 1;
//                        }
//                    }
//
//                } else if (track_difference == 2) {
//                    if ((current_track >= 0) && (current_track < jerry_tom_center.length)) {
//                        if (current_track < 1) {
//                            t.x_center = jerry_tom_center[current_track + 2]; // Move to the next track (safe access)
//                            t.current_track += 1;
//                        }
//                    }
//                } else if (track_difference == -1) {//jerry is there in the track before his own.
//                    if ((current_track > 0) && (current_track < jerry_tom_center.length)) {
//                        if (current_track < 2) {
//                            t.x_center = jerry_tom_center[current_track - 1]; // Move to the next track (safe access)
//                            t.current_track += 1;
//                        }
//                    }
//                } else if (track_difference == -2) {//jerry in track0 and tom in track 2 is the only possible case
//                    if ((current_track > 0) && (current_track < jerry_tom_center.length)) {
//                        if (current_track < 3) {
//                            t.x_center = jerry_tom_center[current_track - 2]; // Move to the next track (safe access)
//                            t.current_track += 1;
//                        }
//                    }
//                }
            }


            @Override
            public boolean onTouchEvent(MotionEvent event) {
       /* int current_track=-1;
        for(int i=0; i<jerry_tom_center.length;i++){
            if (jerry_tom_center[i]==j.x_center){
                current_track=i;
                break;
            }
        }*/
                if (first_touch_jerry) {
                    j.increase_speed(10);
                    first_touch_jerry = false;
                }
                int current_track = j.current_track;
                //  Log.i("outside","jerry's current track:"+ current_track);
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    int jerry_x = (int) event.getX();

                    if ((current_track >= 0) && (current_track < jerry_tom_center.length) && Math.abs(jerry_x - j.x_center) > 100) {
                        if (jerry_x > j.x_center) {
                            if (current_track < 2) {
                                j.x_center = jerry_tom_center[current_track + 1];// Move to the next track (safe access)
                                j.current_track += 1;
                            }
                            if (current_track == 3) {
                                Toast.makeText(getContext(), "Invalid track", Toast.LENGTH_SHORT).show();
                            }
//
                        } else if (jerry_x < j.x_center) {
                            if (current_track > 0) {
                                j.x_center = jerry_tom_center[current_track - 1]; // Move to the previous track (safe access)
                                j.current_track -= 1;
                            }
                        }
                    } else {
                        // Handle the case where current_track is invalid (e.g., -1 or greater than track_width.length)
                        Log.i("invalid", "Invalid current track: " + current_track);
                    }
                }

                return true;
            }

            public void createTracks(Canvas canvas, float width, float gap) {
                float left = 50; //initial gap from screen
                float top = 100;
                float right = left + width;
                float bottom = 2300;
                for (int i = 0; i < 3; i++) {
                    canvas.drawRect(left, top, right, bottom, paint);
                    left += width + gap;
                    right = left + width;
                    invalidate();
                }
            }

            public boolean isTomCollidingWithObstacle(tom tom, ArrayList<obstacle> obstacles) {
                for (obstacle obstacle : obstacles) {
                    if(tom.current_track==obstacle.current_track){
                    if (tom.y_center - tom.radius -20 <= obstacle.bottom) {
                        return true; // Collision detected
                    }

                }}
                return false;
            }


    public obstacle isJerryCollidingWithObstacle(jerry jerry, ArrayList<obstacle> obstacles) {

                     for (obstacle obstacle : obstacles) {
                         // Check if Tom's x and y coordinates overlap with the obstacle's rectangle
                         if (jerry.current_track == obstacle.current_track) {
                             if ((jerry.y_center <= obstacle.bottom && jerry.y_center >= obstacle.top
                             ) || (jerry.y_center - jerry.radius <= obstacle.bottom && jerry.y_center - jerry.radius >= obstacle.top)) {

                                 jerryCollided = true;
                                 return obstacle;

                             }
                         }
                     }

        // Collision detected

        //  if (jerry.y_center - jerry.radius == obstacle.bottom - (float) obstacle.size / 2) {
        //    return true;
        // }

        //    return false; // No collision detected

       /* for (obstacle obstacle : obstacles) {
            // Check if Tom's x and y coordinates overlap with the obstacle's rectangle
            boolean jerryCollided = false;
            if (jerry.current_track == obstacle.current_track) {
                if ( (int) jerry.y_center - jerry.radius - 1 == (int) obstacle.top)
                {   // || jerry.y_center - jerry.radius ==(int) obstacle.top ){//&& (int)jerry.x_center==obstacle.left+ (float) obstacle.size /2) {
                  //  Log.i("j,o",(int)jerry.y_center - jerry.radius+"  "+(int) obstacle.bottom );
                    return obstacle; // Collision detected
                }
                else if ((int)jerry.y_center - jerry.radius  == (int) obstacle.top) {
                    return obstacle;
                } else if ((int)jerry.y_center - jerry.radius == obstacle.bottom) {
                      return obstacle;
                } else if ( (int)jerry.y_center - jerry.radius +1 == (int)obstacle.top ) {
                    return obstacle;                } else if ((int)jerry.y_center - jerry.radius -1== (int) obstacle.bottom) {
                    return obstacle;                  } else if ((int)jerry.y_center+j.radius-1==(int)obstacle.top) {
                    return obstacle;
                } else if ( (int)jerry.y_center+j.radius-1==(int)obstacle.bottom ) {
                    return obstacle;                  } else if ( jerry.y_center-jerry.radius +1 ==(int) obstacle.bottom) {
                    return obstacle;                  } else if ((int)jerry.y_center+j.radius+1==(int)obstacle.top) {
                    return obstacle;                  } else if ((int)jerry.y_center-j.radius==(int)obstacle.top+(float) obstacle.size /2) {
                    return obstacle;                  } else if (jerry.y_center - jerry.radius + 1 == (int) obstacle.top) {
                    return obstacle;                  } else if ((int)jerry.y_center+j.radius==(int)obstacle.top) {
                    return obstacle;                  } else if ((int)jerry.y_center+j.radius==(int)obstacle.bottom) {
                    return obstacle;                  } else if ( (int)jerry.y_center+j.radius+1==(int)obstacle.bottom) {
                    return obstacle;                  }
            } else {
                if (jerry.x_center - jerry.radius == obstacle.right) {
                    return obstacle;                  } else if (jerry.x_center + jerry.radius == obstacle.left) {
                    return obstacle;                  } else if (jerry.x_center - jerry.radius == (int) obstacle.right) {
                    return obstacle;                  } else if (jerry.x_center + jerry.radius == (int) obstacle.left) {
                    return obstacle;                  }
            }*/

                  /*  if (tom.current_track == obstacle.current_track) {
                        if ((tom.x_center == (obstacle.left + (obstacle.size / 2)))) {
                            Log.i("collision", "detected a collision");
                            return true;
                        }
                    }
                }*/

//                for (obstacle obstacle : obstacles) {
//                    // Check if Jerry's x and y coordinates overlap with the obstacle's rectangle
//                    if (jerry.current_track == obstacle.current_track) {
//                        if (jerry.y_center - jerry.radius == (int) obstacle.bottom ||
//                                jerry.y_center - jerry.radius == (int) obstacle.top) {
//                            if (!jerryCollided) {
//                                jerryCollided=true;
//                                return true; // Collision detected
//                            }
//                        }
//                    }
//


        return null;
    }
    public void resetGame() {
        j = new jerry((float) 540, 1600, 70, 0);
        t = new tom((float) 540, 2100, 90, 0);
        obstacle_hit_jerry = 0;
        score = 0;
        score_deducted = false;
        first_touch_jerry = true;
        gameover = false;
        speed = -1;
        max_powe=2;
        obstacles1.clear();
        cheeseCollected=0;
        ma.setCheese(String.valueOf(0));
        hashSet.clear();
        addObstacles();
        powerUps.clear();
        cheeses.clear();
        traps.clear();
        addBitmap();
        addCheese();
        addTrap();
        runGame();
    }
public void JerryPU(jerry jerry,ArrayList<powerUp> powerUPs) {
                float jerry_x=jerry.x_center;
                float jerry_y=jerry.y_center;
    for (powerUp pu : powerUPs) {
        if (pu.current_track == jerry.current_track) {
            float bitmap_x=pu.x_center+40;
            float bitmap_y=pu.y_center+40;
//            if ((jerry.y_center <= pu.y_center+pu.size_x && jerry.y_center >= pu.y_center
//            ) || (jerry.y_center - jerry.radius <= pu.y_center+pu.size_x && jerry.y_center - jerry.radius >= pu.y_center))  {
            if(Math.sqrt(Math.pow(jerry_x-bitmap_x, 2))+Math.pow(jerry_y-bitmap_y,2)<=jerry.radius)
            {
                Log.i("hit ","hit the pu!!");
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 50) {
                    if (isJerryCollidingWithObstacle(j, obstacles1) != null) {
                        //hashSet.clear();
                        Log.i("so", String.valueOf(hashSet.size()));
                    }


                }
                hashSet.clear();
                //max_powe++;
                Log.i("max", String.valueOf(max_powe));

            }
        }
    }
}


    public void JerryCheese(jerry jerry,ArrayList<cheese> cheeses) {
        float jerry_x = jerry.x_center;
        float jerry_y = jerry.y_center;

        for (cheese cheese : cheeses) {
            float bitmap_x = cheese.x_center + 40;
            float bitmap_y = cheese.y_center + 40;
              if (Math.sqrt(Math.pow(jerry_x - bitmap_x, 2)) + Math.pow(jerry_y - bitmap_y, 2) <= jerry.radius) {

                    Log.i("hit ", "hit the cheese!!");
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 50) {
                        try {
                            Thread.sleep(100); // Adjust sleep time as needed (higher = slower)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    cheeseCollected++;
                    ma.setCheese(String.valueOf(cheeseCollected));
                }

            }

        }

    public void JerryTrap(jerry jerry,ArrayList<trap> traps) {
        float jerry_x=jerry.x_center;
        float jerry_y=jerry.y_center;

        for(trap  trap:traps) {
            float bitmap_x = trap.x_center + 40;
            float bitmap_y = trap.y_center + 40;
            if (Math.sqrt(Math.pow(jerry_x - bitmap_x, 2)) + Math.pow(jerry_y - bitmap_y, 2) <= jerry.radius) {
                Log.i("hit ", "hit the trapp!!");
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 50) {
                        try {
                            Thread.sleep(100); // Adjust sleep time as needed (higher = slower)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // if (cheeseCollected>0)ma.showDialog2(); removeCallbacks(gameRunnable);
                    if (cheeseCollected > 0) {
                        Animation ccanim= AnimationUtils.loadAnimation(getContext(),R.anim.fade);
                        ma.setDisappear("CLAIMING YOUR CHEESE !");
                        ma.disappear.setVisibility(VISIBLE);

                        ccanim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ma.disappear.setVisibility(INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                        });
                        ccanim.setRepeatCount(1);
                        ma.disappear.startAnimation(ccanim);
                        cheeseCollected--;
                        ma.setCheese(String.valueOf(cheeseCollected));
                    } else {
                        t.y_center = 1850;
                    }
                }


            }
        }

            public void TomdodgeObstacle() {
         //       Log.i("runnable", "inside tom dodge");
                float max_distance1=0;
                float max_distance2=0;
                float max_distance3=0;
                ArrayList<Float> distances = new ArrayList<>();

                for(obstacle obstacle:obstacles1){
                    if(obstacle.current_track==0 && obstacle.bottom>max_distance1){
                        max_distance1=obstacle.bottom;
                    }
                    if(obstacle.current_track==1 && obstacle.bottom>max_distance2){
                        max_distance2=obstacle.bottom;
                    }
                    if(obstacle.current_track==2 && obstacle.bottom>max_distance3){
                        max_distance3=obstacle.bottom;
                    }
                }

                int current_track = t.current_track;
                distances.add(max_distance1);
                distances.add(max_distance2);
                distances.add(max_distance3);
                Float min_of_all = Collections.min(distances);
                int index = distances.indexOf(min_of_all);
                if ((current_track >= 0) && (current_track < jerry_tom_center.length)) {
                    t.x_center=jerry_tom_center[index];
                    t.current_track=index;
                }
            }




}





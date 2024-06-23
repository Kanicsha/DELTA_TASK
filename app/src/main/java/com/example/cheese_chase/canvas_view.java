package com.example.cheese_chase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;

import android.os.Handler;

import androidx.annotation.NonNull;

public class canvas_view extends View {
    Paint paint, paint_outline;
    Random random = new Random();

    jerry j;
    public tom t;
    int number_obstacles = 7;
    Handler handler=new Handler();
    int obstacle_hit_jerry=0;
    private Runnable gameRunnable;
    ArrayList<obstacle> obstacles1 = new ArrayList<>();
    ArrayList<powerUp> powerUps = new ArrayList<>();
    int number_powerUps=10;
    //obstacle block;
   int[] track_width = {135, 495, 855};
    //int[] track_width = {180,540,900};
    boolean first_touch_jerry=true;
    boolean gameover=false;
    int track_end,track_start;

    int[] jerry_tom_center={180,540,900};
    private Timer timer;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mario);
//my emulator screen has a resolution of 1080x2400 pixels, hence will be using that to arrange my elements

    public canvas_view(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
        init();
       // gameRunnable = new GameRunnable();

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
        j = new jerry((float) 540, 1970, 70, 0);
        t = new tom((float) 540, 2200, 90, 0);
        for (int i = 0; i < number_obstacles; i++) {
            int track_no = random.nextInt(3);//value between 0 and 2
            //  Log.i("arrays",left[i]+"  "+top[i]);
            float top = (float) (Math.random() * (1600 - 500 + 1) + 500);
           // Log.i("random tiop", String.valueOf(top));

            obstacle obstacle = new obstacle(90, track_width[track_no], top);
            obstacle.current_track = track_no;
            if (obstacle.current_track == 1) {
                if (obstacle.top > 1500) {
                    // obstacle.top-=500;
                }
            }
            obstacle.gap_down = 700;
            obstacles1.add(obstacle);
            //obstacle.increase_speed(-2);
        }

        //addBitmap(); //just add them into arraylist now;

        //generating obstacles with a minimum gap between them in each track
        for (int i = 1; i < obstacles1.size(); i++) {
            obstacle current_obstacle = obstacles1.get(i);
            Log.i("obs", obstacles1.get(i - 1).left + " and " + obstacles1.get(i - 1).top + " " + obstacles1.get(i - 1).bottom);
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
        gameover=false;
       // runGame();

    }
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        runGame();
    }

    @Override
            protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        createTracks(canvas, 260, 100);
        t.create_tom(canvas);
        j.create_jerry(canvas);

        createObstacles(canvas);
        invalidate();
    }

    public void createObstacles(Canvas canvas){
        for (obstacle obstacle:obstacles1){
            obstacle.create_obstacle(canvas);
           // obstacle.increase_speed(-2);
        }
    }
   /* class GameRunnable implements Runnable{

        @Override
        public void run() {
            {
                j.increase_speed(0.4F);
                updateObstacles();
                while(!gameover){
                    // if (isTomCollidingWithObstacle(t, obstacles)) {
                    //       TomdodgeObstacle();

                    //   }

                    //if(isJerryCollidingWithObstacle(j,obstacles)) {
                    obstacle_hit_jerry++;
                    Log.i("HIIT", "jerry hit obstacle " + obstacle_hit_jerry + " times");
                    if (obstacle_hit_jerry == 1) {
                        t.increase_speed(2);
                    } else if (obstacle_hit_jerry == 9) {
                        t.increase_speed(2);
                        j.speed -= 1;
                        gameover = true;
                       // catchJerry();
                    }
                    Log.i("HIIT", "gameover:" + gameover);
                }
                postInvalidate(); // invalidate the view to trigger onDraw()
                try {
                    Thread.sleep(16); // 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //updateRectanglePosition();
                //handler.postDelayed(this, 30); // 30 milliseconds interval for animation speed
                //
                //}
            }

        }


    }*/
    private void updateObstacles() {

       /* for (int i = 0; i < obstacles1.size(); i++) {
            obstacle currentObstacle = obstacles1.get(i);
            Log.i("updation", String.valueOf(currentObstacle.bottom));
            if (currentObstacle.bottom >= track_end) {
                obstacles1.remove(currentObstacle); // Remove obstacle from the list

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
                Log.i("added","added obstacles"+obstacle.top+"  "+obstacle.current_track);
                obstacle.increase_speed(-2);
            }
        }*/

            for (int i=0;i<obstacles1.size();i++) {
                obstacle obstacle=obstacles1.get(i);
                obstacle.increase_speed(-2);
                if (obstacle.bottom >= 2200) {
                    //Log.i("end","obstacle croseed!!");
                    obstacle.setTop((float) (Math.random()*(300-50)+50));
                    // obstacle.bottom=obstacle.top+obstacle.size;
                    int track_no = random.nextInt(3);//value between 0 and 2
                    obstacle.current_track=track_no;
                    obstacle.setLeft(track_width[track_no]);
                   // boolean removed=obstacles1.remove(obstacle);
                   // obstacles1.trimToSize();
                   // Log.i("removed?","removal status"+removed);
                  //  if (obstacles1.size() < 100) {
                        //createNewObstacle();
                  //  }
//                    for (int j = i; j <= obstacles1.size(); j++) {
//
//                        if (obstacle.current_track == obstacles1.get(j).current_track) {
//                            float newTop = obstacles1.get(j).bottom + obstacles1.get(j).gap_down;
//                            // Log.i("gap|bottom", obstacles1.get(j).gap_down +"  "+ obstacles1.get(j).bottom);
//                            float newTop1 = Math.max(newTop, obstacle.top);
//                            obstacle.setTop(newTop);
//                            //current_obstacle.gap_down+= current_obstacle.bottom;
//                            break;
//                        }
//                    }

                }
            }

    }
    private void createBitmap(Canvas canvas){
        for(powerUp powerUp:powerUps){
            powerUp.create_powerUP(canvas);
        }
    }
    private void addBitmap(){
        for(int i=0;i<number_powerUps;i++){
            int track_no = random.nextInt(3);//value between 0 and 2
            float top = (float) (Math.random() * (1600 - 500 + 1) + 500);
            powerUp powerUp=new powerUp(bitmap, (float) track_width[track_no], top, 100);
            powerUps.add(powerUp);
            powerUp.current_track=track_no;
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
    public void runGame() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                    updateGameLogic();
                    invalidate();
                    handler.postDelayed(this, 10); // 60 FPS

            }
        });
    }
    private void checkLosses(){
        if (obstacle_hit_jerry==1){
            t.y_center-=100;
        }
        if(obstacle_hit_jerry==4){
           // Log.i("hitt","jerry loses!!!");
            j.setSpeed(0);
            obstacles1.clear();
            catchJerry();
            if (j.y_center==t.y_center-t.radius){
                //displaydialog();
            }
        }
    }
    private void updateGameLogic() {
        updateObstacles();

        updatePowerUps();
       if(isTomCollidingWithObstacle(t, obstacles1)){
           TomdodgeObstacle();
       }
       if(isJerryCollidingWithObstacle(j,obstacles1)){
          // obstacle_hit_jerry++;
                }

       checkLosses();
    }

    private void updatePowerUps() {
        for(powerUp powerUp:powerUps){
            powerUp.increase_speed(-2);
        }
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
                t.increase_speed(2);
                t.current_track=j.current_track;
                t.x_center=jerry_tom_center[t.current_track];
                if(t.y_center==j.y_center && j.x_center==t.x_center){

                    t.setSpeed(0);
                    //displaydialog;
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
                    j.increase_speed(100);
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
                //tom.x_center < obstacle.right &&
                //                            tom.x_center + tom.radius > obstacle.left &&
                for (obstacle obstacle : obstacles) {
                    // Check if Tom's x and y coordinates overlap with the obstacle's rectangle
                    if(tom.current_track==obstacle.current_track){
                    if (tom.y_center - tom.radius -20 <= obstacle.bottom) {
                        Log.i("hitting","TOmmy hit the obstacle");
                        return true; // Collision detected
                    }
                  /*  if (tom.current_track == obstacle.current_track) {
                        if ((tom.x_center == (obstacle.left + (obstacle.size / 2)))) {
                            Log.i("collision", "detected a collision");
                            return true;
                        }
                    }
                }*/
                }}
                return false;
            }


            public boolean isJerryCollidingWithObstacle(jerry jerry, ArrayList<obstacle> obstacles) {
              /*  for (obstacle obstacle : obstacles) {
                    // Check if Tom's x and y coordinates overlap with the obstacle's rectangle
                    if ( jerry.y_center < obstacle.bottom &&
                            jerry.y_center + jerry.radius > obstacle.top) {
                        return true;
                        // Collision detected
                    }
                    // if (jerry.y_center - jerry.radius == obstacle.bottom - (float) obstacle.size / 2) {
                    //   return true;
                    //}
                }

                return false; // No collision detected*/

                for (obstacle obstacle : obstacles) {
                    // Check if Tom's x and y coordinates overlap with the obstacle's rectangle
                    if(jerry.current_track==obstacle.current_track){
                        if (jerry.y_center - jerry.radius -2 <= obstacle.bottom) {
                            Log.i("hitting","jerryy  hit the obstacle");
                            return true; // Collision detected
                        }
                  /*  if (tom.current_track == obstacle.current_track) {
                        if ((tom.x_center == (obstacle.left + (obstacle.size / 2)))) {
                            Log.i("collision", "detected a collision");
                            return true;
                        }
                    }
                }*/
                    }}
                return false;
            }


            public void TomdodgeObstacle() {
                Log.i("runnable", "inside tom dodge");
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
//                    if (current_track < 2) {
//                        t.x_center = jerry_tom_center[current_track + 1]; // Move to the next track (safe access)
//                        t.current_track += 1;
//
//                    } else {//current_track=2
//                        t.x_center = jerry_tom_center[current_track - 1]; // Move to the previous track (safe access)
//                        t.current_track -= 1;
//
//                    }
                    t.x_center=jerry_tom_center[index];
                    t.current_track=index;
                }
                //  if(isTomCollidingWithObstacle(t,obstacles)){
                //    TomdodgeObstacle();
                //}
            }

            public int[] generate_array(int size) {
     /*   Arrays.fill(obstaclePositions, -1);
        int track_no = random.nextInt(3);//value between 0 and 2
        float left = track_width[track_no];
        //  float top = (float) (Math.random() * (2200 - 100 + 1) + 100);
        int filledIndex = 0;
        while (filledIndex < size) {
            boolean isUnique = true;
            for (int i = 0; i < filledIndex; i++) {
                if (Math.abs(obstaclePositions[i] - left) < gap) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                obstaclePositions[filledIndex] = (int) left;
                filledIndex++;
            }
        }*/
                int[] track_width = {135, 495, 855};
                int[] uniqueArray = new int[size];

                for (int i = 0; i < size; i++) {
                    int left;
                    HashSet<Integer> obstaclePositions = new HashSet<>();
                    do {
                        Random random = new Random();
                        int track_no = random.nextInt(3);//value between 0 and 2
                        left = track_width[track_no];

                    } while (obstaclePositions.add((int) left));
                    uniqueArray[i] = (int) left;


                }
                //returning as int[] instead of hashset
                return uniqueArray;
            }

            public  int[] generateUniqueArray(int size, int minDifference) {
                Log.i("array in left", "inside function");
                int[] track_width = {135, 495, 855};
                if (size <= 1 || minDifference <= 0) {
                    throw new IllegalArgumentException("Invalid arguments: size must be greater than 1 and minDifference must be positive");
                }
                boolean isUnique = false;

                // Initialize an empty array to hold the unique elements
                int[] uniqueArray = new int[size];

                // Set a starting point for the elements (can be adjusted based on your needs)
                int startingValue = 50;

                // Loop to generate and fill the array
                HashSet<Integer> usedPositions;

                for (int i = 0; i < size; i++) {

                    int top;
                    usedPositions = new HashSet<>();

                    do {
                        // Generate a random number within a valid range
//
                        int offset = (int) (Math.random() * (Math.max(2200 - startingValue, 0)));
                        top = (int) (Math.random() * 2100 - startingValue);

                        // Ensure top stays within valid range (optional, adjust based on your needs)
                        //top = Math.max(Math.min(top, 2200), 100);

                        // Check if the new value is unique compared to existing elements and add it to usedPositions
                    } while (!usedPositions.add(top));
                    // Add the unique value to the array and increment the starting value for the next iteration
                    uniqueArray[i] = top;
                    startingValue += minDifference;
                }
                return uniqueArray;
            }

        }





package com.example.mytomatogameapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public enum DirectionAction {LEFT, RIGHT}
    private int numberOfLife=2;
    ImageView[] Cars;
    ImageView[][] obstacles;
    ImageView[] boom;
    ImageButton arrowL, arrowR;
    ImageView[] hearts ;
    Timer timer ;
    TimerTask timerTask;
    TextView speedText ;
    long last_time;

    private SensorManager sensorManager;
    Sensor mySensor;
    boolean[] booleanLocations = new boolean[5];

    private static int SPEED=600;
    private int clock=0;

    public void onClickSlower(View v) throws InterruptedException {
        this.timer.cancel();
        if(SPEED<2600)
            SPEED+=200;
        this.speedText.setText(""+SPEED);
        this.timer = new Timer();
        createTaskTimer();
        setTicks(this.timer);
    }
    public void onClickFaster(View v){
        this.timer.cancel();
        if(SPEED>200)
            SPEED-=200;
        this.speedText.setText(""+SPEED);
        this.timer = new Timer();
        createTaskTimer();
        setTicks(this.timer);
    }
    public void createTaskTimer(){
        this.timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                runOnUiThread(() -> moveObstacles());
            }
        };
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //My time
        last_time = System.nanoTime();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, mySensor, 2000000);
        this.speedText = (TextView)findViewById(R.id.speed_id);
        this.speedText.setText (""+SPEED);
        initView();
        MoveCar();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        double x = sensorEvent.values[0]*0.7;

        long time = System.nanoTime();
        int delta_time = (int)((time - last_time) / 1000000);
        if ( x>=1.8 && delta_time > 450) {
            last_time = time;
            moveLeft();
        } else if (x < -1.8 && delta_time > 450 ) {
            last_time = time;
            moveRight();
        }

    }
    private void setTicks(Timer myTimer){
        myTimer.scheduleAtFixedRate(this.timerTask,SPEED, SPEED);
    }
    private void startTicker() {
        timer = new Timer();
        setTicks(timer);
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                runOnUiThread(() -> moveObstacles());
            }
        };
        startTicker();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void moveObstacles(){
        clock++;
        hideBooms();
        for(int i=0;i<=4;i++){
            if (obstacles[i][3].getVisibility()==View.VISIBLE) {
                obstacles[i][3].setVisibility(View.GONE);
            }
            for (int j = 2; j >= 0; j--) {
                if (obstacles[i][j].getVisibility() == View.VISIBLE) {
                    obstacles[i][j].setVisibility(View.INVISIBLE);
                    obstacles[i][j + 1].setVisibility(View.VISIBLE);
                }
            }
        }
        if(clock%2==0){
            int random=(int) ((Math.random()*5)+1);
            switch(random){
                case 1:{
                    obstacles[0][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 2:{
                    obstacles[1][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 3:{
                    obstacles[2][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 4:{
                    obstacles[3][0].setVisibility(View.VISIBLE);
                    break;
                }
                case 5:{
                    obstacles[4][0].setVisibility(View.VISIBLE);
                    break;
                }

            }
        }
        checkHit();
    }
    public  void hideBooms(){

        for (int i=0;i<5;i++){
            boom[i].setVisibility(View.GONE);
        }
        if(booleanLocations[0] &&Cars[0].getVisibility()==View.GONE) {
            Cars[0].setVisibility(View.VISIBLE);
        }else if(booleanLocations[1] &&Cars[1].getVisibility()==View.GONE) {
            Cars[1].setVisibility(View.VISIBLE);
        }else if(booleanLocations[2] &&Cars[2].getVisibility()==View.GONE) {
            Cars[2].setVisibility(View.VISIBLE);
        }else if(booleanLocations[3] &&Cars[3].getVisibility()==View.GONE){
            Cars[3].setVisibility(View.VISIBLE);
        }else if(booleanLocations[4] &&Cars[4].getVisibility()==View.GONE){
            Cars[4].setVisibility(View.VISIBLE);
        }
    }


    public void initView() {
        hearts = new ImageView[]{
                findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)
        };
        Cars = new ImageView[]{
                findViewById(R.id.LeftCar1),findViewById(R.id.LeftCar2),findViewById(R.id.LeftCar3), findViewById(R.id.CenterCar), findViewById(R.id.RightCar)
        };
        obstacles = new ImageView[][]{
                {findViewById(R.id.leftObstacle11), findViewById(R.id.leftObstacle12), findViewById(R.id.leftObstacle13),findViewById(R.id.leftObstacle14)},
                {findViewById(R.id.leftObstacle21), findViewById(R.id.leftObstacle22), findViewById(R.id.leftObstacle23),findViewById(R.id.leftObstacle24)},
                {findViewById(R.id.leftObstacle31), findViewById(R.id.leftObstacle32), findViewById(R.id.leftObstacle33),findViewById(R.id.leftObstacle34)},
                {findViewById(R.id.centerObstacle1),findViewById(R.id.centerObstacle2),findViewById(R.id.centerObstacle3),findViewById(R.id.centerObstacle4)},
                {findViewById(R.id.rightObstacle1),findViewById(R.id.rightObstacle2),findViewById(R.id.rightObstacle3),findViewById(R.id.rightObstacle4)}

        };
        arrowR=findViewById(R.id.ArrowR);
        arrowL=findViewById(R.id.ArrowL);
        boom=new ImageView[]{
                findViewById(R.id.Boom11),findViewById(R.id.Boom21),findViewById(R.id.Boom31),findViewById(R.id.Boom2),findViewById(R.id.Boom3)
        };
        checkWhereIsCar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MoveCar() {

        arrowL.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            moveLeft();
//            for (int i = 0; i < booleanLocations; i++) {
//
//            }
        });
        arrowR.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            moveRight();
        });
    }
    void moveLeft(){
        if (booleanLocations[4]) {
            Cars[3].setVisibility(View.VISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[3] = true;
            booleanLocations[4]=false;
            booleanLocations[2]=false;
            booleanLocations[1]=false;
            booleanLocations[0]=false;

        } else if (booleanLocations[3]) {
            Cars[2].setVisibility(View.VISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[2] = true;
            booleanLocations[3]=false;
            booleanLocations[4]=false;
            booleanLocations[1]=false;
            booleanLocations[0]=false;
        } else if (booleanLocations[2]) {
            Cars[1].setVisibility(View.VISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[1] = true;
            booleanLocations[2]=false;
            booleanLocations[3]=false;
            booleanLocations[4]=false;
            booleanLocations[0]=false;

        } else if (booleanLocations[1]) {
            Cars[0].setVisibility(View.VISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            booleanLocations[0] = true;
            booleanLocations[1]=false;
            booleanLocations[2]=false;
            booleanLocations[3]=false;
            booleanLocations[4]=false;
        }
    }
    void moveRight(){
        if (booleanLocations[0]) {
            Cars[1].setVisibility(View.VISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[1] = true;
            booleanLocations[2]=false;
            booleanLocations[3]=false;
            booleanLocations[4]=false;
            booleanLocations[0]=false;

        } else if (booleanLocations[1]) {
            Cars[2].setVisibility(View.VISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[2] = true;
            booleanLocations[3]=false;
            booleanLocations[4]=false;
            booleanLocations[1]=false;
            booleanLocations[0]=false;
        } else if (booleanLocations[2]) {
            Cars[3].setVisibility(View.VISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[4].setVisibility(View.INVISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            booleanLocations[3] = true;
            booleanLocations[2]=false;
            booleanLocations[1]=false;
            booleanLocations[4]=false;
            booleanLocations[0]=false;
        } else if (booleanLocations[3]) {
            Cars[4].setVisibility(View.VISIBLE);
            Cars[0].setVisibility(View.INVISIBLE);
            Cars[1].setVisibility(View.INVISIBLE);
            Cars[2].setVisibility(View.INVISIBLE);
            Cars[3].setVisibility(View.INVISIBLE);
            booleanLocations[4] = true;
            booleanLocations[1]=false;
            booleanLocations[2]=false;
            booleanLocations[3]=false;
            booleanLocations[0]=false;
        }
    }

    void restartGame(){
        setTicks(timer);
        numberOfLife=2;
            for (int i=0;i<3;i++){
                hearts[i].setVisibility(View.VISIBLE);
            }
        }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void checkHit() {
        if (numberOfLife<0){
            Toast.makeText(this,"Game over",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainMenu.class);
            startActivity(i);
            this.timer.cancel();
//            restartGame();
        }
        if (obstacles[4][3].getVisibility() == View.VISIBLE&&Cars[4].getVisibility()==View.VISIBLE) {
            Cars[4].setVisibility(View.GONE);
            obstacles[4][3].setVisibility(View.GONE);
            boom[4].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[3][3].getVisibility() == View.VISIBLE&&Cars[3].getVisibility()==View.VISIBLE) {
            Cars[3].setVisibility(View.GONE);
            obstacles[3][3].setVisibility(View.GONE);
            boom[3].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[2][3].getVisibility() == View.VISIBLE&&Cars[2].getVisibility()==View.VISIBLE) {
            Cars[2].setVisibility(View.GONE);
            obstacles[2][3].setVisibility(View.GONE);
            boom[2].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[1][3].getVisibility() == View.VISIBLE&&Cars[1].getVisibility()==View.VISIBLE) {
            Cars[1].setVisibility(View.GONE);
            obstacles[1][3].setVisibility(View.GONE);
            boom[1].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        } else if (obstacles[0][3].getVisibility() == View.VISIBLE&&Cars[0].getVisibility()==View.VISIBLE) {
            Cars[0].setVisibility(View.GONE);
            obstacles[0][3].setVisibility(View.GONE);
            boom[0].setVisibility(View.VISIBLE);
            hearts[numberOfLife--].setVisibility(View.INVISIBLE);
            toast();
            vibrate();
        }


    }
    void toast(){

        switch (numberOfLife){
            case  0:  Toast.makeText(this,"One life left",Toast.LENGTH_SHORT).show();
                break;
            case  1:  Toast.makeText(this,"You died",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vibrate() {
        Vibrator v;
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }


    void checkWhereIsCar() {
        if (Cars[0].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE
                &&Cars[1].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            booleanLocations[0] = true;
        }
        if (Cars[1].getVisibility() == View.VISIBLE&&Cars[0].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE
                &&Cars[3].getVisibility() == View.INVISIBLE&&Cars[4].getVisibility() == View.INVISIBLE) {
            booleanLocations[1] = true;
        }
        if (Cars[2].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[0].getVisibility() == View.INVISIBLE
                &&Cars[3].getVisibility() == View.INVISIBLE&&Cars[4].getVisibility() == View.INVISIBLE) {
            booleanLocations[2] = true;
        }
        if (Cars[3].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[0].getVisibility() == View.INVISIBLE
                &&Cars[4].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            booleanLocations[3] = true;
        }
        if (Cars[4].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[0].getVisibility() == View.INVISIBLE
                &&Cars[2].getVisibility() == View.INVISIBLE&&Cars[3].getVisibility() == View.INVISIBLE) {
            booleanLocations[4] = true;
        }
    }








}







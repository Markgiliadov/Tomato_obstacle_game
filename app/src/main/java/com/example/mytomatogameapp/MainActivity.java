package com.example.mytomatogameapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int numberOfLife=2;

    ImageView[] Cars;
    ImageView[][] obstacles;
    ImageView[] boom;
    ImageButton arrowL, arrowR;
    ImageView[] hearts ;
    Timer timer ;
    TimerTask timerTask;
    TextView speedText ;

    boolean isLeft,isCenter, isRight;

    private static int SPEED=600;
    private int clock=0;

    public void onClickSlower(View v) throws InterruptedException {
        this.timer.cancel();
        SPEED+=200;
        this.speedText.setText(""+SPEED);
        this.timer = new Timer();
        createTaskTimer();
        setTicks(this.timer);
    }

    public void onClickFaster(View v){
        this.timer.cancel();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.speedText = (TextView)findViewById(R.id.speed_id);
        this.speedText.setText (""+SPEED);
        initView();
        MoveCar();
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
        for(int i=0;i<=2;i++){
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
            int random=(int) ((Math.random()*3)+1);
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

            }
        }
        checkHit();
    }
    public  void hideBooms(){

        for (int i=0;i<3;i++){
            boom[i].setVisibility(View.GONE);
        }
        if(isLeft &&Cars[0].getVisibility()==View.GONE){
            Cars[0].setVisibility(View.VISIBLE);
        }else if(isCenter &&Cars[1].getVisibility()==View.GONE){
            Cars[1].setVisibility(View.VISIBLE);
        }else if(isRight &&Cars[2].getVisibility()==View.GONE){
            Cars[2].setVisibility(View.VISIBLE);
        }
    }


    public void initView() {
        hearts = new ImageView[]{
                findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)
        };
        Cars = new ImageView[]{
                findViewById(R.id.LeftCar), findViewById(R.id.CenterCar), findViewById(R.id.RightCar)
        };
        obstacles = new ImageView[][]{
                {findViewById(R.id.rightObstacle1),findViewById(R.id.rightObstacle2),findViewById(R.id.rightObstacle3),findViewById(R.id.rightObstacle4)},
                {findViewById(R.id.centerObstacle1),findViewById(R.id.centerObstacle2),findViewById(R.id.centerObstacle3),findViewById(R.id.centerObstacle4)},
                {findViewById(R.id.leftObstacle1), findViewById(R.id.leftObstacle2), findViewById(R.id.leftObstacle3),findViewById(R.id.leftObstacle4)}
        };
        arrowR=findViewById(R.id.ArrowR);
        arrowL=findViewById(R.id.ArrowL);
        boom=new ImageView[]{
                findViewById(R.id.Boom1),findViewById(R.id.Boom2),findViewById(R.id.Boom3)
        };
        checkWhereIsCar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void MoveCar() {

        arrowL.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            if (isRight) {
                Cars[1].setVisibility(View.VISIBLE);
                Cars[2].setVisibility(View.INVISIBLE);
                isCenter = true;
                isRight=false;

            } else if (isCenter) {
                Cars[1].setVisibility(View.INVISIBLE);
                Cars[0].setVisibility(View.VISIBLE);
                isCenter=false;
                isLeft=true;
            }

        });
        arrowR.setOnClickListener(e -> {
            checkWhereIsCar();
            checkHit();
            if (isCenter) {
                Cars[1].setVisibility(View.INVISIBLE);
                Cars[2].setVisibility(View.VISIBLE);
                isCenter = false;
                isRight=true;
            } else if (isLeft) {
                Cars[1].setVisibility(View.VISIBLE);
                Cars[0].setVisibility(View.INVISIBLE);
                isCenter=true;
                isLeft=false;
            }
        });
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
        if (obstacles[2][3].getVisibility() == View.VISIBLE&&Cars[0].getVisibility()==View.VISIBLE) {
            Cars[0].setVisibility(View.GONE);
            obstacles[2][3].setVisibility(View.GONE);
            boom[0].setVisibility(View.VISIBLE);
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
        } else if (obstacles[0][3].getVisibility() == View.VISIBLE&&Cars[2].getVisibility()==View.VISIBLE) {
            Cars[2].setVisibility(View.GONE);
            obstacles[0][3].setVisibility(View.GONE);
            boom[2].setVisibility(View.VISIBLE);
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
        if (Cars[0].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            isLeft = true;
        }
        if (Cars[1].getVisibility() == View.VISIBLE&&Cars[0].getVisibility() == View.INVISIBLE&&Cars[2].getVisibility() == View.INVISIBLE) {
            isCenter = true;
        }
        if (Cars[2].getVisibility() == View.VISIBLE&&Cars[1].getVisibility() == View.INVISIBLE&&Cars[0].getVisibility() == View.INVISIBLE) {
            isRight = true;
        }
    }








}







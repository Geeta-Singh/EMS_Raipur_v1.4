package com.bydesign.ems1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bydesign.ems1.services.SharedVariables;

public class SplashActivity extends Activity {

/*
    int progress = 0;
    int progressStatus = 0;
    ProgressBar pb;
    Handler handler = new Handler();*/

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 4500) {
                        sleep(100);
                        waited += 90;
                    }
                    if(SharedVariables.getUserName(SplashActivity.this).length() == 0)
                    {
                        Intent i=new Intent(getApplicationContext(),Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish(); // call Login Activity
                    }
                    else
                    {
                        Intent i=new Intent(getApplicationContext(),navigationdrawer.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();// Stay at the current activity.
                    }
                   /* Intent intent = new Intent(SplashActivity.this,
                            Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }



  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb=(ProgressBar)findViewById(R.id.progressbar);


        new Thread(new Runnable(){

            @Override
            public void run() {
                while(progress<10){
                    progressStatus=doSomeWork();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }

                    });

                }
                if(SharedVariables.getUserName(SplashActivity.this).length() == 0)
                {
                    Intent i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish(); // call Login Activity
                }
                else
                {
                    Intent i=new Intent(getApplicationContext(),navigationdrawer.class);
                    startActivity(i);
                    finish();// Stay at the current activity.
                }


            }

            private int doSomeWork() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ++progress;
            }

        }).start();*/








   /* int progress = 0;
    int progressStatus = 0;
    ProgressBar pb;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // pb=(ProgressBar)findViewById(R.id.progressbar);
        ImageView im=(ImageView)findViewById(R.id.splash);
      //  Ion.with(im).load("android.resource://com.bydesign.ems1/" + R.drawable.ace_splash);


        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(SharedVariables.getUserName(SplashActivity.this).length() == 0)
                {
                    Intent i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish(); // call Login Activity
                }
                else
                {
                    Intent i=new Intent(getApplicationContext(),navigationdrawer.class);
                    startActivity(i);
                    finish();// Stay at the current activity.
                }


            }



        }).start();*/
        /*Intent i=new Intent(getApplicationContext(),Login.class);
        startActivity(i);
        finish();*/



       /* pb=(ProgressBar)findViewById(R.id.progressbar);


        new Thread(new Runnable(){

            @Override
            public void run() {
                while(progress<10){
                    progressStatus=doSomeWork();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }

                    });

                }
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }

            private int doSomeWork() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ++progress;
            }

        }).start();
    }*/
    //}


}

package com.akash.noteoncloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        //changing the color of navigation bar
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.yellow));
        }


        //to remove status bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      //status bar color code
//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.parseColor("#111B34"));

        getSupportActionBar().hide();
        Thread thread= new Thread(){
            public void run()
            {
                try{
                    sleep(300);
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }
                finally {
                    Intent intent= new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };thread.start();



    }
}
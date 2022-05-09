package com.akash.noteoncloud;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    private  TextView mtitleofnotedetail;
    private  TextView mcontentofnotedetail;
    ImageView mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);


        //for vibration
        final Vibrator vibe = (Vibrator) notedetails.this.getSystemService(Context.VIBRATOR_SERVICE);
        // vibe.vibrate(80);

        mtitleofnotedetail=findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail=findViewById(R.id.contentofnotedetail);
        mgotoeditnote=findViewById(R.id.gotoeditnote);
        Toolbar toolbar=findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);

        //changing the color of navigation bar
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darknavybackground));
        }

       // getSupportActionBar().hide();

      //status color bar code
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#111B34"));


        // for creating scrolview
        mcontentofnotedetail.setMovementMethod(new ScrollingMovementMethod());



        //to remove status bar
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data=getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(30);
                Intent intent=new Intent(view.getContext(), editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);



            }
        });

        mtitleofnotedetail.setText(data.getStringExtra("title"));
        mcontentofnotedetail.setText(data.getStringExtra("content"));



    }



    //Code for Back Press Button
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//
//        if(item.getItemId()==android.R.id.home)
//        {
//            onBackPressed();
//
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        // do something on back.
        startActivity(new Intent(notedetails.this, notesActivity.class));
        return;
    }



}
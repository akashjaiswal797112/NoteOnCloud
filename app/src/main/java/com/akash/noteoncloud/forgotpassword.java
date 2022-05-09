package com.akash.noteoncloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
    private EditText mforgotpassword;
    private TextView mpasswordrecoverbutton;
    private  TextView mgobacktologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        //for vibration
        final Vibrator vibe = (Vibrator) forgotpassword.this.getSystemService(Context.VIBRATOR_SERVICE);

        //status bar color code
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0D1528"));

        //to remove status bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //changing the color of navigation bar
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darknavybackground));
        }

        //This statement is for removing the Action Bar from the top of the layout
        getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);

        firebaseAuth=FirebaseAuth.getInstance();


        //This is used to move the user back to login page from the signupp page
        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for vibration
                final Vibrator vibe = (Vibrator) forgotpassword.this.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(50);
                Intent intent= new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //This is the code for recovering the forgotPassword user
        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibe.vibrate(80);

                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter your email id", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //code it after creating firebase
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                         {
                             Toast.makeText(getApplicationContext(), "Reset link sent to your mail", Toast.LENGTH_SHORT).show();
                             finish();
                             startActivity(new Intent(forgotpassword.this, MainActivity.class));
                         }
                         else
                         {
                             Toast.makeText(getApplicationContext(),"Account not Exist / Email is wrong", Toast.LENGTH_SHORT).show();
                         }

                        }
                    });

                }
            }
        });




    }
}
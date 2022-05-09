
package com.akash.noteoncloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import  android.os.VibrationEffect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTabFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    EditText loginemail,loginpassword;
    TextView forgetpassword;
    TextView loginbutton;
    private FirebaseAuth firebaseAuth;
ImageView emailicon2,emailicon3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container, false);
        float v=0;

        //for vibration
        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // vibe.vibrate(80);

        loginemail = root.findViewById(R.id.loginemail);
        loginpassword = root.findViewById(R.id.loginpassword);
        forgetpassword= root.findViewById(R.id.forgetpassword);
        loginbutton = root.findViewById(R.id.loginbutton);

        //changing the color of navigation bar
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.darknavybackground));
        }



        emailicon2 = root.findViewById(R.id.emailicon2);
        emailicon2.setTranslationY(300);
        emailicon2.setAlpha(v);
        emailicon2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        emailicon3 = root.findViewById(R.id.emailicon3);
        emailicon3.setTranslationY(300);
        emailicon3.setAlpha(v);
        emailicon3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        loginemail = root.findViewById(R.id.loginemail);
        loginemail.setTranslationY(300);
        loginemail.setAlpha(v);
        loginemail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        loginpassword = root.findViewById(R.id.loginpassword);
        loginpassword.setTranslationY(300);
        loginpassword.setAlpha(v);
        loginpassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        forgetpassword = root.findViewById(R.id.forgetpassword);
        forgetpassword.setTranslationY(300);
        forgetpassword.setAlpha(v);
        forgetpassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        loginbutton = root.findViewById(R.id.loginbutton);
        loginbutton.setTranslationY(300);
        loginbutton.setAlpha(v);
        loginbutton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();


        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

       // final Vibrator vibe = (Vibrator) LoginTabFragment.this.get
       // final Vibrator vibe = (Vibrator) LoginTabFragment.this.getsystemservices(Context.VIBRATOR_SERVICE);



        //if the user is already login....redirect him directly to the content
        if(firebaseUser!=null)
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),notesActivity.class));
        }


        loginemail.setAlpha(v);
        loginpassword.setAlpha(v);
        forgetpassword.setAlpha(v);
        loginbutton.setAlpha(v);

        loginemail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        loginpassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        forgetpassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        loginbutton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(50);
                Intent intent=new Intent(getActivity(),forgotpassword.class);
                startActivity(intent);
            }
        });

        //This works when user clicks the Login Button
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibe.vibrate(80);


                String mail=loginemail.getText().toString().trim();
                String password=loginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getContext(), "Please Enter Your Email and Password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    // Check for Internet Connection
                    if (isConnected())
                    {
                        //Toast.makeText(getContext(), "Internet Connected", Toast.LENGTH_SHORT).show();


                        //login using firebase
                        firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {




                                    checkmailverification();

                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Wrong Password / Account doesn't Exist", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }

                    else
                    {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }



                }
            }
        });


        return root;
    }
    //this function is used to check whether the user that is logging in is verified or not...and this is called after line 91 is executed
    private void checkmailverification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true)
        {
            //When the user is verified.....it redirects the user to the content
            Toast.makeText(getContext(), "Logged in", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            startActivity(new Intent(getActivity(),notesActivity.class));
        }
        else
        {
            //When the user is not verified.....it gives a message to verify the email
            Toast.makeText(getContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


    //this function is used to check whether internet is connected or not
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }



}




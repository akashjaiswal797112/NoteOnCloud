package com.akash.noteoncloud;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupTabFragment extends Fragment {
    private EditText signupemail, signuppassword, signupconfirmpassword;
    private TextView signupbutton;
    private FirebaseAuth firebaseAuth;





    //When new user SignUp the firebase send email verification;
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   // Toast.makeText(getContext(), "Verification email is sent, Verify and Login ", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),LoginTabFragment.class));
                    Intent intent = new Intent(getActivity(), LoginTabFragment.class);
                    startActivity(intent);

                }
            });
        }
        else
        {
            Toast.makeText(getContext(), "Failed to send verification Email", Toast.LENGTH_SHORT).show();
        }
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        signupemail = root.findViewById(R.id.signupemail);
        signuppassword = root.findViewById(R.id.signuppassword);
        signupconfirmpassword = root.findViewById(R.id.signupconfirmpassword);
        signupbutton = root.findViewById(R.id.signupbutton);


        firebaseAuth = FirebaseAuth.getInstance();


        //This is used to SignUp the New User
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //for vibration
                Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                 vibe.vibrate(80);


                String mail = signupemail.getText().toString().trim();
                String password = signuppassword.getText().toString().trim();
                String confirmpassword = signupconfirmpassword.getText().toString().trim();


                if(password.equals(confirmpassword))
                {


                    if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
                        Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 6) {
                        Toast.makeText(getContext(), "Too short password", Toast.LENGTH_SHORT).show();
                    } else {


                        if(isConnected())

                        {
                            //Toast.makeText(getContext(), "Internet Connected", Toast.LENGTH_SHORT).show();

                            firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), " Check your email for Verification", Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();

                                    }
                                    else {
                                        Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
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
                else
                {
                    Toast.makeText(getContext(), "Both the password should be same", Toast.LENGTH_SHORT).show();
                }





            }
        });


        return root;

    }


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







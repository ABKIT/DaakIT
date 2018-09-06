package com.daakit.signup;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;


import com.chaos.view.PinView;
import com.daakit.MainActivity;
import com.daakit.R;
import com.daakit.map.registermap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class signup extends AppCompatActivity {
    EditText fullname,email,phone,password;
    Button signup,register;
    TextView show,signin,terms;
    ImageButton ib;
    RelativeLayout form;
    TextView otp2,head;
    PinView otp;
    CheckBox cb;
    String onbackpress="signup";
    private String mverificationid,code;
    FirebaseAuth ath;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    int i=0;
    Boolean checkbox=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getids();


        edlisner(fullname);
        edlisner(email);
        edlisner(password);
        edlisner(phone);
        showall();


        if(ath.getCurrentUser()!=null)
        {

            Intent send=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(send);
            finish();

        }




    }

    public void actions(View view) {
        switch (view.getId())
        {
            case R.id.register :
                if(!checkvalidation(fullname,email,phone,password,false))
                {
                    if(checkbox) {
                        sendVerificationCode(phone.getText().toString());
                        hideall();
                        i = 1;
                    }
                    else {
                        Toast.makeText(this, "Please accept our terms & conditions", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.checkbox :

                if(!checkvalidation(fullname,email,phone,password,false))
                {
                    if(cb.isChecked()) {
                        checkbox = true;
                        btcolorchnage();
                    }
                    else
                    {
                        checkbox=false;
                        btcolor();
                    }
                }
                else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.signup:
                if(!TextUtils.isEmpty(otp.getText().toString()))
                {
                    verifyverificationcode(otp.getText().toString());
                }
                break;
            case R.id.signin :

                break;

        }
    }
    private void edlisner(EditText ed)
    {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!checkvalidation(fullname,email,phone,password,true))
                {
                    cb.setEnabled(true);
                    btcolorchnage();
                }
                else {
                    btcolor();
                    cb.setEnabled(false);
                    cb.setChecked(false);
                    checkbox = false;
                }
            }
        });
    }
    private void btcolor()
    {
        Button bt=(Button)findViewById(R.id.register);
        bt.setBackgroundColor(getResources().getColor(R.color.unselectcl));
        bt.setTextColor(getResources().getColor(R.color.unselecttxcl));

    }

    private void hideall()
    {
        fullname.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        head.setVisibility(View.GONE);
        cb.setVisibility(View.GONE);
        terms.setVisibility(View.GONE);
        ib.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        form.setVisibility(View.GONE);
        otp.setVisibility(View.VISIBLE);
        signup.setEnabled(false);
        show.setVisibility(View.GONE);
        signin.setVisibility(View.GONE);
        otp2.setVisibility(View.VISIBLE);
        register.setEnabled(true);
        register.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);


    }

    private void showall()
    {
        fullname.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        form.setVisibility(View.VISIBLE);
        ib.setVisibility(View.VISIBLE);
        head.setVisibility(View.VISIBLE);
        terms.setVisibility(View.VISIBLE);
        cb.setVisibility(View.VISIBLE);
        signup.setVisibility(View.VISIBLE);
        signup.setEnabled(true);
        otp.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
        signin.setVisibility(View.VISIBLE);
        otp2.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        register.setEnabled(false);
        email.setVisibility(View.VISIBLE);

    }
    private void getids()
    {
        //get ids
        form=(RelativeLayout)findViewById(R.id.form);
        head=(TextView)findViewById(R.id.head);
        otp2=(TextView)findViewById(R.id.otp2);
        cb=(CheckBox)findViewById(R.id.checkbox);
        terms=(TextView)findViewById(R.id.terms);
        ib=(ImageButton)findViewById(R.id.user_profile_photo);
        fullname=(EditText)findViewById(R.id.fullname);
        phone=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        signup=(Button)findViewById(R.id.register);
        register=(Button)findViewById(R.id.signup);
        show=(TextView)findViewById(R.id.show);
        signin=(TextView)findViewById(R.id.signin);
        otp=(PinView) findViewById(R.id.otp);
        ath=FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email);

    }

    private void userregister()
    {
        final String fullname=this.fullname.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        String password=this.password.getText().toString().trim();
        final String phone=this.phone.getText().toString().trim();

        //firebase database
        final DatabaseReference insertuser=FirebaseDatabase.getInstance().getReference("Users").child("customers");

        ath.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String userid=ath.getCurrentUser().getUid();
                    registermap ob=new registermap(fullname,email,phone);
                    insertuser.child(userid).setValue(ob).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(signup.this, "You are successful registered with us", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void btcolorchnage()
    {
        Button regis=(Button)findViewById(R.id.register);

        regis.setBackgroundColor(getResources().getColor(R.color.orangecl));
        regis.setTextColor(getResources().getColor(R.color.white));
    }


    public boolean checkvalidation(EditText fullname,EditText mail, EditText Phone, EditText password,Boolean val)
    {
        String name=fullname.getText().toString();
        String phone=Phone.getText().toString();
        String passcode=password.getText().toString();
        String email=mail.getText().toString();
        Boolean empty=true;
        if(TextUtils.isEmpty(name))
        {
            if(!val) {
                fullname.setError("Name can't be blank");
                fullname.requestFocus();
            }
        }
        else if(TextUtils.isEmpty(email))
        {
            if(!val) {
                mail.setError("Email can't be blank");
                mail.requestFocus();
            }
        }

        else if(TextUtils.isEmpty(phone))
        {
            if(!val) {
                Phone.setError("Phone No can't be blank");
                Phone.requestFocus();
            }
        }

        else if(TextUtils.isEmpty(passcode))
        {
            if(!val) {
                password.setError("Password can't be blank");
                password.requestFocus();
            }
        }


        else
        {
            if(phone.length()>10 || phone.length()<10)
            {
                if(!val) {
                    Phone.setError("Enter a valid number ");
                    Phone.requestFocus();
                }
            }

            else if(passcode.length()<6 || passcode.length()>15)
            {
                if(!val) {
                    password.setError("Password should be between 6-15 characters");
                    password.requestFocus();
                }
                else {
                    empty=false;
                }

            }

            else {
                empty = false;
            }
        }
        return empty;

    }



    @Override
    public void onBackPressed() {
        if(onbackpress.equals("signup") && i==1)
        {
            showall();
            i=0;
        }
        else {
            super.onBackPressed();
        }

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //getting code by sms
            code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                otp.setText(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mverificationid = s;
            resendingToken = forceResendingToken;
        }
    };

    private void verifyverificationcode(String otp)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mverificationid,otp);

        // signing user
        signinwithcredentila(credential);
    }

    private void signinwithcredentila(PhoneAuthCredential credential) {
        ath.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    userregister();
                    Intent start=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(start);
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(getApplicationContext(), "Otp is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}


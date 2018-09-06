package com.daakit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daakit.signup.signup;
import com.daakit.sitting.opendrawer;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth ath;
    private NavigationView navigationView;
    DrawerLayout layout;
    private ImageView toolbarbutton;
    RelativeLayout r1;
    CardView c1,c2,c3,c4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getids();
        cardviewlisner();


        opendrawer ob=new opendrawer();
        navlisner(navigationView);
        ob.opendw(toolbarbutton,layout);

        if(ath.getCurrentUser()==null)
        {

            Intent send=new Intent(getApplicationContext(),signup.class);
            startActivity(send);


        }


    }

    private void navlisner(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                layout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void switchfrag(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragcontainer,fragment).commit();
    }

    private void getids()
    {
        c1=(CardView)findViewById(R.id.create);
        c2=(CardView)findViewById(R.id.order);
        c3=(CardView)findViewById(R.id.track);
        c4=(CardView)findViewById(R.id.payment);
        navigationView = findViewById(R.id.nav_view);
        toolbarbutton=(ImageView)findViewById(R.id.imbttoolbar);
        layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ath=FirebaseAuth.getInstance();
        r1=(RelativeLayout)findViewById(R.id.dash);
    }

    @Override
    public void onClick(View v) {
        if(v==c1)
        {

            Toast.makeText(this, "Create Order", Toast.LENGTH_SHORT).show();
        }
        else if(v==c2)
        {
            Toast.makeText(this, "My Order", Toast.LENGTH_SHORT).show();
        }
        else if(v==c3)
        {
            Toast.makeText(this, "Track Order", Toast.LENGTH_SHORT).show();
        }
        else if(v==c4)
        {
            Toast.makeText(this, "Payment", Toast.LENGTH_SHORT).show();
        }


    }



    private void cardviewlisner()
    {
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);

    }


}

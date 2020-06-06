package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    ViewPager viewPager;
    int[] images = {R.drawable.ic_onboarding_0, R.drawable.ic_onboarding_1};
    int currentPageCounter = 0;
    LinearLayout dotsLayout;
    int customPosition = 0;
    Button mLoginButton;
    TextView mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}

        setContentView(R.layout.activity_intro);

        mLoginButton    =   findViewById(R.id.buttonLogin);
        mRegisterButton =   findViewById(R.id.textSignUp);

        dotsLayout = findViewById(R.id.dotsContainer);
        viewPager = findViewById(R.id.viewPager);

        prepareDots(customPosition++);
        viewPager.setAdapter(new SlideAdapter(images, IntroActivity.this));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(customPosition>2)
                    customPosition = 0;
                prepareDots(customPosition++);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPageCounter == images.length){
                    currentPageCounter = 0;
                }

                viewPager.setCurrentItem(currentPageCounter++,true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000,3000);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }

    private void prepareDots(int currentSlidePosition){
        if(dotsLayout.getChildCount()>0)
            dotsLayout.removeAllViews();

        ImageView dots[] = new ImageView[2];

        for(int i=0; i<2; i++){
            dots[i] = new ImageView(this);
            if(i==currentSlidePosition)
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dot_active));
            else
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dot_inactive));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);
            dotsLayout.addView(dots[i],layoutParams);
        }
    }
}

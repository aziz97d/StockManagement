package com.example.abdulaziz.stockmanagement;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    private TextView welcomeText,welcomeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        welcomeImage = findViewById(R.id.welcome_image);
        welcomeText = findViewById(R.id.welcome_text);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        welcomeText.startAnimation(animation);
        welcomeImage.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();;
            }
        },2500);
    }
}

package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    ImageView inner, outer, container, triangle;
    TextView cine, fast;
    LinearLayout name;
    RelativeLayout logo;
    Animation outer_anim, inner_anim, container_anim, triangle_anim, name_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        applyAnimations();

        new Handler().postDelayed(()->{
            logo.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            name.setVisibility(View.VISIBLE);
            name.setAnimation(name_anim);
            new Handler().postDelayed(()->{
                startActivity(new Intent(Splash.this, Onboarding.class));
                finish();
            }, 2000);
        }, 2500);


    }

    private void init(){
        inner = findViewById(R.id.ivInner);
        outer = findViewById(R.id.ivOuter);
        container = findViewById(R.id.ivContainer);
        triangle = findViewById(R.id.ivTriangle);
        cine = findViewById(R.id.tvCine);
        fast = findViewById(R.id.tvFast);
        name = findViewById(R.id.llName);
        logo = findViewById(R.id.rlLogo);

        outer_anim = AnimationUtils.loadAnimation(this, R.anim.outer_anim);
        inner_anim = AnimationUtils.loadAnimation(this, R.anim.inner_anim);
        container_anim = AnimationUtils.loadAnimation(this, R.anim.container_anim);
        triangle_anim = AnimationUtils.loadAnimation(this, R.anim.triangle_anim);
        name_anim = AnimationUtils.loadAnimation(this, R.anim.name_anim);
    }

    private void applyAnimations(){
        outer.setAnimation(outer_anim);
        container.setAnimation(container_anim);
        inner.setAnimation(inner_anim);
        triangle.setAnimation(triangle_anim);
    }
}
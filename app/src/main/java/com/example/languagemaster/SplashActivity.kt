package com.example.languagemaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 5000

    // Hooks
    private lateinit var a: TextView
    private lateinit var slogan: TextView

    // Animations
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var middleAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        // Hooks

        a = findViewById(R.id.a)
        slogan = findViewById(R.id.tagLine)

        // Animation Calls
        topAnimation = AnimationUtils.loadAnimation(this, R.animator.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.animator.bottom_animation)
        middleAnimation = AnimationUtils.loadAnimation(this, R.animator.middle_animation)

        // Setting Animations to the elements of Splash Screen

        a.startAnimation(middleAnimation)
        slogan.startAnimation(bottomAnimation)

        // Splash Screen Code to call new Activity after some time
        Handler().postDelayed({
            // Calling new Activity
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}

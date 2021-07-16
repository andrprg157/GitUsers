package com.git.gitusers.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.git.gitusers.R

class SplashActivity : AppCompatActivity() {


    private val SPLASH_TIME_OUT = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //THIS UPDATE IS FROM STUDIO TO GIT ON 160721

        Handler().postDelayed(
            {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)
    }
}

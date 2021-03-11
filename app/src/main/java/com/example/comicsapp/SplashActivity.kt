package com.example.comicsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch { loadMainScreen() }
    }

    private  fun loadMainScreen() {
        val splashTimer = System.currentTimeMillis()


        val sleepTime = 2000 - (System.currentTimeMillis() - splashTimer)
        if (sleepTime > 0){
            Thread.sleep(sleepTime)
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
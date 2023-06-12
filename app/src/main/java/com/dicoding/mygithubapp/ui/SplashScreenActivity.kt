package com.dicoding.mygithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar!!.hide()

        val splashScreen: LinearLayout = findViewById(R.id.splash_layout)
        splashScreen.alpha = 0f
        splashScreen.animate().setDuration(1500).alpha(1f).withEndAction {
            val openMainPage = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(openMainPage)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
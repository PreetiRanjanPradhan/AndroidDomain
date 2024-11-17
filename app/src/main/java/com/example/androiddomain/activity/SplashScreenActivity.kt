package com.example.androiddomain.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lumipixdatingapp.MainActivity
import com.example.lumipixdatingapp.R
import com.example.lumipixdatingapp.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser


        Handler(Looper.getMainLooper()).postDelayed({
            if (user == null)
                startActivity(Intent(this,LoginActivity::class.java))
            else
                startActivity(Intent(this,MainActivity::class.java))
            finish()
        },3000)
    }
}
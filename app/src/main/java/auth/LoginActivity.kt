package com.example.lumipixdatingapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lumipixdatingapp.MainActivity
import com.example.lumipixdatingapp.R
import com.example.lumipixdatingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit
//commit
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private var verificationId: String? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize loading dialog
        initializeLoadingDialog()

        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeLoadingDialog() {
        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()
    }

    private fun setupClickListeners() {
        // Send OTP button click listener
        binding.sendOtpBtn.setOnClickListener {
            binding.userNumber.text?.toString()?.let { number ->
                if (number.isEmpty()) {
                    binding.userNumber.error = "Please enter your number"
                } else {
                    sendOtp(number)
                }
            }
        }

        // Verify OTP button click listener
        binding.verifyOtpBtn.setOnClickListener {
            binding.userOtp.text?.toString()?.let { otp ->
                if (otp.isEmpty()) {
                    binding.userOtp.error = "Please enter your OTP"
                } else {
                    verifyOtp(otp)
                }
            }
        }
    }


    private fun verifyOtp(otp: String) {
        verificationId?.let { vId ->
            showLoading()
            val credential = PhoneAuthProvider.getCredential(vId, otp)
            signInWithPhoneAuthCredential(credential)
        } ?: run {
            Toast.makeText(this, "Verification ID is null. Please try again", Toast.LENGTH_SHORT).show()
            hideLoading()
        }
    }

    private fun sendOtp(number: String) {
        showLoading()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                hideLoading()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                hideLoading()
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId = verificationId
                hideLoading()
                // Switch to OTP input layout
                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
            }
        }

        // Configure phone auth options
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.userNumber.text?.toString()?.let { number ->
                        checkUserExist(number)
                    }
                } else {
                    hideLoading()
                    task.exception?.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child("+91$number")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    hideLoading()
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoading()
                    val intent = if (snapshot.exists()) {
                        Intent(this@LoginActivity, MainActivity::class.java)
                    } else {
                        Intent(this@LoginActivity, RegisterActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            })
    }

    private fun showLoading() {
        dialog?.show()
    }

    private fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onDestroy() {
        dialog?.dismiss()
        dialog = null
        super.onDestroy()
    }
}
package com.example.loginpage

import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginpage.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var verificationId: String

    companion object {
        const val KEY = "com.example.loginpage.MainActivity.KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSendOtp.setOnClickListener {
            val phoneNumber = binding.btnPhoneNumber.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
            } else {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber) // Taking Phone number to verify
                    .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout for Authentication
                    .setActivity(this) // Activity (for callback binding/Passing Context)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                            // Accessing of OTP will be done. Additional Features
                            // can also be implemented
                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            Toast.makeText(
                                this@MainActivity,
                                "Verification Failed: ${p0.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(verificationId, token)
                            this@MainActivity.verificationId = verificationId
                        }
                    }) // OnVerificationStateChangedCallbacks
                    .build()

                // OTP will be sent
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.btnInputOTP.text.toString()
            val phoneNumber = binding.btnPhoneNumber.text.toString()
            if (otp.isEmpty()) {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                // Verify OTP
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // OTP verification successful,
                            // verification will be done and Contact details will be passed
                            // from one activity to another activity
                            val intent = Intent(this, SignIn_Page::class.java)
                            intent.putExtra(KEY, phoneNumber)
                            startActivity(intent)
                        } else {
                            // if OTP verification failed, this will be executed
                            // Reason behind Failed Authentication will be displayed
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}


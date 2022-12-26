package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.votingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnLoginLogin.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val userEmail = binding.editLoginEmail.text.toString()
        val userPassword = binding.editLoginPassword.text.toString()

        if (userEmail.isEmpty()) {
            binding.editLoginEmail.apply {
                error = "Email is Required"
                requestFocus()
            }
            return
        }

        if (TextUtils.isEmpty(userPassword)) {
            binding.editLoginPassword.apply {
                error = "Password Required"
                requestFocus()
            }
            return
        }else if (userPassword.length < 6) {
            binding.editLoginPassword.apply {
                error = "Password must be 6 letters or above"
                requestFocus()
            }
            return
        }
        binding.pbLogin.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                binding.pbLogin.visibility = View.GONE
                updateUI()
            } else {
                binding.pbLogin.visibility = View.GONE
                Log.d("Error Message", "Something went Wrong")
            }

        }
    }

    private fun updateUI() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }
}
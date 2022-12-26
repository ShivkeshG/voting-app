package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.votingapp.dao.UserDao
import com.example.votingapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegisterRegister.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        val userName = binding.editRegisterUserName.text.toString().trim()
        val userEmail = binding.editRegisterEmail.text.toString().trim()
        val userPhone = binding.editRegisterPhone.text.toString().trim()
        val userPassword = binding.editRegisterPassword.text.toString().trim()

        if (TextUtils.isEmpty(userName)) {
            binding.editRegisterUserName.apply {
                error = "Name is Required"
                requestFocus()
            }
            return
        }

        if (TextUtils.isEmpty(userEmail)) {
            binding.editRegisterEmail.apply {
               error = "Email is Required"
                requestFocus()
            }
            return
        }

        if (TextUtils.isEmpty(userPassword)) {
            binding.editRegisterPassword.apply {
                error = "Password Required"
                requestFocus()
            }
            return
        } else if (userPassword.length < 6) {
            binding.editRegisterPassword.apply {
                error = "Password must be 6 letters or above"
                requestFocus()
            }
            return
        }
        createUser(userName, userEmail, userPhone, userPassword)

    }

    private fun createUser(
        userName: String,
        userEmail: String,
        userPhone: String,
        userPassword: String
    ) {
        binding.pbRegister.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    sendData(userName, userEmail, userPhone)
                    binding.pbRegister.visibility = View.GONE
//                    val user = auth.currentUser
//                    if (user != null) {
////                        sendData(userName, userEmail, user.uid, userPhone)
//                    }
                    Toast.makeText(
                        baseContext, "Successfully created Account",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    binding.pbRegister.visibility = View.GONE
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendData(userName: String, userEmail: String, userPhone: String) {
        val uid = auth.currentUser?.uid
        val userData = User(userName, userEmail, uid, userPhone)
        val userDao = UserDao()
        userDao.addUser(userData)

//        database = FirebaseDatabase.getInstance().getReference("Users")
//        val user = User(userName, userEmail, uid, userPhone)
//        if (uid != null) {
//            database.child(uid).setValue(user)
//        }

    }

}
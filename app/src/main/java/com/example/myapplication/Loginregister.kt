package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Loginregister : AppCompatActivity() {
    private lateinit var mEmailField: EditText
    private lateinit var mPasswordField: EditText
    private lateinit var mActionButton: Button
    private lateinit var mSwitchText: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginIntent: Intent
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginregister)
        mAuth = FirebaseAuth.getInstance()
        loginIntent = Intent(this, MainActivity::class.java)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        mEmailField = findViewById(R.id.edit_text_email)
        mPasswordField = findViewById(R.id.edit_text_password)
        mActionButton = findViewById(R.id.registerbutton)
        mSwitchText = findViewById(R.id.text_switch)

        mSwitchText.setOnClickListener {
            onSwitchClick()
        }
        mActionButton.setOnClickListener {
            onActionClick()
        }
        retrieveCredentials()
    }

    fun onActionClick() {
        val email = mEmailField.text.toString()
        val password = mPasswordField.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (isLoginMode) {
            loginUser(email, password)
        } else {
            registerUser(email, password)
        }
    }

    fun onSwitchClick() {
        isLoginMode = !isLoginMode
        updateUI()
    }

    private fun updateUI() {
        if (isLoginMode) {
            mActionButton.text = "Login"
            mSwitchText.text = "Don't have an account? Register Now"
        } else {
            mActionButton.text = "Register Now"
            mSwitchText.text = "Already have an account? Login"
        }
    }

    private fun registerUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    // Save email and password to SharedPreferences
                    saveCredentials(email, password)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    saveCredentials(email, password)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun retrieveCredentials(){
        val emailr = sharedPreferences.getString("email", null)
        val passwordr = sharedPreferences.getString("password", null)
         if (emailr != null && passwordr != null) {
             loginUser(emailr, passwordr)
         }
    }

    companion object {
        private var isLoginMode: Boolean = false
    }
}
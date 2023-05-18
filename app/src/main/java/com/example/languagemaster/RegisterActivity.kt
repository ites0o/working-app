package com.example.languagemaster

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstName = findViewById(R.id.et_first_name)
        lastName = findViewById(R.id.et_second_name)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        confirmPassword = findViewById(R.id.et_repassword)
        register = findViewById(R.id.btn_register)
        auth = FirebaseAuth.getInstance()

        register.setOnClickListener {
            checkDataEntered()
        }
    }

    private fun isEmail(text: EditText): Boolean {
        val email = text.text.toString()
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    private fun isEmpty(text: EditText): Boolean {
        val str = text.text.toString()
        return TextUtils.isEmpty(str)
    }

    private fun isPasswordValid(text: EditText): Boolean {
        val password = text.text.toString()
        return password.length >= 6
    }

    private fun doPasswordsMatch(password: EditText, confirmPassword: EditText): Boolean {
        return password.text.toString() == confirmPassword.text.toString()
    }

    private fun checkDataEntered() {
        if (isEmpty(firstName)) {
            firstName.error = "First name is required!"
            return
        }

        if (isEmpty(lastName)) {
            lastName.error = "Last name is required!"
            return
        }

        if (!isEmail(email)) {
            email.error = "Enter valid email!"
            return
        }

        if (!isPasswordValid(password)) {
            password.error = "Password must be at least 6 characters long!"
            return
        }

        if (!doPasswordsMatch(password, confirmPassword)) {
            confirmPassword.error = "Passwords do not match!"
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration failed!", Toast.LENGTH_SHORT).show()
                    task.exception?.let { exception ->
                        Log.e(TAG, "createUserWithEmailAndPassword failed", exception)
                    }
                }
            }

    }
}

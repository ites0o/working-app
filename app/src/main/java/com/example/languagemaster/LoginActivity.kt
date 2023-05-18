package com.example.languagemaster

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_register)
        firebaseAuth = FirebaseAuth.getInstance()


        loginButton.setOnClickListener {
            checkEmailAndPassword()
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkEmailAndPassword() {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (emailText.isEmpty()) {
            email.error = "You must enter an email to login!"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Enter valid email!"
            return
        }

        if (passwordText.isEmpty()) {
            password.error = "You must enter a password to login!"
            return
        }

        if (passwordText.length < 4) {
            password.error = "Password must be at least 4 characters long!"
            return
        }

        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT)
                    t.show()
                }
            }
    }

}

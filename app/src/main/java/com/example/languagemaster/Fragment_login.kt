package com.example.languagemaster

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Fragment_login : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Find views by id
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        loginButton = view.findViewById(R.id.btn_login)

        // Set click listener for login button
        loginButton.setOnClickListener {
            // Get email and password from EditText fields
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate email and password
            if (isValidEmail(email) && isValidPassword(password)) {
                // Perform login
                performLogin(email, password)
            } else {
                // Show error message
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun isValidEmail(email: String): Boolean {
        // Validate email here (e.g. check if it's a valid email address)
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Validate password here (e.g. check if it's at least 6 characters long)
        return password.length >= 6
    }

    private fun performLogin(email: String, password: String) {
        // Perform login logic here (e.g. send login request to server)
        // Once login is successful, you can navigate to another screen or activity
    }
}

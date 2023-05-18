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


class Fragment_register : Fragment() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var retypePasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Find views by id
        fullNameEditText = view.findViewById(R.id.et_name)
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        retypePasswordEditText = view.findViewById(R.id.et_repassword)
        registerButton = view.findViewById(R.id.btn_register)

        // Set click listener for register button
        registerButton.setOnClickListener {
            // Get input values from EditText fields
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val retypePassword = retypePasswordEditText.text.toString()

            // Validate input values
            if (isValidFullName(fullName) && isValidEmail(email) && isValidPassword(password) && isPasswordMatch(password, retypePassword)) {
                // Perform registration
                performRegistration(fullName, email, password)
            } else {
                // Show error message
                Toast.makeText(context, "Invalid input values", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun isValidFullName(fullName: String): Boolean {
        // Validate full name here (e.g. check if it's not empty)
        return fullName.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        // Validate email here (e.g. check if it's a valid email address)
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Validate password here (e.g. check if it's at least 6 characters long)
        return password.length >= 6
    }

    private fun isPasswordMatch(password: String, retypePassword: String): Boolean {
        // Check if password and retype password match
        return password == retypePassword
    }

    private fun performRegistration(fullName: String, email: String, password: String) {
        // Perform registration logic here (e.g. send registration request to server)
        // Once registration is successful, you can navigate to another screen or activity
    }

}

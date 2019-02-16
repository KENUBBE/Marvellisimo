package com.marvellisimo.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.MainActivity
import com.marvellisimo.R
import com.marvellisimo.dto.user.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity(), View.OnClickListener {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth


    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        verifyEmailButton.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        isUserLoggedIn(currentUser)
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    sendEmailVerification()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                hideProgressDialog()
            }
    }

    private fun addUserToDb(currentUser: FirebaseUser?) {
        db.collection("users").document(currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    println("User is already in db")
                } else if (!result.exists()) {
                    val user = User(currentUser?.uid.toString(), currentUser?.email.toString(), "offline")
                    db.collection("users").document(currentUser?.uid.toString())
                        .set(user)
                }
            }
    }

    private fun setUserStatus(currentUser: FirebaseUser?) {
        val userRef = db.collection("users").document(currentUser?.uid.toString())

        userRef
            .update("status", "online")
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    isUserLoggedIn(user)
                    setUserStatus(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                hideProgressDialog()
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }
        return valid
    }

    private fun isUserLoggedIn(user: FirebaseUser?) {
        if (user != null && user.isEmailVerified) {
            addUserToDb(user)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user == null){
            Toast.makeText(baseContext,
                "Wrong password or email ",
                Toast.LENGTH_SHORT).show()
        } else if (!user.isEmailVerified) {
            Toast.makeText(baseContext,
                "Please verify your email first! ",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            // R.id.signOutButton -> signOut()
        }
    }


    private fun showProgressDialog() {
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.isIndeterminate = true
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }


}

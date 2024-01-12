package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.hilguener.marvelsuperheroes.databinding.ActivityGithubLoginBinding
import com.hilguener.marvelsuperheroes.ui.LoadingButton

class GithubLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnLogin: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGithubLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        btnLogin = binding.loginBtnEnterGithub

        setupLoginButton()
    }

    private fun setupLoginButton() {
        // Desabilita o botão de login inicialmente
        btnLogin.isEnabled = false

        binding.loginEditEmailGithub.addTextChangedListener(createTextWatcher())

        btnLogin.setOnClickListener {
            val email = getEmail()
            val provider = OAuthProvider.newBuilder("github.com")
            provider.addCustomParameter("login", email)
            provider.scopes = listOf("user:email")

            val pendingResultTask = mAuth.pendingAuthResult
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {
                        // Handle failure.
                    }
            } else {
                mAuth
                    .startActivityForSignInWithProvider(this@GithubLoginActivity, provider.build())
                    .addOnSuccessListener {
                        openNextActivity()
                    }
                    .addOnFailureListener {
                        // Handle failure.
                    }
            }
        }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Não é necessário implementar antes da mudança de texto
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Não é necessário implementar durante a mudança de texto
            }

            override fun afterTextChanged(editable: Editable?) {
                // Habilita ou desabilita o botão com base no preenchimento do campo de e-mail
                btnLogin.isEnabled = isValidEmail(editable.toString())
            }
        }
    }

    private fun openNextActivity() {
        val intent = Intent(this@GithubLoginActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun getEmail(): String {
        return binding.loginEditEmailGithub.text.toString()
    }

    private fun isValidEmail(email: String): Boolean {
        // Implemente sua lógica de validação de e-mail aqui
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

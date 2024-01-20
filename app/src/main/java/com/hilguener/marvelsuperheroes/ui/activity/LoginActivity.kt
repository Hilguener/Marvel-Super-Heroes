package com.hilguener.marvelsuperheroes.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.databinding.ActivityLoginBinding
import com.hilguener.marvelsuperheroes.datasource.callback.LoginContract
import com.hilguener.marvelsuperheroes.presenter.LoginPresenter


class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter()
        presenter.attachView(this)

        binding.loginEditEmail.addTextChangedListener(presenter.getTextWatcher())
        binding.loginEditPassword.addTextChangedListener(presenter.getTextWatcher())

        val rememberMeCheckbox = binding.rememberMeCheckBox
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        // Verifique o estado do checkbox salvo no SharedPreferences
        rememberMeCheckbox.isChecked =
            sharedPreferences.getBoolean(getString(R.string.pref_key_remember_me), false)

        val loginButton = binding.loginBtnEnter
        loginButton.setOnClickListener {
            loginButton.showProgress(true)
            Handler(Looper.getMainLooper()).postDelayed({
                loginButton.showProgress(false)
                val email = getEmail()
                val password = getPassword()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    presenter.onLoginButtonClicked(email, password)
                }
            }, 2000)
        }


        binding.loginBtnGithub.setOnClickListener {
            githubLogin()
        }
        binding.loginBtnGoogle.setOnClickListener {
            presenter.onGoogleSignInButtonClicked()
        }

        binding.loginTxtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean(getString(R.string.pref_key_remember_me), isChecked)
            editor.apply()
        }
    }

    override fun isRememberMeChecked(): Boolean {
        return binding.rememberMeCheckBox.isChecked
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun startGoogleSignInActivityForResult(intent: Intent) {
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun getContext(): Context {
        return applicationContext
    }

    override fun getEmail(): String {
        return binding.loginEditEmail.text.toString()
    }

    override fun getPassword(): String {
        return binding.loginEditPassword.text.toString()
    }

    override fun enableLoginButton(enabled: Boolean) {
        binding.loginBtnEnter.isEnabled = enabled
    }

    override fun showLoginFailed(message: String) {
        // Exibir mensagem de falha de login para o usuário
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoginSuccess() {

        var displayName = FirebaseAuth.getInstance().currentUser?.displayName

        // Se o usuário não tiver fornecido um nome de exibição, use o endereço de e-mail
        displayName ?: run {
            displayName = displayName ?: FirebaseAuth.getInstance().currentUser?.email
        }

        // Redirecionar para a próxima tela ou executar ações após o login bem-sucedido
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun startGithubSignInActivityForResult(intent: Intent) {
        startActivityForResult(intent, GIT_SIGN_IN_REQUEST_CODE)
    }


    override fun getActivity(): Activity {
        return this
    }

    private fun githubLogin() {
        val auth = FirebaseAuth.getInstance()
        val githubProvider = OAuthProvider.newBuilder("github.com")

        auth.startActivityForSignInWithProvider(this, githubProvider.build())
            .addOnSuccessListener {

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            presenter.handleGoogleSignInResult(data)
        } else {
        }
    }


    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 123 // Use o número desejado
        private const val GIT_SIGN_IN_REQUEST_CODE = 456
    }
}



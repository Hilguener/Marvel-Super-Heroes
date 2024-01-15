package com.hilguener.marvelsuperheroes.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hilguener.marvelsuperheroes.databinding.ActivityRegisterBinding
import com.hilguener.marvelsuperheroes.datasource.callback.RegisterContract
import com.hilguener.marvelsuperheroes.presenter.RegisterPresenter

class RegisterActivity : AppCompatActivity(), RegisterContract {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegisterPresenter(this)

        val enterButton = binding.registerBtnCreateAccount

        binding.registerEditEmail.addTextChangedListener(presenter.textWatcher)
        binding.registerEditPassword.addTextChangedListener(presenter.textWatcher)
        binding.registerEditConfirmPassword.addTextChangedListener(presenter.textWatcher)

        enterButton.setOnClickListener {
            enterButton.showProgress(true)
            Handler(Looper.getMainLooper()).postDelayed({
                enterButton.showProgress(false)
                presenter.onRegisterButtonClicked(
                    getEmail(),
                    getPassword(),
                    getConfirmPassword()
                )
            }, 2000)
        }

        binding.registerTxtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    override fun showEmailError(message: String?) {}

    override fun showPasswordError(message: String) {
        binding.registerEditPassword.error = message
    }

    override fun showConfirmPasswordError(message: String?) {
        if (message != null) {
            binding.registerEditConfirmPassword.error = message
        } else {
            binding.registerEditConfirmPassword.error = null // Clear previous error
        }
    }


    override fun enableRegisterButton(enabled: Boolean) {
        binding.registerBtnCreateAccount.isEnabled = enabled
    }

    override fun getContext(): Context {
        return applicationContext
    }


    override fun showRegisterSuccess() {
        Toast.makeText(this, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showRegisterFailed(message: String) {
        binding.registerEditEmail.error = message
    }

    override fun clearError() {

    }

    override fun showError(message: String) {
        binding.registerEditPassword.error = message
    }

    override fun getEmail(): String = binding.registerEditEmail.text.toString()

    override fun getPassword(): String = binding.registerEditPassword.text.toString()

    override fun getConfirmPassword(): String = binding.registerEditConfirmPassword.text.toString()
}


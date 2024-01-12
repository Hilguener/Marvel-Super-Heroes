package com.hilguener.marvelsuperheroes.presenter

import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.hilguener.marvelsuperheroes.datasource.callback.RegisterContract

class RegisterPresenter(private val view: RegisterContract) {
    private val auth = FirebaseAuth.getInstance()

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().signOut()
                    view.showRegisterSuccess()
                } else {
                    view.showRegisterFailed(task.exception?.message ?: "Falha no registro")
                }
            }
    }

    fun onRegisterButtonClicked(email: String, password: String, confirmPassword: String) {
        if (validateData(email, password, confirmPassword)) {
            checkEmailExists(email, password)
        }
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        val emailValid =
            email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val passwordValid = isPasswordValid(password)
        val passwordsMatch = password == confirmPassword

        view.enableRegisterButton(emailValid && passwordValid && passwordsMatch)

        return emailValid && passwordValid && passwordsMatch
    }

    private fun isPasswordValid(password: String): Boolean {
        val minLength = 10

        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSymbol = password.any { it.isLetterOrDigit().not() }

        val passwordValid = password.length >= minLength && hasLetter && hasDigit && hasSymbol

        return passwordValid
    }



    private fun checkEmailExists(email: String, password: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        registerUser(email, password)
                    }
                } else {
                    view.showRegisterFailed(task.exception?.message ?: "Erro ao verificar o e-mail")
                }
            }
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val email = view.getEmail()
            val password = view.getPassword()
            val confirmPassword = view.getConfirmPassword()

            val isDataValid = validateData(email, password, confirmPassword)
            view.enableRegisterButton(isDataValid)

            if (!isDataValid) {
                view.showConfirmPasswordError("As senhas não conferem")
            } else {
                view.showConfirmPasswordError(null) // Limpar a mensagem de erro se as senhas estiverem corretas
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

}





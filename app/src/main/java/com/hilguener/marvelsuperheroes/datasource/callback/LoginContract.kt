package com.hilguener.marvelsuperheroes.datasource.callback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextWatcher

interface LoginContract {
    interface View {
        // Métodos da View
        fun startGoogleSignInActivityForResult(intent: Intent)
        fun getContext(): Context
        fun getEmail(): String
        fun getPassword(): String
        fun enableLoginButton(enabled: Boolean)

        fun showLoginFailed(message: String)
        fun showLoginSuccess()

        fun startGithubSignInActivityForResult(intent: Intent)
        fun isRememberMeChecked(): Boolean


        fun getActivity(): Activity // Método para obter a Activity atual


        // Outros métodos necessários para interação com a View
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun onGoogleSignInButtonClicked()
        fun onLoginButtonClicked(email: String, password: String)
        fun getTextWatcher(): TextWatcher

        fun handleGoogleSignInResult(data: Intent?)


        // Adicione este método
        // Outros métodos do Presenter necessários
    }
}


package com.hilguener.marvelsuperheroes.presenter

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hilguener.marvelsuperheroes.R
import com.hilguener.marvelsuperheroes.datasource.callback.LoginContract
import java.util.*

class LoginPresenter : LoginContract.Presenter {
    private var view: LoginContract.View? = null

    override fun attachView(view: LoginContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        // Limpar recursos, se necessário
    }


    override fun onGoogleSignInButtonClicked() {
        val context = view?.getContext()
        val clientId = context?.getString(R.string.default_web_client_id)

        if (clientId != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            val signInIntent = googleSignInClient.signInIntent
            view?.startGoogleSignInActivityForResult(signInIntent)
        } else {
            view?.showLoginFailed("Erro: ID do cliente não está disponível.")
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    view?.showLoginSuccess()
                } else {
                    // Login falhou
                    view?.showLoginFailed("Erro ao fazer login com o Firebase: ${task.exception?.message}")
                }
            }
    }


    override fun handleGoogleSignInResult(data: Intent?) {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            view?.showLoginFailed("Erro ao fazer login com o Google: ${e.message}")
        }
    }


    override fun onLoginButtonClicked(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido

                    if (view?.isRememberMeChecked() == true) {
                        // Salvar o estado de login para manter o usuário conectado
                        val sharedPreferences = view?.getContext()?.getSharedPreferences(
                            view?.getContext()?.getString(R.string.pref_key_remember_me),
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean(view?.getContext()?.getString(R.string.pref_key_remember_me), true)
                        editor?.apply()
                    }

                    view?.showLoginSuccess()
                } else {
                    // Login falhou
                    view?.showLoginFailed(task.exception?.message ?: "Erro ao fazer login")
                }
            }
    }


    override fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = view?.getEmail() ?: ""
                val password = view?.getPassword() ?: ""
                val isDataValid = validateData(email, password)
                view?.enableLoginButton(isDataValid)
            }

            override fun afterTextChanged(s: Editable?) {}

            private fun validateData(email: String, password: String): Boolean {
                // Sua lógica de validação aqui
                val emailValid =
                    email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()
                val passwordValid = password.length >= 6
                return emailValid && passwordValid
            }
        }
    }




}



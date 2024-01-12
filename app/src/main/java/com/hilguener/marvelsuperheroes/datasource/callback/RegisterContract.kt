package com.hilguener.marvelsuperheroes.datasource.callback

import android.content.Context

interface RegisterContract {
    fun showEmailError(message: String?)
    fun showPasswordError(message: String)
    fun showConfirmPasswordError(message: String?)
    fun enableRegisterButton(enabled: Boolean)
    fun getContext(): Context
    fun showRegisterSuccess()
    fun showRegisterFailed(message: String)

    fun clearError()
    fun showError(message: String)
    fun getEmail(): String
    fun getPassword(): String
    fun getConfirmPassword(): String
}

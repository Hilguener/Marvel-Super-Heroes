package com.hilguener.marvelsuperheroes.model

interface UserModel {
    interface RegistrationCallback {
        fun onRegistrationSuccess()
        fun onRegistrationFailure(errorMessage: String)
        fun onEmailCheckResult(emailExists: Boolean)
    }

    fun checkIfEmailExists(email: String, callback: RegistrationCallback)
    fun registerUser(email: String, password: String, callback: RegistrationCallback)
}

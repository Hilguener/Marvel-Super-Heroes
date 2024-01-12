package com.hilguener.marvelsuperheroes.presenter

import com.google.firebase.auth.FirebaseAuth
import com.hilguener.marvelsuperheroes.model.UserModel

class UserModelImpl : UserModel {
    private val auth = FirebaseAuth.getInstance()

    override fun checkIfEmailExists(email: String, callback: UserModel.RegistrationCallback) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    callback.onEmailCheckResult(signInMethods != null && signInMethods.isNotEmpty())
                } else {
                    callback.onRegistrationFailure("Erro ao verificar o e-mail: ${task.exception?.message}")
                }
            }
    }

    override fun registerUser(email: String, password: String, callback: UserModel.RegistrationCallback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback.onRegistrationSuccess()
                } else {
                    callback.onRegistrationFailure("Erro ao criar usuário: ${task.exception?.message}")
                }
            }
    }
}

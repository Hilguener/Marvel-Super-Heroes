package com.hilguener.marvelsuperheroes.datasource.callback

interface UserRepository {
    fun checkEmailExists(email: String, callback: (Boolean) -> Unit)
    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit)
}
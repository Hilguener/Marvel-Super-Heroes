package com.hilguener.marvelsuperheroes.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hilguener.marvelsuperheroes.R

class GoogleAccountSelectionActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001 // ou qualquer código de sua escolha

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicialize o cliente do Google Sign In aqui usando GoogleSignInOptions

        // Execute o login com o Google
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Login bem-sucedido com o Google
                val account = task.getResult(ApiException::class.java)!!
                // Enviar a conta de volta para a LoginActivity
                val resultIntent = Intent()
                resultIntent.putExtra("selectedAccount", account)
                setResult(Activity.RESULT_OK, resultIntent)
            } catch (e: ApiException) {
                // Trate a falha no login do Google
                setResult(Activity.RESULT_CANCELED)
            } finally {
                finish()
            }
        }
    }
}

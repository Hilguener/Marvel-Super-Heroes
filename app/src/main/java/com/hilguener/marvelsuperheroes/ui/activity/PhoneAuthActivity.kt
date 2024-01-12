package com.hilguener.marvelsuperheroes.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hilguener.marvelsuperheroes.R

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        // Obter o código de verificação enviado pela LoginActivity
        verificationId = intent.getStringExtra("verificationId") ?: ""

        val verifyButton = findViewById<Button>(R.id.buttonVerify)
        val codeEditText = findViewById<EditText>(R.id.editTextVerificationCode)

        verifyButton.setOnClickListener {
            val code = codeEditText.text.toString().trim()
            if (code.isNotEmpty()) {
                verifyPhoneNumberWithCode(code)
            } else {
                // Exibir uma mensagem de erro se o campo estiver vazio
                Toast.makeText(this, "Por favor, insira o código de verificação", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        // Enviar o código de verificação para a LoginActivity usando Intent
        val resultIntent = Intent()
        resultIntent.putExtra("verificationId", verificationId)
        resultIntent.putExtra("code", code)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

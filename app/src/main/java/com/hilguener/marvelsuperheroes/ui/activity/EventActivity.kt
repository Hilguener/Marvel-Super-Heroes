package com.hilguener.marvelsuperheroes.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hilguener.marvelsuperheroes.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}







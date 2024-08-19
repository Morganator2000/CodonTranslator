package com.hedgehax.codontranslator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val rnaToTextButton : Button = findViewById(R.id.rna_to_text_button)
        val textToRnaButton : Button = findViewById(R.id.text_to_rna_button)

        rnaToTextButton.setOnClickListener {
            val intent = Intent(this,RnaToText::class.java)
            startActivity(intent)
        }

        textToRnaButton.setOnClickListener {
            val intent = Intent(this,TextToRna::class.java)
            startActivity(intent)
        }
    }
}

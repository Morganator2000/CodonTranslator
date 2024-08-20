package com.hedgehax.codontranslator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SwitchCompat

class TextToRna : ComponentActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_text_to_rna)

        val textInput: EditText = findViewById(R.id.textInput)
        val translateButton: Button = findViewById(R.id.translateButton)
        val rnaOutput: TextView = findViewById(R.id.rnaOutput)
        val spaceToggle: Switch = findViewById(R.id.splitSwitch)

        fun formatRnaOutput(rna: String): String {
            return if (spaceToggle.isChecked) {
                rna.chunked(3).joinToString(" ")
            } else {
                rna.replace(" ", "")
            }
        }

        fun updateRnaOutput() {
            val text = textInput.text.toString()
            if (validateText(text)) {
                val acidString = textToCodon(text)
                rnaOutput.text = formatRnaOutput(acidString)
            }
        }

        spaceToggle.setOnCheckedChangeListener { _, _ ->
            updateRnaOutput()
        }

        translateButton.setOnClickListener {
            val text = textInput.text.toString()
            if (text.isBlank()) {
                showErrorDialog("You must type something in.")
                return@setOnClickListener
            }
            try {
                updateRnaOutput()
            } catch (e: InvalidSequenceException) {
                showErrorDialog(e.message)
            } catch (e: InvalidCharacterException) {
                showErrorDialog(e.message)
            } catch (e: Exception) {
                showErrorDialog("An unknown error occurred.")
            }
        }

    }

    class InvalidSequenceException(message: String) : Exception(message)
    class InvalidCharacterException(message: String) : Exception(message)

    private fun validateText(text: String): Boolean {
        val processedText = text.uppercase().trim().replace("[^A-Z]".toRegex(), "")
        val invalidCharacters = setOf('B', 'J', 'O', 'U', 'X', 'Z')

        for (char in processedText) {
            if (char in invalidCharacters) {
                throw InvalidCharacterException("Invalid character '$char'. There is no amino acid for B, J, O, U, X, or Z.")
            }
        }
        return true
    }

    private fun translateAcids(aminoAcid: String): String {
        return when (aminoAcid) {
            "A" -> listOf("GCA", "GCC", "GCG", "GCU").random()
            "R" -> listOf("CGA", "CGC", "CGG", "CGU", "AGA", "AGG").random()
            "N" -> listOf("AAU", "AAC").random()
            "D" -> listOf("GAU", "GAC").random()
            "C" -> listOf("UGU", "UGC").random()
            "Q" -> listOf("CAA", "CAG").random()
            "E" -> listOf("GAA", "GAG").random()
            "G" -> listOf("GGA", "GGC", "GGU", "GGG").random()
            "H" -> listOf("CAU", "CAC").random()
            "I" -> listOf("AUA", "AUC", "AUU").random()
            "L" -> listOf("UUA", "UUG", "CUA", "CUC", "CUG", "CUU").random()
            "K" -> listOf("AAA", "AAG").random()
            "M" -> "AUG"
            "F" -> listOf("UUU", "UUC").random()
            "P" -> listOf("CCA", "CCC", "CCG", "CCU").random()
            "S" -> listOf("AGU", "AGC", "UCA", "UCC", "UCG", "UCU").random()
            "T" -> listOf("ACA", "ACC", "ACG", "ACU").random()
            "W" -> "UGG"
            "Y" -> listOf("UAU", "UAC").random()
            "V" -> listOf("GUA", "GUC", "GUG", "GUU").random()
            else -> " Invalid value. How did you do that? "
        }
    }

        private fun textToCodon(text: String): String {
        val modifiedText = text.uppercase().trim().replace("[^A-Z]".toRegex(), "")
        val aminoAcids = modifiedText.chunked(1)
        val codons = mutableListOf<String>()

        for (amino in aminoAcids) {
            val codon = translateAcids(amino)
            codons.add(codon)
        }

        return codons.joinToString("")
    }

    private fun showErrorDialog(message: String?) {
        android.app.AlertDialog.Builder(this, R.style.AlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }


}
package com.hedgehax.codontranslator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TextToRna : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_text_to_rna)

        val textInput: EditText = findViewById(R.id.textInput)
        val translateButton: Button = findViewById(R.id.translateButton)
        val rnaOutput: TextView = findViewById(R.id.rnaOutput)

        translateButton.setOnClickListener {
            val rnaSequence = textInput.text.toString()
            if (rnaSequence.isBlank()) {
                showErrorDialog("You must type something in.")
                return@setOnClickListener
            }
            try {
                if (validateSequence(rnaSequence)) {
                    val aminoAcidString = sequenceToCodon(rnaSequence)
                    rnaOutput.text = aminoAcidString
                }
            } catch (e: InvalidSequenceException) {
                showErrorDialog(e.message)
            } catch (e: InvalidCharacterException) {
                showErrorDialog(e.message)
            } catch (e: Exception) {
                showErrorDialog("An unknown error occurred.")
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    class InvalidSequenceException(message: String) : Exception(message)
    class InvalidCharacterException(message: String) : Exception(message)

    //TODO: validateText
    private fun validateSequence(sequence: String): Boolean {
        if (sequence.length % 3 != 0) {
            throw InvalidSequenceException("Incorrect number of characters. Sequence should be in a multiple of three. You have " + (sequence.length % 3) + " extra character(s).")
        }

        val upperSequence = sequence.uppercase()
        val validCharacters = setOf('A', 'C', 'G', 'U')

        for (char in upperSequence) {
            if (char !in validCharacters) {
                throw InvalidCharacterException("Invalid character '$char'.")
            }
        }
        return true
    }

    //TODO: can this be removed
    private fun splitSequence(sequence: String): List<String> {
        return sequence.chunked(3)
    }

    //TODO: translateText
    private fun translateCodon(codon: String): String {
        return when (codon) {
            "GCA", "GCC", "GCG", "GCU" -> "A"
            "CGA", "CGC", "CGG", "CGU", "AGA", "AGG" -> "R"
            "AAU", "AAC" -> "N"
            "GAU", "GAC" -> "D"
            "UGU", "UGC" -> "C"
            "CAA", "CAG" -> "Q"
            "GAA", "GAG" -> "E"
            "GGA", "GGC", "GGU", "GGG" -> "G"
            "CAU", "CAC" -> "H"
            "AUA", "AUC", "AUU" -> "I"
            "UUA", "UUG", "CUA", "CUC", "CUG", "CUU" -> "L"
            "AAA", "AAG" -> "K"
            "AUG" -> "M"
            "UUU", "UUC" -> "F"
            "CCA", "CCC", "CCG", "CCU" -> "P"
            "AGU", "AGC", "UCA", "UCC", "UCG", "UCU" -> "S"
            "ACA", "ACC", "ACG", "ACU" -> "T"
            "UGG" -> "W"
            "UAU", "UAC" -> "Y"
            "GUA", "GUC", "GUG", "GUU" -> "V"
            "UAA", "UAG", "UGA" -> " [stop codon] "
            else -> " Invalid value. How did you do that? "
        }
    }

    //TODO: codonToSequence
    private fun sequenceToCodon(sequence: String): String {
        val upperSequence = sequence.uppercase()
        validateSequence(upperSequence)
        val codons = splitSequence(upperSequence)
        val aminoAcids = mutableListOf<String>()

        for (codon in codons) {
            val aminoAcid = translateCodon(codon)
            aminoAcids.add(aminoAcid)
        }

        return aminoAcids.joinToString("")
    }

    private fun showErrorDialog(message: String?) {
        android.app.AlertDialog.Builder(this, R.style.AlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
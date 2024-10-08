package com.hedgehax.codontranslator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class RnaToText : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rna_to_text)

        val rnaInput: EditText = findViewById(R.id.rnaInput)
        val translateButton: Button = findViewById(R.id.translateButton)
        val textOutput: TextView = findViewById(R.id.textOutput)

        translateButton.setOnClickListener {
            val rnaSequence = rnaInput.text.toString().replace("\\s".toRegex(), "")
            if (rnaSequence.isBlank()) {
                showErrorDialog("You must type something in.")
                return@setOnClickListener
            }
            try {
                if (validateSequence(rnaSequence)) {
                    val aminoAcidString = sequenceToCodon(rnaSequence)
                    textOutput.text = aminoAcidString
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

    class InvalidSequenceException(message: String) : Exception(message)
    class InvalidCharacterException(message: String) : Exception(message)

    private fun validateSequence(sequence: String): Boolean {

        val upperSequence = sequence.uppercase()
        val validCharacters = setOf('A', 'C', 'G', 'U')

        for (char in upperSequence) {
            if (char !in validCharacters) {
                throw InvalidCharacterException("Invalid character '$char'.")
            }
        }
        if (upperSequence.length % 3 != 0) {
            throw InvalidSequenceException("Incorrect number of characters. Sequence should be in a multiple of three. You have " + (sequence.length % 3) + " extra character(s).")
        }
        return true
    }

    private fun splitSequence(sequence: String): List<String> {
        return sequence.chunked(3)
    }

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

    private fun sequenceToCodon(sequence: String): String {
        val upperSequence = sequence.uppercase()
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
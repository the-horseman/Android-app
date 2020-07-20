@file:Suppress("SpellCheckingInspection")

package com.example.first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.accessibility.AccessibilityViewCommand
import java.lang.NumberFormatException

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {
    private lateinit var result : EditText
    private lateinit var newNumber : EditText
    private  val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }
    // Variables to hold operands and type of operations
    private var operand1 : Double? = null
    private  var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newnumber)

        // Data input buttons
        val button0 : Button = findViewById(R.id.button14)
        val button1 : Button = findViewById(R.id.button9)
        val button2 : Button = findViewById(R.id.button10)
        val button3 : Button = findViewById(R.id.button11)
        val button4 : Button = findViewById(R.id.button5)
        val button5 : Button = findViewById(R.id.button6)
        val button6 : Button = findViewById(R.id.button7)
        val button7 : Button = findViewById(R.id.button)
        val button8 : Button = findViewById(R.id.button2)
        val button9 : Button = findViewById(R.id.button3)
        val buttonDot : Button = findViewById(R.id.button13)

        //Operation input buttons
        val buttonEquals = findViewById<Button>(R.id.button16)
        val buttonDivide = findViewById<Button>(R.id.button15)
        val buttonMultiply = findViewById<Button>(R.id.button4)
        val buttonMinus = findViewById<Button>(R.id.button8)
        val buttonPlus = findViewById<Button>(R.id.button12)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val oplistener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performoperation(value, op)
            } catch(e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(oplistener)
        buttonPlus.setOnClickListener(oplistener)
        buttonMinus.setOnClickListener(oplistener)
        buttonMultiply.setOnClickListener(oplistener)
        buttonDivide.setOnClickListener(oplistener)
    }

    private fun performoperation(value: Double, op: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
                "*" -> operand1 = operand1!! * value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
        if (op == "=") {
            operand1 = null
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if(operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        } else {
            outState.putBoolean(STATE_OPERAND1_STORED, false)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED )) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text = pendingOperation
    }
}

package com.thelis.budget

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.amountInput
import kotlinx.android.synthetic.main.activity_add_transaction.amountLayout
import kotlinx.android.synthetic.main.activity_add_transaction.closeBtn
import kotlinx.android.synthetic.main.activity_add_transaction.descripstion_input
import kotlinx.android.synthetic.main.activity_add_transaction.labelInput
import kotlinx.android.synthetic.main.activity_add_transaction.labeleLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var transaction : Transction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        transaction  = intent.getSerializableExtra("transaction")as Transction


        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descripstion_input.setText(transaction.description)


        rootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }


        labelInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if (it!!.count() > 0)
                labeleLayout.error = null
        }
        amountInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if (it!!.count() > 0)
                amountLayout.error = null
        }

        descripstion_input.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE

        }




        updateBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descripstion_input.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()
            if (label.isEmpty())
                labeleLayout.error = "Пожалуйста введите назвние"

            else if (amount == null)
                amountLayout.error = "Введите цифры"
            else {
                val transactions = Transction(transaction.id,label,amount,description)
                update(transactions)
            }
        }
        closeBtn.setOnClickListener {
            finish()
        }

    }

    private fun update(transaction: Transction) {
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)

            finish()
        }
    }

}

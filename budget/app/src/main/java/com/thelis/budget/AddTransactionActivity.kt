package com.thelis.budget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        labelInput.addTextChangedListener {
            if (it!!.count() > 0)
                labeleLayout.error = null
        }
        amountInput.addTextChangedListener {
            if (it!!.count() > 0)
                amountLayout.error = null
        }




        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descripstion_input.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()
            if (label.isEmpty())
                labeleLayout.error = "Пожалуйста введите назвние"

            else if (amount == null)
                amountLayout.error = "Введите цифры"
            else {
                val transactions = Transction(0,label,amount,description)
                insert(transactions)
            }
        }
        closeBtn.setOnClickListener {
            finish()
        }

    }

    private fun insert(transaction: Transction) {
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }

}
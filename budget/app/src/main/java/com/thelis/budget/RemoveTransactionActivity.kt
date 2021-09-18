package com.thelis.budget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_remove_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RemoveTransactionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_transaction)

        labelInput_remove.addTextChangedListener {
            if (it!!.count() > 0)
                labeleLayout_Remove.error = null
        }
        amountInput_remove.addTextChangedListener {
            if (it!!.count() > 0)
                amountLayout_remove.error = null
        }

        addTransactionBtnRemove.setOnClickListener {
            val label = labelInput_remove.text.toString()
            val description = descripstion_input_remove.text.toString()
            val amount = amountInput_remove.text.toString().toDoubleOrNull()
            if (label.isEmpty())
                labeleLayout_Remove.error = "Пожалуйста введите назвние"

            else if (amount == null)
                amountLayout_remove.error = "Введите цифры"
            else {
                val transactions = Transction(0,label,amount,description)
                insert(transactions)
            }
        }
        closeBtn_remove.setOnClickListener {
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
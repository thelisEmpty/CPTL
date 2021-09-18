package com.thelis.budget

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var deleteTransactions: Transction
    private lateinit var transctions: List<Transction>
    private lateinit var oldTransctions: List<Transction>
    private lateinit var transctionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transctions = arrayListOf()

        transctionAdapter = TransactionAdapter(transctions)
        linearLayoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        recyvlerview.apply {
            adapter = transctionAdapter
            layoutManager = linearLayoutManager
        }

        //удание
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransactions(transctions[viewHolder.adapterPosition])
            }

        }
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyvlerview)

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)

        }
       /* removeBtn.setOnClickListener {
            val intent = Intent(this, RemoveTransactionActivity::class.java)
            startActivity(intent)
        }*/
    }

    private fun fetchAll() {
        GlobalScope.launch {
            transctions = db.transactionDao().getAll()
            runOnUiThread {
                updateDashboard()
                transctionAdapter.setData(transctions)
            }
        }
    }

    private fun updateDashboard() {
        val totalAmount = transctions.map { it.amount }.sum()
        val budgetAmount = transctions.filter { it.amount > 0 }.map { it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        balance.text = "%.2f ₽".format(totalAmount)
        budget.text = "%.2f ₽".format(budgetAmount)
        expense.text = "%.2f ₽".format(expenseAmount)

    }

    private fun undoDelete() {
        GlobalScope.launch {
            db.transactionDao().insertAll(deleteTransactions)
            transctions = oldTransctions
            runOnUiThread {
                transctionAdapter.setData(transctions)
                updateDashboard()

            }
        }
    }

    private fun showSnackbar() {
        val view = findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view, "Сумма удалена", Snackbar.LENGTH_LONG)
        snackbar.setAction("Отменить") {
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun deleteTransactions(transactions: Transction) {
        deleteTransactions = transactions
        oldTransctions = transctions

        GlobalScope.launch {
            db.transactionDao().delete(transactions)

            transctions = transctions.filter { it.id != transactions.id }
            runOnUiThread {
                updateDashboard()
                transctionAdapter.setData(transctions)
                showSnackbar()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        fetchAll()
    }

}
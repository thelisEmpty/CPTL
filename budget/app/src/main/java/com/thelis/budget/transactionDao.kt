package com.thelis.budget

import android.view.SurfaceControl
import androidx.room.*


@Dao
interface transactionDao {
    @Query("SELECT * from transactions")
    fun getAll(): List<Transction>

    @Insert
    fun insertAll(vararg transactions: Transction)

    @Delete
    fun delete(transactions: Transction)

    @Update
    fun update(vararg transactions: Transction)

}
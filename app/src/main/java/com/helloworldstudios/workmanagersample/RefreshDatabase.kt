package com.helloworldstudios.workmanagersample

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val getData = inputData
        val myNumber = getData.getInt("savedNumber", 0)
        refreshDatabase(myNumber)
        return Result.success()
    }

    private fun refreshDatabase(myNumber: Int){
        val sharedPreferences = context.getSharedPreferences("com.helloworldstudios.workmanagersample", Context.MODE_PRIVATE)
        var mySavedNumber = sharedPreferences.getInt("mySavedNumber", 0)
        val mySavedTime = sharedPreferences.getString("mySavedTime", Calendar.getInstance().time.toString())
        mySavedNumber += myNumber
        println(mySavedNumber)
        println(mySavedTime)
        sharedPreferences.edit().putInt("mySavedNumber", mySavedNumber).apply()
        sharedPreferences.edit().putString("mySavedTime", Calendar.getInstance().time.toString()).apply()
    }
}
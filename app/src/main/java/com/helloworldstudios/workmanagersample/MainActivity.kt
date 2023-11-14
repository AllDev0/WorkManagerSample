package com.helloworldstudios.workmanagersample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.helloworldstudios.workmanagersample.ui.theme.WorkManagerSampleTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val data = Data.Builder()
                .putInt("savedNumber", 1)
                .build()

            val constraints = Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            /*
            val myOneTimeWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
                .setConstraints(constraints)
                .setInputData(data)
                //.setInitialDelay(5, TimeUnit.MINUTES)
                //.addTag("OneTimeWorkRequest")
                .build()

            WorkManager.getInstance(this@MainActivity).enqueue(myOneTimeWorkRequest)
            */

            val myPeriodicWorkRequest: WorkRequest =
                PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .setInputData(data)
                    .build()

            WorkManager.getInstance(this@MainActivity).enqueue(myPeriodicWorkRequest)

            WorkManager.getInstance(this@MainActivity)
                .getWorkInfoByIdLiveData(myPeriodicWorkRequest.id)
                .observe(this@MainActivity, Observer {
                    println(when(it.state){
                        WorkInfo.State.ENQUEUED -> "Enqueued"
                        WorkInfo.State.RUNNING -> "Running"
                        WorkInfo.State.SUCCEEDED -> "Succeeded"
                        WorkInfo.State.FAILED -> "Failed"
                        WorkInfo.State.BLOCKED -> "Blocked"
                        WorkInfo.State.CANCELLED -> "Cancelled"
                        else -> "Something Went Wrong"
                    })
                })

            //WorkManager.getInstance(this@MainActivity).cancelAllWork()

            //Chanining | It can work on OneTimeWorkRequest

            val oneTimeWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>().
                setConstraints(constraints)
                .setInputData(data)
                .build()

            WorkManager.getInstance(this@MainActivity)
                .beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue()



            WorkManagerSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}
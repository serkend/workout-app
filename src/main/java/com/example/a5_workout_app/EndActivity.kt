package com.example.a5_workout_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.a5_workout_app.databinding.ActivityEndBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EndActivity : AppCompatActivity() {
    private var binding: ActivityEndBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater).also { setContentView(it.root) }
        binding?.finishBtn?.setOnClickListener { goToMainActivity() }
        setSupportActionBar(binding?.toolbarEnd)
        binding?.toolbarEnd?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        addDateToDatabase(dao)
    }

    private fun goToMainActivity() {
        val intent = Intent(this@EndActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun addDateToDatabase(historyDao: HistoryDao) {
        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date: ", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("Date: ", "" + date)
        }
    }
}


















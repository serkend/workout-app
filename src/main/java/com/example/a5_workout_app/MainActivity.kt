package com.example.a5_workout_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a5_workout_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        viewBinding?.flStart?.setOnClickListener {
            goToExerciseActivity()
        }

        viewBinding?.flBmi?.setOnClickListener {
            goToBmiActivity()
        }

        viewBinding?.flHistory?.setOnClickListener {
            goToHistoryActivity()
        }

    }

    private fun goToExerciseActivity() {
        val intent = Intent(this@MainActivity, ExerciseActivity::class.java)
        startActivity(intent)
    }

    private fun goToBmiActivity() {
        var intent = Intent(this@MainActivity, BmiActivity::class.java)
        startActivity(intent)
    }

    private fun goToHistoryActivity() {
        var intent = Intent(this@MainActivity, HistoryActivity::class.java)
        startActivity(intent)
    }
}
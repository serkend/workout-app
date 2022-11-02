package com.example.a5_workout_app

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a5_workout_app.databinding.ActivityExerciseBinding
import com.example.a5_workout_app.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var restTimerDuration: Long = 1
    var exerciseTimerDuration: Long = 3
    var countDownTimer: CountDownTimer? = null
    var restProgress: Int = 0
    var isExercise: Boolean = false
    var progressBarTimer: ProgressBar? = null
    var flView: FrameLayout? = null
    var timerTV: TextView? = null

    var exerciseList: ArrayList<ExerciseModel>? = null
    var currentExercisePosition = -1

    var tts: TextToSpeech? = null
    var player: MediaPlayer? = null

    private var binding: ActivityExerciseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        progressBarTimer = binding?.progressBarRestTimer
        flView = binding?.flRestView
        timerTV = binding?.tvTimer
        setSupportActionBar(binding?.toolbarExercise)

        exerciseList = Constants.defaultExerciseList()

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogRun()
        }

        createTimer(restTimerDuration * 1000)?.start()
        binding?.tvUpcomingExercise?.text = "Upcoming exercise: ${exerciseList!![0].getName()}"

        tts = TextToSpeech(this, this)
        setUpRecyclerView()

    }


    override fun onBackPressed() {
        customDialogRun()
    }

    private fun customDialogRun() {
        var dialog = Dialog(this)
        var dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        dialogBinding.yesDialogBtn.setOnClickListener {
            this@ExerciseActivity.finish()
            dialog.dismiss()
        }
        dialogBinding.noDialogBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    private fun setUpRecyclerView() {
        var adapter = RecyclerViewAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = adapter
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    fun createTimer(newDuration: Long): CountDownTimer? {
        countDownTimer = object : CountDownTimer(newDuration, 1000) {
            override fun onTick(millisLeft: Long) {
                restProgress++
                progressBarTimer?.progress = progressBarTimer?.max?.minus(restProgress)!!

                timerTV?.text = (millisLeft / 1000).toString()
            }

            override fun onFinish() {
                if (!isExercise) {
//                    Toast.makeText(
//                        this@ExerciseActivity, "Here you can start the exercise.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    currentExercisePosition++
                    prepareForExercise()
                    createTimer(exerciseTimerDuration * 1000)?.start()
                } else {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    binding?.rvExerciseStatus?.adapter?.notifyDataSetChanged()
//                    Toast.makeText(
//                        this@ExerciseActivity, "Finish exercise.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    if (currentExercisePosition == (exerciseList?.size?.minus(1) ?: 0)) {
                        val intent = Intent(this@ExerciseActivity, EndActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this@ExerciseActivity,
                            "Congratulations! You have completed 7 minutes workout.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        prepareForRest()
                        createTimer(restTimerDuration * 1000)?.start()
                    }
                }
            }
        }
        return countDownTimer
    }

    fun prepareForExercise() {
        restProgress = 0
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE

        flView?.visibility = View.INVISIBLE
        flView = binding?.flExerciseView
        flView?.visibility = View.VISIBLE
        progressBarTimer = binding?.progressBarExercise
        timerTV = binding?.tvTimerExercise
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        exerciseList!![currentExercisePosition].setIsSelected(true)
        binding?.rvExerciseStatus?.adapter?.notifyDataSetChanged()
        speakOut(binding?.tvExerciseName?.text.toString())
        isExercise = true
    }

    fun prepareForRest() {
        isExercise = false
        restProgress = 0
        try {
            val soundURI =
                Uri.parse("android.resource://com.example.a5_workout_app" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            print(e.stackTrace)
        }
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.text =
            "Upcoming exercise: ${exerciseList!![currentExercisePosition + 1].getName()}"

        flView?.visibility = View.INVISIBLE
        flView = binding?.flRestView
        flView?.visibility = View.VISIBLE
        progressBarTimer = binding?.progressBarRestTimer
        binding?.ivImage?.visibility = View.INVISIBLE
        timerTV = binding?.tvTimer
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        restProgress = 0

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }
}
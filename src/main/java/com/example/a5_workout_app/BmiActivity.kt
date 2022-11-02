package com.example.a5_workout_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a5_workout_app.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

class BmiActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var bmiBinding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bmiBinding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(bmiBinding?.root)

        setSupportActionBar(bmiBinding?.toolbarBmiActivity)


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }

        bmiBinding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        bmiBinding?.calculateBmiBtn?.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricUnits()) {
                    val heightValue: Float =
                        bmiBinding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                    val weightValue: Float =
                        bmiBinding?.etMetricUnitWeight?.text.toString().toFloat()

                    val bmi = weightValue / heightValue.pow(2)
                    displayBmiResult(bmi)
                } else {
                    Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (validateUsMetricUnits()) {
                    val usUnitHeightValueFeet: String =
                        bmiBinding?.etMetricUSUnitFeet?.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        bmiBinding?.etMetricUSUnitInch?.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = bmiBinding?.etMetricUnitWeight?.text.toString()
                        .toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBmiResult(bmi) // Displaying the result into UI
                } else {
                    Toast.makeText(
                        this@BmiActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        bmiBinding?.rgUnits?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMetricUnits) {
                currentVisibleView = METRIC_UNITS_VIEW
                makeVisibleMetricUnits()
            } else {
                currentVisibleView = US_UNITS_VIEW
                makeVisibleUsMetricUnits()
            }
        }
    }

    private fun makeVisibleMetricUnits() {
        bmiBinding?.llUsMetricUnits?.visibility = View.INVISIBLE
        bmiBinding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        bmiBinding?.llDisplayBMIResult?.visibility = View.INVISIBLE
        bmiBinding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        bmiBinding?.tilUsMetricUnitWeight?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsMetricUnits() {
        bmiBinding?.llUsMetricUnits?.visibility = View.VISIBLE
        bmiBinding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        bmiBinding?.llDisplayBMIResult?.visibility = View.INVISIBLE
        bmiBinding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        bmiBinding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
    }

    private fun displayBmiResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        bmiBinding?.llDisplayBMIResult?.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        bmiBinding?.tvBMIValue?.text = bmiValue // Value is set to TextView
        bmiBinding?.tvBMIType?.text = bmiLabel // Label is set to TextView
        bmiBinding?.tvBMIDescription?.text = bmiDescription // Description is set to TextView

    }

    private fun validateMetricUnits(): Boolean {
        if (bmiBinding?.etMetricUnitWeight?.text.toString().isEmpty()
            || bmiBinding?.etMetricUnitHeight?.text.toString().isEmpty()
        ) {
            return false
        }
        return true
    }

    private fun validateUsMetricUnits(): Boolean {
        if (bmiBinding?.etMetricUnitWeight?.text.toString().isEmpty()
            || bmiBinding?.etMetricUSUnitFeet?.text.toString().isEmpty()
            || bmiBinding?.etMetricUSUnitInch?.text.toString().isEmpty()
        ) {
            return false
        }
        return true
    }


}
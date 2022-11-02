package com.example.a5_workout_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a5_workout_app.databinding.ItemExerciseBinding

class RecyclerViewAdapter(val exerciseList: ArrayList<ExerciseModel>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ExerciseViewHolder>() {
    class ExerciseViewHolder(var itemBinding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: ExerciseModel) {
            var exerciseTV = itemBinding.exerciseNumTV
            exerciseTV.text = model.getId().toString()
            when {
                model.getIsSelected() -> {
                    exerciseTV.background = ContextCompat.getDrawable(
                        this.itemView.context,
                        R.drawable.item_circular_thin_color_accent_border
                    )
                    exerciseTV.setTextColor(Color.parseColor("#212121"))
                }
                model.getIsCompleted() -> {
                    exerciseTV.setBackgroundResource(R.drawable.item_circular_color_accent_bg)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        var model = exerciseList[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
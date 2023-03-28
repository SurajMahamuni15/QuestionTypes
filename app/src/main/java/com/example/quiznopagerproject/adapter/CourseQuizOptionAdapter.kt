package com.example.quiznopagerproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiznopagerproject.databinding.CourseQuizOptionItemBinding
import com.example.quiznopagerproject.interfaceDir.ItemClickListener
import com.example.quiznopagerproject.viewModel.QuizOptionViewHolder

class CourseQuizOptionAdapter(
    val context: Context,
    private val opList: List<String>,
    private val itemClickListener: ItemClickListener,
    private val count: Int
) :
    RecyclerView.Adapter<QuizOptionViewHolder>() {
    private var ans = HashMap<String, String>()
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizOptionViewHolder {

        val bindingOption =
            CourseQuizOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizOptionViewHolder(bindingOption)
    }

    override fun onBindViewHolder(holder: QuizOptionViewHolder, position: Int) {
        Log.e("position", opList[position])
        holder.binding.op.text = opList[position]
        holder.binding.op.isChecked = (position == selectedPosition)
        holder.binding.op.setOnCheckedChangeListener { compoundButton, b  ->
            Log.e("what","compoundButton:${compoundButton.text} isChecked:$b")
            if (b) {
                ans["Q${count+1}"] = holder.binding.op.text.toString()
                selectedPosition = holder.adapterPosition
                itemClickListener.onRadioClick(
                    holder.binding.op.text.toString()
                )
                Log.e("hashMap","$ans")
            }
        }
//        adapterPosition.getPosition(position)
    }

    override fun getItemCount(): Int {
        return opList.size
    }
}
package com.example.quiznopagerproject.activity

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiznopagerproject.R
import com.example.quiznopagerproject.adapter.CourseQuizOptionAdapter
import com.example.quiznopagerproject.databinding.ActivityMainBinding
import com.example.quiznopagerproject.databinding.RewardDialogItemBinding
import com.example.quiznopagerproject.interfaceDir.AdapterPosition
import com.example.quiznopagerproject.interfaceDir.ItemClickListener
import com.example.quiznopagerproject.model.QuizData
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), ItemClickListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var opList = ArrayList<String>()
    private var opList1 = ArrayList<String>()
    private var courseQuizArrData = ArrayList<QuizData>()
    private var countDownTimer: CountDownTimer? = null
    private var count: Int = 0
    private var itemClickListener: ItemClickListener? = null
    private var ans = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initAdapter(count)
        initListener()
        setCountDownTimer()
        sizeTxt()
    }

    private fun initAdapter(adapterCount: Int) {
        itemClickListener = object : ItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onRadioClick(s: String?) {
                Log.e("check_text", "Q${count + 1}")
                ans["Q${count + 1}"] = s.toString()
                binding.optionRv.post(Runnable { binding.optionRv.adapter?.notifyDataSetChanged() })
            }
        }
        binding.optionRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.optionRv.adapter =
            CourseQuizOptionAdapter(
                this,
                courseQuizArrData[adapterCount].opList,
                itemClickListener as ItemClickListener,count
            )
        binding.questionTxt.text =
            courseQuizArrData[adapterCount].question
    }

    private fun initListener() {
        nextBtnListener()
        previousBtnListener()
        finishBtnListener()
    }

    private fun setCountDownTimer() {
//        countDownTimer = object : CountDownTimer(91000, 1000) {
        countDownTimer = object : CountDownTimer(25000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // logic to set the EditText could go here
                initCountDown(false, millisUntilFinished)
            }

            override fun onFinish() {
                initCountDown(true, 0)
            }
        }.start()
    }

    private fun initCountDown(flag: Boolean, millisUntilFinished: Long) {
        if (flag) {
            val endQuizBuilder = MaterialAlertDialogBuilder(binding.root.context)
            endQuizBuilder.setTitle("TimeOut!")
            endQuizBuilder.setMessage("Your answer has be submitted automatically Good Luck!!")
            endQuizBuilder.setCancelable(false)
            endQuizBuilder.setPositiveButton("Yes") { dialogYes, which ->
                dialogYes.dismiss()
                onBackPressed()
            }
            val dialog = endQuizBuilder.create()
            dialog.show()

        } else {
            var diff = millisUntilFinished
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val elapsedMinutes = diff / minutesInMilli
            diff %= minutesInMilli
            val elapsedSeconds = diff / secondsInMilli
            if (elapsedSeconds.toString().length < 2) {
                binding.quizCountdownText.text = getString(
                    R.string.quiz_countdown,
                    "0$elapsedMinutes",
                    "0$elapsedSeconds"
                )
            } else {
                binding.quizCountdownText.text = getString(
                    R.string.quiz_countdown,
                    "0$elapsedMinutes",
                    "$elapsedSeconds"
                )
            }
        }
    }


    private fun finishBtnListener() {
        binding.finishBtn.setOnClickListener {
            finishAction()
        }
    }

    private fun finishAction() {
        //showing filled question data
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Finish Quiz")
        builder.setMessage("Are you sure you want to finish the quiz.")
        //no button click
        builder.setNegativeButton("No") { dialogNo, which ->
            dialogNo.dismiss()
        }
        //yes button click
        builder.setPositiveButton("Yes") { dialogYes, which ->
            //custom dialog for reward
            val bindingReward = RewardDialogItemBinding.inflate(layoutInflater)
            bindingReward.animationLottieReward.setAnimation(R.raw.reward)
            val rewardDialog = Dialog(this)
            rewardDialog.setContentView(bindingReward.root)
            rewardDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            rewardDialog.show()
            bindingReward.animationLottieReward.setAnimation(R.raw.reward)
            //dismiss dialog by clicking outside false
            rewardDialog.setCancelable(false)
            //close button click
            bindingReward.closeBtn.setOnClickListener {
                rewardDialog.dismiss()
            }
            //continue button click
            bindingReward.continuePlayingButton.setOnClickListener {
                rewardDialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun previousBtnListener() {
        binding.previousBtn.setOnClickListener {
            Log.e("size:", "courseQuizArrData: ${courseQuizArrData.size} count: $count")
            if (count == 1) {
                binding.previousBtn.visibility = View.GONE
                previusData()

            } else {
                previusData()
            }
        }
    }

    private fun previusData() {
        count--
        sizeTxt()
        initAdapter(count)
    }


    private fun nextBtnListener() {
        binding.nextBtn.setOnClickListener {
            if (ans["Q${count + 1}"].isNullOrEmpty()) {
                Log.e("size:", "courseQuizArrData: ${courseQuizArrData.size} count: ${count + 2}")
                if (courseQuizArrData.size != count + 1) {
                    count++
                    sizeTxt()
                    if (count > 0) {
                        binding.previousBtn.visibility = View.VISIBLE
                        initAdapter(count)
                    }
                }
            }else{

                if (courseQuizArrData.size != count + 1) {
                count++
                sizeTxt()
                if (count > 0) {
                    binding.previousBtn.visibility = View.VISIBLE
                    initAdapter(count)
                }
            }


            }
        }
    }

    private fun initData() {
        opList.add("6")
        opList.add("7")
        opList.add("8")
        opList.add("9")




        opList1.add("7jnsdf")
        opList1.add("7jnsdfvnsj")
        opList1.add("7jnsdfvnsj")
        opList1.add("7jns")
        opList1.add("7jns")


        courseQuizArrData.add(QuizData("Number of primitive data1", opList))
        courseQuizArrData.add(QuizData("Number of primitive dat2", opList1))
        courseQuizArrData.add(QuizData("Number of primitive data3", opList1))
        courseQuizArrData.add(QuizData("Number of primitive data4", opList))
        courseQuizArrData.add(QuizData("Number of primitive data5", opList1))

    }


    private fun sizeTxt() {
//        increase num txt size
        val ss1 =
            SpannableString("${count + 1}/${courseQuizArrData.size}")
        ss1.setSpan(RelativeSizeSpan(2f), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.countText.text = ss1
    }

    override fun onRadioClick(s: String?) {
//        Log.e("check_text",s.toString())
//        Toast.makeText(binding.root.context,s.toString(),Toast.LENGTH_SHORT).show()
    }
}
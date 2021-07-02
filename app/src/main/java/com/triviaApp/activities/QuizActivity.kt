package com.triviaApp.activities

import android.os.Bundle
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.triviaApp.R
import com.triviaApp.core.BaseActivity
import com.triviaApp.core.BindingActivity
import com.triviaApp.databinding.ActivityQuizBinding
import com.triviaApp.model.Quiz
import com.triviaApp.utils.AppUtils
import com.triviaApp.viewmodel.QuizViewModel
import org.json.JSONArray
import org.koin.androidx.viewmodel.ext.android.viewModel


class QuizActivity() : BindingActivity<ActivityQuizBinding>() {
    override fun getLayoutResId() = R.layout.activity_quiz
//    private lateinit var viewModel: QuizViewModel<Any>
    private lateinit var quiz:Quiz
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).showProgressDialog()
        callapi()


        mBinding.buttonOptionA.setOnClickListener {
            if(quiz.answer!!.equals(mBinding.buttonOptionA.text)){
                AppUtils.showSnackBar("Succsess")
                count++
                mBinding.textViewDefaultScore.text = "Score : "+count.toString()

                callapi()
            }else{
                AppUtils.showErrorSnackBar("Failure")
            }
        }

        mBinding.buttonOptionB.setOnClickListener {
            if(quiz.answer!!.equals(mBinding.buttonOptionB.text)){
                AppUtils.showSnackBar("Succsess")

                callapi()
            }else{
                AppUtils.showErrorSnackBar("Failure")
            }
        }

        mBinding.buttonOptionC.setOnClickListener {
            if(quiz.answer!!.equals(mBinding.buttonOptionC.text)){
                AppUtils.showSnackBar(getString(R.string.text_you_unlocked_medium))

                callapi()
            }else{
                AppUtils.showErrorSnackBar("Failure")
            }
        }
    }

    fun callapi()
    {
        AndroidNetworking.get("https://jservice.io/api/random")
            .setPriority(Priority.LOW)
            .build()
            .getAsObjectList(Quiz::class.java, object : ParsedRequestListener<List<Quiz>> {
                override fun onResponse(response: List<Quiz>?) {
                    Log.d("AAAA", "AAAA" + response!!.get(0).answer)
                    response.forEach({ it
                        mBinding.data = it
                        quiz = it
                    })
                    hideProgressDialog()
                }

                override fun onError(anError: ANError?) {
                    AppUtils.showErrorSnackBar(anError!!.message.toString())
                }
            })

    }}
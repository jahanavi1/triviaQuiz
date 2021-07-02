package com.triviaApp.activities

import android.os.Bundle
import com.triviaApp.R
import com.triviaApp.core.BaseActivity
import com.triviaApp.core.BindingActivity
import com.triviaApp.databinding.ActivityPlayGameBinding


class PlayGameActivity() : BindingActivity<ActivityPlayGameBinding>() {
    override fun getLayoutResId() = R.layout.activity_play_game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.btnPlay.setOnClickListener {
            (activity as BaseActivity).launch(QuizActivity::class.java)
        }

    }
}
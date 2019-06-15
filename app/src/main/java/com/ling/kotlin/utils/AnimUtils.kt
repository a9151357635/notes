package com.ling.kotlin.utils

import android.animation.ValueAnimator
import android.widget.TextView


object AnimUtils {

    fun startAnimator(textView: TextView, test: String) {
        var text = test
        if (test.contains(",")) {
            text = test.replace(",", "")
        }
        val accountBalanceFloat = java.lang.Float.valueOf(text)
        val animator = ValueAnimator.ofFloat(0f, accountBalanceFloat)
        animator.duration = 1000
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            textView?.text = AppUtils.decimalFormat(value.toString())
        }
        animator.start()
    }
}

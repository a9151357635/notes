package com.ling.kotlin.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.Nullable

import java.util.regex.Pattern

class DecimalInputTextWatcher : TextWatcher {

    private val mDecimalInputEt: EditText
    private var mPattern: Pattern? = null
    private var listener: onTextWatcherListener? = null

    fun setListener(@Nullable listener: onTextWatcherListener) {
        this.listener = listener
    }

    /**
     * 不限制整数位数和小数位数
     */
    constructor(decimalInputEt: EditText) {
        mDecimalInputEt = decimalInputEt
    }

    /**
     * 限制整数位数或着限制小数位数
     *
     * @param type   限制类型
     * @param number 限制位数
     */
    constructor(decimalInputEt: EditText, type: Type, number: Int) {
        mDecimalInputEt = decimalInputEt
        if (type == Type.decimal) {
            mPattern = Pattern.compile("^[0-9]+(\\.[0-9]{0,$number})?$")
        } else if (type == Type.integer) {
            mPattern = Pattern.compile("^[0-9]{0,$number}+(\\.[0-9]*)?$")
        }
    }

    /**
     * 既限制整数位数又限制小数位数
     *
     * @param integers 整数位数
     * @param decimals 小数位数
     */

    constructor(decimalInputEt: EditText, integers: Int, decimals: Int) {
        mDecimalInputEt = decimalInputEt
        mPattern = Pattern.compile("^[0-9]{0,$integers}+(\\.[0-9]{0,$decimals})?$")
    }

    constructor(decimalInputEt: EditText, integers: Int) {
        mDecimalInputEt = decimalInputEt
        mPattern = Pattern.compile("^[0-9]{0,$integers}?$")
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val editable = mDecimalInputEt.text
        var text = s.toString()
        if (TextUtils.isEmpty(text)) {
            text = "0"
        }
        if (listener != null) {
            listener!!.afterTextChanged(text)
        }
        if (s.length > 1 && s[0] == '0' && s[1] != '.') {   //删除首位无效的“0”
            editable.delete(0, 1)
            return
        }
        if (text == ".") {                                    //首位是“.”自动补“0”
            editable.insert(0, "0")
            return
        }
        if (mPattern != null && !mPattern!!.matcher(text).matches() && editable.isNotEmpty()) {
            editable.delete(editable.length - 1, editable.length)
            return
        }
    }

    enum class Type {
        integer, decimal
    }

    interface onTextWatcherListener {

        fun afterTextChanged(s: String)
    }
}

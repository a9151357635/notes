package com.ling.kotlin.utils

class CombineUtils {


    /**
     * 实现了数学中的组合方法C(n,r)即 n!/(n-r)!r!
     * @param m
     * @param n
     * @return
     */
    fun combineSize(m: Int, n: Int): Int {
        return factorial(m) / (factorial(m - n) * factorial(n))
    }

    /**
     * 实现了数学中阶乘的方法 factorialA(5)即5!
     * @param number
     * @return
     */
    private fun factorial(number: Int): Int {
        return combineMath(number, number)
    }

    /**
     * 数学中的统计方法，用于整数，A(3,2)即3*2,A(5,3)即5*4*3
     * @param first
     * @param second
     * @return
     */
    private fun combineMath(first: Int, second: Int): Int {
        var tmp = first
        var result = first
        var count = 0
        while (count < second - 1) {
            if (second == 1) {
                return first
            } else {
                count++
                tmp--
                result *= tmp
            }
        }
        return result
    }

}
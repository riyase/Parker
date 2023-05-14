package com.keepqueue.sparepark.data

import android.util.Patterns

object Util {

    fun CharSequence.isValidEmail()
        = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun CharSequence.isValidPhone()
            = !isNullOrEmpty() && this.length > 9

    fun CharSequence.isValidPassword()
            = !isNullOrEmpty() && this.length > 9
}
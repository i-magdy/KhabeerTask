package com.devwarex.kabeertask.models

import com.devwarex.kabeertask.BuildConfig

data class LogInModel(
    val MobileNumber: String = BuildConfig.mobile_number,
    val Password: Int = BuildConfig.password.toInt()
)

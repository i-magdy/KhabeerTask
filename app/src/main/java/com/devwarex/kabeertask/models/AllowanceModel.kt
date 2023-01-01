package com.devwarex.kabeertask.models

data class AllowanceModel(
    val EMP_ID: Int,
    val SAL_COMP_CODE: Int,
    val COMP_DESC_AR: String,
    val COMP_DESC_EN: String,
    val SAL_VALUE: Float,
    val SAL_COMP_TYPE: Int,
    val currency_ar: String = "",
    val currency_en: String = ""
)

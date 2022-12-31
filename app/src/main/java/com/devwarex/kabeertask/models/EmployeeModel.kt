package com.devwarex.kabeertask.models

data class EmployeeModel(
    val Activation: Boolean,
    val Success: Boolean,
    val Payroll: PayrollModel,
    val EnglishMessage: String,
    val ArabicMessage: String
)

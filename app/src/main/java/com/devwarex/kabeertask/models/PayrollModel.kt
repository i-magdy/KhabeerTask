package com.devwarex.kabeertask.models

data class PayrollModel(
    val Employee: List<Employee>,
    val Allowences: List<AllowenceModel>,
    val Deduction: List<AllowenceModel>,
    val Date: String
)

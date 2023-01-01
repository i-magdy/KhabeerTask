package com.devwarex.kabeertask.models

data class PayrollModel(
    val Employee: List<Employee>,
    val Allowences: List<AllowanceModel>,
    val Deduction: List<AllowanceModel>,
    val Date: String
)

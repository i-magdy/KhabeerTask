package com.devwarex.kabeertask

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.devwarex.kabeertask.api.ApiClient
import com.devwarex.kabeertask.databinding.ActivityEmployeeBinding
import com.devwarex.kabeertask.models.AllowanceModel
import com.devwarex.kabeertask.models.EmployeeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.Calendar

class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding
    private val salariesAdapter = SalaryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.salariesRv.apply {
            adapter = this@EmployeeActivity.salariesAdapter
            layoutManager = LinearLayoutManager(this@EmployeeActivity)
        }
        intent.getStringExtra("USER_TOKEN")?.let {
            CoroutineScope(Dispatchers.Unconfined).launch {
                val response = ApiClient.create().getEmployee("Bearer $it")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        updateUi(body)
                    }
                }
            }
        }
    }

    private suspend fun updateUi(
        employee: EmployeeModel
    ) = withContext(Dispatchers.Main) {
        binding.employeeNameTv.text = getProperTextByLanguage(
            ar = employee.Payroll.Employee[0].EMP_DATA_AR,
            en = employee.Payroll.Employee[0].EMP_DATA_EN
        )
        binding.employeeJobTitleTv.text = getProperTextByLanguage(
            ar = employee.Payroll.Employee[0].SEC_NAME_AR,
            en = employee.Payroll.Employee[0].SEC_NAME_EN
        )
        binding.employeeJoinedDateTv.text = getDateFormat(employee.Payroll.Date)
        binding.totalSalaryValueTv.text = calculateTotalSalary(
            allowances = employee.Payroll.Allowences,
            deduction = employee.Payroll.Deduction,
            currencyAr = employee.Payroll.Employee[0].CURRSYMBOL_AR,
            currencyEn = employee.Payroll.Employee[0].CURRSYMBOL_EN,
        )
        val allowances = calculateSalary(
            salaries = employee.Payroll.Allowences
        )
        val deduction = calculateSalary(
            salaries = employee.Payroll.Deduction
        )
        binding.allowancesValueTv.text = String.format("%.2f %s",
            allowances,
            getProperTextByLanguage(
                ar = employee.Payroll.Employee[0].CURRSYMBOL_AR,
                en = employee.Payroll.Employee[0].CURRSYMBOL_EN
        ))
        binding.deductionValueTv.text = String.format("%.2f %s",
            deduction,
            getProperTextByLanguage(
                ar = employee.Payroll.Employee[0].CURRSYMBOL_AR,
                en = employee.Payroll.Employee[0].CURRSYMBOL_EN
            ))
        binding.chartTotlaSalaryTv.text = calculateTotalSalary(
            allowances = employee.Payroll.Allowences,
            deduction = employee.Payroll.Deduction,
            currencyAr = employee.Payroll.Employee[0].CURRSYMBOL_AR,
            currencyEn = employee.Payroll.Employee[0].CURRSYMBOL_EN,
        )
        binding.salaryPieChart.addPieSlice(
            PieModel(
                "",
                allowances,
                getColor(R.color.allowance_color)
            )
        )
        binding.salaryPieChart.addPieSlice(
            PieModel(
                "",
                deduction,
                getColor(R.color.deduct_color)
            )
        )
        val salaries = ArrayList<AllowanceModel>()
        salaries.add(
            AllowanceModel(
                EMP_ID = -1,
                SAL_COMP_CODE = -1,
                SAL_COMP_TYPE = -1,
                SAL_VALUE = -1f,
                COMP_DESC_AR = "",
                COMP_DESC_EN = "",
                currency_ar = employee.Payroll.Employee[0].CURRSYMBOL_AR,
                currency_en = employee.Payroll.Employee[0].CURRSYMBOL_EN
            )
        )
        salaries.addAll(employee.Payroll.Allowences)
        salaries.addAll(employee.Payroll.Deduction)
        salariesAdapter.setSalaries(salaries)
    }


    private fun getProperTextByLanguage(
        ar: String,
        en: String
    ): String {
        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        } else {
            resources.configuration.locale.language
        }
        return if (lang == "ar") ar else en
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormat(
        date: String
    ): String {
        if (!date.matches("\\d{2}/\\d{4}".toRegex())) return ""
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, date.subSequence(0, 2).toString().toInt()-1)
        calendar.set(Calendar.YEAR,date.subSequence(3,7).toString().toInt())
        val format = SimpleDateFormat("MMMM, yyyy")
        return format.format(calendar.time)
    }

    private fun calculateTotalSalary(
        allowances: List<AllowanceModel>,
        deduction: List<AllowanceModel>,
        currencyAr: String,
        currencyEn: String
    ): String{
        var salary = 0.0
        allowances.forEach {
            salary += it.SAL_VALUE
        }
        deduction.forEach {
            salary -= it.SAL_VALUE
        }
        return String.format("%.2f %s",salary,getProperTextByLanguage(
            ar = currencyAr,
            en = currencyEn
        ))
    }

    private fun calculateSalary(
        salaries: List<AllowanceModel>
    ): Float{
       if(salaries.isEmpty()) return  0.0f
        var s = 0.0f
        salaries.forEach {
            s += it.SAL_VALUE
        }
        return s
    }
}
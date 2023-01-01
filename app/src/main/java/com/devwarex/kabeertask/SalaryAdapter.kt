package com.devwarex.kabeertask

import android.annotation.SuppressLint
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devwarex.kabeertask.models.AllowanceModel

class SalaryAdapter: RecyclerView.Adapter<SalaryAdapter.SalaryViewHolder>() {

    private var salaries: List<AllowanceModel> = emptyList()
    private var currency = ""

    inner class SalaryViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view){
        private val quantity = view.findViewById<TextView>(R.id.salary_item_quantity_tv)
        private val clause = view.findViewById<TextView>(R.id.salary_item_clause_tv)
        private val value = view.findViewById<TextView>(R.id.salary_item_value_tv)
        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemView.resources.configuration.locales[0].language
        } else {
            itemView.resources.configuration.locale.language
        }
        fun onBind(
            salary: AllowanceModel,
            position: Int
        ) = if (position != 0){
            quantity.text = String.format("%d",position)
            value.text = if (salary.SAL_COMP_TYPE == 2){
                value.setTextColor(itemView.context.getColor(R.color.color_error))
                val span = SpannableString(String.format("%.2f %s",salary.SAL_VALUE,currency))
                span.setSpan(StrikethroughSpan(),0,span.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                span
            }else{
                String.format("%.2f %s",salary.SAL_VALUE,currency)
            }
            clause.text = if (lang == "ar"){
                salary.COMP_DESC_AR
            }else{
                salary.COMP_DESC_EN
            }
        }else{ currency = if (lang == "ar") salary.currency_ar else salary.currency_en }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryViewHolder = SalaryViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.salary_list_item,parent,false)
    )

    override fun onBindViewHolder(holder: SalaryViewHolder, position: Int) {
        holder.onBind(
            salary = salaries[position],
            position = position
        )

    }

    override fun getItemCount(): Int = salaries.size

    @SuppressLint("NotifyDataSetChanged")
    fun setSalaries(salaries: List<AllowanceModel>){
        this.salaries = salaries
        notifyDataSetChanged()
    }
}
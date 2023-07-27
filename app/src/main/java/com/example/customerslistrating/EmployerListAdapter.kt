package com.example.customerslistrating

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.customerslistrating.databinding.EmployerSampleBinding

class EmployerListAdapter(val listener : ButtonListenerRV) : RecyclerView.Adapter<EmployerListAdapter.EmployerListHolder>() {
    private lateinit var context: Context
    private var employerList = ArrayList<Employer>()

    class EmployerListHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = EmployerSampleBinding.bind(view)
        fun bind(employer: Employer, listener: ButtonListenerRV) = with(binding){
            nameEmployer.text = employer.name
            ratingBar2.rating = employer.rating
            siteWorkerName.text = employer.site
            showInfo.setOnClickListener {
                listener.onClick(employer)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployerListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employer_sample, parent, false)
        context = parent.context
        return EmployerListHolder(view)
    }

    override fun getItemCount(): Int {
        return employerList.size
    }

    override fun onBindViewHolder(holder: EmployerListHolder, position: Int) {
        holder.bind(employerList[position],listener)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addEmployer(employer: Employer){
        employerList.add(employer)
        notifyDataSetChanged()
    }
    fun getList() : ArrayList<Employer> {
        return employerList
    }



}
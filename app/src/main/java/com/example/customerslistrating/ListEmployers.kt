package com.example.customerslistrating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerslistrating.databinding.ActivityListEmployersBinding
import com.google.firebase.database.*

class ListEmployers : AppCompatActivity(), ButtonListenerRV {

    private lateinit var listEmployersBinding: ActivityListEmployersBinding
    private val adapter = EmployerListAdapter(this)
    private lateinit var databaseReference : DatabaseReference
    private var classKey = "Employer"
    private lateinit var searchView : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listEmployersBinding = ActivityListEmployersBinding.inflate(layoutInflater)
        setContentView(listEmployersBinding.root)
        init()
        getDataFromDB()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtredList(newText)
                return true
            }

        })
    }

    private fun filtredList(text: String?) {
        var filteredList = ArrayList<Employer>()
        for(employer : Employer in adapter.getList()) {
            if(employer.name.lowercase().contains(text!!.lowercase())) {
                filteredList.add(employer)
            }
        }

        if (filteredList.isEmpty()) {

        }
        else {
            adapter.setFilteredList(filteredList)
        }

    }

    private fun init(){
        searchView = findViewById(R.id.searchField)
        searchView.clearFocus()
        listEmployersBinding.apply {
            listEmployers.layoutManager = LinearLayoutManager(this@ListEmployers)
            listEmployers.adapter = adapter
            databaseReference = FirebaseDatabase.getInstance().getReference(classKey)
        }
    }

    private fun getDataFromDB() {
        val valueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(adapter.getList().size  > 0) {
                    adapter.getList().clear()
                }
                for(ds : DataSnapshot in snapshot.children) {
                    var employer = ds.getValue(Employer::class.java) as Employer
                    if (employer != null) {
                        adapter.addEmployer(employer)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReference.addValueEventListener(valueListener)
    }

    override fun onClick(employer: Employer) {
        intent = Intent(this,DetailEmployerInfo::class.java)
        intent.putExtra("employerName", employer.name)
        intent.putExtra("siteWorkerName", employer.site)
        intent.putExtra("ratingBar2", employer.rating)
        startActivity(intent)
        finish()
    }

}
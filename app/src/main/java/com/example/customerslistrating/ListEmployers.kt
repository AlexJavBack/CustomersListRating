package com.example.customerslistrating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerslistrating.databinding.ActivityListEmployersBinding
import com.google.firebase.database.*

class ListEmployers : AppCompatActivity(), ButtonListenerRV {

    private lateinit var listEmployersBinding: ActivityListEmployersBinding
    private val adapter = EmployerListAdapter(this)
    private lateinit var databaseReference : DatabaseReference
    private var classKey = "Employer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listEmployersBinding = ActivityListEmployersBinding.inflate(layoutInflater)
        setContentView(listEmployersBinding.root)
        init()
        getDataFromDB()
    }

    private fun init(){
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
        intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, employer.rating.toString(), Toast.LENGTH_SHORT).show()
    }

}
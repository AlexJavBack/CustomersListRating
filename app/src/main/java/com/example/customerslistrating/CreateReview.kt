package com.example.customerslistrating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.customerslistrating.databinding.ActivityCreateReviewBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects

class CreateReview : AppCompatActivity() {
    private lateinit var createReviewBinding: ActivityCreateReviewBinding
    lateinit var editTextCustomerName : EditText
    lateinit var editTextMessangerName : EditText
    lateinit var edtiMultilineText: EditText
    private lateinit var databaseReference : DatabaseReference
    private var classesKey = arrayListOf("Employer","Comment" )
    private var COMMENT_KEY = "Comment"
    private lateinit var ratingBar  : RatingBar
    private lateinit var textLimit : TextView
    private val limit : Int = 200;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createReviewBinding = ActivityCreateReviewBinding.inflate(layoutInflater)
        setContentView(createReviewBinding.root)
        setContentView(R.layout.activity_create_review)
        init()
        edtiMultilineText.commentChanged()
    }

    fun cancelButtonClick(view: View?) {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun save(view: View?) {
        val id = databaseReference.key ?: "0"
        val employerName = editTextCustomerName.text.toString()
        val messangerType = editTextMessangerName.text.toString()
        val commentText = edtiMultilineText.text.toString()
        val ratingEmployer = ratingBar.rating
        if(!TextUtils.isEmpty(employerName) && !TextUtils.isEmpty(messangerType) && ratingEmployer != 0.0f) {
            val comment = Comment(employerName, "Asa", commentText, ratingEmployer)
            val employer =  Employer(id, employerName, messangerType, ratingEmployer, commentText)
            changeKeyFireBase(classesKey[0])
            databaseReference.push().setValue(employer)
            changeKeyFireBase(classesKey[1])
            databaseReference.push().setValue(comment)
        }
        else {
            Toast.makeText(this, "Fields name customer, messanger and rating can't be empty",
            Toast.LENGTH_LONG).show()
        }
    }

    private fun init(){
        databaseReference = FirebaseDatabase.getInstance().reference
        editTextCustomerName = findViewById(R.id.etCustomerName)
        editTextMessangerName = findViewById(R.id.etMessangerName)
        edtiMultilineText = findViewById(R.id.ettmComment)
        ratingBar = findViewById(R.id.ratingBar)
        textLimit = findViewById(R.id.tSymbolLimit)

        //databaseReference = FirebaseDatabase.getInstance().getReference(COMMENT_KEY)
    }
    private fun EditText.commentChanged() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textLimit.text = limit.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(edtiMultilineText.text.toString() != s.toString()) {
                    var textSize = edtiMultilineText.length()
                    var limitRemaining = limit - textSize
                    textLimit.text = limitRemaining.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                var textSize = edtiMultilineText.length()
                var limitRemaining = limit - textSize
                textLimit.text = limitRemaining.toString()
            }
        })
    }

    private fun changeKeyFireBase(key: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference(key)
    }

}
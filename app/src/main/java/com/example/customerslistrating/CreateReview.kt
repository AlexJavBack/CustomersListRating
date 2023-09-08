package com.example.customerslistrating

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customerslistrating.databinding.ActivityCreateReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreateReview : AppCompatActivity() {
    private lateinit var createReviewBinding: ActivityCreateReviewBinding
    lateinit var editTextCustomerName : EditText
    lateinit var editTextMessangerName : EditText
    lateinit var edtiMultilineText: EditText
    private lateinit var databaseReference : DatabaseReference
    private lateinit var databaseComment : DatabaseReference
    private lateinit var databaseUserApp : DatabaseReference
    private var classesKey = arrayListOf("Employer","Comment","UserApp" )
    private lateinit var ratingBar  : RatingBar
    private lateinit var textLimit : TextView
    private val limit : Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createReviewBinding = ActivityCreateReviewBinding.inflate(layoutInflater)
        setContentView(createReviewBinding.root)
        setContentView(R.layout.activity_create_review)
        init()
        databaseReference = FirebaseDatabase.getInstance().getReference("Employer")
        databaseComment = FirebaseDatabase.getInstance().getReference("Comment")
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
            val firebasheUser = FirebaseAuth.getInstance().currentUser
            val comment = Comment(employerName,
                firebasheUser!!.displayName!!, commentText, ratingEmployer)
            val employer =  Employer(id, employerName, messangerType, ratingEmployer)
            if (firebasheUser != null) {
                val userApp = UserApp(firebasheUser.displayName!!,
                    firebasheUser.email!!, firebasheUser.photoUrl.toString())
                databaseUserApp.push().setValue(userApp)
            }
            getDataFromDB(employer, comment)
            //databaseReference.push().setValue(employer)
            //databaseComment.push().setValue(comment)
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
        databaseReference = FirebaseDatabase.getInstance().getReference(classesKey[0])
        databaseComment = FirebaseDatabase.getInstance().getReference(classesKey[1])
        databaseUserApp = FirebaseDatabase.getInstance().getReference(classesKey[2])
    }
    private fun EditText.commentChanged() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textLimit.text = limit.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(edtiMultilineText.text.toString() != s.toString()) {
                    val textSize = edtiMultilineText.length()
                    val limitRemaining = limit - textSize
                    textLimit.text = limitRemaining.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val textSize = edtiMultilineText.length()
                val limitRemaining = limit - textSize
                textLimit.text = limitRemaining.toString()
            }
        })
    }

    private fun changeKeyFireBase(key: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference(key)
    }

    private fun getDataFromDB(femployer: Employer, fcomment: Comment) {
        var isCorrect = true
        var isCorrecte = true
        val array = ArrayList<Float>()
        val valueListener2 = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds : DataSnapshot in snapshot.children) {
                    val comment = ds.getValue(Comment::class.java) as Comment
                    if(comment.employerName == femployer.name) {
                        array.add(comment.stars)
                    }
                    if (comment.authorName == fcomment.authorName && comment.employerName == femployer.name) {
                        isCorrecte = false
                        break
                    }
                }
                if(isCorrecte) {
                    if(fcomment.employerName == femployer.name) {
                        array.add(fcomment.stars)
                    }
                    databaseComment.push().setValue(fcomment)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        databaseComment.addValueEventListener(valueListener2)

        val valueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(databaseReference.key == classesKey[0] ) {
                    for(ds : DataSnapshot in snapshot.children) {
                        val employer = ds.getValue(Employer::class.java) as Employer
                        if (employer.name == femployer.name && isCorrect ) {
                            val hashMap : HashMap<String, Any> = HashMap()
                            hashMap["id"] = femployer.id
                            hashMap["name"] = femployer.name
                            hashMap["site"] = femployer.site
                            var sum = 0f
                            for(af : Float in array) {
                                sum +=af
                            }
                            hashMap["rating"] = sum / array.size
                            val str = ds.key
                            databaseReference.child(str!!).updateChildren(hashMap)
                            isCorrect = false
                            break
                        }
                    }
                    if(isCorrect) {
                        databaseReference.push().setValue(femployer)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReference.addValueEventListener(valueListener)


    }


}
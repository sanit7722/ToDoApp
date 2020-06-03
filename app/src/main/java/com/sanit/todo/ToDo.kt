package com.sanit.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.view.*

import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes


import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.sanit.todo.Adapter.RecyclerViewAdapter
import com.sanit.todo.Model.Notes
import java.util.*
import kotlin.collections.ArrayList


class ToDo : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var etNote: EditText
    private lateinit var etSubtitle: EditText
    private lateinit var btnAdd: Button
    private var notesArrayList = ArrayList<Notes>()
    private var firebaseUser: FirebaseUser? = null
    var RC_SIGN_IN: Int = 1234
    private var response: IdpResponse? = null
    var TAG : String = "TAG"
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==R.id.refresh){


        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        FirebaseApp.initializeApp(this)
//        Crashlytics.getInstance().crash();

        recyclerView = findViewById(R.id.ourdoes)
        etNote = findViewById(R.id.etNote)
        btnAdd = findViewById(R.id.btnAdd)
        etSubtitle = findViewById(R.id.etSubtitle)

        val dbRef = FirebaseDatabase.getInstance().reference
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val recyclerViewAdapter = RecyclerViewAdapter(notesArrayList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        if (firebaseUser != null) {

            btnAdd.setOnClickListener { dbRef.child("Notes").child(firebaseUser!!.uid).push().setValue(Notes(etNote.text.toString(), etSubtitle.text.toString())) }
            dbRef.child("Notes").child(firebaseUser!!.uid).addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val notes = dataSnapshot.getValue(Notes::class.java)
                    notesArrayList.add(Notes(notes!!.title!!, notes.subtitle!!))
                    recyclerViewAdapter.notifyDataSetChanged()
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            e(TAG, firebaseUser!!.displayName)
            e(TAG,firebaseUser!!.uid)
        } else {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(
                        Arrays.asList(
//                                    AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build(),
                        AuthUI.IdpConfig.AnonymousBuilder().build()))
                    .build(),
                RC_SIGN_IN)
        }



        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            super.onActivityResult(requestCode, resultCode, data);
            // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
            if (requestCode == RC_SIGN_IN) {
                response = IdpResponse.fromResultIntent(data);

                // Successfully signed in
                if (resultCode == RESULT_OK) {
                    btnAdd.setOnClickListener { dbRef.child("Notes").child(firebaseUser!!.uid).push().setValue(Notes(etNote.text.toString(), etSubtitle.text.toString())) }
                    dbRef.child("Notes").child(firebaseUser!!.uid).addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                            val notes = dataSnapshot.getValue(Notes::class.java)
                            notesArrayList.add(Notes(notes!!.title!!, notes.subtitle!!))
                            recyclerViewAdapter.notifyDataSetChanged()
                        }

                        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                        }

                        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                        }

                        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
                    finish()
                } else {
                    // Sign in failed
                    if (response == null) {
                        // User pressed back button
                        return
                    }

                    if (response!!.getError()!!.getErrorCode() == ErrorCodes.NO_NETWORK) {
                        return
                    }

                    e(TAG, "Sign-in error: ", response!!.getError())
                }
            }
        }



    }
}
package com.sanit.todo


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import io.reactivex.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.sanit.todo.Adapter.ToDoAdapter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.add_list_items.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_does.view.*


class ToDo : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    val RC_SIGN_IN: Int = 123
    lateinit var providers: List<AuthUI.IdpConfig>

    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var dayt: String


    private var db: ToDoDB? = null
    private var todoDAO: ToDoDAO? = null

    @SuppressLint("CheckResult", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this@ToDo)


        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),

            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        showSignInOptions()


        val button = findViewById<Button>(R.id.btn1)
        val dltbtn = findViewById<ImageButton>(R.id.deletebtn)

        recyclerView.layoutManager = LinearLayoutManager(this)


        Observable.fromCallable {
            db = ToDoDB.getAPPDataBase(this)
            todoDAO = db?.ToDoDAO()


            db?.ToDoDAO()?.getToDoList()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(

            {
                Log.d("size", it?.size.toString())
                recyclerView.adapter = ToDoAdapter(it!!, this)
            },
            {
                Log.d("error", "error")
            }

        )

        button.setOnClickListener {


            AddItems()
        }



    }

    fun AddItems() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_list_items, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add To Do ")
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogAddBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            title = mDialogView.edtitle.text.toString()
            desc = mDialogView.eddesc.text.toString()
            dayt = mDialogView.edday.text.toString()

            Toast.makeText(this, title + "" + desc + "" + dayt, Toast.LENGTH_SHORT).show()

            Observable.fromCallable {
                db = ToDoDB.getAPPDataBase(this)
                todoDAO = db?.ToDoDAO()
                var todolist = ToDoStore(null, title, desc, dayt)
                with(todoDAO) {
                    this?.insert(todolist)
                }

                db?.ToDoDAO()?.getToDoList()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    Log.d("size", it?.size.toString())
                    recyclerView.adapter = ToDoAdapter(it!!, this)
                },
                {
                    Log.d("error", "error")
                }
            )


        }
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()
                //  btn.isEnabled = true
            } else {
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_addToDo -> {
                AddItems()
            }

            R.id.nav_logout -> {
                AuthUI.getInstance().signOut(this@ToDo)
                    .addOnCompleteListener {

                        showSignInOptions()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
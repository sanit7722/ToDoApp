package com.sanit.todo


import android.annotation.SuppressLint
import io.reactivex.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.sanit.todo.Adapter.ToDoAdapter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.add_list_items.view.*
import kotlinx.android.synthetic.main.item_does.view.*


class ToDo : AppCompatActivity() {


    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var dayt: String


    private var db: ToDoDB? = null
    private var todoDAO: ToDoDAO? = null

    @SuppressLint("CheckResult", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)


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



    }

}
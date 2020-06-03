package com.sanit.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollBtn: Button =findViewById(R.id.button)
        rollBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, ToDo::class.java)
            startActivity(intent)
        }
    }


}

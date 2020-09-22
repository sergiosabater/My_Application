package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        /* --- Flecha para regresar al anterior Activity --- */
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val textView = findViewById<TextView>(R.id.textViewIntent)

        val bundle = intent.extras

        if(bundle?.getString("saludo") != null){
            val saludo = bundle.getString("saludo")
            textView.text = saludo
        }else{
            Toast.makeText(this, "Está vacío", Toast.LENGTH_SHORT).show()
        }

        btnToThirdActivity.setOnClickListener {
            startActivity(this, ThirdActivity::class.java)
        }

    }

    fun startActivity(activity: Activity, nextActivity: Class<*>){
        val intent = Intent(activity, nextActivity)
        activity.startActivity(intent)
        activity.finish()
    }

    /* Método para volver atrás con la fecha en ActionBar */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
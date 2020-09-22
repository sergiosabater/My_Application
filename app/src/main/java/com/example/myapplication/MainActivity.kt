package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val SALUDO = "Hola desde el MainActivity principal en NuevaRama"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* --- Icono en el ActionBar --- */
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher)

        buttonCalcular.setOnClickListener{
            val annioNacimiento : Int = editTextNumber.text.toString().toInt()
            val annioActual = Calendar.getInstance().get(Calendar.YEAR)
            val miEdad = annioActual - annioNacimiento
            textView.text = "Tu edad es $miEdad años"
        }

        buttonSiguiente.setOnClickListener{
            nuevaActivity(this, SecondActivity::class.java)
        }
    }

    //INTENT EXPLÍCITO
    fun nuevaActivity(activity : Activity, nextActivity : Class<*>){
        val intent = Intent(activity, nextActivity)
        intent.putExtra("saludo", SALUDO)
        activity.startActivity(intent)
        activity.finish()
    }


}
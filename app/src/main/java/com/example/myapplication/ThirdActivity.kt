package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_third.*

class ThirdActivity : AppCompatActivity() {

    private val PHONE_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        /* --- Flecha para regresar al anterior Activity --- */
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /* --- Botón para llamar por teléfono --- */ //--> Llamada con permisos
        imageButtonPhone!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber = editTextPhone!!.text.toString()
                if (!phoneNumber.isEmpty()) {
                    /* Comprobamos la versión del SDK de Android */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        /* Comprobamos si el permiso está aceptado */
                        if (verificarPermiso(android.Manifest.permission.CALL_PHONE)) {
                            /* El permiso está en el manifest. Ha aceptado */
                            val intentAceptado =
                                Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                            /* Verificamos que exista el permiso en el manifest explícitamente */
                            if (ActivityCompat.checkSelfPermission(
                                    this@ThirdActivity,
                                    android.Manifest.permission.CALL_PHONE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            startActivity(intentAceptado)
                        } else {
                            /* El permiso no está aceptado. Preguntar por el permiso */
                            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE)) {
                                /* Si nunca se preguntó por el permiso anteriormente, lo haremos */
                                requestPermissions(
                                    arrayOf(android.Manifest.permission.CALL_PHONE),
                                    PHONE_CODE
                                )
                            } else {
                                /* Si ya se denegó el permiso, lo redirigimos a los Ajustes para que lo habilite de nuevo */
                                Toast.makeText(
                                    this@ThirdActivity,
                                    "Habilite el permiso correspondiente para continuar",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intentSettings =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intentSettings.addCategory(Intent.CATEGORY_DEFAULT)
                                intentSettings.data = Uri.parse("package:$packageName")
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(intentSettings)
                            }
                        }
                    } else {
                        versionAntigua(phoneNumber)
                    }
                } else {
                    Toast.makeText(
                        this@ThirdActivity,
                        "Debes marcar un número. Inténtalo de nuevo",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            fun versionAntigua(phoneNumer: String) {
                val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumer"))
                if (verificarPermiso(android.Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(
                            this@ThirdActivity,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    startActivity(intentCall)
                }
            }
        })

        /* --- Botón para la web --- */
        imageButtonWeb!!.setOnClickListener {
            val url = editTextWeb!!.text.toString()

            val intentWeb = Intent()
            intentWeb.action = Intent.ACTION_VIEW
            intentWeb.data = Uri.parse("http://$url")
            startActivity(intentWeb)
        }

        /* --- Botón para el Email --- */

        buttonEmailMe!!.setOnClickListener {

            val email = "miemail@gmail.com"

            val intentEmail = Intent(Intent.ACTION_SEND, Uri.parse(email))
            intentEmail.type = "plain/text"
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Asunto del email")
            intentEmail.putExtra(Intent.EXTRA_TEXT, "El cuerpo del mensaje")
            intentEmail.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("user@mail.com", "user2@mail.com", "user2@mail.com")
            )
            startActivity(Intent.createChooser(intentEmail, "Elige cliente de correo"))

        }

        /* --- Botón para contactar --- */ //--> Llamada sin permisos

        buttonContactPhone!!.setOnClickListener {
            val intentCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:900400400"))
            startActivity(intentCall)
        }





    }

    /* Verificamos si el permiso está aceptado */
    fun verificarPermiso(permission: String): Boolean {
        val resultado = this.checkCallingOrSelfPermission(permission)
        return resultado == PackageManager.PERMISSION_GRANTED
    }

    /* Método asíncrono para comprobar permisos */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            /* Caso de uso */
            PHONE_CODE -> {
                val permiso = permissions[0]
                val resultado = grantResults[0]

                if (permiso == android.Manifest.permission.CALL_PHONE) {
                    /* Comprobar si ha sido aceptada o denegada la petición de permiso */
                    if (resultado == PackageManager.PERMISSION_GRANTED) {
                        /* El permiso ha sido concedido */
                        val phoneNumber = editTextPhone!!.text.toString()
                        val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))

                        /* Ahora debemos verificar que exista el permiso en el manifest explícitamente,
                        ya que el usuario puede rechazar esta petición de permiso */

                        if (ActivityCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.CALL_PHONE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        startActivity(intentCall)
                    } else {
                        /* Se denegó el permiso */
                        Toast.makeText(this, "Ha denegado el permiso", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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
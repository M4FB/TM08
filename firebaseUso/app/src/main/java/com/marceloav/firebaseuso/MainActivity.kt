package com.marceloav.firebaseuso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCarrera: EditText
    private lateinit var etCurso: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVerLista: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        etNombre = findViewById(R.id.etNombre)
        etCarrera = findViewById(R.id.etCarrera)
        etCurso = findViewById(R.id.etCurso)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnVerLista = findViewById(R.id.btnVerLista)

        btnGuardar.setOnClickListener {
            guardarEstudiante()
        }

        btnVerLista.setOnClickListener {
            val intent = Intent(this, EstudiantesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun guardarEstudiante() {
        val nombre = etNombre.text.toString().trim()
        val carrera = etCarrera.text.toString().trim()
        val curso = etCurso.text.toString().trim()

        if (nombre.isEmpty() || carrera.isEmpty() || curso.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = dbRef.push().key ?: return
        val estudiante = Estudiante(id, nombre, carrera, curso)

        dbRef.child(id).setValue(estudiante)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.msg_guardado), Toast.LENGTH_SHORT).show()
                etNombre.text.clear()
                etCarrera.text.clear()
                etCurso.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
            }
    }
}
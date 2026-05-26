package com.marceloav.firebaseuso

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EstudiantesActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: EstudianteAdapter
    private val lista = mutableListOf<Estudiante>()
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estudiantes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_estudiantes)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Estudiantes")

        rv = findViewById(R.id.rvEstudiantes)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = EstudianteAdapter(lista, { est -> mostrarDialogoUpdate(est) }, { id -> eliminar(id) })
        rv.adapter = adapter

        obtenerDatos()
    }

    private fun obtenerDatos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                for (snap in snapshot.children) {
                    val est = snap.getValue(Estudiante::class.java)
                    if (est != null) lista.add(est)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EstudiantesActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminar(id: String) {
        dbRef.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(this, getString(R.string.msg_eliminado), Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoUpdate(estudiante: Estudiante) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        val etNom = dialogView.findViewById<EditText>(R.id.etNombre)
        val etCar = dialogView.findViewById<EditText>(R.id.etCarrera)
        val etCur = dialogView.findViewById<EditText>(R.id.etCurso)
        val btnG = dialogView.findViewById<android.widget.Button>(R.id.btnGuardar)
        val btnV = dialogView.findViewById<android.widget.Button>(R.id.btnVerLista)

        etNom.setText(estudiante.nombre)
        etCar.setText(estudiante.carrera)
        etCur.setText(estudiante.curso)
        btnG.text = getString(R.string.btn_modificar)
        btnV.visibility = android.view.View.GONE

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.btn_actualizar))
            .setView(dialogView)
            .create()

        btnG.setOnClickListener {
            val n = etNom.text.toString()
            val c = etCar.text.toString()
            val u = etCur.text.toString()
            
            val updateData = mapOf(
                "nombre" to n,
                "carrera" to c,
                "curso" to u
            )

            dbRef.child(estudiante.id).updateChildren(updateData).addOnSuccessListener {
                Toast.makeText(this, getString(R.string.msg_actualizado), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}
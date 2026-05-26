package com.marceloav.firebaseuso

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListadoActivity : AppCompatActivity() {

    private lateinit var rvClases: RecyclerView
    private lateinit var adapter: ClaseAdapter
    private val listaClases = mutableListOf<Clase>()
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_listado)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        rvClases = findViewById(R.id.rvClases)
        rvClases.layoutManager = LinearLayoutManager(this)
        adapter = ClaseAdapter(listaClases)
        rvClases.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("Clases").child("Lecciones")

        obtenerDatos()
    }

    private fun obtenerDatos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaClases.clear()
                if (snapshot.exists()) {
                    for (claseSnap in snapshot.children) {
                        val clase = claseSnap.getValue(Clase::class.java)
                        if (clase != null) {
                            listaClases.add(clase)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListadoActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
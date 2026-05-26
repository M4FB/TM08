package com.marceloav.firebaseuso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstudianteAdapter(
    private val lista: List<Estudiante>,
    private val onEdit: (Estudiante) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<EstudianteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreEst)
        val tvCarrera: TextView = view.findViewById(R.id.tvCarreraEst)
        val tvCurso: TextView = view.findViewById(R.id.tvCursoEst)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudiante, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = item.nombre
        holder.tvCarrera.text = "Carrera: ${item.carrera}"
        holder.tvCurso.text = "Curso: ${item.curso}"

        holder.btnEditar.setOnClickListener { onEdit(item) }
        holder.btnEliminar.setOnClickListener { onDelete(item.id) }
    }

    override fun getItemCount(): Int = lista.size
}
package com.marceloav.firebaseuso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClaseAdapter(private val listaClases: List<Clase>) :
    RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder>() {

    class ClaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSeccion: TextView = view.findViewById(R.id.tvSeccion)
        val tvArea: TextView = view.findViewById(R.id.tvArea)
        val tvTema: TextView = view.findViewById(R.id.tvTema)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase, parent, false)
        return ClaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        val clase = listaClases[position]
        holder.tvSeccion.text = "Sección: ${clase.seccion}"
        holder.tvArea.text = "Asignatura: ${clase.area}"
        holder.tvTema.text = "Tema: ${clase.tema}"
    }

    override fun getItemCount(): Int = listaClases.size
}
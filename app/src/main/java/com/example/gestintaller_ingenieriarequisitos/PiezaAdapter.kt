package com.example.gestintaller_ingenieriarequisitos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PiezaAdapter(private val context: Context, private val piezas: List<MainActivity.Pieza>) : BaseAdapter() {

    override fun getCount(): Int {
        return piezas.size
    }

    override fun getItem(position: Int): Any {
        return piezas[position]
    }

    override fun getItemId(position: Int): Long {
        return piezas[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        val pieza = piezas[position]

        // Configurar los datos
        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        text1.text = pieza.nombre
        text2.text = pieza.fabricante

        return view
    }
}

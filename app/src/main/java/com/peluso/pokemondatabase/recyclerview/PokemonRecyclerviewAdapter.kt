package com.peluso.pokemondatabase.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peluso.pokemondatabase.R
import com.peluso.pokemondatabase.model.PokemonCell
import kotlinx.android.synthetic.main.pokemon_cell.view.*
import org.w3c.dom.Text

class PokemonRecyclerviewAdapter(val context: Context,
                                 val pokemonCells: List<PokemonCell>)
    : RecyclerView.Adapter<PokemonRecyclerviewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nameText: TextView = itemView.pokemon_cell_name
        var idText: TextView = itemView.pokemon_cell_ID
        var hpText: TextView = itemView.pokemon_cell_HP
        var attackText: TextView = itemView.pokemon_cell_attack
        var defenseText: TextView = itemView.pokemon_cell_defense
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemonCells.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // first we fill a cell
        val cell = pokemonCells[position]

        // then we fill the values
        val nameText = holder.nameText
        val hpText = holder.hpText
        val idText = holder.idText
        val attackText = holder.attackText
        val defenseText = holder.defenseText

        nameText.text = cell.name
        idText.text = "ID: ${cell.idNum}\t"
        hpText.text = "HP: ${cell.HP}\t"
        attackText.text = "Attack: ${cell.attack}\t"
        defenseText.text = "Defense: ${cell.defense}\t"

    }
}
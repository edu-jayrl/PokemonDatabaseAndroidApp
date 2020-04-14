package com.peluso.pokemondatabase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peluso.pokemondatabase.model.*
import com.peluso.pokemondatabase.recyclerview.PokemonRecyclerviewAdapter
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {

    private lateinit var db: Database
    private val filterOptions: List<String> =
        listOf("Game","Generation","Name","Trainer","Type")

    // Keeping track of the filter the user selected
    private lateinit var filterSelection: Filter
    // the list of cells all our Queries will be adding to
    private lateinit var pokemonCells: List<PokemonCell>

    //recyclerview
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: PokemonRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)



        val client = APIClient.retrofitInstance!!.create(APIService::class.java)
        db = Database(this,client)

        // initialize the pokemon list
        db.getPokemon {
            pokemonCells = it
            initRecyclerView()
        }

        // Default filter selection is trainer
        filterSelection = Filter.TRAINER

        // launching the listview from the select filter button
        select_filter_term_button.setOnClickListener {
            launchFilterTermsDialog()
        }

        // returning the results of the query
        select_search_button.setOnClickListener {
            if(select_search_input.text.equals("")){
                Toast.makeText(this,"Cannot search without term",Toast.LENGTH_LONG)
                return@setOnClickListener
            }
            val searchTerm = select_search_input.text.toString()

            when(filterSelection){
                Filter.TYPE -> db.filterByType(searchTerm, this::displayFilteredResponse)
                Filter.GENERATION -> db.filterByGen(searchTerm.toInt(), this::displayFilteredResponse)
                Filter.TRAINER -> db.filterByTrainer(searchTerm.toInt(),this::displayFilteredResponse)
                Filter.GAME -> db.filterByGame(searchTerm.toInt(), this::displayFilteredResponse)
                Filter.NAME -> db.filterByName(searchTerm, this::displayFilteredResponse)
            }
        }
    }

    private fun displayFilteredResponse(list: List<PokemonCell>){
        pokemonCells = list
        val newAdapter = PokemonRecyclerviewAdapter(this,pokemonCells)
        recyclerView.swapAdapter(newAdapter,false)
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.pokemon_recyclerview)
        recyclerViewAdapter = PokemonRecyclerviewAdapter(this,pokemonCells)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun launchFilterTermsDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Filter by:")
            .setItems(toArrayCharOut(filterOptions), this::onItemClick)
        val dialog = builder.create()

        dialog.show()
    }

    private fun toArrayCharOut(list: List<String>): Array< CharSequence> {
        val out = ArrayList<String>(list.size)
        list.forEach {
            out.add(it)
        }
        return out.toArray(arrayOfNulls<CharSequence>(out.size))
    }


    private fun onItemClick(d: DialogInterface,i: Int) {
        when (i) {
            0 -> filterSelection = Filter.GAME
            1 -> filterSelection = Filter.GENERATION
            2 -> filterSelection = Filter.NAME
            3 -> filterSelection = Filter.TRAINER
            4 -> filterSelection = Filter.TYPE
        }
        filterTermTextView.text = "Filter term: ${filterOptions[i]}"
    }
}

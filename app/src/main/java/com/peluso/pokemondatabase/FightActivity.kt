package com.peluso.pokemondatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.peluso.pokemondatabase.model.APIClient
import com.peluso.pokemondatabase.model.APIService
import com.peluso.pokemondatabase.model.Database
import com.peluso.pokemondatabase.model.Pokemon
import kotlinx.android.synthetic.main.activity_fight.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep

class FightActivity : AppCompatActivity() {
    lateinit var db: Database
    var p1 = 0
    var p2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)
        val client = APIClient.retrofitInstance!!.create(APIService::class.java)
        db = Database(this,client)

        getPokemonButton.setOnClickListener {
            p1 = pokemon1EditText.text.toString().toInt()
            p2 = pokemon1EditText2.text.toString().toInt()
            db.getById(p1,this::displayPokemon1)
            sleep(1000)
            db.getById(p2,this::displayPokemon2)
        }

        fight_trigger_button.setOnClickListener {
            db.getWinner(p1, p2, this::displayWinner)
        }


    }

    private fun displayWinner(p: Pokemon) {
        winner_textview.text = "Winner: ${p.name}!!"
    }

    private fun displayPokemon1(p: Pokemon) {
        pokemon1_textview.text = p.toString()
    }
    private fun displayPokemon2(p: Pokemon) {
        pokemon1_textview2.text = p.toString()
    }
}

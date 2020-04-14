package com.peluso.pokemondatabase.model

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.peluso.pokemondatabase.MainActivity
import retrofit2.Call
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import java.lang.Thread.sleep


// WRAPPER FOR DATABASE INTERACTIONS
class Database(val context: Context,
               val client: APIService) {

    fun displayToast(msg: String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }

    // adder functions

    fun add_gen(gen: Int,
                date: String){
        val response = client.addGeneration(gen,date)
        response.enqueue(object: Callback<List<Generation>>{
            override fun onFailure(call: Call<List<Generation>>?, t: Throwable?) {
                displayToast(t!!.localizedMessage)
            }

            override fun onResponse(
                call: Call<List<Generation>>?,
                response: Response<List<Generation>>?
            ) {
                if(!response!!.isSuccessful){
                    displayToast(response.errorBody().toString())
                    return
                }
                displayToast("Successfully added $gen $date to the database!")
            }

        })
    }

    fun addTrainer(name: String){
        val response = client.addTrainer(name)
        response.enqueue(object: Callback<List<Trainer>>{
            override fun onFailure(call: Call<List<Trainer>>?, t: Throwable?) {
                displayToast(t!!.localizedMessage)
            }

            override fun onResponse(
                call: Call<List<Trainer>>?,
                response: Response<List<Trainer>>?
            ) {
                if(!response!!.isSuccessful){
                    displayToast(response.errorBody().toString())
                    return
                }
                displayToast("Successfully added $name to the database!")
            }

        })
    }

    fun addPokemon(
        name: String,
        hp: Int,
        speed: Int,
        attack: Int,
        special_atack: Int,
        defense: Int,
        special_defense: Int,
        evolve_id: Int,
        generation: Int,
        type1: String,
        type2: String) {
        val response = client.addPokemon(
            name,
            hp,
            speed,
            attack,
            special_atack,
            defense,
            special_defense,
            evolve_id,
            generation,
            type1,
            type2
        )
        response.enqueue(object : Callback<List<Pokemon>> {
            override fun onFailure(call: Call<List<Pokemon>>?, t: Throwable?) {
                displayToast(t!!.localizedMessage)
            }

            override fun onResponse(
                call: Call<List<Pokemon>>?,
                response: Response<List<Pokemon>>?
            ) {
                if (!response!!.isSuccessful) {
                    displayToast(response.errorBody().toString())
                    return
                }
                displayToast("Successfully added $name to the database!")
            }

        })
    }

        fun addPokemonToTrainer(t_id: Int, p_id: Int){
            val response = client.addPokemonToTrainer(t_id,p_id)
            response.enqueue(object: Callback<List<Trainer_pokemon>>{
                override fun onFailure(call: Call<List<Trainer_pokemon>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<Trainer_pokemon>>?,
                    response: Response<List<Trainer_pokemon>>?
                ) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    displayToast("Successfully added $t_id $p_id to the database!")
                }

            })
        }

        // filter returns

        private fun filterReturn(response: Call<List<PokemonCell>>, display: (List<PokemonCell>) -> Unit) {
            response.enqueue(object: Callback<List<PokemonCell>>{
                override fun onFailure(call: Call<List<PokemonCell>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<PokemonCell>>?,
                    response: Response<List<PokemonCell>>?
                ) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    display(response.body())
                }
            })
        }

        fun filterByType(type: String, display: (List<PokemonCell>) -> Unit) {
            val response = client.filterByType(type)
            filterReturn(response, display)
        }

        fun filterByGen(gen: Int,  display: (List<PokemonCell>) -> Unit) {
            val response = client.filterByGen(gen)
            filterReturn(response, display)
        }

        fun filterByTrainer(trainer: Int,  display: (List<PokemonCell>) -> Unit){
            val response = client.filterByTrainerID(trainer)
            return filterReturn(response, display)
        }

        fun filterByGame(game: Int,  display: (List<PokemonCell>) -> Unit) {
            val response = client.filterByGame(game)
            return filterReturn(response, display)
        }

        fun filterByName(name: String,  display: (List<PokemonCell>) -> Unit){
            val response = client.filterByName(name)
            return filterReturn(response, display)
        }

        // delete functions

        fun deletePokemon(id: Int) {
            val response = client.deletePokemon(id)
            response.enqueue(object: Callback<List<Pokemon>>{
                override fun onFailure(call: Call<List<Pokemon>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<Pokemon>>?,
                    response: Response<List<Pokemon>>?
                ) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    displayToast("Successfully removed $id from the database")
                }
            })
        }

        fun deleteTrainer(id: Int) {
            val response = client.deleteTrainer(id)
            response.enqueue(object: Callback<List<Trainer>>{
                override fun onFailure(call: Call<List<Trainer>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<Trainer>>?,
                    response: Response<List<Trainer>>?
                ) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    displayToast("Successfully removed $id from the database")
                }
            })
        }

        // get functions

        fun getById(id: Int, fxn: (Pokemon) -> Unit) {
            val response = client.getById(id)
            response.enqueue(object: Callback<List<Pokemon>>{
                override fun onFailure(call: Call<List<Pokemon>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(call: Call<List<Pokemon>>?, response: Response<List<Pokemon>>?) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    val pokemon = response.body()[0]
                    fxn(pokemon)
                }
            })
        }

        fun getWinner(attack_id: Int,
                      defense_id: Int,
                      displayWinner: (Pokemon) -> Unit){
            lateinit var pokemon: Pokemon
            val response = client.getWinner(attack_id, defense_id)
            response.enqueue(object: Callback<List<List<Pokemon>>>{
                override fun onFailure(call: Call<List<List<Pokemon>>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(call: Call<List<List<Pokemon>>>?, response: Response<List<List<Pokemon>>>?) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    displayWinner(response.body()[0][0])
                }
            })
        }

        fun getEffect(attack: Int, defense: Int) : List<Effect>{
            val response = client.getEffect(attack,defense)
            var out = emptyList<Effect>()
            response.enqueue(object: Callback<List<Effect>>{
                override fun onFailure(call: Call<List<Effect>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<Effect>>?,
                    response: Response<List<Effect>>?
                ) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                        return
                    }
                    out = response.body()
                    return
                }
            })
            return out
        }

        fun getEvolution(id: Int) : Evolution {
            lateinit var evo: Evolution
            val response = client.getEvolution(id)
            response.enqueue(object: Callback<List<Evolution>>{
                override fun onFailure(call: Call<List<Evolution>>?, t: Throwable?) {
                    displayToast(t!!.localizedMessage)
                }

                override fun onResponse(call: Call<List<Evolution>>?, response: Response<List<Evolution>>?) {
                    if(!response!!.isSuccessful){
                        displayToast(response.errorBody().toString())
                    }
                    evo = response.body()[0]
                }
            })
            return evo
        }

    fun getPokemon(displayPokemon: (List<PokemonCell>) -> Unit) {
        val response = client.getAll()
        response.enqueue(object: Callback<List<PokemonCell>>{
            override fun onFailure(call: Call<List<PokemonCell>>?, t: Throwable?) {
                displayToast(t!!.localizedMessage)
            }

            override fun onResponse(
                call: Call<List<PokemonCell>>?,
                response: Response<List<PokemonCell>>?
            ) {
                if(!response!!.isSuccessful){
                    displayToast(response.toString())
                    return
                }
                displayPokemon(response.body())
            }

        })
    }

    // Update functions
    fun updateTrainer(id: Int, name: String) {
        val response = client.updateTrainer(id,name)
        response.enqueue(object: Callback<List<Trainer>>{
            override fun onFailure(call: Call<List<Trainer>>?, t: Throwable?) {
                displayToast("Update failed, please try again")
            }

            override fun onResponse(
                call: Call<List<Trainer>>?,
                response: Response<List<Trainer>>?
            ) {
                if(!response!!.isSuccessful){
                    displayToast(" Update failed: ${response.errorBody()}")
                    return
                }
                displayToast("Successfully updated Trainer $id to $name")
            }

        })
    }

    // Connect to database and test function
    fun pingDatabase(handshake: (Boolean) -> Unit){
        val response = client.pingHomePage()
        response.enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                displayToast("Database connection failure! Please try another IP address")
                handshake(false)
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if(!response!!.isSuccessful){
                    displayToast("Database connection successful, but response object failed: ${response.errorBody()}")
                    handshake(false)
                    return
                }
                handshake(true)
                displayToast(response.body())
            }

        })
    }

}

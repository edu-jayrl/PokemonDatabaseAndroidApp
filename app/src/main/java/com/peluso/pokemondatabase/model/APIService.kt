package com.peluso.pokemondatabase.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    // Filter functions

    @GET("f_type?")
    fun filterByType(@Query("type") type: String): Call<List<PokemonCell>>

    @GET("f_gen")
    fun filterByGen(@Query("gen") gen: Int): Call<List<PokemonCell>>

    @GET("f_trainer")
    fun filterByTrainerID(@Query("trainer") trainer: Int): Call<List<PokemonCell>>

    @GET("f_game")
    fun filterByGame(@Query("game") game: Int): Call<List<PokemonCell>>

    @GET("f_name")
    fun filterByName(@Query("name") name: String): Call<List<PokemonCell>>

    // Adding function

    @GET("a_trainer")
    fun addTrainer(@Query("name") name: String): Call<List<Trainer>>

    @GET("a_pokemon")
    fun addPokemon(@Query("name") name: String,
                   @Query("hp") hp: Int,
                   @Query("speed") speed: Int,
                   @Query("attack") attack: Int ,
                   @Query("special_attack") special_atack: Int,
                   @Query("defense") defense: Int,
                   @Query("special_defense") special_defense: Int,
                   @Query("evolve_id") evolve_id: Int,
                   @Query("generation") generation: Int,
                   @Query("type1") type1: String,
                   @Query("type2") type2: String): Call<List<Pokemon>>

    @GET("a_pt")
    fun addPokemonToTrainer(@Query("t_id") t_id: Int,
                            @Query("p_id") p_id: Int): Call<List<Trainer_pokemon>>

    @GET("a_generation")
    fun addGeneration(@Query("gen") gen: Int,
                      @Query("date") date: String): Call<List<Generation>>

    // Delete functions

    @GET("d_trainer")
    fun deleteTrainer(@Query("t_id") t_id: Int) : Call<List<Trainer>>

    @GET("d_pokemon")
    fun deletePokemon(@Query("p_id") p_id: Int) : Call<List<Pokemon>>

    // Get functions

    @GET("g_effect")
    fun getEffect(@Query("attack") attack: Int,
                  @Query("defense") defense: Int) : Call<List<Effect>>

    @GET("g_evolution")
    fun getEvolution(@Query("evolution") evolution: Int) : Call<List<Evolution>>

    @GET("g_id")
    fun getById(@Query("id") id: Int) : Call<List<Pokemon>>

    @GET("g_winner")
    fun getWinner(@Query("attack") attack: Int,
                  @Query("defense") defense: Int) : Call<List<List<Pokemon>>>

    @GET("g_all")
    fun getAll() : Call<List<PokemonCell>>

    // Update functions
    @GET("u_trainer")
    fun updateTrainer(@Query("t_id") t_id: Int,
                      @Query("name") name: String): Call<List<Trainer>>

    // Basic webpage ping to ensure a connection has gone through
    @GET("home")
    fun pingHomePage(): Call<String>



}
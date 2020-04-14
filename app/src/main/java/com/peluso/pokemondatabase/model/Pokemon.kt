package com.peluso.pokemondatabase.model

data class Pokemon(
    val idNum: Int,
    val name: String,
    val HP: Int,
    val speed: Int,
    val attack: Int,
    val special_attack: Int,
    val defense: Int,
    val special_defense: Int,
    val generation: Int
) {
    override fun toString(): String {
        return "Name: $name\n idNum: $idNum\n HP: $HP\n Attack: $attack\n Defense: $defense\n Gen: $generation\n"
    }
}
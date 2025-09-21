package com.example.myweatherdiary.database

// Luokka, joka edustaa yksittäistä merkintää tietokannassa
data class Merkinta(
    val id: Long = 0,
    val paiva: String,
    val paikka: String,
    val lampotila: String,
    val teksti: String
)

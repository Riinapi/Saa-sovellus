package com.example.myweatherdiary.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SijaintiViewModel : ViewModel() {

    // Luodaan MutableLiveData, joka pitää sisällään sijaintitiedon
    private val _sijainti = MutableLiveData<String>()

    // Julkinen LiveData, joka antaa pääsyn sijaintitietoon
    val sijainti: LiveData<String> get() = _sijainti

    // Funktio sijainnin päivittämiseen
    fun paivitaSijainti(uusiSijainti: String) {
        // Asetetaan uusi sijainti LiveDataan
        _sijainti.value = uusiSijainti
    }
}
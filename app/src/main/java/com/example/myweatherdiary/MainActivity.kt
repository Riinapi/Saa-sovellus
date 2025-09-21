package com.example.myweatherdiary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myweatherdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Muuttuja sitomaan käyttöliittymä (UI) luokkaan ActivityMainBinding
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Alustetaan binding-objekti ja asetetaan käyttöliittymä näkymäksi
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Oletusnäkymä SaaFragment
        replaceFragment(SaaFragment())

        // Kuunnellaan navigointipalkin valintoja
        binding.bottomNavigationView.setOnItemSelectedListener {

            // fragmenttia vaihdetaan käyttäjän valinnan mukaan
            when(it.itemId){

                R.id.saa -> replaceFragment(SaaFragment())
                R.id.merkinta -> replaceFragment(MerkintaFragment())
                R.id.kaikki -> replaceFragment(KaikkiFragment())

                else -> { }
            }
            true
        }
    }

    // Metodi vaihtamaan näkyvää fragmenttia
    private fun replaceFragment(fragment : Fragment) {

        // Haetaan fragmentinhallinnan instanssi
        val fragmentManager = supportFragmentManager

        // Aloitetaan uusi fragmenttitapahtuma
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Korvataan R.id.frame_Layout nykyisellä fragmentilla
        fragmentTransaction.replace(R.id.frame_Layout, fragment)

        // Suoritetaan fragmenttitapahtuma
        fragmentTransaction.commit()

    }
}

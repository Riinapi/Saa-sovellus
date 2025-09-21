package com.example.myweatherdiary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherdiary.database.MerkintaDbHelper
import com.example.myweatherdiary.viewmodels.SijaintiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MerkintaFragment : Fragment() {

    // Käyttöliittymäelementit
    private lateinit var etxPaiva: EditText
    private lateinit var etxPaikka: EditText
    private lateinit var etxLampotila: EditText
    private lateinit var etxTeksti: EditText
    private lateinit var btnTallenna: Button
    private lateinit var btnTyhjenna: Button
    private lateinit var sijaintiViewModel: SijaintiViewModel  // ViewModel sijaintitietojen hallintaan
    private lateinit var merkintaDbHelper: MerkintaDbHelper  // Tietokannan apulainen


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_merkinta, container, false)

        // Alustetaan ViewModel ja tietokannan apulainen
        sijaintiViewModel = ViewModelProvider(requireActivity())[SijaintiViewModel::class.java]
        merkintaDbHelper = MerkintaDbHelper(requireContext())  // Contextin käyttö tietokannan avaamiseen

        // Alustetaan käyttöliittymäelementit
        etxPaiva = view.findViewById(R.id.etxPaiva)
        etxPaikka = view.findViewById(R.id.etxPaikka)
        etxLampotila = view.findViewById(R.id.etxLampotila)
        etxTeksti = view.findViewById(R.id.etxTeksti)
        btnTallenna = view.findViewById(R.id.btnTallenna)
        btnTyhjenna = view.findViewById(R.id.btnTyhjenna)

        // Asetetaan tämänhetkinen päivämäärä "Paiva"-kenttään
        val tamaPaiva = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        etxPaiva.setText(tamaPaiva)

        // Kuunnellaan sijaintitietojen päivityksiä ViewModelista
        sijaintiViewModel.sijainti.observe(viewLifecycleOwner) { uusiSijainti ->
            // Päivitetään paikka-kenttä ViewModelin mukaan
            etxPaikka.setText(uusiSijainti)
        }

        // Tyhjennä napin toiminto
        btnTyhjenna.setOnClickListener {
            etxLampotila.text.clear()
            etxTeksti.text.clear()
        }

        // Tallenna napin toiminto
        btnTallenna.setOnClickListener {
            // Haetaan käyttäjän syöttämät tiedot
            val paiva = etxPaiva.text.toString()
            val paikka = etxPaikka.text.toString()
            val lampotila = etxLampotila.text.toString()
            val teksti = etxTeksti.text.toString()

            // Varmistetaan, että kaikki kentät eivät ole tyhjiä
            if (paiva.isNotBlank() && paikka.isNotBlank()  && lampotila.isNotBlank() && teksti.isNotBlank()) {
                // Tallennetaan tiedot tietokantaan
                merkintaDbHelper.lisaaMerkinta(paiva, paikka, lampotila, teksti)

                // Ilmoitetaan tallennuksesta
                Toast.makeText(requireContext(), "Merkintä tallennettu", Toast.LENGTH_SHORT).show()

                // Tyhjennetään kentät
                etxPaiva.text.clear()
                etxPaikka.text.clear()
                etxLampotila.text.clear()
                etxTeksti.text.clear()

            } else {
                // Ilmoitetaan virhe, jos kentät ovat tyhjiä
                Toast.makeText(requireContext(), "Kaikki kentät on täytettävä", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

package com.example.myweatherdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherdiary.adapters.MerkintaAdapter
import com.example.myweatherdiary.database.MerkintaDbHelper
import com.example.myweatherdiary.database.Merkinta

class KaikkiFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView  // RecyclerView, johon listan elementit lisätään
    private lateinit var merkintaDbHelper: MerkintaDbHelper  // Tietokannan apulainen (hakee ja poistaa merkintöjä)
    private lateinit var adapter: MerkintaAdapter  // Adapteri, joka yhdistää merkinnät RecyclerView:hen
    private lateinit var searchView: SearchView  // Hakukenttä (SearchView)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kaikki, container, false)

        // Alustetaan tietokannan apulainen ja RecyclerView
        merkintaDbHelper = MerkintaDbHelper(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)

        // Alustetaan hakukenttä (SearchView)
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Tämä metodi kutsutaan, kun käyttäjä painaa hakupainiketta
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapter.filter(it) }  // Suoritetaan haku (suodatus) hakutermillä
                return true
            }

            // Tämä metodi kutsutaan, kun käyttäjä kirjoittaa hakutermiä
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filter(it) }  // Suoritetaan reaaliaikainen suodatus kirjoitetun tekstin perusteella
                return true
            }
        })

        // Alustetaan LayoutManager ja adapteri
        recyclerView.layoutManager = LinearLayoutManager(requireContext())  // Asetetaan LinearLayoutManager (listanäkymä)

        val merkinnat = merkintaDbHelper.haeMerkinnat()  // Haetaan kaikki merkinnät tietokannasta
        adapter = MerkintaAdapter(merkinnat) { merkinta ->  // Alustetaan adapteri ja määritellään poisto-toiminto
            poistaMerkinta(merkinta)  // Poistetaan merkintä, kun poista-nappia painetaan
        }
        recyclerView.adapter = adapter  // Asetetaan adapteri RecyclerView:hen

        return view
    }

    // Funktio, joka poistaa merkinnän tietokannasta ja päivittää listan
    private fun poistaMerkinta(merkinta: Merkinta) {
        merkintaDbHelper.poistaMerkinta(merkinta.id)  // Poistetaan merkintä tietokannasta sen ID:n perusteella
        // Päivitetään lista poistamisen jälkeen
        adapter.updateMerkinnat(merkintaDbHelper.haeMerkinnat())  // Haetaan päivitetyt merkinnät tietokannasta
        // Näytetään käyttäjälle viesti, että merkintä on poistettu
        Toast.makeText(requireContext(), "Merkintä poistettu", Toast.LENGTH_SHORT).show()
    }
}

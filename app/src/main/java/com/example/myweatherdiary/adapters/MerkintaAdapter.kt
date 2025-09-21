package com.example.myweatherdiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherdiary.R
import com.example.myweatherdiary.database.Merkinta
import java.util.Locale

// Adapteri, joka vastaa merkintöjen näyttämisestä RecyclerView:ssa
class MerkintaAdapter(private var merkinnat: List<Merkinta>, private val onDelete: (Merkinta) -> Unit) :
    RecyclerView.Adapter<MerkintaAdapter.MerkintaViewHolder>() {

    // Suodatetut merkinnät (käytetään hakutulosten näyttämiseen)
    private var filteredMerkinnat: List<Merkinta> = merkinnat

    // Luodaan uusi ViewHolder, joka hallitsee yksittäistä merkintää
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerkintaViewHolder {
        // Tämä luo näkymän (XML-tiedosto) jokaiselle listan riville
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_merkinta, parent, false)
        // Palautetaan uusi ViewHolder, joka sisältää tämän näkymän
        return MerkintaViewHolder(itemView)
    }

    // Sidotaan tiedot merkinnän tietoihin (esim. päivämäärä, paikka, teksti)
    override fun onBindViewHolder(holder: MerkintaViewHolder, position: Int) {
        val merkinta = filteredMerkinnat[position]
        holder.paivaTextView.text = merkinta.paiva
        holder.paikkaTextView.text = merkinta.paikka
        holder.lampotilaTextView.text = merkinta.lampotila+"°C"
        holder.tekstiTextView.text = merkinta.teksti

        // Poista napin toiminnalisuus
        holder.poistaButton.setOnClickListener {
            // Kutsutaan onDelete-funktiota, joka poistaa merkinnän
            onDelete(merkinta)
        }
    }

    // Palautetaan suodatettujen merkintöjen määrä, joka määrittää kuinka monta riviä näytetään
    override fun getItemCount(): Int = filteredMerkinnat.size

    // Suodatustoiminto, joka suodattaa merkintöjä hakutuloksen mukaan
    fun filter(query: String) {
        // Muutetaan hakutermi pieniksi kirjaimiksi
        val lowerCaseQuery = query.lowercase(Locale.getDefault())

        // Jos hakutermi on tyhjä, näytetään kaikki merkinnät
        filteredMerkinnat = if (query.isEmpty()) {
            merkinnat
        } else {
            // Muutoin suodatetaan merkinnät haun mukaan
            merkinnat.filter {
                it.paiva.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        it.paikka.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        it.lampotila.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        it.teksti.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            }
        }
        // Päivitetään näkymä suodatuksen jälkeen
        notifyDataSetChanged()
    }

    // Funktio, joka päivittää adapteriin uudet merkinnät (esim. merkinnän poiston jälkeen)
    fun updateMerkinnat(newMerkinnat: List<Merkinta>) {
        merkinnat = newMerkinnat
        filteredMerkinnat = newMerkinnat
        notifyDataSetChanged()  // Päivitetään näkymä uusilla tiedoilla
    }

    // ViewHolder-luokka, joka pitää sisällään yksittäisen merkinnän UI-komponentit
    class MerkintaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paivaTextView: TextView = itemView.findViewById(R.id.paivaTextView)
        val paikkaTextView: TextView = itemView.findViewById(R.id.paikkaTextView)
        val lampotilaTextView: TextView = itemView.findViewById(R.id.lampotilaTextView)
        val tekstiTextView: TextView = itemView.findViewById(R.id.tekstiTextView)
        val poistaButton: Button = itemView.findViewById(R.id.poistaButton)
    }
}

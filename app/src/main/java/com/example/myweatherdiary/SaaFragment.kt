package com.example.myweatherdiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myweatherdiary.viewmodels.SijaintiViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaaFragment : Fragment() {

    // Hallitaan sijaintitietoja
    private lateinit var sijaintiViewModel: SijaintiViewModel

    // Käyttöliittymäelementit
    private lateinit var txvSijainti: TextView
    private lateinit var txvLampotila: TextView
    private lateinit var txvKuvaus: TextView
    private lateinit var txvTuntuuKuin: TextView
    private lateinit var txvTuulenNopeus: TextView
    private lateinit var txvKosteus: TextView
    private lateinit var txvAuringonNousu: TextView
    private lateinit var txvAuringonLasku: TextView
    private lateinit var imgSaa: ImageView

    // Google Location Services sijaintitietojen hakemiseen
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // OpenWeather API:n avain
    private val apiKey = "PUT_YOUR_API_KEY_HERE"

    // URL säätiedon hakua varten
    private var weatherUrl = ""

    // Lupa sijainnin käyttämiseen
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saa, container, false)

        sijaintiViewModel = ViewModelProvider(requireActivity())[SijaintiViewModel::class.java]

        txvSijainti = view.findViewById(R.id.txvSijainti)
        txvLampotila = view.findViewById(R.id.txvLampotila)
        txvKuvaus = view.findViewById(R.id.txvKuvaus)
        txvTuntuuKuin = view.findViewById(R.id.txvTuntuuKuin)
        txvTuulenNopeus = view.findViewById(R.id.txvTuulenNopeus)
        txvKosteus = view.findViewById(R.id.txvKosteus)
        txvAuringonNousu = view.findViewById(R.id.txvAuringonNousu)
        txvAuringonLasku = view.findViewById(R.id.txvAuringonLasku)
        imgSaa = view.findViewById(R.id.imgSaa)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Tarkistetaan sijaintiluvat ja haetaan sijainti
        sijaintiLupa()

        return view
    }

    // Funktio kysymään lupaa sijaintiin
    private fun sijaintiLupa() {
        // Tarkistetaan, onko käyttäjä antanut tarvittavat sijaintiluvat
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Pyydetään käyttäjältä luvat sijainnin käyttöön
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Haetaan sijaintitiedot
            obtainLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        // Pyritään hakemaan käyttäjän viimeisin sijainti
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                // Muodostetaan URL säätiedon hakemista varten käyttäjän sijainnin perusteella
                weatherUrl =
                    "https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&units=metric&lang=fi&appid=$apiKey"
                // Haetaan säätiedot API:sta
                getWeatherData()
            } else {
                // Näytetään virheilmoitus, jos sijaintia ei löydy
                Toast.makeText(requireContext(), "Sijaintia ei löytynyt", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // Näytetään virheilmoitus, jos sijainnin haku epäonnistuu
            Toast.makeText(requireContext(), "Sijainnin haku epäonnistui", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getWeatherData() {
        // Alustetaan Volley-pyyntöjonot
        val queue = Volley.newRequestQueue(requireContext())
        val stringRequest = StringRequest(Request.Method.GET, weatherUrl, { response ->
            // Parsitaan JSON-vastaus
            val obj = JSONObject(response)

            // Haetaan pääsäätilatiedot
            val main = obj.getJSONObject("main")
            val temperature = main.getString("temp")
            val feelsLike = main.getString("feels_like")
            val humidity = main.getString("humidity")

            // Haetaan sääkuvauksen tiedot
            val weather = obj.getJSONArray("weather").getJSONObject(0)
            val weatherDescription = weather.getString("description")
            val iconCode = weather.getString("icon")

            // Haetaan tuulitiedot
            val wind = obj.getJSONObject("wind")
            val windSpeed = wind.getString("speed") // Tuulen nopeus

            // Haetaan auringon nousu- ja laskuajat
            val sys = obj.getJSONObject("sys")
            val sunriseUnix = sys.getLong("sunrise")  // Auringon nousu (Unix aika)
            val sunsetUnix = sys.getLong("sunset")  // Auringon lasku (Unix aika)

            // Muunnetaan Unix-aika normaaliin aika-muotoon
            val sunrise = convertUnixToTime(sunriseUnix)
            val sunset = convertUnixToTime(sunsetUnix)

            // Haetaan sijainnin nimi
            val city = obj.getString("name")

            // Päivitetään ViewModeliin sijainti
            sijaintiViewModel.paivitaSijainti(city)

            // Määritetään oma kuva säälle
            val customIconResId = when (iconCode) {
                "01d" -> R.drawable.clear_day
                "01n" -> R.drawable.clear_night
                "02d" -> R.drawable.few_clouds_day
                "02n" -> R.drawable.few_clouds_night
                "03d", "03n" -> R.drawable.cloud
                "04d", "04n" -> R.drawable.clouds
                "09d", "09n" -> R.drawable.rainy
                "10d", "10n" -> R.drawable.rain
                "11d", "11n" -> R.drawable.thunder
                "13d", "13n" -> R.drawable.snow
                "50d", "50n" -> R.drawable.mist
                else -> R.drawable.default_img
            }

            // Näytetään sää kuva Glide-kirjastolla
            Glide.with(this)
                .load(customIconResId)
                .error(R.drawable.default_img)
                .into(imgSaa)

            // Päivitetään käyttöliittymän tekstit säätietojen perusteella
            txvSijainti.text = "$city"
            txvLampotila.text = "$temperature °C"
            txvKuvaus.text = "$weatherDescription"
            txvTuntuuKuin.text = "$feelsLike °C"
            txvTuulenNopeus.text = "$windSpeed m/s"
            txvAuringonNousu.text = "$sunrise"
            txvAuringonLasku.text = "$sunset"
            txvKosteus.text = "$humidity %"

        }, {
            // Näytetään virheilmoitus, jos API-kutsu epäonnistuu
            txvSijainti.text = "Tietojen hakeminen epäonnistui!"
        })
        // Lisätään pyyntö jonoon
        queue.add(stringRequest)
    }

    // Funktio Unix-aikamuodon muuntamiseksi normaaliin aikaan
    private fun convertUnixToTime(unixTime: Long): String {
        val date = Date(unixTime * 1000)  // Unix-aika on sekunteina, joten kerrotaan 1000:lla
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())  // Määritetään aikamuoto
        return sdf.format(date)  // Palautetaan aikamuotoiltu aika
    }
}


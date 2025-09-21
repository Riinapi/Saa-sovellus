# Sää-sovellus (Weather Diary)

Kotlinilla toteutettu Android-sovellus, joka hyödyntää **OpenWeather APIa**.

## Tekniikat
- Kotlin
- Android Studio
- SQLite (paikalliset merkinnät)
- OpenWeather API

## Kuvaus
Sovellus hakee ajantasaiset säätiedot käyttäjän sijainnin perusteella ja näyttää sään visuaalisesti.

Käyttäjä voi myös tallentaa omia merkintöjä säästä tai muista huomioista, jotka säilyvät paikallisesti **SQLite-tietokannassa**. Näitä merkintöjä voi myöhemmin selata ja hakea, jolloin sovellus toimii myös henkilökohtaisena sääpäiväkirjana.

## API-avain
Sovellus vaatii OpenWeather API-avaimen.  
Korvaa SaaFragment.kt tiedostossa:
**private val apiKey = "PUT_YOUR_API_KEY_HERE"**

## Koodi
Sovelluksen koodi löytyy pääosin app/src/main/java/ -kansiosta

## Screenshots
<p float="left">
  <img src="Screenshots/saa_1.png" width="200" />
  <img src="Screenshots/saa_3.png" width="200" />
  <img src="Screenshots/saa_4.png" width="200" />
</p>

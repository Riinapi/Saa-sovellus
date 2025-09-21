package com.example.myweatherdiary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log

class MerkintaDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Tietokannan nimi
        const val DATABASE_NAME = "merkinnat.db"
        // Tietokannan versio
        const val DATABASE_VERSION = 2
        // Taulun nimi
        const val TABLE_NAME = "merkinnat"
        // Taulun sarakkeet
        const val COLUMN_ID = "id"
        const val COLUMN_PAIVA = "paiva"
        const val COLUMN_PAIKKA = "paikka"
        const val COLUMN_LAMPOTILA = "lampotila"
        const val COLUMN_TEKSTI = "teksti"
    }

    // Suoritetaan, kun tietokanta luodaan ensimmäisen kerran
    override fun onCreate(db: SQLiteDatabase?) {
        // Luodaan taulu tietokantaan
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,  
                $COLUMN_PAIVA TEXT,  
                $COLUMN_PAIKKA TEXT,
                $COLUMN_LAMPOTILA TEXT,
                $COLUMN_TEKSTI TEXT   
            )
        """
        // Suoritetaan SQL-kysely taulun luomiseksi
        db?.execSQL(createTableQuery)
    }

    // Suoritetaan, kun tietokannan versio muuttuu
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Poistetaan vanha taulu ja luodaan uusi
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Funktio, joka lisää uuden merkinnän tietokantaan
    fun lisaaMerkinta(paiva: String, paikka: String, lampotila: String, teksti: String) {
        val db = writableDatabase  // Kirjoitusoikeudet
        // Täytetään ContentValues-objekti
        val values = ContentValues().apply {
            put(COLUMN_PAIVA, paiva)
            put(COLUMN_PAIKKA, paikka)
            put(COLUMN_LAMPOTILA, lampotila)
            put(COLUMN_TEKSTI, teksti)
        }
        // Lisää merkintä tietokantaan
        db.insert(TABLE_NAME, null, values)
        db.close()  // Suljetaan tietokanta
    }

    fun haeMerkinnat(): List<Merkinta> {
        val merkinnat = mutableListOf<Merkinta>()  // Lista, johon merkinnät tallennetaan
        val db = readableDatabase  // Lukuoikeudet
        // Kysely tietokantaan
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_PAIVA DESC" // Järjestetään paivan mukaan nousevasti
        )

        if (cursor != null) {
            // Haetaan sarakkeiden indeksit
            val idColumnIndex = cursor.getColumnIndex(COLUMN_ID)
            val paivaColumnIndex = cursor.getColumnIndex(COLUMN_PAIVA)
            val paikkaColumnIndex = cursor.getColumnIndex(COLUMN_PAIKKA)
            val lampotilaColumnIndex = cursor.getColumnIndex(COLUMN_LAMPOTILA)
            val tekstiColumnIndex = cursor.getColumnIndex(COLUMN_TEKSTI)

            // Tarkistetaan, että kaikki sarakkeet löytyvät
            if (idColumnIndex >= 0 && paivaColumnIndex >= 0 && paikkaColumnIndex >= 0 && lampotilaColumnIndex >= 0 && tekstiColumnIndex >= 0) {
                while (cursor.moveToNext()) {
                    // Haetaan sarakkeiden arvot
                    val id = cursor.getLong(idColumnIndex)
                    val paiva = cursor.getString(paivaColumnIndex)
                    val paikka = cursor.getString(paikkaColumnIndex)
                    val lampotila = cursor.getString(lampotilaColumnIndex)
                    val teksti = cursor.getString(tekstiColumnIndex)
                    // Lisätään uusi Merkinta-olio listalle
                    merkinnat.add(Merkinta(id, paiva, paikka, lampotila, teksti))
                }
            } else {
                // Virheenkäsittely, jos sarakkeita puuttuu
                Log.d("DatabaseError", "Column not found")
            }
            cursor.close()  // Suljetaan cursor (tietokannan tulokset)
        }
        db.close()  // Suljetaan tietokanta
        return merkinnat  // Palautetaan listatut merkinnät
    }


    // Funktio, joka poistaa merkinnän tietokannasta sen ID:n perusteella
    fun poistaMerkinta(id: Long) {
        val db = writableDatabase  // Kirjoitusoikeudet
        // Kysely poistaa merkinnän ID:n perusteella
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()  // Suljetaan tietokanta
    }
}

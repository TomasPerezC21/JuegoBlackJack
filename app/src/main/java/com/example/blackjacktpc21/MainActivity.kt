package com.example.blackjacktpc21


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.blackjacktpc21.Baraja.Mazo
import com.example.blackjacktpc21.Cartas.Carta
import com.example.blackjacktpc21.Cartas.Palo
import com.example.blackjacktpc21.Cartas.Rango

class MainActivity : AppCompatActivity() {


    private var mazo = Mazo() //Creamos un mazo

    private var manoJugador = mutableListOf<Carta?>()
    private var manoCrupier = mutableListOf<Carta?>()

    private var puntosJugador = 0
    private var puntosCrupier = 0

    //Variables del XML

    private lateinit var tvPuntuacionJugador: TextView
    private lateinit var tvManoJugador: TextView
    private lateinit var btnPedir: Button
    private lateinit var btnPlantarse: Button

    private lateinit var tvManoCrupier: TextView

    private lateinit var tvPuntuacionCrupier: TextView

    private lateinit var tvEstadoJuego: TextView

    private lateinit var btnNuevoJuego: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bindeo

        tvPuntuacionJugador = findViewById(R.id.tvPuntuacionJugador)
        tvManoJugador = findViewById(R.id.tvJugador)
        btnPedir = findViewById(R.id.btnPedir)
        btnPlantarse = findViewById(R.id.btnPlantarse)
        tvManoCrupier = findViewById(R.id.tvCrupier)
        tvPuntuacionCrupier = findViewById(R.id.tvPuntuacionCrupier)
        tvEstadoJuego = findViewById(R.id.tvEstadoJuego)
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego)

        btnPedir.setOnClickListener {
            // Lógica para pedir carta
            pedirCartaJugador()
        }

        btnPlantarse.setOnClickListener {
            // Lógica para plantarse
            plantarse()
        }

        btnNuevoJuego.setOnClickListener {
            iniciarJuego()
        }

        iniciarJuego()
    }

    fun iniciarJuego(){


        mazo = Mazo() // Crea un mazo nuevo y barajado
        manoJugador.clear()
        manoCrupier.clear()
        puntosJugador = 0
        puntosCrupier = 0


        btnPedir.isEnabled = true
        btnPlantarse.isEnabled = true
        btnNuevoJuego.visibility = View.GONE
        tvEstadoJuego.text = "¡Empieza la partida!"


        manoJugador.add(mazo.repartirCarta())
        manoJugador.add(mazo.repartirCarta())

        manoCrupier.add(mazo.repartirCarta())
        manoCrupier.add(mazo.repartirCarta())

        puntosJugador = calcularPuntuaciones(manoJugador)
        puntosCrupier = calcularPuntuaciones(manoCrupier)


        actualizarVistas(juegoTerminado = false)
    }

    private fun pedirCartaJugador(){

        manoJugador.add(mazo.repartirCarta())

        puntosJugador = calcularPuntuaciones(manoJugador)

        actualizarVistas()

        if (puntosJugador > 21) {
            tvEstadoJuego.text = "¡Te has pasado con $puntosJugador! Gana el Crupier."

            //deshabilitamos los botones
            btnPedir.isEnabled = false
            btnPlantarse.isEnabled = false

            btnNuevoJuego.visibility = View.VISIBLE
        }

    }

    private fun plantarse(){

        btnPedir.isEnabled = false
        btnPlantarse.isEnabled = false

        // Lógica del Crupier (pedir mientras su mano valga menos de 17)
        while (puntosCrupier < 17) {
            manoCrupier.add(mazo.repartirCarta())
            puntosCrupier = calcularPuntuaciones(manoCrupier)
        }

        actualizarVistas(juegoTerminado = true)

        determinarGanador()
    }

    private fun determinarGanador() {
        val tvEstado = findViewById<TextView>(R.id.tvEstadoJuego) // El TextView de mensajes

        if (puntosCrupier > 21) {
            tvEstadoJuego.text = "¡El Crupier se ha pasado! ¡Ganas!"
        } else if (puntosJugador > puntosCrupier) {
            tvEstadoJuego.text = "¡Has ganado!"
        } else if (puntosCrupier > puntosJugador) {
            tvEstadoJuego.text = "¡Gana el Crupier!"
        } else {
            tvEstadoJuego.text = "¡Empate!"
        }

        btnNuevoJuego.visibility = View.VISIBLE
    }

    private fun calcularPuntuaciones(mano: MutableList<Carta?>) : Int{

        var suma = 0
        var ases = 0

        for (carta in mano){
            if (carta != null) {

                suma += carta.rango.valor

                if (carta.rango == Rango.AS) {
                    ases++
                }
            }
        }

        while (ases > 0 && suma + 10 <= 21) {
            suma += 10 // El As pasa de valer 1 a 11
            ases--     // "Gastamos" un As
        }

        return suma
    }

    private fun formatearMano(mano: List<Carta?>): String {
        val stringBuilder = StringBuilder()
        for (carta in mano) {
            if (carta != null) {
                // Añade "AS de PICAS, "
                stringBuilder.append("${carta.rango} de ${carta.palo}, ")
            }
        }

        return stringBuilder.toString().dropLast(2)
    }

    private fun actualizarVistas(juegoTerminado: Boolean = false){

        tvPuntuacionJugador.text = "Puntuación: $puntosJugador"
        tvManoJugador.text = formatearMano(manoJugador)

        if (juegoTerminado) {
            // El juego ha terminado: Muestra todo
            tvPuntuacionCrupier.text = "Puntuación: $puntosCrupier"
            tvManoCrupier.text = formatearMano(manoCrupier)
        } else {
            // El juego está en curso: Esconde la primera carta
            tvPuntuacionCrupier.text = "Puntuación: ?"

            //Se esconde la primera carta del crupier y se muestra la siguiente
            if (manoCrupier.size > 1) {
                val primeraCartaVisible = manoCrupier[1]
                tvManoCrupier.text = "Carta Oculta, ${formatearMano(listOf(primeraCartaVisible))}"
            }
        }

    }

}

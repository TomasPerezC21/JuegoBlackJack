package com.example.blackjacktpc21.Baraja

import com.example.blackjacktpc21.Cartas.Carta
import com.example.blackjacktpc21.Cartas.Palo
import com.example.blackjacktpc21.Cartas.Rango


class Mazo {

    private val baraja: MutableList<Carta> = mutableListOf()

    init {
        //Primero creamos el mazo con 52 cartas
        crearMazo()

        //Una vez creado lo ponemos aleatorio
        baraja.shuffle()
    }



    fun crearMazo(){

        for(palo in Palo.entries){
            for (rango in Rango.entries){
                //Creamos una carta
                val cartaBaraja = Carta(palo, rango)

                //La metemos a la baraja
                baraja.add(cartaBaraja)
            }
        }

    }

    fun repartirCarta() :Carta? {

        //Esta funciona devuelve la ultima carta, la borra y si la lista esta vacia devuelve null
        return baraja.removeLastOrNull()
    }


}
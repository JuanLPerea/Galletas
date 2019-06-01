package com.hornogalletas.Model;

import com.hornogalletas.MainActivity;

/**
 * Created by TorreJL on 04/04/2017.
 */

public class Tarro {

    private static Tarro INSTANCE = null;
    private static int numGalletas;
    private static boolean abierto;

    // Private constructor suppresses
    private Tarro(){}

    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple
    private synchronized static void createInstance(int galletasInicio) {
        if (INSTANCE == null) {
            INSTANCE = new Tarro();
            numGalletas = galletasInicio;
            abierto = true;

        }
    }

    public static Tarro getInstance(int galletasInicio) {
        if (INSTANCE == null) createInstance(galletasInicio);
        return INSTANCE;
    }

    public boolean isAbierto(){
        return abierto;
    }

    public void cogerGalleta(){
        abierto = false;
        numGalletas--;
        abierto = true;
    }

    public void setNumGalletas(int valor){
        numGalletas = valor;
    }

    public void dejarGalletas(int hornada){
        abierto = false;
        numGalletas += hornada;
        abierto = true;
    }

    public static int getNumGalletas() {
        return numGalletas;
    }

}
